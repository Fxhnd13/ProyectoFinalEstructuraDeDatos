/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Grafo.Grafo;
import Modelos.Grafo.Nodo;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jose_
 */
public class VistaIngresoController implements Initializable {

    @FXML
    private Button CargarMapaButton;
    @FXML
    private Button VerMapaButton;
    @FXML
    private ComboBox<String> PosicionesComboBox;

    private Grafo grafo;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void CargarMapa(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("No seleccionó un archivo.");
            alert.showAndWait();
        }else{
            this.grafo = ArchivosController.lecturaDeArchivo(selectedFile);
            ObservableList<String> nodosCombo = FXCollections.observableArrayList();
            for (Nodo nodo : grafo.getNodos()) {
                nodosCombo.add(nodo.getIdentidad());
            }
            this.PosicionesComboBox.setItems(nodosCombo);
            this.PosicionesComboBox.getSelectionModel().select(0);
            this.PosicionesComboBox.setDisable(false);
            this.VerMapaButton.setDisable(false);
        }
    }

    @FXML
    private void VerMapa(ActionEvent event) {
        try {
            // Cargo la vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/VistaPrincipal.fxml"));

            // Cargo el padre
            Parent root = loader.load();

            // Obtengo el controlador
            VistaPrincipalController controlador = loader.getController();
            controlador.inicializar(this.grafo, this.PosicionesComboBox.getSelectionModel().getSelectedIndex());

            // Creo la scene y el stage
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            // Asocio el stage con el scene
            stage.setScene(scene);
            stage.show();

            // Indico que debe hacer al cerrar
            stage.setOnCloseRequest(e -> controlador.closeWindow());

            // Ciero la ventana donde estoy
            Stage myStage = (Stage) this.CargarMapaButton.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Error desconocido en inicio de sesion administrador.");
            alert.showAndWait();
        }
    }
    
}
