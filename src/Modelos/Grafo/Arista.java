/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos.Grafo;

/**
 *
 * @author jose_
 */
public class Arista {
    String origen, destino;
    double tiempoV, tiempoC, gasolina, esfuerzo, distancia;

    Arista(String origen, String destino, double tiempoV, double tiempoC, double gasolina, double esfuerzo, double distancia) {
        this.origen = origen;
        this.destino = destino;
        this.tiempoV = tiempoV;
        this.tiempoC = tiempoC;
        this.gasolina = gasolina;
        this.esfuerzo = esfuerzo;
        this.distancia = distancia;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public double getTiempoV() {
        return tiempoV;
    }

    public void setTiempoV(double tiempoV) {
        this.tiempoV = tiempoV;
    }

    public double getTiempoC() {
        return tiempoC;
    }

    public void setTiempoC(double tiempoC) {
        this.tiempoC = tiempoC;
    }

    public double getGasolina() {
        return gasolina;
    }

    public void setGasolina(double gasolina) {
        this.gasolina = gasolina;
    }

    public double getEsfuerzo() {
        return esfuerzo;
    }

    public void setEsfuerzo(double esfuerzo) {
        this.esfuerzo = esfuerzo;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }
}
