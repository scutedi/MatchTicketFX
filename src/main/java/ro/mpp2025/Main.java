package ro.mpp2025;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
        stage.setTitle("Autentificare");
        stage.setScene(scene);

        LoginController ctrl = fxmlLoader.getController();
        ctrl.setService(service);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}