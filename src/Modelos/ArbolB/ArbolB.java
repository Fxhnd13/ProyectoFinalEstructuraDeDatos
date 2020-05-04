/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos.ArbolB;

import Modelos.Grafo.Arista;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author jose_
 */
public class ArbolB {
    
    int grado;
    ArrayList<Integer> claves;
    ArrayList<ArbolB> hijos;

    public int getGrado() {
        return grado;
    }

    public void setGrado(int grado) {
        this.grado = grado;
    }

    public ArrayList<Integer> getClaves() {
        return claves;
    }

    public void setClaves(ArrayList<Integer> claves) {
        this.claves = claves;
    }

    public ArrayList<ArbolB> getHijos() {
        return hijos;
    }

    public void setHijos(ArrayList<ArbolB> hijos) {
        this.hijos = hijos;
    }
    
    public ArbolB(int grado){
        this.grado = grado;
        this.claves = new ArrayList<Integer>();
        this.hijos = new ArrayList<ArbolB>();
    }
    
    //metodo que borra la clave más a la derecha del nodo seleccionado
    public boolean delete(){
        boolean minimo = false;
        this.claves.remove(this.claves.size()-1);
        int factor = (this.claves.size()/ 2)-1;
        if(this.claves.size() < (factor)) minimo = true;
        return minimo;
    }
    
    //Inserta la clave en el nodo indicado
    public boolean insert(int clave){
        boolean sobrePaso = false;
        int indice;//obtenemos el indice donde deberá posicionarse el valor
        for (indice = 0; indice < this.claves.size(); indice++) {//recorremos las claves que tengamos
            if(this.claves.get(indice) > clave) break; //cuando obtengamos una clave mayor detenemos el ciclo
        }
        if((this.claves.isEmpty()) || (indice == (this.claves.size()))){ //si se agrega al final, usamos add(clave)
            this.claves.add(clave);
            if(this.claves.size() > this.grado-1) sobrePaso = true; //verificamos si sobrepaso el limite de claves
        }else{
            this.claves.add(indice, clave);//si se agrega en una posicion que no sea al final, usamos add(indice, clave)
            if(this.claves.size() > this.grado-1) sobrePaso = true;//verificamos si sobrepaso el limite de claves
        }
        return sobrePaso;
    }
    
    public boolean eliminarClave(){
        boolean minimo = false;
        if(this.hijos.isEmpty()){
            minimo = this.delete();
        }else{
            if(this.eliminarClave()){
                int factor = (this.hijos.get(this.hijos.size()-2).getClaves().size()/2);
                if(this.hijos.get(this.hijos.size()-2).getClaves().size() > factor){
                    int temporal = this.claves.get(this.claves.size()-1);
                    this.hijos.get(this.hijos.size()-1).getClaves().add(0, temporal);
                    this.claves.set(this.claves.size()-1, this.hijos.get(this.hijos.size()-2).getClaves().get(this.hijos.get(this.hijos.size()-2).getClaves().size()-1));
                    this.hijos.get(this.hijos.size()-2).getClaves().remove(this.hijos.get(this.hijos.size()-2).getClaves().size()-1);
                }else{
                    if(this.hijos.size() > 2){
                        int indiceClave = this.claves.size()-1;
                        this.hijos.get(indiceClave).getClaves().add(this.claves.get(indiceClave));
                        for (Integer clave : this.hijos.get(indiceClave+1).getClaves()) {
                            this.hijos.get(indiceClave+1).getClaves().add(clave);
                        }
                        this.hijos.remove(indiceClave+1);
                        this.claves.remove(indiceClave);
                    }else{
                        minimo = true;
                    }
                }
            }
        }
        return minimo;
    }
    
    //Busca el nodo en el que deberá insertarse la clave ingresada
    public boolean insertar(int clave){
        boolean sobrePaso = false; //si hace falta dividir el arbol hoja
        if(this.hijos.isEmpty()){ //si es nodo hoja, agregamos el valor
            sobrePaso = this.insert(clave);//hace falta dividir el nodo por encima de este
        }else{//si tiene hijos
            int indiceHijo = getIndiceDeClaveEnHijos(clave); //obtenemos el indice del arreglo de hijos donde debería ir insertado
            if(this.hijos.get(indiceHijo).insertar(clave)){ //si el hijo está sobrecargado
                int indiceDeClaveSubida = (Integer) this.hijos.get(indiceHijo).getClaves().size() / 2; //obtenemos el indice medio de la clave
                int claveSubida = this.hijos.get(indiceHijo).getClaves().get(indiceDeClaveSubida); //obtenemos la clave en el indice
                ArrayList<Integer> izquierda = new ArrayList<Integer>();
                ArrayList<Integer> derecha = new ArrayList<Integer>();
                for (int i = 0; i < indiceDeClaveSubida; i++) {//guardamos lo que esta a la izquierda de la clave que queremos subir
                    izquierda.add(this.hijos.get(indiceHijo).getClaves().get(i));
                }
                for (int i = (indiceDeClaveSubida+1); i < this.hijos.get(indiceHijo).getClaves().size(); i++) {//guardamos lo que está a la izquierda de la clave que quermos subir
                    derecha.add(this.hijos.get(indiceHijo).getClaves().get(i));
                }
                this.hijos.get(indiceHijo).getClaves().clear();//limpiamos las claves que tenía el hijo
//                
                int indice;//obtenemos el indice donde deberá posicionarse el valor
                for (indice = 0; indice < this.claves.size(); indice++) {//recorremos las claves que tengamos
                    if(this.claves.get(indice) > claveSubida) break; //cuando obtengamos una clave mayor detenemos el ciclo
                }
                sobrePaso = this.insert(claveSubida); //si hace falta subir un nivel hacia arriba
                ArbolB nodoIzquierdo = new ArbolB(this.grado);//creamos el nodo izquierdo de la division
                ArbolB nodoDerecho = new ArbolB(this.grado);//creamos el nodo derecho de la division
                nodoIzquierdo.setClaves(izquierda);//agregamos las claves a el nodo izquierdo
                nodoDerecho.setClaves(derecha);//agregamos las claves al nodo derecho
                
                if(this.hijos.isEmpty() || (indice == (this.hijos.size()-1))){
                    if(this.hijos.isEmpty()){
                        this.hijos.add(nodoIzquierdo);
                        this.hijos.add(nodoDerecho);
                    }else{
                        if(indice == (this.hijos.size()-1)){
                            this.hijos.set(indice, nodoIzquierdo);
                        }
                        this.hijos.add(nodoDerecho);
                    }
                }else{
                    this.hijos.set(indice, nodoIzquierdo);
                    this.hijos.add(indice+1, nodoDerecho);
                }
            }
        }
        return sobrePaso;
    }
    
