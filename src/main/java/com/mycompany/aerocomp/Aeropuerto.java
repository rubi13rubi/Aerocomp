/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aerocomp;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author ruben
 */
public class Aeropuerto {

    String ciudad;
    AtomicInteger numPersonasAerop = new AtomicInteger(0); //Atomic integer porque es un recurso compartido
    AtomicInteger numAvionesHangar = new AtomicInteger(0);
    AtomicInteger numAvionesAreaEst = new AtomicInteger(0);
    Random rand = new Random();
    private static AerocompInterfaz interfaz;
    

    public Aeropuerto() {
    }
    
    public Aeropuerto(String ciudad) {
        this.ciudad = ciudad;
    }

    public static void iniciarSimulacion(int nBus, int nAviones) throws InterruptedException {
        //Inicialización del log
        try {
            Log.initialize();
            Log.logEvent("Abre el Aeropuerto");
        } catch (IOException e) {
        }

        //Inicialización de la interfaz
        interfaz = new AerocompInterfaz();
        interfaz.setVisible(true);

        Generador generador = new Generador();
        generador.generar(nAviones, nBus);
    }

    //generación de aviones y buses
    public void busLlegaCiudad(Autobus bus) {
        try {
            Log.logEvent("autobus " + bus.identificador + " llega a la ciudad con " + bus.personas + " personas");
            int esperaPasajeros = rand.nextInt(5000) + 2000;
            //esperar el tiempo adecuado
            try {
                Thread.sleep(esperaPasajeros);
            } catch (InterruptedException ex) {
                Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
            }
            int personas = rand.nextInt(50);
            bus.subirPasajeros(personas);
            Log.logEvent("autobus " + bus.identificador + " recoge " + personas + " pasajeros de la ciudad");
        } catch (IOException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void busVaAeropuerto(Autobus bus) {
        interfaz.actualizarTransfersAeropuerto(bus, true);
        try {
            Log.logEvent("autobus " + bus.identificador + " va al aeropuerto");
        } catch (IOException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        int viajeAeropuerto = rand.nextInt(5000) + 5000;
        //esperar el tiempo adecuado
        try {
            Thread.sleep(viajeAeropuerto);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        interfaz.actualizarTransfersAeropuerto(bus, false);
    }

    public void busBajaPasajerosAeropuerto(Autobus bus) {
        try {
            Log.logEvent("autobus " + bus.identificador + " baja " + bus.personas + " pasajeros en el aeropuerto");
            //Aqui se meten en el aeropuerto
            int cuantas = numPersonasAerop.addAndGet(bus.personas);
            interfaz.actualizarPersonasAeropuerto(cuantas);
            Log.logEvent("Ahora en el aeropuerto hay " + cuantas + " personas");
            bus.bajarPasajeros();
            Log.logEvent("Ahora el bus " + bus.identificador + " tiene " + bus.personas + " pasajeros");
        } catch (IOException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void busSubePasajerosAeropuerto(Autobus bus) {
        try {
            Log.logEvent("bus " + bus.identificador + " Esperando nuevos pasajeros");
            int esperarPasajeros = rand.nextInt(3000) + 2000;
            //esperar el tiempo adecuado
            try {
                Thread.sleep(esperarPasajeros);
            } catch (InterruptedException ex) {
                Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
            }

            int pasajerosSalir = rand.nextInt(50);
            int pasajerosActuales = numPersonasAerop.get();
            //Por si en el aeropuerto hay menos personas de las que se quieren sacar
            if (pasajerosSalir > pasajerosActuales) {
                Log.logEvent("No hay suficientes personas para sacar, probamos otra vez");
                int max = numPersonasAerop.get(); //Se pone como tope de personas que vayan a salir las que hay dentro del aeropuerto
                pasajerosSalir = rand.nextInt(max);
            }
            bus.subirPasajeros(pasajerosSalir);
            Log.logEvent("autobus " + bus.identificador + " sube " + pasajerosSalir + " pasajeros del aeropuerto");
            //Aqui se restan del sistema aeropuerto
            int cuantas = numPersonasAerop.addAndGet(-pasajerosSalir);
            interfaz.actualizarPersonasAeropuerto(cuantas);
            Log.logEvent("Ahora en el aeropuerto hay " + cuantas + " personas");

        } catch (IOException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void busVaCiudad(Autobus bus) {
        interfaz.actualizarTransfersCiudad(bus, true);
        try {
            Log.logEvent("autobus " + bus.identificador + " va a la ciudad");
            int irCiudad = rand.nextInt(5000) + 10000;
            //esperar el tiempo adecuado
            try {
                Thread.sleep(irCiudad);
            } catch (InterruptedException ex) {
                Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
            }
            Log.logEvent("Bus " + bus.identificador + " llega a la ciudad con " + bus.personas + " personas");
        } catch (IOException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        interfaz.actualizarTransfersCiudad(bus, true);
    }

    public void busBajaPasajerosCiudad(Autobus bus) {
        try {
            //Esta funcion podria ser la misma que la que baja los pasajeros en el aeropuerto
            Log.logEvent("autobus " + bus.identificador + " baja " + bus.personas + " pasajeros en la ciudad");
            //Aqui desaparecen los pasajeros
            bus.bajarPasajeros();
            Log.logEvent("Ahora el bus " + bus.identificador + " tiene " + bus.personas + " pasajeros");
        } catch (IOException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void entrarAreaEstacionamiento(Avion avion) {
        try {
            //el avion entra al area de estacionamiento con capacidad ilimitada
            interfaz.actualizarAreaEstacionamiento(avion, true);
            Log.logEvent("El avion " + avion.identificador + " esta en el area de estacionamiento");
            int cuantos = numAvionesAreaEst.incrementAndGet(); //No se suman correctamente???
            Log.logEvent("Ahora en el area de estacionamiento hay " + cuantos + " aviones");
        } catch (IOException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getCiudad() {
        return ciudad;
    }

}
