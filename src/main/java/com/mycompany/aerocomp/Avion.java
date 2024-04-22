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
    Gestor gestor;
    
    public Avion (String identificador, Gestor gestor){
        this.identificador = identificador;
        this.gestor = gestor;
    }
    
    public void run(){
        //codigo del hilo avion
    }
    
    public Boolean esPar(String identificador){
        
        String parteNumerica = identificador.substring(2);
        int numero = Integer.parseInt(parteNumerica);
        return numero % 2 == 0; 
    }

    public String getIdentificador() {
        return identificador;
    }
    
}
