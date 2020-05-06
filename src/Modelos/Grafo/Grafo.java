/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos.Grafo;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author jose_
 */
public class Grafo {
    
    String posicionActual;
    ObservableList<Nodo> nodos = FXCollections.observableArrayList();;
    ObservableList<Arista> aristas = FXCollections.observableArrayList();
    
    public Arista getAristaEntre(String origen, String destino){
        Arista aristaFinal = null;
        for (Arista arista : this.aristas) {
            if(arista.getOrigen().equals(origen) && arista.getDestino().equals(destino)) aristaFinal = arista;
        }
        return aristaFinal;
    }
    
    public Nodo getNodoPorIdentidad(String nombre){
        Nodo nodo = null;
        for (Nodo nodoTemp : this.nodos) {
            if(nodoTemp.getIdentidad().equals(nombre)) nodo = nodoTemp;
        }
        return nodo;
    }
    
    public void agregarArista(String origen, String destino, double tiempoV, double tiempoC, double gasolina, double esfuerzo){
        boolean existeOrigen = false, existeDestino = false;
        for (Nodo nodo : this.nodos) {
            if(nodo.getIdentidad().equals(origen)) existeOrigen = true;
            if(nodo.getIdentidad().equals(destino)) existeDestino = true;
        }
        if(existeOrigen && existeDestino){//si existen los dos puntos a unir
            boolean existeArista = false;
            Arista arista = new Arista(origen, destino, tiempoV, tiempoC, gasolina, esfuerzo);
            for (Arista aristaTemp : this.aristas) {//verificamos que no haya ya una arista entre nuestros puntos
                if(aristaTemp.equals(arista)) existeArista = true;
            }
            if(!existeArista){
                this.aristas.add(arista);//si no existe, agregamos la arista
                getNodoPorIdentidad(origen).getNodos().add(getNodoPorIdentidad(destino));
            }
        }else{
            if(existeOrigen && !existeDestino){
                Nodo nuevoDestino = new Nodo(destino);
                this.nodos.add(nuevoDestino);
                getNodoPorIdentidad(origen).getNodos().add(nuevoDestino);
            }
            if(!existeOrigen && existeDestino){
                Nodo nuevoOrigen = new Nodo(origen);
                nuevoOrigen.getNodos().add(getNodoPorIdentidad(destino));
                this.nodos.add(nuevoOrigen);
            }
            if(!existeOrigen && !existeDestino){
                Nodo nuevoOrigen = new Nodo(origen);
                Nodo nuevoDestino = new Nodo(destino);
                nuevoOrigen.getNodos().add(nuevoDestino);
                this.nodos.add(nuevoOrigen);
                this.nodos.add(nuevoDestino);
            }
            Arista arista = new Arista(origen, destino, tiempoV, tiempoC, gasolina, esfuerzo);
            this.aristas.add(arista);
        }
    }

    public String getPosicionActual() {
        return posicionActual;
    }

    public void setPosicionActual(String posicionActual) {
        this.posicionActual = posicionActual;
    }

    public ObservableList<Nodo> getNodos() {
        return nodos;
    }

    public void setNodos(ObservableList<Nodo> nodos) {
        this.nodos = nodos;
    }

    public ObservableList<Arista> getAristas() {
        return aristas;
    }

    public void setAristas(ObservableList<Arista> aristas) {
        this.aristas = aristas;
    }

    public void generarImagen(boolean datos, int opcion) {
        FileWriter flwriter = null;
        try {
            String cadena = "digraph G{\n     node[shape=circle];\n"
                + "     node[style=filled];\n"
                + "     node[fillcolor=\"#EEEEEE\"];\n"
                + "     edge[color=\"#31CEF0\"];";
            if(opcion == 1) cadena+= "     edge[arrowhead=\"none\"];";
            for (Arista arista : aristas) {
                String datosArista = "[label=\"Distancia: "+arista.getDistancia()+"\nTiempo: "+arista.getTiempoV()+"\nGasolina: "+arista.getGasolina()+"\"];";
                cadena += "\n     "+arista.getOrigen()+"->"+arista.getDestino();
                cadena += (datos)? datosArista : ";";
            }
            cadena += "\n     "+posicionActual+"[fillcolor=\"forestgreen\"];\n     rankdir=LR;\n}";
                //crea el flujo para escribir en el archivo
                flwriter = new FileWriter("src/Images/grafo.dot");
                //crea un buffer o flujo intermedio antes de escribir directamente en el archivo
                BufferedWriter bfwriter = new BufferedWriter(flwriter);
                bfwriter.write(cadena);
                //cierra el buffer intermedio
                bfwriter.close();
        } catch (IOException e) {
                e.printStackTrace();
        } finally {
                if (flwriter != null) {
                        try {//cierra el flujo principal
                                flwriter.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }
        
        try {
            String [] cmd = {"dot","-Tpng","-o", "src/Images/grafo.png", "src/Images/grafo.dot"};
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ioe) {
                System.out.println (ioe);
        }
        
        try {
            Thread.sleep (200);
        } catch (Exception e) {
        // Mensaje en caso de que falle
        }
        
    }
        
}
