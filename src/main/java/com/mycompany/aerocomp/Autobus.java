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
        while (true) {
            aeropuerto.comprobarPausa();
            personas = aeropuerto.busLlegaCiudad(id, personas);
            aeropuerto.comprobarPausa();
            aeropuerto.busVaAeropuerto(id);
            aeropuerto.comprobarPausa();
            aeropuerto.busBajaPasajerosAeropuerto(id, personas);
            aeropuerto.comprobarPausa();
            this.personas = 0;
            this.personas = aeropuerto.busSubePasajerosAeropuerto(id);
            aeropuerto.comprobarPausa();
            aeropuerto.busVaCiudad(id);
            aeropuerto.comprobarPausa();
            aeropuerto.busBajaPasajerosCiudad(id, personas);
        }
    }
}
