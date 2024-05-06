/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aerocomp;
import java.util.Random;

/**
 *
 * @author alvarocamacho
 */
public class Avion extends Thread {

    private String identificador;
    private Aeropuerto madrid;
    private Aeropuerto barcelona;
    private Aeropuerto aeropuerto;
    private Random rand = new Random();
    
    private int capacidad;
    
    private int puerta; //puerta de embarque
    private boolean embarque; //determina si va a embarcar o no

    public Avion(String identificador, Aeropuerto madrid, Aeropuerto barcelona, boolean ciudad) {
        this.identificador = identificador;
        this.madrid = madrid;
        this.barcelona = barcelona;
        this.aeropuerto = ciudad ? madrid : barcelona;
        this.capacidad = rand.nextInt(201) + 100;
    }

    public void run() {
        //codigo del hilo avion
        Log.logEvent("Creado el avion " + this.identificador + " de " + this.aeropuerto.getCiudad() + " con capacidad " + this.capacidad);
        aeropuerto.comprobarPausa();
        aeropuerto.aparecerEnHangar(identificador);
        int vuelos = 0;
        while (true) {
            aeropuerto.comprobarPausa();
            aeropuerto.esperarEmbarqueDesembarque(this, identificador, true);
            aeropuerto.comprobarPausa();
            int pasajeros = aeropuerto.embarcar(identificador, capacidad, puerta);
            aeropuerto.comprobarPausa();
            int pista = aeropuerto.esperarPistaDespegue(identificador);
            aeropuerto.comprobarPausa();
            aeropuerto.despegar(identificador, pista);
            aeropuerto.comprobarPausa();
            aeropuerto.volar(identificador);
            aeropuerto.comprobarPausa();
            this.cambiarAeropuerto();
            vuelos++;
            aeropuerto.aterrizar(identificador);
            aeropuerto.comprobarPausa();
            aeropuerto.esperarEmbarqueDesembarque(this, identificador, false);
            aeropuerto.comprobarPausa();
            aeropuerto.desembarcar(identificador, pasajeros, puerta);
            aeropuerto.comprobarPausa();
            aeropuerto.comprobacionesAreadeEstacionamiento(identificador);
            aeropuerto.comprobarPausa();
            aeropuerto.inspeccionTaller(identificador, (vuelos%15==0));
            aeropuerto.comprobarPausa();
            if (rand.nextBoolean()) aeropuerto.iraHangar(identificador);
        }
    }
    
    private void cambiarAeropuerto(){
        if (this.aeropuerto == madrid){
            this.aeropuerto = barcelona;
        }
        else{
            this.aeropuerto = madrid;
        }
    }

    public int getPuerta() {
        return puerta;
    }

    public void setPuerta(int puerta) {
        this.puerta = puerta;
    }

    public boolean isEmbarque() {
        return embarque;
    }

    public void setEmbarque(boolean embarque) {
        this.embarque = embarque;
    }

    

}
