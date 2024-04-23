/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aerocomp;

/**
 *
 * @author alvarocamacho
 */
public class Avion extends Thread{
    String identificador;
    Aeropuerto aeropuerto;
    
    public Avion (String identificador, Aeropuerto aeropuerto){
        this.identificador = identificador;
        this.aeropuerto = aeropuerto;
    }
    
    public void run(){
        //codigo del hilo avion
        System.out.println("Soy el avion " + this.identificador + " de " + this.aeropuerto.getCiudad());
    }

    public String getIdentificador() {
        return identificador;
    }
    
}
