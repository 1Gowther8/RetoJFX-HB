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
import modelos.Copia;
import modelos.Pelicula;
import Utilidades.Sesion;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Optional;

import static com.example.aplicacion.HibernateUtil.sessionFactory;

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


        ContextMenu contextMenu = new ContextMenu();
        MenuItem detallesItem = new MenuItem("Ver Detalles e Imprimir PDF");


        detallesItem.setOnAction(event -> {
            Pelicula peliculaSeleccionada = tablapeliculas.getSelectionModel().getSelectedItem();
            int idPelicula = peliculaSeleccionada.getId();

            Session session = sessionFactory.openSession();
            session.doWork(connection -> {
                try {
                    // 1. Crear la consulta SQL
                    String query = "SELECT p.*, c.* FROM Peliculas AS p INNER JOIN Copias AS c ON p.id = c.id_pelicula WHERE p.id = ?;";


                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, idPelicula);
                    ResultSet resultSet = statement.executeQuery();


                    // Log para confirmar la consulta
                    System.out.println("Consulta ejecutada: " + query);

                    // 2. Cargar archivo .jasper
                    InputStream reportStream = getClass().getResourceAsStream("/reports/ReporteInformacionDetallada.jasper");

                    // 3. Crear DataSource y llenar el reporte
                    JRResultSetDataSource dataSource = new JRResultSetDataSource(resultSet);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, new HashMap<>(), dataSource);

                    // 6. Visualizar el informe
                    JasperViewer.viewReport(jasperPrint, false);

                    // 4. Exportar a PDF
                    String outputPath = "reporte_detallado.pdf";
                    JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);


                    System.out.println("Reporte generado exitosamente");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            session.close();

        });
        // Asociar el menú con la tabla
        contextMenu.getItems().add(detallesItem);
        tablapeliculas.setContextMenu(contextMenu);


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
    void salirApp(MouseEvent event) throws IOException {
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

    @FXML
    void generarListadoPeliculasPDF(ActionEvent event) {
        Session session = sessionFactory.openSession();
        session.doWork(connection -> {
            try {

                String query = "SELECT * FROM Peliculas";
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();


                System.out.println("Consulta ejecutada: " + query);


                InputStream reportStream = getClass().getResourceAsStream("/reports/ReportePeliculas.jasper");
                if (reportStream == null) {
                    System.err.println("Error: No se encontró el archivo ReportePeliculas.jasper en /reports.");
                    return;
                }


                JRResultSetDataSource dataSource = new JRResultSetDataSource(resultSet);
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, new HashMap<>(), dataSource);


                JasperViewer.viewReport(jasperPrint, false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        session.close();
    }


    @FXML
    void reportePeliculasMalEstado(ActionEvent event) {
        Session session = sessionFactory.openSession();
        session.doWork(connection -> {
            try {

                String query = "SELECT p.titulo, c.estado FROM Copias AS c INNER JOIN Peliculas AS p ON c.id_pelicula = p.id WHERE c.estado = 'muy malo';";

                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                // Log para confirmar la consulta
                System.out.println("Consulta ejecutada: " + query);

                // Verifica si hay datos
                if (!resultSet.isBeforeFirst()) {
                    System.err.println("La consulta SQL no devolvió resultados.");
                    return;
                }

                InputStream reportStream = getClass().getResourceAsStream("/reports/ReportePeliculasMalEstado.jasper");


                JRResultSetDataSource dataSource = new JRResultSetDataSource(resultSet);
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, new HashMap<>(), dataSource);


                JasperViewer.viewReport(jasperPrint, false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        session.close();


    }


    @FXML
    void ReportePeliculasMas1Copia(ActionEvent event) {
        Session session = sessionFactory.openSession();
        session.doWork(connection -> {
            try {

                String query = "SELECT p.* FROM Peliculas AS p WHERE (SELECT COUNT(*) FROM Copias AS c WHERE c.id_pelicula = p.id) > 1;";


                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery(query);


                System.out.println("Consulta ejecutada: " + query);


                if (!resultSet.isBeforeFirst()) {
                    System.err.println("La consulta SQL no devolvió resultados.");
                    return;
                }


                InputStream reportStream = getClass().getResourceAsStream("/reports/ReporteMasDeUnaCopia.jasper");


                JRResultSetDataSource dataSource = new JRResultSetDataSource(resultSet);
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, new HashMap<>(), dataSource);


                JasperViewer.viewReport(jasperPrint, false);


            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        session.close();

    }


}




