/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aerocomp;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author ruben
 */
public class Gestor {
    //Todo se implementa mediante listas para que varie dependiendo del aeropuerto
    private AtomicInteger[] personasaeropuerto = new AtomicInteger[2];
    private Semaphore[] semaforospuertaembarque = new Semaphore[2];
    private Semaphore[] semaforospuertadesembarque = new Semaphore[2];
    private Semaphore[] semaforospuertasmixtas = new Semaphore[2];
    private AtomicBoolean[] arraypuertas = new AtomicBoolean[12];
    private Semaphore[] semaforospistas = new Semaphore[2];
    private AtomicBoolean[] arraypistas = new AtomicBoolean[8];
    private Semaphore[] talleres = new Semaphore[2];
    private Semaphore[] entradatalleres = new Semaphore[2];
    private Random r = new Random();

    public Gestor() {
        //Inicializa todos los arrays
        Arrays.fill(personasaeropuerto, new AtomicBoolean(false));
        Arrays.fill(semaforospuertasmixtas, new Semaphore(1, true));
        Arrays.fill(semaforospuertasmixtas, new Semaphore(1, true));
        Arrays.fill(semaforospuertasmixtas, new Semaphore(6, true));
        Arrays.fill(arraypuertas, new AtomicBoolean(false));
        Arrays.fill(semaforospistas, new Semaphore(4, true));
        Arrays.fill(arraypistas, new AtomicBoolean(false));
        Arrays.fill(talleres, new Semaphore(20, true));
        Arrays.fill(entradatalleres, new Semaphore(1));
    }
    
    public void autobusACiudad(int id){
        
    }
    
    public int autobusCargarCiudad(String id) throws InterruptedException{
        Thread.sleep(r.nextInt(2000,5000));
        int pasajeros = r.nextInt(0,50);
        System.out.println("Se montan " + pasajeros + " pasajeros en el bus " + id);
        return pasajeros;
    }
    
    public void moverAutobus(String id, int ciudad, Boolean sentido) throws InterruptedException{
        Thread.sleep(r.nextInt(5000,10000));
    }
}