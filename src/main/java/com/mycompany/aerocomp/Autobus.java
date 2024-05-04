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

    String id;
    Aeropuerto aeropuerto;
    int personas;

    public Autobus(String identificador, Aeropuerto aeropuerto) {
        this.id = identificador;
        this.aeropuerto = aeropuerto;
        this.personas = 0;
    }

    @Override
    public void run() {
        //codigo del hilo autobus
        Log.logEvent("Creado el autobus " + this.id + " de " + this.aeropuerto.getCiudad());
        personas = aeropuerto.busLlegaCiudad(id, personas);
        aeropuerto.busVaAeropuerto(id);
        aeropuerto.busBajaPasajerosAeropuerto(id, personas);
        this.personas = 0;
        this.personas = aeropuerto.busSubePasajerosAeropuerto(id);
        aeropuerto.busVaCiudad(id);
        aeropuerto.busBajaPasajerosCiudad(id, personas);       
    }
}
