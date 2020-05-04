/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.ArbolB.ArbolB;
import Modelos.Grafo.Arista;
import Modelos.Grafo.Grafo;
import Modelos.Grafo.Nodo;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jose_
 */
public class VistaPrincipalController implements Initializable {

    @FXML
    private ImageView ImagenGrafo;
    @FXML
    private ComboBox<String> TipoMovimientoComboBox;
    @FXML
    private ComboBox<String> OrigenComboBox;
    @FXML
    private ComboBox<String> DestinoComboBox;

    @FXML
    private CheckBox VerDatosCheckBox;
    @FXML
    private ImageView ImagenArbol;
    @FXML
    private TextField DatoArbol;
    
    private Grafo grafo;
    private ArbolB arbol = new ArbolB(5);
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void inicializar(Grafo grafo, int indice){
        this.grafo = grafo; //creamos el grafo para este mapa
        
        ObservableList<String> tiposMovimiento = FXCollections.observableArrayList(); //agregamos los tipos de movimiento
        tiposMovimiento.add("Vehiculo");
        tiposMovimiento.add("Caminando");
        this.TipoMovimientoComboBox.setItems(tiposMovimiento); 
        
        //agregamos los destinos y origenes disponibles
        ObservableList<String> nodosCombo = FXCollections.observableArrayList();
        for (Nodo nodo : grafo.getNodos()) {
            nodosCombo.add(nodo.getIdentidad());
        }
        this.OrigenComboBox.setItems(nodosCombo);
        this.OrigenComboBox.getSelectionModel().select(indice);
        this.grafo.setPosicionActual(grafo.getNodos().get(indice).getIdentidad());
        this.DestinoComboBox.setItems(nodosCombo);
        
        this.TipoMovimientoComboBox.getSelectionModel().select(0);
        
        //cargamos la imagen del grafo
        this.RecargarImagen(null);
    }
    
    public void cargarImagen(boolean datos, int opcion){
        InputStream isImage = null;
        try {
            this.grafo.generarImagen(datos, opcion);
            File img = new File("src\\Images\\grafo.png");
            isImage = (InputStream) new FileInputStream(img);
            this.ImagenGrafo.setImage(new Image(isImage));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VistaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                isImage.close();
            } catch (IOException ex) {
                Logger.getLogger(VistaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void closeWindow(){
        try {
            // Cargo la vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/VistaIngreso.fxml"));

            // Cargo el padre
            Parent root = loader.load();

            // Creo la scene y el stage
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            // Asocio el stage con el scene
            stage.setScene(scene);
            stage.show();

            // Ciero la ventana donde estoy
            Stage myStage = (Stage) this.ImagenGrafo.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void ConsultarCamino(ActionEvent event) {
    }


    @FXML
    private void CambiarPosicion(ActionEvent event) {
    }

    @FXML
    private void RecargarImagen(ActionEvent event) {
        this.cargarImagen(this.VerDatosCheckBox.isSelected(), this.TipoMovimientoComboBox.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void InsertarDato(ActionEvent event) {
        InputStream isImage = null;
        try{
            int clave = Integer.parseInt(this.DatoArbol.getText());
            if(this.arbol.insertar(clave)){
                ArbolB nuevoNodo = new ArbolB(this.arbol.getGrado());
                this.arbol.CrearNuevoNodo(nuevoNodo, this.arbol);
                this.arbol = nuevoNodo;
            }
            this.arbol.generarGrafico();
            File img = new File("src\\Images\\arbol.png");
            isImage = (InputStream) new FileInputStream(img);
            this.ImagenArbol.setImage(new Image(isImage));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VistaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                isImage.close();
            } catch (IOException ex) {
                Logger.getLogger(VistaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
