/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Grafo.Grafo;
import Modelos.Grafo.Nodo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author jose_
 */
public class ArchivosController {

    public static Grafo lecturaDeArchivo(File file) {
        Grafo grafo = new Grafo();
        FileReader fr = null;
        BufferedReader br = null;

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            // Lectura del fichero
            ArrayList<String> lineas = new ArrayList<String>();
            String temporal = null;
            while ((temporal = br.readLine()) != null) {
                lineas.add(temporal);
            }
            
            for (String linea : lineas) {
                String[] datos = linea.split(",");
                grafo.agregarArista(datos[0], datos[1],
                        Double.parseDouble(datos[2]),
                        Double.parseDouble(datos[3]),
                        Double.parseDouble(datos[4]),
                        Double.parseDouble(datos[5]),
                        Double.parseDouble(datos[6]));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return grafo;
    }
}
