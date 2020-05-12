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
    
    public void agregarArista(String origen, String destino, double tiempoV, double tiempoC, double gasolina, double esfuerzo, double distancia){
        boolean existeOrigen = false, existeDestino = false;
        for (Nodo nodo : this.nodos) {
            if(nodo.getIdentidad().equals(origen)) existeOrigen = true;
            if(nodo.getIdentidad().equals(destino)) existeDestino = true;
        }
        if(existeOrigen && existeDestino){//si existen los dos puntos a unir
            boolean existeArista = false;
            Arista arista = new Arista(origen, destino, tiempoV, tiempoC, gasolina, esfuerzo, distancia);
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
            Arista arista = new Arista(origen, destino, tiempoV, tiempoC, gasolina, esfuerzo, distancia);
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
            if(opcion == 1){
                cadena+= "     edge[arrowhead=\"none\"];";
                for (Arista arista : aristas) {
                    String datosArista = "[label=\"Distancia: "+arista.getDistancia()+"\nTiempo: "+arista.getTiempoC()+"\nEsfuerzo: "+arista.getEsfuerzo()+"\"];";
                    cadena += "\n     "+arista.getOrigen()+"->"+arista.getDestino();
                    cadena += (datos)? datosArista : ";";
                }
            }else{
                for (Arista arista : aristas) {
                    String datosArista = "[label=\"Distancia: "+arista.getDistancia()+"\nTiempo: "+arista.getTiempoV()+"\nGasolina: "+arista.getGasolina()+"\"];";
                    cadena += "\n     "+arista.getOrigen()+"->"+arista.getDestino();
                    cadena += (datos)? datosArista : ";";
                }
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
//            aquí es donde tenemos que ejecutar el comando apra crear la imagen del grafo 
            String [] cmd = {"dot","-Tpng","-o", "src/Images/grafo.png", "src/Images/grafo.dot"};
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ioe) {
                System.out.println (ioe);
        }
        
        try {
            Thread.sleep (300);
        } catch (Exception e) {
        // Mensaje en caso de que falle
        }
        
    }

    public ArrayList<Nodo> nodosAccesibles(String origen, int opcion){
        ArrayList<Nodo> nodosAccesibles = new ArrayList<Nodo>();
        for (Nodo nodo : this.nodos) {
            switch(opcion){
                case 0:{
                    if(getAristaEntre(origen, nodo.getIdentidad())!=null) nodosAccesibles.add(nodo);
                    break;
                }
                case 1:{
                    if((getAristaEntre(nodo.getIdentidad(), origen)!=null)||(getAristaEntre(origen, nodo.getIdentidad())!=null)) nodosAccesibles.add(nodo);
                    break;
                }
            }
        }
        return nodosAccesibles;
    }
    
    public void cargarTodasLasRutas(ArrayList<Ruta> rutas, Ruta ruta, String destino, int opcion, int opcionPeso){
        Nodo nodoActual = this.getNodoPorIdentidad(ruta.getNodos().get(ruta.getNodos().size()-1));//obtenemos el nodo en el que nos encontramos
        if(nodoActual.getIdentidad().equals(destino)){
            rutas.add(ruta);
        }else{
            for (Nodo nodo : this.nodosAccesibles(nodoActual.getIdentidad() , opcion)) {
                if(!yaEstuvimosEn(ruta, nodo.getIdentidad())){
                    Ruta temporal = new Ruta();
                    for (String nodo1 : ruta.getNodos()) {
                        temporal.getNodos().add(nodo1);
                    }
                    temporal.setPesoTotal(ruta.getPesoTotal());
                    temporal.getNodos().add(nodo.getIdentidad());
                    if(opcionPeso!=0){
                        temporal.setPesoTotal(temporal.getPesoTotal()+this.pesoEntre(nodoActual.getIdentidad(), nodo.getIdentidad(), opcion, opcionPeso));
                    }
                    cargarTodasLasRutas(rutas, temporal, destino, opcion, opcionPeso);
                }
            }
        }
    }

    private boolean yaEstuvimosEn(Ruta ruta, String lugar) {
        boolean valor = false;
        for (int i = 0; i < ruta.getNodos().size(); i++) {
            if(ruta.getNodos().get(i).equals(lugar)) valor = true;
        }
        return valor;
    }
    
    private double pesoEntre(String origen, String destino, int opcion, int opcionPeso){
        double valor = 0;
        switch(opcion){
            case 0:{//si es en vehiculo
                if(opcionPeso == 1 || opcionPeso == 2) valor = getAristaEntre(origen, destino).getDistancia();
                if(opcionPeso == 3 || opcionPeso == 4) valor = getAristaEntre(origen, destino).getTiempoV();
                if(opcionPeso == 5 || opcionPeso == 6) valor = getAristaEntre(origen, destino).getGasolina();
                if(opcionPeso == 7 || opcionPeso == 8) valor = (getAristaEntre(origen, destino).getEsfuerzo() + getAristaEntre(origen, destino).getDistancia())/2;
                break;
            }
            case 1:{//si es caminando
                if(opcionPeso == 1 || opcionPeso == 2){
                    if(getAristaEntre(origen, destino)!=null){
                        valor = getAristaEntre(origen, destino).getDistancia();
                    }else{
                        valor = getAristaEntre(origen, destino).getDistancia();
                    }
                }
                if(opcionPeso == 3 || opcionPeso == 4){
                    if(getAristaEntre(origen, destino)!=null){
                        valor = getAristaEntre(origen, destino).getTiempoC();
                    }else{
                        valor = getAristaEntre(destino, origen).getTiempoC();
                    }
                }
                if(opcionPeso == 5 || opcionPeso == 6){
                    if(getAristaEntre(origen, destino)!=null){
                        valor = getAristaEntre(origen, destino).getEsfuerzo();
                    }else{
                        valor = getAristaEntre(origen, destino).getEsfuerzo();
                    }
                }
                if(opcionPeso == 7 || opcionPeso == 8){
                    if(getAristaEntre(origen, destino)!=null){
                        valor = (getAristaEntre(origen, destino).getEsfuerzo() + getAristaEntre(origen,destino).getDistancia())/2;
                    }else{
                        valor = (getAristaEntre(origen, destino).getEsfuerzo() + getAristaEntre(destino, origen).getDistancia())/2;
                    }
                }
                break;
            }
        }
        return valor;
    }
        
}
