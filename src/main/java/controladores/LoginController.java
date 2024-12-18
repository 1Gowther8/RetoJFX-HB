package controladores;

import Utilidades.Rutas;
import dao.UsuarioDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.example.aplicacion.HibernateUtil;
import Utilidades.Sesion;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.io.Serializable;

public class LoginController implements Serializable {

    private static final String usuario = "Jorge";
    private static final String contraseña = "1234";

    @FXML
    private PasswordField txfContraseñalogin;

    @FXML
    private TextField txfUsuarioLogin;


    /**
     * Maneja el acceso al sistema.
     * Si el usuario y la contraseña son correctos, se redirige a la vista correspondiente si es admin o no es admin.
     * Si no, se muestra un mensaje de error.
     *
     * @param event el evento de acción que desencadena el método
     * @throws IOException si ocurre un error al cargar la vista
     */
    @FXML
    void acceder(ActionEvent event) throws IOException {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        SessionFactory sessionFactory = HibernateUtil.sessionFactory;

        UsuarioDAO usuarioDao = new UsuarioDAO(sessionFactory);
        boolean esValido = usuarioDao.validarUsuario(txfUsuarioLogin.getText(), txfContraseñalogin.getText());

        if (esValido) {
            Sesion.usuariosesion = txfUsuarioLogin.getText();
            Sesion.contrasenasesion = txfContraseñalogin.getText();
            if (!usuarioDao.isAdmin(txfUsuarioLogin.getText(), txfContraseñalogin.getText())) {
                alert.setTitle("Login");
                alert.setHeaderText(null);
                alert.setContentText("Acceso correcto");
                alert.showAndWait();
                Sesion.usuariosesion = txfUsuarioLogin.getText();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Rutas.TABLACOPIAS));
                AnchorPane root = loader.load();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(Rutas.CSS);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();


            } else if (usuarioDao.isAdmin(txfUsuarioLogin.getText(), txfContraseñalogin.getText())) {
                alert.setTitle("Login");
                alert.setHeaderText(null);
                alert.setContentText("Acceso correcto");
                alert.showAndWait();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Rutas.TABLAPELICULAS));
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

        } else {
            alert.setTitle("Login");
            alert.setHeaderText(null);
            alert.setContentText("Acceso incorrecto");
            alert.showAndWait();
        }
    }


}
