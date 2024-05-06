/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aerocomp;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author ruben
 */
public class Aeropuerto {

    private String ciudad;
    private final AerocompInterfaz interfaz;
    private Random rand = new Random();
    private AtomicInteger numPersonasAerop = new AtomicInteger(0); //Atomic integer porque es un recurso compartido

    private boolean[] puertasEmbarque = new boolean[6];
    private LinkedBlockingQueue<Avion> colaEmbarque = new LinkedBlockingQueue();
    private Lock lockPuertas = new ReentrantLock();
    private Condition condicionPuertas = lockPuertas.newCondition();

    private int[] estadoPistas = new int[4];//Estado de las pistas: 0 es disponible, 1 es ocupado y -1 es cerrado y -2 es cerrandose
    private Lock lockPistas = new ReentrantLock();
    private Semaphore semaforoPistas = new Semaphore(4, true);
    private AtomicInteger pistasPendientesCerrar = new AtomicInteger(0);

    private Semaphore puertaTaller = new Semaphore(20, true);
    
    private Lock lockPausa = new ReentrantLock();
    private Condition condicionPausa = lockPausa.newCondition();
    private boolean pausado;

    public Aeropuerto(String ciudad, AerocompInterfaz interfaz) {
        this.ciudad = ciudad;
        this.interfaz = interfaz;
        Arrays.fill(puertasEmbarque, false);
        Arrays.fill(estadoPistas, 0);
    }

