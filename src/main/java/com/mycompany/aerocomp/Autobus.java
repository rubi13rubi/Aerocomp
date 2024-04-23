/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aerocomp;

/**
 *
 * @author alvarocamacho
 */
public class Autobus extends Thread{
    String identificador;
    String ciudad;
    Aeropuerto aeropuerto;
    
    public Autobus (String identificador, Aeropuerto aeropuerto){
        this.identificador = identificador;
        this.aeropuerto = aeropuerto;
        this.ciudad = ciudad;
    }
    
    public void run(){
        //Codigo del hilo autobus
    }

    public String getIdentificador() {
        return identificador;
    }
    
    
}

