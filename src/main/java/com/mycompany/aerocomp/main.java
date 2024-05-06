/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aerocomp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class main {

    public static void main(String[] args) throws InterruptedException {
        //Inicialización del log
        Log.initialize();
        Log.logEvent("Abre el Aeropuerto");

        //Inicialización de la interfaz y el generador
        AerocompInterfaz interfaz = new AerocompInterfaz();
        Aeropuerto madrid = new Aeropuerto("Madrid", interfaz);
        Aeropuerto barcelona = new Aeropuerto("Barcelona", interfaz);
        interfaz.setVisible(true);
        interfaz.setAeropuertos(madrid, barcelona);
        Generador generador = new Generador(madrid, barcelona);
        int nAviones = 8000;
        int nBus = 4000;
        generador.generar(nAviones, nBus);
        
        //Servidor
        try {
            ServerSocket servidor = new ServerSocket(5000); // Creamos un ServerSocket en el puerto 5000
            System.out.println("Servidor Arrancado....");

            while (true) {
                Socket socket = servidor.accept();     // Esperamos una conexión
                System.out.println("Conexión establecida");
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());  // Abrimos los canales de E/S fuera del bucle
                ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
                
                while (!socket.isClosed()) {
                    boolean[][] informacionPistas = (boolean[][]) entrada.readObject();    //Leemos el mensaje del cliente
                    System.out.println(Arrays.toString(informacionPistas));
                    madrid.setInfoPistas(informacionPistas[0]);
                    barcelona.setInfoPistas(informacionPistas[1]);
                    String[][] infoEnviar = new String[2][6];
                    infoEnviar[0] = madrid.getDatos();
                    infoEnviar[1] = barcelona.getDatos();
                    salida.flush();
                    salida.writeObject(infoEnviar);  // Enviamos la lista al cliente
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace(); // Manejo básico de la interrupción
                    }
                }

                socket.close();                           // Y cerramos la conexión
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Imprime cualquier excepción ocurrida para depuración
        }
    }
}
