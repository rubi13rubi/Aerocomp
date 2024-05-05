/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aerocomp;

/**
 *
 * @author alvarocamacho
 */
public class main {
    public static void main(String[] args) throws InterruptedException{
        //Inicialización del log
        Log.initialize();
        Log.logEvent("Abre el Aeropuerto");

        //Inicialización de la interfaz
        AerocompInterfaz interfaz = new AerocompInterfaz();
        interfaz.setVisible(true);

        Generador generador = new Generador(interfaz);
        int nAviones = 100;
        int nBus = 100;
        generador.generar(nAviones, nBus);
    }
}
