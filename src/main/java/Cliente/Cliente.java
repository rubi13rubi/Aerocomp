/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author ruben
 */


public class Cliente {
    static boolean[][] listasalida;
    
    public static void main(String[] args) throws InterruptedException {
        Socket cliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        String[][] informacionServidor;
        InterfazCliente interfaz = new InterfazCliente();
        interfaz.setVisible(true);

        try {
            cliente = new Socket(InetAddress.getLocalHost(), 5000);//Creamos el socket para conectarnos al puerto 5000 del servidor
            salida = new ObjectOutputStream(cliente.getOutputStream());
            entrada = new ObjectInputStream(cliente.getInputStream());//Creamos los canales de entrada/salida

            while (!cliente.isClosed()) {
                listasalida = interfaz.getEstadoBotones();
                salida.flush();
                salida.writeObject(listasalida);// Enviamos un mensaje al servidor
                informacionServidor = (String[][]) entrada.readObject();  // Leemos la respuesta: una lista con la información para la interfaz
                interfaz.actualizarInterfazCliente(informacionServidor); //Actualizamos la interfaz
            }
        } catch (IOException | ClassNotFoundException e) {
        } finally {
            try {
                if (cliente != null)
                    cliente.close(); // Cerramos la conexión
                if (entrada != null)
                    entrada.close(); // Cerramos el canal de entrada
                if (salida != null)
                    salida.close(); // Cerramos el canal de salida
            } catch (IOException e) {
            }
        }
    }
    
    public static synchronized void actualizarSalida(boolean valor, int aeropuerto, int posicion){
        listasalida[aeropuerto][posicion] = valor;
    }
    
    public static synchronized boolean[][] getSalida(){
        return listasalida;
    }
}

