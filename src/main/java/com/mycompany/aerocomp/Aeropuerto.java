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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ruben
 */
public class Aeropuerto {

    String ciudad;
    Random rand = new Random();

    public Aeropuerto() {
        //Inicializa todos los arrays

    }

    public Aeropuerto(String ciudad) {
        this.ciudad = ciudad;
    }

    public void busLlegaCiudad(Autobus bus) {
        System.out.println("autobus " + bus.identificador + " llega a la ciudad con " + bus.personas + " personas");
        int esperaPasajeros = rand.nextInt(5000) + 2000;
        //esperar el tiempo adecuado
        try {
            Thread.sleep(esperaPasajeros);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        int personas =  rand.nextInt(50);
        bus.subirPasajeros(personas);
        System.out.println("autobus " + bus.identificador + " recoge " + personas + " pasajeros de la ciudad");
    }

    public void busVaAeropuerto(Autobus bus) {
        System.out.println("autobus " + bus.identificador + " va al aeropuerto");
        int viajeAeropuerto = rand.nextInt(5000) + 5000;
        //esperar el tiempo adecuado
        try {
            Thread.sleep(viajeAeropuerto);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void busBajaPasajerosAeropuerto(Autobus bus) {
        System.out.println("autobus " + bus.identificador + " baja " + bus.personas + " pasajeros en el aeropuerto");
        //Aqui se meten en el aeropuerto, no he mirado todavia como
        bus.bajarPasajeros();
        System.out.println("Ahora el bus " + bus.identificador + " tiene " + bus.personas + " pasajeros");
    }

    public void busSubePasajerosAeropuerto(Autobus bus) {
        System.out.println("Esperando nuevos pasajeros");
        int esperarPasajeros = rand.nextInt(3000) + 2000;
        //esperar el tiempo adecuado
        try {
            Thread.sleep(esperarPasajeros);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        int pasajeros = rand.nextInt(50);
        //Aqui se restan del sistema aeropuerto
        bus.subirPasajeros(pasajeros);
        System.out.println("autobus " + bus.identificador + " sube "+pasajeros+" pasajeros del aeropuerto");
    }

    public void busVaCiudad(Autobus bus) {
        System.out.println("autobus " + bus.identificador + " va a la ciudad");
        int irCiudad = rand.nextInt(5000) + 10000;
        //esperar el tiempo adecuado
        try {
            Thread.sleep(irCiudad);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Bus "+bus.identificador+" llega a la ciudad con "+ bus.personas+" personas");
    }

    public void busBajaPasajerosCiudad(Autobus bus) {
        //Esta funcion podria ser la misma que la que baja los pasajeros en el aeropuerto
        System.out.println("autobus " + bus.identificador + " baja " + bus.personas + " pasajeros en la ciudad");
        //Aqui desaparecen los pasajeros
        bus.bajarPasajeros();
        System.out.println("Ahora el bus " + bus.identificador + " tiene " + bus.personas + " pasajeros");
    }

    public String getCiudad() {
        return ciudad;
    }
}
