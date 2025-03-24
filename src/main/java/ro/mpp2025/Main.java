package ro.mpp2025;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import ro.mpp2025.Controllers.LoginController;
import ro.mpp2025.Domain.*;
import ro.mpp2025.Repository.*;
import ro.mpp2025.Repository.DataBase.*;
import ro.mpp2025.Service.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main extends Application {

    private IRepositoryBilet repositoryBilet;
    private IRepositoryClient repositoryClient;
    private IRepositoryEchipa repositoryEchipa;
    private IRepositoryMeci repositoryMeci;
    private IRepositoryUser repositoryUser;
    public Service service;

    static double xOffset = 500;
    static double yOffset = 500;

    @Override
    public void start(Stage stage) throws Exception {

        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }


        this.repositoryBilet = new BiletRepoDB(props);
        this.repositoryClient = new ClientRepoDB(props);
        this.repositoryEchipa = new EchipaRepoDB(props);
        this.repositoryMeci = new MeciRepoDB(props);
        this.repositoryUser = new UserRepoDB(props);

        Iterable<Meci> meciuri = repositoryMeci.findAll();
        for(Meci meci : meciuri){
            System.out.println(meci);
        }

        this.service = new Service(repositoryBilet, repositoryClient, repositoryEchipa , repositoryMeci , repositoryUser);

        init(stage);
        stage.show();
    }


    private void init(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);

        LoginController ctrl = fxmlLoader.getController();
        ctrl.setService(service);

        addCustomTitleBar(stage, scene);
    }

    public static void addCustomTitleBar(Stage stage, Scene scene) {
        // Creează un HBox care va reprezenta bara de titlu personalizată
        HBox titleBar = new HBox();
        titleBar.setStyle("-fx-padding: 2px;");
        titleBar.setPrefHeight(15);  // Setează înălțimea barei de titlu mai mică
        titleBar.setSpacing(5);      // Setează un spațiu mai mic între butoane

        // Butonul de minimizare
        Button minimizeButton = new Button("—");
        applyEffect(minimizeButton);
        minimizeButton.setOnAction(e -> stage.setIconified(true));

        // Butonul de închidere
        Button closeButton = new Button("X");
        applyEffect(closeButton);
        closeButton.setOnAction(e -> stage.close());

        // Crează un Region pentru a împinge butoanele la dreapta
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);  // Permite expansiunea spacer-ului

        // Adaugă butoanele și spacer-ul în HBox
        titleBar.getChildren().addAll(spacer, minimizeButton, closeButton);

        // Adaugă bara de titlu personalizată în scenă
        AnchorPane root = (AnchorPane) scene.getRoot();
        root.getChildren().add(0, titleBar);

        // Poziționează bara de titlu în partea de sus a ferestrei și extinde-o pe toată lățimea
        AnchorPane.setTopAnchor(titleBar, 0.0);
        AnchorPane.setLeftAnchor(titleBar, 0.0);  // Poziționează bara în stânga
        AnchorPane.setRightAnchor(titleBar, 0.0);  // Poziționează bara în dreapta (ocupă toată lățimea)

        // Permite mutarea feroniei
        titleBar.setOnMousePressed(event -> {
            titleBar.setCursor(Cursor.CLOSED_HAND);
            xOffset = event.getScreenX() - stage.getX();
            yOffset = event.getScreenY() - stage.getY();
        });

        titleBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    public static void applyEffect(Button button) {
        button.setPrefHeight(5);  // Menține înălțimea butonului mai mică
        button.setPrefWidth(30);

        // Setează stilul normal al butonului
        button.setStyle("-fx-background-color: transparent; -fx-text-fill:  #ffffff; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: Arial;");

        // Efect la hover
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: #341157; -fx-text-fill: #ffffff; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: Arial;");
        });

        // Efect la ieșirea mouse-ului
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffffff;; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: Arial;");
        });

        // Efect la apăsare
        button.setOnMousePressed(e -> {
            button.setStyle("-fx-background-color: #341157; -fx-text-fill: #ffffff; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: Arial;");
        });

        // Revine la culoarea hover când se eliberează
        button.setOnMouseReleased(e -> {
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffffff; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: Arial;");
        });

    }

    public static void main(String[] args)
    {
        launch(args);
    }
}