    //______________METODOS DE AUTOBUSES____________
    public int busLlegaCiudad(String id, int personasllegan) {
        Log.logEvent("autobus " + id + " llega a " + this.ciudad + " con " + personasllegan + " personas");
        //esperar el tiempo adecuado
        try {
            Thread.sleep(rand.nextInt(2000, 7000));
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        int personas = rand.nextInt(50);
        Log.logEvent("autobus " + id + " recoge " + personas + " pasajeros de " + this.ciudad);
        return personas;
    }

    public void busVaAeropuerto(String id) {
        interfaz.actualizarTransfersAeropuerto(this.ciudad, id, true);
        Log.logEvent("autobus " + id + " va al aeropuerto de " + this.ciudad);
        //esperar el tiempo adecuado
        try {
            Thread.sleep(rand.nextInt(2000, 7000));
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        interfaz.actualizarTransfersAeropuerto(this.ciudad, id, false);
    }

    public void busBajaPasajerosAeropuerto(String id, int personasllegan) {
        Log.logEvent("autobus " + id + " baja " + personasllegan + " pasajeros en el aeropuerto de " + this.ciudad);
        //Aqui se meten en el aeropuerto
        int personasaeropuerto = numPersonasAerop.addAndGet(personasllegan);
        interfaz.actualizarPersonasAeropuerto(this.ciudad, personasaeropuerto);
        Log.logEvent("Ahora en el aeropuerto de " + this.ciudad + " hay " + personasaeropuerto + " personas");
    }

    public int busSubePasajerosAeropuerto(String id) {
        Log.logEvent("bus " + id + " Esperando nuevos pasajeros");
        //esperar el tiempo adecuado
        try {
            Thread.sleep(rand.nextInt(2000, 5000));
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Obtener el numero de personas que se van a subir
        int max = 50;//Maximo de personas que se van a sacar
        int personasAeropuerto = numPersonasAerop.get();
        //Se compara por si en el aeropuerto hay menos personas que el maximo
        if (max > personasAeropuerto) {
            max = personasAeropuerto;//Se sacan como maximo el numero de personas del aeropuerto
        }
        int pasajerosSalir = 0; //Fix para cuando hay 0 personas que no de excepcion el random
        if (max > 0) pasajerosSalir = rand.nextInt(max);
        Log.logEvent("autobus " + id + " sube " + pasajerosSalir + " pasajeros del aeropuerto de " + this.ciudad);
        //Restar las personas del sistema del aeropuerto
        int personasaeropuerto = numPersonasAerop.addAndGet(-pasajerosSalir);
        interfaz.actualizarPersonasAeropuerto(this.ciudad, personasaeropuerto);
        Log.logEvent("Ahora en el aeropuerto de " + this.ciudad + " hay " + personasaeropuerto + " personas");
        return pasajerosSalir;
    }

    public void busVaCiudad(String id) {
        interfaz.actualizarTransfersCiudad(this.ciudad, id, true);
        Log.logEvent("autobus " + id + " va a " + this.ciudad);
        //esperar el tiempo adecuado
        try {
            Thread.sleep(rand.nextInt(10000, 15000));
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        interfaz.actualizarTransfersCiudad(this.ciudad, id, false);
    }

    public void busBajaPasajerosCiudad(String id, int personas) {
        //Es solo un log pero se mantiene por claridad en el codigo
        Log.logEvent("autobus " + id + " baja " + personas + " pasajeros en " + this.ciudad);
    }

    //______________METODOS DE AVIONES____________
    public void aparecerEnHangar(String id) {
        //Es solo un log pero se mantiene por claridad en el codigo
        Log.logEvent("El avion " + id + " aparece en el hangar del aeropuerto de " + this.ciudad);
    }

    public void esperarEmbarqueDesembarque(Avion avion, String id, boolean embarque) {
        try {
            //Pone la condición de este hilo para que espere y si va a embarcar o desembarcar
            avion.setPuerta(-1);
            avion.setEmbarque(embarque);
            lockPuertas.lock();
            try {
                colaEmbarque.put(avion);
                if (embarque) {
                    interfaz.actualizarAreaEstacionamiento(ciudad, id, true);
                    Log.logEvent("El avion " + id + " accede al area de estacionamiento del aeropuerto de " + this.ciudad + " esperando a embarcar");
                } else {
                    interfaz.actualizarAreaRodaje(ciudad, id, true);
                    Log.logEvent("El avion " + id + " accede al area de rodaje del aeropuerto de " + this.ciudad + " y se dirige a las puertas de desembarque");
                    Thread.sleep(rand.nextInt(3000, 5000));
                    Log.logEvent("El avion " + id + " esta esperando a desembarcar en " + this.ciudad);
                }

                Log.logEvent("Hay " + colaEmbarque.size() + " aviones esperando en la cola de embarque/desembarque");
                actualizarEstadoPuertas();
                while (avion.getPuerta() < 0) {
                    condicionPuertas.await();
                }
                puertasEmbarque[avion.getPuerta()] = true; //Entra en la puerta que le corresponde
            } finally {
                lockPuertas.unlock();
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int embarcar(String id, int capacidad, int puerta) {
        int personasenavion = 0;
        try {
            int intento = 1;
            interfaz.actualizarAreaEstacionamiento(ciudad, id, false);
            interfaz.actualizarPuerta(ciudad, id, true, true, puerta);
            Log.logEvent("El avion " + id + " entra a embarcar en la puerta " + puerta + " del aeropuerto de " + this.ciudad);
            while (intento <= 3 && personasenavion < capacidad) {
                Log.logEvent("El avion " + id + " intenta embarcar en la puerta " + puerta + " del aeropuerto de " + this.ciudad + "(intento " + intento + ")");
                Thread.sleep(rand.nextInt(1000, 3000));
                int personasrestantes = capacidad - personasenavion;
                int personasaeropuerto = numPersonasAerop.get();
                if (personasaeropuerto < personasrestantes) {
                    personasenavion += personasaeropuerto;
                    numPersonasAerop.addAndGet(-personasaeropuerto);
                } else {
                    numPersonasAerop.addAndGet(-personasrestantes);
                    personasenavion = capacidad;
                }
                intento++;
            }
            if (intento == 3) {
                Log.logEvent("El avion " + id + " deja de intentar embarcar tras 3 intentos");
            }

            lockPuertas.lock();
            try {
                puertasEmbarque[puerta] = false; //Libera la puerta
                interfaz.actualizarPuerta(ciudad, id, false, true, puerta);
                Log.logEvent("El avion " + id + " termina de embarcar en la puerta " + puerta + " del aeropuerto de " + this.ciudad + " " + personasenavion + " personas");
                actualizarEstadoPuertas();
            } finally {
                lockPuertas.unlock();
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return personasenavion;
    }

    public void desembarcar(String id, int personasenavion, int puerta) {
        try {
            interfaz.actualizarAreaRodaje(ciudad, id, false);
            interfaz.actualizarPuerta(ciudad, id, true, false, puerta);
            Log.logEvent("El avion " + id + " entra a desembarcar en la puerta " + puerta + " del aeropuerto de " + this.ciudad);
            Thread.sleep(rand.nextInt(1000, 5000));
            numPersonasAerop.addAndGet(personasenavion);

            lockPuertas.lock();
            try {
                puertasEmbarque[puerta] = false; //Libera la puerta
                interfaz.actualizarPuerta(ciudad, id, false, false, puerta);
                Log.logEvent("El avion " + id + " termina de desembarcar en la puerta " + puerta + " del aeropuerto de " + this.ciudad + " " + personasenavion + " personas");
                actualizarEstadoPuertas();
            } finally {
                lockPuertas.unlock();
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void actualizarEstadoPuertas() {//Si hay hueco en la lista, despertar al primer hilo que pueda entrar

        int numpuerta = 6;//6 significa que no hay hueco
        int i = 0;
        boolean embdes = false;
        for (boolean puerta : puertasEmbarque) {//For por todas las puertas. Marca la ultima que se encuentra abierta
            if (puerta == false) {
                if (i == 1 && numpuerta == 0) {
                    embdes = true; //Si tanto embarque como desembarque estan abiertas, marca el bool para tenerlo en cuenta
                }
                numpuerta = i;
            }
            i++;
        }

        if (numpuerta <= 5) {//Numpuerta 6 significa que todas estaban ocupadas
            boolean salir = false;
            for (Object o : colaEmbarque.toArray()) {//For por los hilos de la cola para despertar al primero que cumpla la condición necesaria para salir
                Avion a = (Avion) o;
                if (!salir) {
                    boolean embarque = a.isEmbarque();
                    if (numpuerta > 1 || (numpuerta == 0 && embarque) || (numpuerta == 1 && !embarque)) {
                        a.setPuerta(numpuerta); //Pone la condicion del hilo que tiene que salir
                        colaEmbarque.remove(a);
                        condicionPuertas.signalAll(); //Signal a la condicion para que salga
                        salir = true; //Para salir del for
                    } else if (embdes && numpuerta == 1) {
                        //Caso de que solo esten abiertas las puertas de embarque y desembarque y necesite embarcar uno
                        //Estaría marcada la puerta de desembarque ya que es la ultima y por eso se marca el caso con un bool
                        a.setPuerta(0); //Pone la condicion del hilo que tiene que salir
                        colaEmbarque.remove(a);
                        condicionPuertas.signalAll(); //Signal a la condicion para que salga
                        salir = true; //Para salir del for
                    }
                } else {
                    break;
                }
            }
        }

    }

    public int esperarPistaDespegue(String id) {
        interfaz.actualizarAreaRodaje(ciudad, id, true);
        Log.logEvent("El avion " + id + " entra al area de rodaje" + " del aeropuerto de " + this.ciudad + " para realizar comprobaciones antes de solicitar una pista");
        try {
            Thread.sleep(rand.nextInt(1000, 5000));
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        Log.logEvent("El avion " + id + " espera una pista para despegar en el area de rodaje del aeropuerto de " + this.ciudad);
        int pista = 0;
        try {
            semaforoPistas.acquire();
            lockPistas.lock();
            for (int estado : estadoPistas) {
                if (estado == 0) {
                    break; //0 significa disponible
                }
                pista++;
            }
            estadoPistas[pista] = 1; //Ocupada
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lockPistas.unlock();
        }
        Log.logEvent("El avion " + id + " accede a la pista " + (pista + 1) + " del aeropuerto de " + this.ciudad);
        interfaz.actualizarAreaRodaje(ciudad, id, false);
        interfaz.actualizarPista(ciudad, id, true, true, pista);
        return pista;
    }

    public void despegar(String id, int pista) {
        try {
            Log.logEvent("El avion " + id + " realiza unas ultimas comprobaciones antes de despegar");
            Thread.sleep(rand.nextInt(1000, 3000));
            Log.logEvent("El avion " + id + " comienza el despegue desde la pista " + pista + " del aeropuerto de " + this.ciudad);
            Thread.sleep(rand.nextInt(1000, 5000));
            Log.logEvent("El avion " + id + " despega exitosamente de la pista " + pista + " del aeropuerto de " + this.ciudad);
            interfaz.actualizarPista(ciudad, id, false, true, pista);
            lockPistas.lock();
            if (estadoPistas[pista] == 1) {
                estadoPistas[pista] = 0; //Libera la pista solo si seguia ocupada
            } else if (estadoPistas[pista] == -2) { //-2 es el estado cerrandose (cuando se intenta cerrar y hay alguien dentro)
                estadoPistas[pista] = -1; //Al salir la cierra definitivamente (estado -1)
                pistasPendientesCerrar.incrementAndGet(); //Hace que no se libere el semaforo al liberar la siguiente pista
            }
            //En el caso de que se haya cerrado la pista (estado -1) mientras despegaba, la deja cerrada
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lockPistas.unlock();
        }
        if (pistasPendientesCerrar.get() > 0) pistasPendientesCerrar.decrementAndGet();
        else semaforoPistas.release();
    }

    public void volar(String id) {
        try {
            Log.logEvent("El avion " + id + " accede a la aerovia de " + this.ciudad);
            interfaz.actualizarAerovia(ciudad, id, true);
            Thread.sleep(rand.nextInt(15000, 30000));
            interfaz.actualizarAerovia(ciudad, id, false);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void aterrizar(String id) {
        int pista = 0;
        try {
            Log.logEvent("El avion " + id + " solicita una pista para aterrizar en el aeropuerto de " + this.ciudad);
            while (!semaforoPistas.tryAcquire()) {
                Log.logEvent("El avion " + id + " da un rodeo al aeropuerto de " + this.ciudad);
                Thread.sleep(rand.nextInt(1000, 5000));
                Log.logEvent("El avion " + id + " solicita de nuevo una pista para aterrizar en el aeropuerto de " + this.ciudad);
            }
            lockPistas.lock();
            for (int estado : estadoPistas) {
                if (estado == 0) {
                    break; //0 significa disponible
                }
                pista++;
            }
            estadoPistas[pista] = 1; //Ocupada

        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lockPistas.unlock();
        }

        Log.logEvent("El avion " + id + " accede a la pista " + (pista + 1) + " del aeropuerto de " + this.ciudad);
        interfaz.actualizarPista(ciudad, id, true, false, pista);
        try {
            Thread.sleep(rand.nextInt(1000, 5000));
            Log.logEvent("El avion " + id + " aterriza con exito en la pista " + (pista + 1) + " del aeropuerto de " + this.ciudad);
            lockPistas.lock();
            if (estadoPistas[pista] == 1) {
                estadoPistas[pista] = 0; //Libera la pista solo si seguia ocupada
            } else if (estadoPistas[pista] == -2) { //-2 es el estado cerrandose (cuando se intenta cerrar y hay alguien dentro)
                estadoPistas[pista] = -1; //Al salir la cierra definitivamente (estado -1)
                pistasPendientesCerrar.incrementAndGet(); //Hace que no se libere el semaforo al liberar la siguiente pista
            }
            //En el caso de que se haya cerrado la pista (estado -1) mientras despegaba, la deja cerrada
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lockPistas.unlock();
        }
        if (pistasPendientesCerrar.get() > 0) pistasPendientesCerrar.decrementAndGet();
        else semaforoPistas.release();
        interfaz.actualizarPista(ciudad, id, false, false, pista);
    }

    public void comprobacionesAreadeEstacionamiento(String id) {
        try {
            interfaz.actualizarAreaEstacionamiento(ciudad, id, true);
            Log.logEvent("El avion " + id + " accede al area de estacionamiento de " + this.ciudad + " para realizar comprobaciones");
            Thread.sleep(rand.nextInt(1000, 5000));
            interfaz.actualizarAreaEstacionamiento(ciudad, id, false);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void inspeccionTaller(String id, boolean profundidad) {
        try {
            puertaTaller.acquire();
            Log.logEvent("El avion " + id + " accede al taller de " + this.ciudad + " para realizar una revision " + (profundidad ? "en profundidad" : "rapida"));
            interfaz.actualizarTaller(ciudad, id, true);
            int tiempo = profundidad ? rand.nextInt(5000, 10000) : rand.nextInt(1000, 5000);
            Thread.sleep(tiempo);
            puertaTaller.release();
            Log.logEvent("El avion " + id + " sale del taller de " + this.ciudad);
            interfaz.actualizarTaller(ciudad, id, false);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void iraHangar(String id) {
        interfaz.actualizarHangar(ciudad, id, true);
        Log.logEvent("El avion " + id + " va al hangar del aeropuerto de " + this.ciudad);
        try {
            Thread.sleep(rand.nextInt(15000, 30000));
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        Log.logEvent("El avion " + id + " sale del hangar del aeropuerto de " + this.ciudad);
        interfaz.actualizarHangar(ciudad, id, false);
    }

    public void cerrarPista(int pista) {

        try {
            Log.logEvent("Cerrando pista " + (pista + 1) + " del aeropuerto de " + this.ciudad);
            lockPistas.lock();
            if (estadoPistas[pista] == 0) { //Pista libre
                estadoPistas[pista] = -1; //La cierra directamente
                if (!semaforoPistas.tryAcquire()) {
                    pistasPendientesCerrar.incrementAndGet(); //Hace que no se libere el semaforo al liberar la siguiente pista
                }
            } else if (estadoPistas[pista] == 1) { //Pista ocupada
                estadoPistas[pista] = -2; //Cambia a estado cerrando, el cierre lo maneja el propio avion al salir de la pista
            }
        } finally {
            lockPistas.unlock();
        }
    }

    public void abrirPista(int pista) {
        try {
            Log.logEvent("Abriendo pista " + (pista + 1) + " del aeropuerto de " + this.ciudad);
            lockPistas.lock();
            if (estadoPistas[pista] == -1) { //Pista cerrada
                estadoPistas[pista] = 0; //La abre directamente
                semaforoPistas.release();
            } else if (estadoPistas[pista] == -2) { //Pista cerrando, quiere decir que no ha llegado a cerrarse y no hace falta liberar el semaforo
                estadoPistas[pista] = 1; //La vuelve a dejar ocupada
            }
        } finally {
            lockPistas.unlock();
        }
    }
    
    public void comprobarPausa() {

        lockPausa.lock();
        try {
            if (pausado) {
                condicionPausa.await();
            }
        } catch (InterruptedException ex) {
        } finally {
            lockPausa.unlock();
        }
    }
    
    public void pausa(boolean p) {

        pausado = p;
        if (!pausado) {
            lockPausa.lock();
            try {
                condicionPausa.signalAll();
            } finally {
                lockPausa.unlock();
            }
        }
    }

    public String getCiudad() {
        return ciudad;
    }

}
