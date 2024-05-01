/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aerocomp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

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

    public static void initialize() throws IOException {
        synchronized (lock) {
            writer = new BufferedWriter(new FileWriter(LOG_FILE_NAME));
        }
    }

    public static void logEvent(String event) throws IOException {
        synchronized (lock) {
            writer.write(new Date().toString() + " - " + event);
            writer.newLine();
            writer.flush();
        }
    }

    public static void close() throws IOException {
        synchronized (lock) {
            writer.close();
        }
    }
}
