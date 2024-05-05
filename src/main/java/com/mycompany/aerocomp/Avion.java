/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aerocomp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alvarocamacho
 */
public class Avion extends Thread {

    String identificador;
    Aeropuerto madrid;
    Aeropuerto barcelona;
    Aeropuerto aeropuerto;
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
        //Va al hangar
        while (true) {
            
        }
    }

    public Aeropuerto getAeropuerto(boolean ubicacion) {
        //Funcion que devuelve el nombre del aeropuerto en el que se encuentra. True es madrid y false Barcelona
        if (ubicacion == true) {
            return this.madrid;
        } else {
            return this.barcelona;
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
