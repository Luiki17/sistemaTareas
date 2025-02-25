package lcs.tareas.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lcs.tareas.modelo.Tarea;
import lcs.tareas.servicio.TareaServicio;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;


@Component
public class IndexControlador implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);

    @Autowired
    private TareaServicio tareaServicio;

    @FXML
    private TableView<Tarea> tablaTareas;

    @FXML
    private TableColumn<Tarea, Integer> idTareaColumna;

    @FXML
    private TableColumn<Tarea, String> tareaColumna;

    @FXML
    private TableColumn<Tarea, String> responsableColumna;

    @FXML
    private TableColumn<Tarea, String> estadoColumna;

    private final ObservableList<Tarea> listaTarea = FXCollections.observableArrayList();

    @FXML
    private TextField tareaTexto;

    @FXML
    private TextField responsableTexto;

    @FXML
    private TextField estadoTexto;

    private Integer idTareaInterno;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tablaTareas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        configurarColumnas();
        listarTareas();
    }

    private void configurarColumnas(){
        idTareaColumna.setCellValueFactory(new PropertyValueFactory<>("idTarea"));
        tareaColumna.setCellValueFactory(new PropertyValueFactory<>("nombreTarea"));
        responsableColumna.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        estadoColumna.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void listarTareas() {
        logger.info("Ejecutando lista de tareas");
        listaTarea.clear();
        listaTarea.addAll(tareaServicio.listarTareas());
        tablaTareas.setItems(listaTarea);
    }

    public void agregarTarea(){
        if(tareaTexto.getText().isEmpty()){
            mostrarMensaje("Error de validación", "Debe de proporcionar una tarea");
            tareaTexto.requestFocus();
            return;
        }
        else{
            var tarea = new Tarea();
            recogerDatosFormulario(tarea);
            tarea.setIdTarea(null);
            tareaServicio.guardarTarea(tarea);
            mostrarMensaje("Información", "Tarea agregada");
            limpiarFormulario();
            listarTareas();
        }
    }

    public void cargarTarea(){
        var tarea = tablaTareas.getSelectionModel().getSelectedItem();
        if(tarea != null){
            idTareaInterno = tarea.getIdTarea();
            tareaTexto.setText(tarea.getNombreTarea());
            responsableTexto.setText(tarea.getResponsable());
            estadoTexto.setText(tarea.getEstado());
        }
    }

    public void modificarTarea(){
        if(idTareaInterno == null){
            mostrarMensaje("Información", "Debe de seleccionar una tarea");
            return;
        }
        if(tareaTexto.getText().isEmpty()){
            mostrarMensaje("Error de validación", "Debe de proporcionar una tarea");
            tareaTexto.requestFocus();
            return;
        }
        var tarea = new Tarea();
        recogerDatosFormulario(tarea);
        tareaServicio.guardarTarea(tarea);
        mostrarMensaje("Información", "Tarea modificada");
        limpiarFormulario();
        listarTareas();
    }

    public void eliminarTarea(){
        var tarea = tablaTareas.getSelectionModel().getSelectedItem();
        if(tarea != null){
            logger.info("Registro a eliminar: " + tarea.toString());
            tareaServicio.eliminarTarea(tarea);
            mostrarMensaje("Información", "Tarea eliminada: " + tarea.getIdTarea());
            limpiarFormulario();
            listarTareas();
        }
        else{
            mostrarMensaje("Error", "No se ha seleccionado ninguna tarea");
        }
    }


    private void recogerDatosFormulario(Tarea tarea){
        if(idTareaInterno != null) tarea.setIdTarea(idTareaInterno);
        tarea.setNombreTarea(tareaTexto.getText());
        tarea.setResponsable(responsableTexto.getText());
        tarea.setEstado(estadoTexto.getText());
    }

    public void limpiarFormulario(){
        idTareaInterno = null;
        tareaTexto.clear();
        responsableTexto.clear();
        estadoTexto.clear();
    }

    private void mostrarMensaje(String titulo, String mensaje){
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
