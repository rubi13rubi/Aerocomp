/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aerocomp;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author alvarocamacho
 */
public class Avion extends Thread {

    String identificador;
    Aeropuerto madrid;
    Aeropuerto barcelona;
    boolean ciudad;
    int capacidad;
    Random rand = new Random();
    String ubicacion;
    ArrayList<String> zonas = new ArrayList<>();

    public Avion(String identificador, Aeropuerto madrid, Aeropuerto barcelona, boolean cuidad) {
        this.identificador = identificador;
        this.madrid = madrid;
        this.barcelona = barcelona;
        this.ciudad = cuidad;
        this.capacidad = rand.nextInt(201) + 100;
        this.ubicacion = "Hangar";
    }

    public void run() {
        //codigo del hilo avion
        System.out.println("Soy el avion " + this.identificador + " de " + this.getAeropuerto(this.ciudad).getCiudad() + " con capacidad " + this.capacidad);
        if (this.ciudad){
            madrid.entrarAreaEstacionamiento(this);
        }
        else{
            barcelona.entrarAreaEstacionamiento(this);
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

    public String getIdentificador() {
        return identificador;
    }

}
