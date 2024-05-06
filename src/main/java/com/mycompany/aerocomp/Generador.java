/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aerocomp;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alvarocamacho
 */
public class Generador {
    private Random rand = new Random();
    private Aeropuerto madrid;
    private Aeropuerto barcelona;
    
    public Generador (AerocompInterfaz interfaz){
        this.madrid = new Aeropuerto("Madrid", interfaz);
        this.barcelona = new Aeropuerto("Barcelona", interfaz);
        interfaz.setVisible(true);
        interfaz.setAeropuertos(madrid, barcelona);
    }
    
    public void generar(int nAviones, int nBus){
        Thread hiloAviones = new Thread(new Runnable() {
            Avion a;

            @Override

            public void run() {

                for (int i = 1; i <= nAviones; i++) {
                    int delay = rand.nextInt(3000) + 1000;

                    if (i % 2 == 0) { //Si es par se le asigna madrid
                        char char1 = (char) (rand.nextInt(26) + 'A');
                        char char2 = (char) (rand.nextInt(26) + 'A');
                        String identificador = String.format("%c%c-%04d", char1, char2, i); //Para dar el formato correcto al nombre del avion
                        a = new Avion(identificador, madrid, barcelona, true);
                        a.start();

                    } else { //Si es impar se le asigna barcelona
                        char char1 = (char) (rand.nextInt(26) + 'A');
                        char char2 = (char) (rand.nextInt(26) + 'A');
                        String identificador = String.format("%c%c-%04d", char1, char2, i);
                        a = new Avion(identificador, madrid, barcelona, false);
                        a.start();
                    }
                    try {
                        Thread.sleep(delay); //Para que se generen de forma escalonada su hilo creador se duerme 
                    } catch (InterruptedException ex) {
                        Logger.getLogger(AerocompInterfaz.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        //Hilo generador de buses
        Thread hiloBuses = new Thread(new Runnable() {
            Autobus b;

            @Override
            public void run() {
                for (int i = 1; i <= nBus; i++) {
                    int delay = rand.nextInt(1000) + 500;
                    if (i % 2 == 0) { //Si es par se le asigna madrid
                        String identificador = String.format("B-%04d", i); //Se le asigna a cada avión el identificador y la ciudad a la que pertenece
                        b = new Autobus(identificador, madrid);
                        b.start();
                    } else {
                        String identificador = String.format("B-%04d", i);
                        b = new Autobus(identificador, barcelona);
                        b.start();
                    }
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(AerocompInterfaz.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        hiloAviones.start();
        hiloBuses.start();

    }
    }
