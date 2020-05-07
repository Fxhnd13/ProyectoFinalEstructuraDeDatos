/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos.Grafo;

import java.util.ArrayList;

/**
 *
 * @author jose_
 */
public class Ruta implements Comparable<Ruta> {
    
    ArrayList<String> nodos = new ArrayList<String>();
    double pesoTotal;

    public double getPesoTotal() {
        return pesoTotal;
    }

    public void setPesoTotal(double pesoTotal) {
        this.pesoTotal = pesoTotal;
    }

    public ArrayList<String> getNodos() {
        return nodos;
    }

    public void setNodos(ArrayList<String> nodos) {
        this.nodos = nodos;
    }

    @Override
    public int compareTo(Ruta o) {
        if(o.getPesoTotal()>pesoTotal){
            return -1;
        }else if(o.getPesoTotal()>pesoTotal){
            return 0;
        }else{
            return 1;
        }
    }
    
}
