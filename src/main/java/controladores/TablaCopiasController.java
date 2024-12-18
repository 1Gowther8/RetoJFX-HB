package controladores;

import Utilidades.Rutas;
import Utilidades.Sesion;
import com.example.aplicacion.HibernateUtil;
import dao.CopiaDao;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import modelos.*;
import org.hibernate.Session;
import org.hibernate.query.Query;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static com.example.aplicacion.HibernateUtil.sessionFactory;

public class TablaCopiasController {

    @FXML
    private TableColumn<Copia, String> coltitulo;

    @FXML
    private TableColumn<Copia, String> colestado;
    @FXML
    private TableColumn<Copia, String> colsoporte;

    @FXML
    private ImageView ivtablacopias;

    @FXML
    private TableView<Copia> tablacopias;

    @FXML
    private Label usuarioactualpeliculas = new Label(Sesion.getUsuariosesion());

    @FXML
    public void initialize() {

        usuarioactualpeliculas.setText(Sesion.getUsuariosesion());
        mostrarAvatar();

        Usuario usuarioAutenticado = obtenerUsuarioPorCredenciales();
        CopiaDao copiaDao = new CopiaDao(sessionFactory);

        if (usuarioAutenticado != null) {

            List<Copia> copias = copiaDao.obtenerCopiasPorUsuario(usuarioAutenticado);


            colsoporte.setCellValueFactory(new PropertyValueFactory<>("soporte"));
            colestado.setCellValueFactory(new PropertyValueFactory<>("estado"));
            coltitulo.setCellValueFactory(cellData -> {
                Pelicula pelicula = cellData.getValue().getPelicula();
                return new SimpleStringProperty(pelicula != null ? pelicula.getTitulo() : "Desconocido");
            });


            tablacopias.getItems().addAll(copias);

            ContextMenu contextMenu = new ContextMenu();
            MenuItem detallesItem = new MenuItem("Ver Detalles");
            MenuItem editarItem = new MenuItem("Editar");
            MenuItem eliminarItem = new MenuItem("Eliminar");

            editarItem.setOnAction(event ->{
                Copia copiaSeleccionada = tablacopias.getSelectionModel().getSelectedItem();
                if (copiaSeleccionada != null) {
                    mostrarDialogoEdicion(copiaSeleccionada);
                } else {

                    Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                    warningAlert.setTitle("Advertencia");
                    warningAlert.setHeaderText("No se ha seleccionado ninguna copia");
                    warningAlert.setContentText("Por favor, selecciona una copia para editar.");
                    warningAlert.showAndWait();
                }
            });



            detallesItem.setOnAction(event -> {
                Copia copiaSeleccionada = tablacopias.getSelectionModel().getSelectedItem();
                if (copiaSeleccionada != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource(Rutas.DETALLES));
                        AnchorPane root = loader.load();

                        DetallesController detallesController = loader.getController();

                        detallesController.setDetalles(copiaSeleccionada);

                        // Crear y mostrar la nueva ventana
                        Stage stage = new Stage();
                        stage.setTitle("Detalles de la Copia");
                        stage.setScene(new Scene(root));
                        stage.setWidth(1000);
                        stage.setHeight(700);
                        stage.resizableProperty().setValue(Boolean.FALSE);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Si no hay ninguna copia seleccionada, mostrar un mensaje de advertencia
                    Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                    warningAlert.setTitle("Advertencia");
                    warningAlert.setHeaderText("No se ha seleccionado ninguna copia");
                    warningAlert.setContentText("Por favor, selecciona una copia para ver los detalles.");
                    warningAlert.showAndWait();
                }
            });


            eliminarItem.setOnAction(event -> borrarCopia(new ActionEvent()));

            contextMenu.getItems().addAll(detallesItem,editarItem,eliminarItem);
            tablacopias.setContextMenu(contextMenu); // Asigna el menú contextual a la tabla

        } else {

            System.out.println("Credenciales incorrectas");
        }
    }


    /**
     * Obtiene un usuario autenticado basado en las credenciales almacenadas en la sesión.
     *
     * @return el usuario autenticado si las credenciales son correctas, de lo contrario, null.
     */
    public Usuario obtenerUsuarioPorCredenciales() {
        Session session = HibernateUtil.sessionFactory.openSession();
        Usuario usuario = null;

        try {
            session.beginTransaction();
            Query<Usuario> query = session.createQuery("FROM Usuario u WHERE u.usuario = :username AND u.contraseña = :password", Usuario.class);
            query.setParameter("username", Sesion.getUsuariosesion());
            query.setParameter("password", Sesion.getContrasenasesion());
            usuario = query.uniqueResult(); // Obtiene un único usuario que coincida
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return usuario;
    }

    /**
     * Maneja la acción de borrar una copia seleccionada de la tabla.
     * Muestra un cuadro de diálogo de confirmación antes de eliminar la copia.
     * Si no hay ninguna copia seleccionada, muestra un mensaje de advertencia.
     */
    @FXML
    void borrarCopia(ActionEvent event) {
        Copia copiaSeleccionada = tablacopias.getSelectionModel().getSelectedItem();
        if (copiaSeleccionada != null) {
            // Mostrar un cuadro de diálogo de confirmación
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("¿Estás seguro de que deseas eliminar esta copia?");
            alert.setContentText("Esta acción no se puede deshacer.");

            // Mostrar el diálogo y esperar la respuesta
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // El usuario confirmó la eliminación
                try {
                    CopiaDao copiaDao1 = new CopiaDao(sessionFactory);
                    copiaDao1.delete(copiaSeleccionada);
                    tablacopias.getItems().remove(copiaSeleccionada);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Si no hay ninguna copia seleccionada, mostrar un mensaje de advertencia
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Advertencia");
            warningAlert.setHeaderText("No se ha seleccionado ninguna copia");
            warningAlert.setContentText("Por favor, selecciona una copia para eliminar.");
            warningAlert.showAndWait();
        }
    }

    /**
     * Muestra el avatar del usuario autentificado.
     * Obtiene la ruta del avatar desde la base de datos y carga la imagen en el ImageView.
     * Si la ruta es nula o el archivo no existe, se muestra un mensaje de error.
     */
    private void mostrarAvatar() {  //Hice muchos controles para ver donde falla , igualmente lo dejo implementado para que lo veas
        CopiaDao copiaDao = new CopiaDao(sessionFactory);
        String rutaAvatar = copiaDao.obtenerRutaAvatar();
        if (rutaAvatar == null || getClass().getResourceAsStream(rutaAvatar) == null) {
            System.out.println("Error al obtener la ruta del avatar o el archivo no existe"); //Aqui me salta el error
            return;
        }
        try (InputStream inputStream = getClass().getResourceAsStream(rutaAvatar)) {
            if (inputStream == null) {
                System.out.println("El archivo no existe en la ruta especificada: " + rutaAvatar);
                return;
            }
            Image image = new Image(inputStream);
            ivtablacopias.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la imagen desde la ruta: " + rutaAvatar);
        }
    }


    @FXML
    void salirApp(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Rutas.LOGIN));
        AnchorPane root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Rutas.CSS);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.show();


    }



    @FXML
    void añadirCopia(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Rutas.TABLAAÑADIRPELICULA));
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
    /**
     * Muestra un diálogo para editar una copia seleccionada.
     * Permite al usuario modificar el soporte y el estado de la copia.
     */
    private void mostrarDialogoEdicion(Copia copiaSeleccionada) {
        Dialog<Copia> dialog = new Dialog<>();
        dialog.setTitle("Editar Copia");
        dialog.setHeaderText("Edita el soporte y el estado de la copia seleccionada");

        // Crear los campos de texto para soporte y estado
        TextField soporteField = new TextField(copiaSeleccionada.getSoporte());
        TextField estadoField = new TextField(copiaSeleccionada.getEstado());

        // Crea un formulario para el diálogo
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Soporte:"), 0, 0);
        ChoiceBox<String> soporteChoiceBox = new ChoiceBox<>();
        soporteChoiceBox.getItems().addAll("DVD", "Blu-ray", "Digital");
        soporteChoiceBox.setValue(copiaSeleccionada.getSoporte());
        grid.add(soporteChoiceBox, 1, 0);

        grid.add(new Label("Estado:"), 0, 1);
        ChoiceBox<String> estadoChoiceBox = new ChoiceBox<>();
        estadoChoiceBox.getItems().addAll("muy malo", "malo", "bueno", "perfecto");
        estadoChoiceBox.setValue(copiaSeleccionada.getEstado());
        grid.add(estadoChoiceBox, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Botones
        ButtonType guardarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);

        // Manejar el  "Guardar"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == guardarButtonType) {
                // Actualizar la copia seleccionada con los nuevos valores
                copiaSeleccionada.setSoporte(soporteChoiceBox.getValue());
                copiaSeleccionada.setEstado(estadoChoiceBox.getValue());
                return copiaSeleccionada;
            }
            return null;
        });

        // Mostrar el diálogo y esperar
        Optional<Copia> resultado = dialog.showAndWait();
        resultado.ifPresent(copia -> {

            try {
                CopiaDao copiaDao = new CopiaDao(sessionFactory);
                copiaDao.update(copia);
                tablacopias.refresh();
            } catch (Exception e) {
                e.printStackTrace();

            }
        });
    }


}


