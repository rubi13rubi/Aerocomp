/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aerocomp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alvarocamacho
 */
public class Autobus extends Thread {

    String identificador;
    Aeropuerto aeropuerto;
    int personas;

    public Autobus(String identificador, Aeropuerto aeropuerto) {
        this.identificador = identificador;
        this.aeropuerto = aeropuerto;
        this.personas = 0;
    }

    @Override
    public void run() {
        try {
            //codigo del hilo autobus
            Log.logEvent("Creado el autobus " + this.identificador + " de " + this.getAeropuerto().getCiudad());
        } catch (IOException ex) {
            Logger.getLogger(Autobus.class.getName()).log(Level.SEVERE, null, ex);
        }
        aeropuerto.busLlegaCiudad(this);
        aeropuerto.busVaAeropuerto(this);
        aeropuerto.busBajaPasajerosAeropuerto(this);
        aeropuerto.busSubePasajerosAeropuerto(this);
        aeropuerto.busVaCiudad(this);
        aeropuerto.busBajaPasajerosCiudad(this);       
    }

    public Aeropuerto getAeropuerto() {
        //Funcion que devuelve el nombre del aeropuerto en el que se encuentra. 
        return this.aeropuerto;
    }
    
    public void subirPasajeros(int numPasajeros){
        this.personas = numPasajeros;
    }
    
    public void bajarPasajeros(){
        this.personas = 0;
    }

}
