package ro.mpp2025.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ro.mpp2025.Domain.User;
import ro.mpp2025.Main;
import ro.mpp2025.Service.Service;
import ro.mpp2025.Utils.MessageAlert;

import java.io.IOException;
import java.util.Optional;


public class RegisterController {
    Service service;

    @FXML
    private TextField input_email;
    @FXML
    private PasswordField input_password;
    @FXML
    private Button btn_login;

    public void setService(Service service) {
        this.service = service;
        //service.addObserver(this);
        applyEffect(btn_login);
    }

    @FXML
    private void handleClicks(ActionEvent event) {
        if (event.getSource() == btn_login)
            gui_register();
    }

    private void gui_register() {
        String email = input_email.getText();
        String password = input_password.getText();

        User user = new User(email, password);
        service.saveUser(user);

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/login-view.fxml"));
            Parent signupView = loader.load();

            Stage signupStage = new Stage();
            signupStage.initStyle(StageStyle.TRANSPARENT);
            signupStage.setTitle("Bun venit!");
            Scene signupScene = new Scene(signupView);
            signupStage.setScene(signupScene);


            LoginController orgController = loader.getController();
            orgController.setService(service);

            Main.addCustomTitleBar(signupStage, signupScene);
            Stage currentStage = (Stage) input_email.getScene().getWindow();
            currentStage.close();

            signupStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void applyEffect(Button button) {
        // Setează stilul normal al butonului
        button.setStyle("-fx-background-color: #20639B; -fx-background-radius: 10px; -fx-text-fill:  #ffffff; -fx-font-size: 13px; -fx-font-weight: bold; -fx-font-family: Arial;");

        // Efect la hover
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: #20639B; -fx-background-radius: 10px; -fx-opacity: 0.7; fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: bold; -fx-font-family: Arial;");
        });

        // Efect la ieșirea mouse-ului
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: #20639B; -fx-background-radius: 10px; -fx-text-fill: #ffffff;; -fx-font-size: 13px; -fx-font-weight: bold; -fx-font-family: Arial;");
        });

        // Efect la apăsare
        button.setOnMousePressed(e -> {
            button.setStyle("-fx-background-color: #20639B; -fx-background-radius: 10px; -fx-opacity: 0.7; -fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: bold; -fx-font-family: Arial;");
        });

        // Revine la culoarea hover când se eliberează
        button.setOnMouseReleased(e -> {
            button.setStyle("-fx-background-color: #20639B; -fx-background-radius: 10px; -fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: bold; -fx-font-family: Arial;");
        });


    }
}