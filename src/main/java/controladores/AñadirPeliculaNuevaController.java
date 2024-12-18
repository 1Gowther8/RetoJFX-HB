
package controladores;
import Utilidades.Rutas;
import dao.PeliculaDao;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import modelos.Pelicula;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

import static com.example.aplicacion.HibernateUtil.sessionFactory;

public class AñadirPeliculaNuevaController {

        @FXML
        private Label infoAñadirPeliculas;
        @FXML
        private TextField tfTitulo;

        @FXML
        private TextField tfAño;

        @FXML
        private TextArea tfDescripcion;

        @FXML
        private TextField tfDirector;

        @FXML
        private ChoiceBox<String> chGenero;






        @FXML
        public void initialize() {
            List<String> generos = List.of(
                    "Acción", "Aventura", "Comedia", "Drama", "Fantasía", "Terror", "Romance", "Ciencia Ficción",
                    "Crimen", "Musical", "Histórico", "Comedia Dramática");

            chGenero.getItems().addAll(generos);
            chGenero.setValue("Acción");
        }
    /**
     * Maneja el evento de confirmación para añadir una nueva película.
     * Recoge los datos del formulario, valida el año, guarda la nueva película en la base de datos
     * y limpia los campos del formulario.
     *
     */
        @FXML
        void confirmar(ActionEvent event) {
            // Recoger datos del formulario
            String titulo = tfTitulo.getText();
            String genero = chGenero.getValue();
            int año;
            try {
                año = Integer.parseInt(tfAño.getText());
            } catch (NumberFormatException e) {
                infoAñadirPeliculas.setText("Por favor, ingresa un año válido.");
                return;
            }


            String descripcion = tfDescripcion.getText();
            String director = tfDirector.getText();

            PeliculaDao peliculaDao = new PeliculaDao(sessionFactory);
            peliculaDao.save(new Pelicula(titulo, genero, año, descripcion, director));
            infoAñadirPeliculas.setText("Película añadida correctamente.");

            limpiarCampos();

        }

        private void limpiarCampos() {
            tfTitulo.clear();
            tfAño.clear();
            tfDescripcion.clear();
            tfDirector.clear();
            chGenero.setValue("Acción"); // Restablecer a la opción por defecto
        }


        @FXML
        void limpiar(ActionEvent event) {
            limpiarCampos();
        }


        @FXML
        void salir(MouseEvent event) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Rutas.LOGIN));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.setScene(scene);
            stage.show();

        }

    @FXML
    void volver(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Rutas.TABLAPELICULAS));
        AnchorPane root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.show();

    }


    }

