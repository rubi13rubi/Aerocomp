/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aerocomp;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author ruben
 */
public class Aeropuerto {

    String ciudad;

    public Aeropuerto() {
        //Inicializa todos los arrays

    }

    public Aeropuerto(String ciudad) {
        this.ciudad = ciudad;
    }

    public void busLlegaCiudad(Autobus bus) {
        System.out.println("autobus " + bus.identificador + " llega a la ciudad");
    }

    public void busSubePasajerosCiudad(Autobus bus) {
        System.out.println("autobus " + bus.identificador + " recoge pasajeros de la ciudad");
    }

    public void busVaAeropuerto(Autobus bus) {
        System.out.println("autobus " + bus.identificador + " va al aeropuerto");
    }

    public void busBajaPasajerosAeropuerto(Autobus bus) {
        System.out.println("autobus " + bus.identificador + " baja pasajeros en el aeropuerto");
    }

    public void busSubePasajerosAeropuerto(Autobus bus) {
        System.out.println("autobus " + bus.identificador + " sube pasajeros del aeropuerto");
    }

    public void busVaCiudad(Autobus bus) {
        System.out.println("autobus " + bus.identificador + " va a la ciudad");
    }

    public void busBajaPasajerosCiudad(Autobus bus) {
        System.out.println("autobus " + bus.identificador + " baja pasajeros en la ciudad");
    }

    public String getCiudad() {
        return ciudad;
    }
}
