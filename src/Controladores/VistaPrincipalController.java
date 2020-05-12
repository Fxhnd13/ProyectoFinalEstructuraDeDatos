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
import Modelos.Grafo.Ruta;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import javafx.scene.layout.Region;
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
    
    @FXML
    private ComboBox<String> ClasificacionRutas;
    @FXML
    private ComboBox<String> DestinoComboBox1;
    @FXML
    private TextField PosicionActualTexto;
    
    
    private Grafo grafo;
    private ArbolB arbol = new ArbolB(5);
    private ArrayList<Ruta> rutasActivas = new ArrayList<Ruta>();
    @FXML
    private ComboBox<Integer> IdRutasComboBox;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    //metodo que carga todo lo visual que hay en el programa
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
            File img = new File("src/Images/grafo.png");
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
        this.limpiarArbol();
        this.rutasActivas.clear(); //limpiamos las rutas posibles que puedan haber cargadas
        int clasificacionRutas = this.ClasificacionRutas.getSelectionModel().getSelectedIndex(); //miramos como se quieren consultar las rutas
        //Aqui dentro se consultan las rutas
        switch(this.TipoMovimientoComboBox.getSelectionModel().getSelectedIndex()){
            case 0:{//si es en vehiculo
                if(this.ClasificacionRutas.getSelectionModel().getSelectedIndex()==0){//obtenemos el tipo de clasificacion de las rutas que deseamos
                    Ruta temporal = new Ruta();//agregamos la ruta inicial partiendo del punto origen seleccionado
                    temporal.getNodos().add(this.OrigenComboBox.getSelectionModel().getSelectedItem());
                    //cargamos todas las rutas posibles a partir de ese punto
                    this.grafo.cargarTodasLasRutas(rutasActivas, temporal, this.DestinoComboBox.getSelectionModel().getSelectedItem(), 0, 0);
                    //las ordenamos por coste, segun lo que se haya especificado antes
                    Collections.sort(rutasActivas);
                }else{
                    if(clasificacionRutas>0 && clasificacionRutas<9){
                        //aqui es donde se muestran las mejores y peores rutas según lo especificado
                        Ruta temporal = new Ruta();
                        temporal.getNodos().add(this.OrigenComboBox.getSelectionModel().getSelectedItem());
                        this.grafo.cargarTodasLasRutas(rutasActivas, temporal, this.DestinoComboBox.getSelectionModel().getSelectedItem(), 0, clasificacionRutas);
                        Collections.sort(rutasActivas);
                        mostrarMensajeDeRutas(0, clasificacionRutas);
                    }
                }
                break;
            }
            case 1:{
                if(this.ClasificacionRutas.getSelectionModel().getSelectedIndex()==0){
                    Ruta temporal = new Ruta();
                    temporal.getNodos().add(this.OrigenComboBox.getSelectionModel().getSelectedItem());
                    this.grafo.cargarTodasLasRutas(rutasActivas, temporal, this.DestinoComboBox.getSelectionModel().getSelectedItem(), 1, 0);
                    Collections.sort(rutasActivas);
                }else{
                    if(clasificacionRutas>0 && clasificacionRutas<9){
                        //aqui es donde se muestran las mejores y peores rutas según lo especificado
                        Ruta temporal = new Ruta();
                        temporal.getNodos().add(this.OrigenComboBox.getSelectionModel().getSelectedItem());
                        this.grafo.cargarTodasLasRutas(rutasActivas, temporal, this.DestinoComboBox.getSelectionModel().getSelectedItem(), 1, clasificacionRutas);
                        Collections.sort(rutasActivas);
                        mostrarMensajeDeRutas(1, clasificacionRutas);
                    }
                }
                break;
            }
        }
        //mostramos las rutas que se calcularon
        if(clasificacionRutas == 0){
            String mensaje = "";
            int id = 1;
            for (Ruta rutasActiva : rutasActivas) {
                mensaje+=id+") ";
                for (String nodo : rutasActiva.getNodos()) {
                    mensaje+=nodo+"->";
                }
                mensaje = mensaje.substring(0, mensaje.length()-2);
                mensaje+="\n\n";
                id++;
            }
            this.mostrarMensajeInformativo("Las Rutas Disponibles son: \n\n"+mensaje);
        }
        //agreagamos el listado de rutas a el listado del arbolB
        ObservableList<Integer> rutas = FXCollections.observableArrayList();
        for (int i = 0; i < rutasActivas.size(); i++) {
            rutas.add(i+1);
        }
        this.IdRutasComboBox.setItems(rutas);
        for (int i = 0; i < rutasActivas.size(); i++) {
            this.DatoArbol.setText(String.valueOf(i));
            this.InsertarDato(null);
        }
        this.DatoArbol.setText("");
    }

    @FXML
    private void CambiarPosicion(ActionEvent event) {
        String origen = grafo.getPosicionActual(); //el punto en el que estamos
        String destino = this.DestinoComboBox1.getSelectionModel().getSelectedItem(); //el punto al que queremos movernos (debe ser adyacente)
        boolean seMovio = false;
        if((!destino.isEmpty()||(destino!=null))){//si seleccionó un destino
            if(!grafo.getPosicionActual().equals(destino)){//y no nos encontramos en ese punto
                if(this.TipoMovimientoComboBox.getSelectionModel().getSelectedIndex()==0){//elegimos un tipo de movimiento
                    if(grafo.getAristaEntre(origen, destino) != null){//si existe una ruta de origen a destino
                        seMovio = true;
                    }else{
                        this.mostrarMensajeError("No se puede mover a la posicion especifiada en vehiculo, no hay vía.");
                    }
                }else{
                    if((grafo.getAristaEntre(origen, destino)!=null)||(grafo.getAristaEntre(destino, origen)!=null)){//si es adyacente
                        seMovio = true;
                    }
                }
            }else{
                this.mostrarMensajeError("Ya se encuentra en la posicion indicada.");
            }
        }else{
            this.mostrarMensajeError("No selecciono una ubicacion destino.");
        }
        if(seMovio){
            this.grafo.setPosicionActual(destino);//nos movemos al destino
            this.PosicionActualTexto.setText(this.grafo.getPosicionActual());
            this.RecargarImagen(null);//recargamos la imagen
            this.mostrarMensajeInformativo("Se ha movido con éxito de "+origen+" hacia "+destino); seMovio= true;
            if(!rutasActivas.isEmpty()){//si hay una o varias rutas cargadas entonces
                if(rutasActivas.get(0).getNodos().get(0).equals(origen)){
                    seMovio = false;//utilizamos seMovio para saber si el movimiento fue sobre la alguna de las rutas calculadas
                    for (Ruta ruta : rutasActivas) {//por cada ruta precargada que tengamos
                        if(ruta.getNodos().get(1).equals(destino)) seMovio = true;
                    }
                    if(seMovio){//si se movio sobre la ruta
                        for (int i = 0; i < rutasActivas.size(); i++) { //removemos todas las rutas que no pertenezcan
                            if(this.rutasActivas.get(i).getNodos().get(1)!=this.grafo.getPosicionActual()){
                                this.rutasActivas.remove(i);//removemos la ruta si no coincide con el movimiento que hicimos
                            }else{
                                this.rutasActivas.get(i).getNodos().remove(0);//eliminamos el nodo incial de cada ruta, si el movimiento coincide
                            }
                        }
                    }else{//si no se movio sobre la ruta
                        this.OrigenComboBox.getSelectionModel().select(this.grafo.getPosicionActual());//cambiamos el origen 
                        String destinoTemporal = this.rutasActivas.get(0).getNodos().get(this.rutasActivas.get(0).getNodos().size()-1);
                        this.DestinoComboBox.getSelectionModel().select(destinoTemporal);
                        this.ConsultarCamino(null);
                    }
                }else{
                    this.rutasActivas.clear();
                    this.limpiarArbol();
                }
            }
        }
    }

    @FXML
    private void RecargarImagen(ActionEvent event) {
        this.cargarImagen(this.VerDatosCheckBox.isSelected(), this.TipoMovimientoComboBox.getSelectionModel().getSelectedIndex());
        this.recargarClasificacionRutas();
    }

    @FXML
    private void InsertarDato(ActionEvent event) {
            int clave = Integer.parseInt(this.DatoArbol.getText());
            if(this.arbol.insertar(clave)){
                ArbolB nuevoNodo = new ArbolB(this.arbol.getGrado());
                this.arbol.CrearNuevoNodo(nuevoNodo, this.arbol);
                this.arbol = nuevoNodo;
            }
            recargarArbolB();
    }
        
    public void recargarArbolB(){
        if(this.arbol.getClaves().size()>0){
            InputStream isImage = null;
            try{
                this.arbol.generarGrafico();
                File img = new File("src/Images/arbol.png");
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
        }else{
            this.ImagenArbol.setImage(null);
        }
    }
    
    public void mostrarMensajeError(String mensaje){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText(mensaje);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }
    
    public void mostrarMensajeInformativo(String mensaje){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Informacion");
        alert.setContentText(mensaje);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    @FXML
    private void EliminarClave(ActionEvent event) {
        if(!arbol.getHijos().isEmpty()){
            if(arbol.eliminarClave()){
                int padreTemp = arbol.getClaves().get(0);
                ArrayList<Integer> hijosDerecha = arbol.getHijos().get(1).getClaves();
                arbol.getHijos().remove(1);
                ArbolB auxiliar = arbol.getHijos().get(0);
                auxiliar.getClaves().add(padreTemp);
                for (Integer clave : hijosDerecha) {
                    auxiliar.getClaves().add(clave);
                }
                arbol = auxiliar;
            }   
        }else{
            if(!arbol.getClaves().isEmpty()){
                arbol.getClaves().remove(arbol.getClaves().size()-1);
            }
        }
        this.recargarArbolB();
    }
    
    private void limpiarArbol(){
        while(this.arbol.getClaves().size()>0){
            this.EliminarClave(null);
        }
    }

    private void recargarClasificacionRutas() {
        ObservableList<String> funcionalidades = FXCollections.observableArrayList();
        funcionalidades.add("Todas las rutas posibles");
        funcionalidades.add("Mejor ruta por Distancia");
        funcionalidades.add("Peor ruta por distancia");
        funcionalidades.add("Mejor ruta por tiempo");
        funcionalidades.add("Peor ruta por tiempo");
        switch(this.TipoMovimientoComboBox.getSelectionModel().getSelectedIndex()){
            case 0:{
                funcionalidades.add("Mejor ruta por gasolina");
                funcionalidades.add("Peor ruta por gasolina");
                funcionalidades.add("Mejor ruta por gasolina y distancia");
                funcionalidades.add("Peor ruta por gasolina y distancia");
                break;
            }
            case 1:{
                funcionalidades.add("Mejor ruta por esfuerzo");
                funcionalidades.add("Peor ruta por esfuerzo");
                funcionalidades.add("Mejor ruta por esfuerzo y distancia");
                funcionalidades.add("Peor ruta por esfuerzo y distancia");
                break;
            }
        }
        this.ClasificacionRutas.setItems(funcionalidades);
    }
    
    public void mostrarMensajeDeRutas(int tipoMovimiento, int tipoReporte){
        String mensaje = "La ";
        mensaje+= this.ClasificacionRutas.getItems().get(tipoReporte)+" es: \n";
        Ruta rutasActiva = null;
        if(this.ClasificacionRutas.getItems().get(tipoReporte).contains("Peor")){
            //peor
            rutasActiva = rutasActivas.get(rutasActivas.size()-1);
            for (String nodo : rutasActiva.getNodos()) {
                mensaje+=nodo+"->";
            }
            mensaje = mensaje.substring(0, mensaje.length()-2);
            mensaje+="\n\n";
        }else{
            //mejor
            rutasActiva = rutasActivas.get(0);
            for (String nodo : rutasActiva.getNodos()) {
                mensaje+=nodo+"->";
            }
            mensaje = mensaje.substring(0, mensaje.length()-2);
            mensaje+="\n\n";
        }
        mensaje+="Con un total de: "+rutasActiva.getPesoTotal();
        this.mostrarMensajeInformativo(mensaje);
    }

    @FXML
    private void MoverseUnEspacioEnRuta(ActionEvent event) {
        if(this.rutasActivas.isEmpty()){
            this.mostrarMensajeError("No hay una ruta calculada con anterioridad");
        }else{
            int idRuta = this.IdRutasComboBox.getSelectionModel().getSelectedIndex();
            if(this.grafo.getPosicionActual().equals(this.rutasActivas.get(idRuta).getNodos().get(0))){
                this.DestinoComboBox1.getSelectionModel().select(this.rutasActivas.get(idRuta).getNodos().get(1));
                this.CambiarPosicion(null);
                for (int i = 0; i < rutasActivas.size(); i++) {
                        if(this.rutasActivas.get(i).getNodos().get(1)!=this.grafo.getPosicionActual()){
                            this.rutasActivas.remove(i);
                        }else{
                            this.rutasActivas.get(i).getNodos().remove(0);
                        }
                    if(this.rutasActivas.get(i).getNodos().size()==1){
                        this.mostrarMensajeInformativo("Ya se llegó al final de la ruta");
                        this.rutasActivas.clear();
                    }
                }
            }
        }
    }


    @FXML
    private void VerRuta(ActionEvent event) {
        String mensaje = "";
        int id = this.IdRutasComboBox.getSelectionModel().getSelectedIndex();
        mensaje+=(id+1)+") ";
        for (String nodo : this.rutasActivas.get(id).getNodos()) {
            mensaje+=nodo+"->";
        }
        mensaje = mensaje.substring(0, mensaje.length()-2);
        this.mostrarMensajeInformativo("La ruta está conformada así: \n\n");
    }

    @FXML
    private void VerRutas(ActionEvent event) {
        //mostramos las rutas que se calcularon
        String mensaje = "";
        int id = 1;
        for (Ruta rutasActiva : rutasActivas) {
            mensaje+=id+") ";
            for (String nodo : rutasActiva.getNodos()) {
                mensaje+=nodo+"->";
            }
            mensaje = mensaje.substring(0, mensaje.length()-2);
            mensaje+="\n\n";
            id++;
        }
        this.mostrarMensajeInformativo("Las Rutas Disponibles son: \n"+mensaje);
    }
    
}
