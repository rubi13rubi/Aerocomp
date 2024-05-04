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
    private AerocompInterfaz interfaz;

    public Aeropuerto(String ciudad) {
        this.ciudad = ciudad;
    }

    //generación de aviones y buses
    public int busLlegaCiudad(String id, int personasllegan) {
        Log.logEvent("autobus " + id + " llega a la ciudad con " + personasllegan + " personas");
        //esperar el tiempo adecuado
        try {
            Thread.sleep(rand.nextInt(2000, 7000));
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        int personas = rand.nextInt(50);
        Log.logEvent("autobus " + id + " recoge " + personas + " pasajeros de la ciudad");
        return personas;
    }

    public void busVaAeropuerto(String id) {
        //Falta interfaz
        Log.logEvent("autobus " + id + " va al aeropuerto");
        //esperar el tiempo adecuado
        try {
            Thread.sleep(rand.nextInt(2000, 7000));
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Falta interfaz
    }

    public void busBajaPasajerosAeropuerto(String id, int personasllegan) {
        Log.logEvent("autobus " + id + " baja " + personasllegan + " pasajeros en el aeropuerto");
        //Aqui se meten en el aeropuerto
        int personasaeropuerto = numPersonasAerop.addAndGet(personasllegan);
        interfaz.actualizarPersonasAeropuerto(personasaeropuerto);
        Log.logEvent("Ahora en el aeropuerto hay " + personasaeropuerto + " personas");
    }

    public int busSubePasajerosAeropuerto(String id) {
        Log.logEvent("bus " + id + " Esperando nuevos pasajeros");
        //esperar el tiempo adecuado
        try {
            Thread.sleep(2000, 5000);
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
        int pasajerosSalir = rand.nextInt(max);
        Log.logEvent("autobus " + id + " sube " + pasajerosSalir + " pasajeros del aeropuerto");
        //Restar las personas del sistema del aeropuerto
        int personasaeropuerto = numPersonasAerop.addAndGet(-pasajerosSalir);
        interfaz.actualizarPersonasAeropuerto(personasaeropuerto);
        Log.logEvent("Ahora en el aeropuerto hay " + personasaeropuerto + " personas");
        return pasajerosSalir;
    }

    public void busVaCiudad(String id) {
        //Falta interfaz
        Log.logEvent("autobus " + id + " va a la ciudad");
        //esperar el tiempo adecuado
        try {
            Thread.sleep(10000, 15000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Falta interfaz
    }

    public void busBajaPasajerosCiudad(String id, int personas) {
        //Es solo un log pero se mantiene por claridad en el codigo
        Log.logEvent("autobus " + id + " baja " + personas + " pasajeros en la ciudad");
    }

    public void entrarAreaEstacionamiento(Avion avion) {
        //el avion entra al area de estacionamiento con capacidad ilimitada
        interfaz.actualizarAreaEstacionamiento(avion, true);
        Log.logEvent("El avion " + avion.identificador + " esta en el area de estacionamiento");
        int cuantos = numAvionesAreaEst.incrementAndGet(); //No se suman correctamente???
        Log.logEvent("Ahora en el area de estacionamiento hay " + cuantos + " aviones");

    }

    public String getCiudad() {
        return ciudad;
    }

}
