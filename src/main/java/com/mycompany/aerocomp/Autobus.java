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
    Gestor gestor;
    
    public Autobus (String identificador, Gestor gestor){
        this.identificador = identificador;
        this.gestor = gestor;
    }
    
    public void run(){
        //Codigo del hilo autobus
    }
    
    /**
     * Funcion que indica si el identificador del bus es par
     * @param identificador
     * @return
     */
    public Boolean esPar(String identificador){
        
        String parteNumerica = identificador.substring(2);
        int numero = Integer.parseInt(parteNumerica);
        return numero % 2 == 0; 
    }

    public String getIdentificador() {
        return identificador;
    }
    
    
}

