package com.example.aplicacion;

import Utilidades.Rutas;
import dao.CopiaDao;
import dao.PeliculaDao;
import dao.UsuarioDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import modelos.HibernateUtil;
import modelos.Pelicula;
import org.hibernate.SessionFactory;


import java.io.IOException;
import java.util.List;


public class App extends Application {
    public void start(Stage stage) throws IOException {
        AnchorPane load = FXMLLoader.load(getClass().getResource(Rutas.LOGIN));
        Scene scene = new Scene(load);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.centerOnScreen();
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.show();

    }


    public static void main(String[] args) {
           launch();




    }



}
