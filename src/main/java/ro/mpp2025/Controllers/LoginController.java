package ro.mpp2025.Controllers;

import ro.mpp2025.Domain.User;
import ro.mpp2025.Service.Service;
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
import ro.mpp2025.Utils.MessageAlert;

import java.io.IOException;
import java.util.Optional;

public class LoginController {
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
    }

    @FXML
    private void handleClicks(ActionEvent event)
    {
        if(event.getSource()==btn_login)
            gui_login();
    }

    private void gui_login() {
        String email = input_email.getText();
        String password = input_password.getText();

        Optional<User> org = service.findOneUserByEmail(email);
        if(org.isEmpty())
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Autentificare esuata", "Nu exista un utilizator cu acest email!");
        else if (org.get().getPassword().equals(password))
        {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/user-view.fxml"));
                Parent signupView = loader.load();

                Stage signupStage = new Stage();
                signupStage.setTitle("Bun venit!");
                signupStage.setScene(new Scene(signupView));

                UserController orgController = loader.getController();
                orgController.setService(service, org.get());
                signupStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Autentificare esuata", "parola incorecta!");
    }

}
