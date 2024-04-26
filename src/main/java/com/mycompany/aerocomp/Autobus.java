/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aerocomp;

/**
 *
 * @author alvarocamacho
 */
public class Autobus extends Thread {

    String identificador;
    Aeropuerto aeropuerto;

    public Autobus(String identificador, Aeropuerto aeropuerto) {
        this.identificador = identificador;
        this.aeropuerto = aeropuerto;

    }

    @Override
    public void run() {
        //codigo del hilo autobus
        System.out.println("Soy el autobus " + this.identificador + " de " + this.getAeropuerto().getCiudad());
        aeropuerto.busLlegaCiudad(this);
        aeropuerto.busSubePasajerosCiudad(this);
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

}
