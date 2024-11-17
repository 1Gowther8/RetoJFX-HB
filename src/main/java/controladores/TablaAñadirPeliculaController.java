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
import modelos.Copia;
import modelos.Pelicula;
import modelos.Sesion;
import modelos.Usuario;
import org.hibernate.Session;

import java.io.IOException;

import static modelos.HibernateUtil.sessionFactory;

public class TablaAñadirPeliculaController {
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
        private TableView<Pelicula> tablaPeliculasAñadir;

        @FXML
        private Label usuarioactualpeliculas;

        @FXML
        private ChoiceBox<String> chestado;

        @FXML
        private ChoiceBox<String> chsoporte;


        @FXML
        private Label labelInfoAñadir;




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
            tablaPeliculasAñadir.getItems().addAll(peliculaDao.findAll());

            chestado.getItems().addAll("muy malo", "malo","bueno","perfecto");
            chsoporte.getItems().addAll("DVD", "Blu-ray","Digital");
            chestado.setValue("malo");
            chsoporte.setValue("Digital");


        }
    /**
     * Maneja la confirmación de la adición de una nueva copia de una película seleccionada.
     * Si se selecciona una película, se crea una nueva copia con el estado y soporte especificados,
     * y se guarda en la base de datos.
     *
     */
        @FXML
        void Confirmar(ActionEvent event) {
            Pelicula selectedPelicula = tablaPeliculasAñadir.getSelectionModel().getSelectedItem();

            if (selectedPelicula != null) {
                String estado = chestado.getValue();
                String soporte = chsoporte.getValue();
                CopiaDao copiaDao = new CopiaDao(sessionFactory);


                // Obtener el ID del usuario a partir de las credenciales
                int idUsuario = copiaDao.obtenerIdUsuarioPorCredenciales();

                if (idUsuario != -1) {

                    Usuario usuarioSeleccionado = obtenerUsuarioPorId(idUsuario);


                    Copia nuevaCopia = new Copia();
                    nuevaCopia.setEstado(estado);
                    nuevaCopia.setSoporte(soporte);

                    // Establecer las relaciones bidireccionales
                    nuevaCopia.añadirPelicula(selectedPelicula);
                    nuevaCopia.añadirUsuario(usuarioSeleccionado);


                    copiaDao = new CopiaDao(sessionFactory);
                    copiaDao.save(nuevaCopia);

                    labelInfoAñadir.setText("Copia añadida correctamente.");



            }
        }else {
            labelInfoAñadir.setText("Debes seleccionar una pelicula.");

        }
        }



        private Usuario obtenerUsuarioPorId(Integer idUsuario) {
            Usuario usuario = null;
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                usuario = session.get(Usuario.class, idUsuario);
                session.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return usuario;
        }

        @FXML
        void Volver(ActionEvent event) throws IOException {
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
        void salirApp(MouseEvent event)throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Rutas.LOGIN));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Rutas.CSS);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();


        }


}
