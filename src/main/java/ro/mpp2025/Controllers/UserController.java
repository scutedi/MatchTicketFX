package ro.mpp2025.Controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import ro.mpp2025.Domain.*;
import ro.mpp2025.Main;
import ro.mpp2025.Service.Service;
import ro.mpp2025.Utils.MessageAlert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController {

    Service service;
    User user;

    public ObservableList<Meci> modelProbe = FXCollections.observableArrayList();
    public ObservableList<Bilet> modelProbeBilet = FXCollections.observableArrayList();

    @FXML
    private TableView<Bilet> tableFilter;
    @FXML
    private TextField numeFiler;
    @FXML
    private TextField adresaFiler;
    @FXML
    private TableColumn<Bilet, String> col_nume_filter;
    @FXML
    private TableColumn<Bilet, String> col_meci_filter;
    @FXML
    private TableColumn<Bilet, String> col_adresa_filter;
    @FXML
    private TableColumn<Bilet, String> col_bilete_filter;
    @FXML
    private Button btn_filtrare;
    @FXML
    private TextField nume_filter;
    @FXML
    private TextField adresa_filter;

    @FXML
    private TextField nume;
    @FXML
    private TextField adresa;
    @FXML
    private TextField nrBilete;
    @FXML
    private Button btn_cumpara;
    @FXML
    private TableView<Meci> tabel_probe;
    @FXML
    private TableColumn<Meci, String> col_echipaA;
    @FXML
    private TableColumn<Meci, String> col_echipaB;
    @FXML
    private TableColumn<Meci, String> col_nume_meci;
    @FXML
    private TableColumn<Meci, Integer> col_nr_loc;
    @FXML
    private TableColumn<Meci, Integer> col_pret;
    @FXML
    private TableColumn<Meci, String> col_status;

    public void setService(Service service, User org) {
        this.service = service;
        this.user = org;
        initModelMeci();
    }

    @FXML
    public void initialize() {
        initializeTableMeci();
    }

    private void initializeTableMeci() {
        // Legarea coloanelor din tabel cu datele corespunzătoare
        col_echipaA.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEchipaA().getName())); // Echipa A

        // Legarea coloanei pentru echipa B (String)
        col_echipaB.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEchipaB().getName())); // Echipa B

        // Legarea coloanei pentru numele meciului (String)
        col_nume_meci.setCellValueFactory(new PropertyValueFactory<>("nume_meci"));

        // Legarea coloanei pentru numărul de locuri (String)
        col_nr_loc.setCellValueFactory(new PropertyValueFactory<>("nr_loc"));

        // Legarea coloanei pentru preț (Integer)
        col_pret.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getPret()).asObject()); // Preț

        col_status.setCellFactory(col -> new TableCell<Meci, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setStyle(""); // Resetare stil
                } else {
                    Meci meci = getTableRow().getItem();
                    if (meci != null && meci.getNr_loc() == 0) {
                        setStyle("-fx-text-fill: red"); // Stilizare roșie
                        setText("SOLD OUT");

                    } else {
                        setStyle("");
                        setText("");// Resetare stil dacă prețul nu e 0
                    }
                }
            }
        });

            // Pentru preț
            tabel_probe.setItems(modelProbe);
            tabel_probe.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    private void initModelMeci() {
        Label noContentMessage = new Label("Nu sunt date disponibile în acest moment.");
        noContentMessage.setTextFill(javafx.scene.paint.Color.WHITE);
        tableFilter.setPlaceholder(noContentMessage);
        Iterable<Meci> probe = service.findAllMeci();
        List<Meci> lista_probe = StreamSupport.stream(probe.spliterator(),false).collect(Collectors.toList());
        modelProbe.setAll(lista_probe);
    }

    @FXML
    private void handleClicks(ActionEvent event)
    {
        if(event.getSource()==btn_cumpara)
            gui_cumpara_bilet();
        if(event.getSource()==btn_filtrare)
            gui_vizualizare_bilete_client();
    }

    private void gui_cumpara_bilet() {
        ObservableList<Meci> selected = tabel_probe.getSelectionModel().getSelectedItems();
        List<Meci> meci = new ArrayList<>();

        String nume1 = nume.getText();
        Integer nrBilete1 = Integer.valueOf(nrBilete.getText());
        String adresa1 = adresa.getText();

        Optional<Meci> meci1 = service.findOneMeciByName(selected.get(0).getNume_meci());
        System.out.println(meci1.get().getNume_meci());
        Optional<Client> client = service.findOneClientByNameAndClient(nume1 , adresa1);



        if(meci1.get().getNr_loc() - nrBilete1 >= 0) {
            Meci meci2 = new Meci(meci1.get().getEchipaA(), meci1.get().getEchipaB(), meci1.get().getNume_meci(), meci1.get().getNr_loc() - nrBilete1, meci1.get().getPret());
            meci2.setId(meci1.get().getId());
            service.updateMeci(meci2);
            while(nrBilete1 > 0) {
                service.saveBilete(meci1.get(), client.get());
                nrBilete1--;
            }
        }
        else
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Nu mai sunt locuri.", "Nu mai sunt locuri , alege mai putine!");

        initModelMeci();
    }

    private void gui_vizualizare_bilete_client() {

        String nume1 = nume_filter.getText();
        String adresa1 = adresa_filter.getText();

        Iterable<Bilet> bilets = service.findAllOneByName(nume1);

        for(Bilet i : bilets)
            System.out.println(i.toString());

        col_nume_filter.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClient_id().getNume()));

        col_meci_filter.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMeci_id().getNume_meci()));

        col_adresa_filter.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClient_id().getAdresa()));

        col_bilete_filter.setCellValueFactory(cellData -> {
            Bilet bilet = cellData.getValue();
            int numarBilete = service.getNumarBilete(
                    bilet.getClient_id().getNume(),
                    bilet.getMeci_id().getNume_meci()
            );
            return new SimpleStringProperty(String.valueOf(numarBilete));
        });

        List<Bilet> lista_bilete = StreamSupport.stream(bilets.spliterator(),false).collect(Collectors.toList());
        tableFilter.setItems(modelProbeBilet);
        modelProbeBilet.setAll(lista_bilete);
        initModelMeci();
    }

    @FXML
    private void gui_logout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/login-view.fxml"));
            Parent loginView = loader.load();

            // Crearea noii ferestre de login
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.TRANSPARENT); // Dacă vrei o fereastră fără margini
            loginStage.setTitle("Bun venit!");
            Scene loginScene = new Scene(loginView);
            loginStage.setScene(loginScene);

            LoginController orgController = loader.getController();
            orgController.setService(service);

            Main.addCustomTitleBar(loginStage, loginScene);
            Stage currentStage = (Stage) tableFilter.getScene().getWindow(); // Poți folosi orice componentă de pe scenă
            currentStage.close();

            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
