package controladores;

import Utilidades.Rutas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import modelos.Copia;
import modelos.Pelicula;
import Utilidades.Sesion;

import java.io.IOException;

public class DetallesController {

    @FXML
    private Label lbañoDetalles;

    @FXML
    private Label lbdirectorDetalles;

    @FXML
    private Label lbestadoDetalles;

    @FXML
    private Label lbgeneroDetalles;

    @FXML
    private Label lbsoporteDetalles;

    @FXML
    private Label lbtituloDetalles;

    @FXML
    private TextArea txaDescripción;

    @FXML
    private Label usuarioactualpeliculas;

    @FXML
    public void initialize() {
        usuarioactualpeliculas.setText(Sesion.usuariosesion);
    }


    @FXML
    void volver(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Rutas.TABLACOPIAS));
        AnchorPane root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Rutas.CSS);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.show();

    }



    @FXML
    void salir(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Rutas.LOGIN));
        AnchorPane root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Rutas.CSS);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.show();

    }
    // Método para establecer los detalles de la copia
    public void setDetalles(Copia copia) {
        if (copia != null) {
            lbestadoDetalles.setText(copia.getEstado());
            lbsoporteDetalles.setText(copia.getSoporte());

            // Acceder a la película asociada
            Pelicula pelicula = copia.getPelicula();
            if (pelicula != null) {
                lbtituloDetalles.setText(pelicula.getTitulo());
                lbdirectorDetalles.setText(pelicula.getDirector());
                lbgeneroDetalles.setText(pelicula.getGenero());
                lbañoDetalles.setText(String.valueOf(pelicula.getAño()));
                txaDescripción.setText(pelicula.getDescripcion());
            }
        }
    }


}