package controladores;

import Utilidades.Rutas;
import dao.CopiaDao;
import dao.PeliculaDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Data;
import modelos.Pelicula;
import modelos.Sesion;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

import static modelos.HibernateUtil.sessionFactory;

@Data

public class TablaPeliculasController implements Serializable {


    @FXML
    private TableColumn<Pelicula, Integer> colid;

    @FXML
    private TableColumn<Pelicula, String> coltitulo;

    @FXML
    private TableColumn<Pelicula, String> colgenero;


    @FXML
    private TableColumn<Pelicula, Integer> colaño;
    @FXML
    private TableColumn<Pelicula, String> coldescripcion;

    @FXML
    private TableColumn<Pelicula, String> coldirector;


    @FXML
    private ImageView ivtablapeliculas;

    @FXML
    private TableView<Pelicula> tablapeliculas;

    @FXML
    private Label usuarioactualpeliculas;









    @FXML
    public void initialize() {
        colid.setCellValueFactory(new PropertyValueFactory<>("id"));
        coltitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colgenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colaño.setCellValueFactory(new PropertyValueFactory<>("año"));
        coldescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        coldirector.setCellValueFactory(new PropertyValueFactory<>("director"));

        usuarioactualpeliculas.setText(Sesion.getUsuariosesion());
        // Obtener la lista de películas de la base de datos
        PeliculaDao peliculaDao = new PeliculaDao(sessionFactory);
        tablapeliculas.getItems().addAll(peliculaDao.findAll());



    }

    @FXML
    void añadir(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Rutas.AÑADIRPELICULANUEVA));
        AnchorPane root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Rutas.CSS);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.show();

    }

    @FXML
    void borrar(ActionEvent event) {

        Pelicula peliculaSeleccionada = tablapeliculas.getSelectionModel().getSelectedItem();

        if (peliculaSeleccionada != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("¿Estás seguro de que deseas eliminar esta copia?");
            alert.setContentText("Esta acción no se puede deshacer.");


            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

            PeliculaDao peliculaDao = new PeliculaDao(sessionFactory);
            CopiaDao copiaDao = new CopiaDao(sessionFactory);

            // Elimina todas las copias de la película seleccionada
            copiaDao.eliminarCopiasPorPelicula(peliculaSeleccionada.getId());


            peliculaDao.delete(peliculaSeleccionada);


            tablapeliculas.getItems().remove(peliculaSeleccionada);

        } else {
            // Mostrar un mensaje de advertencia si no hay película seleccionada
            System.out.println("Por favor, selecciona una película para eliminar.");
        }
    }
    }








    @FXML
    void salirApp(MouseEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Rutas.LOGIN));
        AnchorPane root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.show();


    }







}