    public int getIndiceDeClaveEnHijos(int clave){
        if(clave < this.claves.get(0)){
            return 0;
        }else{
            int valor = 1;
            for (int i = 1; i < this.claves.size(); i++) {
                if(clave > this.claves.get(i)) valor++;
            }
            return valor;
        }
    }
    
    public void CrearNuevoNodo(ArbolB padre, ArbolB hijoADividir){
        int indiceDeClaveSubida = (Integer) hijoADividir.getClaves().size() / 2; //obtenemos el indice medio de la clave
        int claveSubida = hijoADividir.getClaves().get(indiceDeClaveSubida); //obtenemos la clave en el indice
        ArrayList<Integer> izquierda = new ArrayList<Integer>();
        ArrayList<Integer> derecha = new ArrayList<Integer>();
        for (int i = 0; i < indiceDeClaveSubida; i++) {//guardamos lo que esta a la izquierda de la clave que queremos subir
            izquierda.add(hijoADividir.getClaves().get(i));
        }
        for (int i = (indiceDeClaveSubida+1); i < hijoADividir.getClaves().size(); i++) {//guardamos lo que está a la izquierda de la clave que quermos subir
            derecha.add(hijoADividir.getClaves().get(i));
        }
        
        ArbolB nodoIzquierdo = new ArbolB(hijoADividir.getGrado());//creamos el nodo izquierdo de la division
        ArbolB nodoDerecho = new ArbolB(hijoADividir.getGrado());//creamos el nodo derecho de la division
        
        if(!hijoADividir.getHijos().isEmpty()){
            for (int i = 0; i < (indiceDeClaveSubida+1); i++) {
                nodoIzquierdo.getHijos().add(hijoADividir.getHijos().get(i));
            }
            for (int i = (indiceDeClaveSubida+1); i < hijoADividir.getHijos().size(); i++) {
                nodoDerecho.getHijos().add(hijoADividir.getHijos().get(i));
            }
        }
        nodoIzquierdo.setClaves(izquierda);//agregamos las claves a el nodo izquierdo
        nodoDerecho.setClaves(derecha);//agregamos las claves al nodo derecho
        
        padre.getClaves().add(claveSubida);
        padre.getHijos().add(nodoIzquierdo);
        padre.getHijos().add(nodoDerecho);
    }
    
    public int escribirNodo(DatosArbol datos, int idPadre, int idPadrePadre){
        //if(id != idPadre) hay que señalar del padre al hijo.
        ArrayList<Integer> padres = new ArrayList<Integer>();
        if(datos.id != idPadre && datos.id != idPadrePadre) datos.cadena+="\"nodo"+(idPadrePadre)+"\":nodo"+(idPadre)+" -> nodo"+datos.id+";\n";
        datos.cadena+="nodo"+(datos.id++); padres.add(datos.id-1);
        datos.cadena+="[label=\"<nodo"+(datos.id++); padres.add(datos.id-1);
        datos.cadena+=">|<nodo"+(datos.id++);
        datos.cadena+=">"+this.getClaves().get(0)+"|<nodo"+(datos.id++); padres.add(datos.id-1);
        datos.cadena+=">";
        for (int i = 1; i < this.claves.size(); i++) {
            datos.cadena+= "|<nodo"+(datos.id++)+">"+this.claves.get(i)+"|<nodo"+(datos.id++)+">"; padres.add(datos.id-1);
        }
        datos.cadena+="\"];\n";
        if(!this.hijos.isEmpty()){
            for (int i = 0; i < this.hijos.size(); i++) {
                datos.id = this.hijos.get(i).escribirNodo(datos, padres.get(i+1), padres.get(0));
            }
        }
        return datos.id;
    }

    public void generarGrafico() {
        DatosArbol datos = new DatosArbol();
        datos.cadena += "digraph{\nnode [shape = record, height=.1];\n";
        this.escribirNodo(datos, 0, 0);
        datos.cadena += "}";
        FileWriter flwriter = null;
        try {
                //crea el flujo para escribir en el archivo
                flwriter = new FileWriter("src/Images/arbol.dot");
                //crea un buffer o flujo intermedio antes de escribir directamente en el archivo
                BufferedWriter bfwriter = new BufferedWriter(flwriter);
                bfwriter.write(datos.cadena);
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
            String [] cmd = {"dot","-Tpng","-o", "src/Images/arbol.png", "src/Images/arbol.dot"};
            Runtime.getRuntime().exec(cmd);
            
        } catch (IOException ioe) {
                System.out.println (ioe);
        }
        try {
            Thread.sleep (150);
        } catch (Exception e) {
        // Mensaje en caso de que falle
        }
    }
    
}
