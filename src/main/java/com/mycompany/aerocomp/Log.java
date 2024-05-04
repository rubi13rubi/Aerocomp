/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aerocomp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase encargada de gestionar el fichero de log y añadir entradas garantizando
 * exclusión mutua.
 *
 * @author 
 */
public class Log {

    private static final String LOG_FILE_NAME = "evolucionAeropuerto.txt";
    private static final Object lock = new Object();
    private static BufferedWriter writer;

    public static void initialize(){
        synchronized (lock) {
            try {
                writer = new BufferedWriter(new FileWriter(LOG_FILE_NAME));
            } catch (IOException ex) {
                Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void logEvent(String event){
        synchronized (lock) {
            try {
                writer.write(new Date().toString() + " - " + event);
                writer.newLine();
                writer.flush();
            } catch (IOException ex) {
                Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void close(){
        synchronized (lock) {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
