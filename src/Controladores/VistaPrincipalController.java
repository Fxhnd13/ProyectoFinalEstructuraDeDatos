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
import javafx.scene.control.Alert;
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
    @FXML
    private ComboBox<String> ClasificacionRutas;
    @FXML
    private ComboBox<String> DestinoComboBox1;
    @FXML
    private TextField PosicionActualTexto;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void inicializar(Grafo grafo, String posicionActual){
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
        this.grafo.setPosicionActual(posicionActual);
        this.PosicionActualTexto.setText(posicionActual);
        this.DestinoComboBox1.setItems(nodosCombo);
        this.DestinoComboBox.setItems(nodosCombo);
        
        this.TipoMovimientoComboBox.getSelectionModel().select(0);
        
        this.recargarClasificacionRutas();
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
            this.mostrarMensajeError("Se produjo un error al acceder al archivo \"grafo.png\"");
        } finally {
            try {
                isImage.close();
            } catch (IOException ex) {
                this.mostrarMensajeError("Se produjo un error al cerrar el Inputstream para la imagen del grafo.");
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
            this.mostrarMensajeError("Se produjo un error al cerrar esta pestaña.");
        }
    }
    
    @FXML
    private void ConsultarCamino(ActionEvent event) {
    }


    @FXML
    private void CambiarPosicion(ActionEvent event) {
        String origen = grafo.getPosicionActual();
        String destino = this.DestinoComboBox1.getSelectionModel().getSelectedItem();
        if((!destino.isEmpty()||(destino!=null))){
            if(!grafo.getPosicionActual().equals(destino)){
                if(this.TipoMovimientoComboBox.getSelectionModel().getSelectedIndex()==0){    
                    if(grafo.getAristaEntre(origen, destino) != null){
                        this.grafo.setPosicionActual(destino);
                        this.PosicionActualTexto.setText(this.grafo.getPosicionActual());
                        this.RecargarImagen(null);
                        this.mostrarMensajeInformativo("Se ha movido con éxito de "+origen+" hacia "+destino);
                    }else{
                        this.mostrarMensajeError("No se puede mover a la posicion especifiada en vehiculo, no hay vía.");
                    }
                }else{
                    if((grafo.getAristaEntre(origen, destino)!=null)||(grafo.getAristaEntre(destino, origen)!=null)){
                        this.grafo.setPosicionActual(destino);
                        this.PosicionActualTexto.setText(this.grafo.getPosicionActual());
                        this.RecargarImagen(null);
                        this.mostrarMensajeInformativo("Se ha movido con éxito de "+origen+" hacia "+destino);
                    }
                }
            }else{
                this.mostrarMensajeError("Ya se encuentra en la posicion indicada.");
            }
        }else{
            this.mostrarMensajeError("No selecciono una ubicacion destino.");
        }
    }

    @FXML
    private void RecargarImagen(ActionEvent event) {
        this.cargarImagen(this.VerDatosCheckBox.isSelected(), this.TipoMovimientoComboBox.getSelectionModel().getSelectedIndex());
        this.recargarClasificacionRutas();
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
            this.DatoArbol.setText("");
        } catch (FileNotFoundException ex) {
            this.mostrarMensajeError("Se produjo un error al acceder al archivo \"arbol.png\"");
        } finally {
            try {
                isImage.close();
            } catch (IOException ex) {
                this.mostrarMensajeError("Se produjo un error al cerrar el Inputstream de la imagen arbol");
            }
        }
    }
    
    public void mostrarMensajeError(String mensaje){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    public void mostrarMensajeInformativo(String mensaje){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Informacion");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void EliminarClave(ActionEvent event) {
    }

    private void recargarClasificacionRutas() {
        ObservableList<String> funcionalidades = FXCollections.observableArrayList();
        switch(this.TipoMovimientoComboBox.getSelectionModel().getSelectedIndex()){
            case 0:{
                funcionalidades.add("Mejor ruta por gasolina");
                funcionalidades.add("Peor ruta por gasolina");
                funcionalidades.add("Mejor ruta por distancia");
                funcionalidades.add("Peor ruta por distancia");
                funcionalidades.add("Mejor ruta por gasolina y distancia");
                funcionalidades.add("Peor ruta por gasolina y distancia");
                break;
            }
            case 1:{
                funcionalidades.add("Mejor ruta por esfuerzo");
                funcionalidades.add("Peor ruta por esfuerzo");
                funcionalidades.add("Mejor ruta por distancia");
                funcionalidades.add("Peor ruta por distancia");
                funcionalidades.add("Mejor ruta por esfuerzo y distancia");
                funcionalidades.add("Peor ruta por esfuerzo y distancia");
                break;
            }
        }
        this.ClasificacionRutas.setItems(funcionalidades);
    }
}
