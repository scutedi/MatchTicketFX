package ro.mpp2025.Controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import ro.mpp2025.Domain.Client;
import ro.mpp2025.Domain.Echipa;
import ro.mpp2025.Domain.Meci;
import ro.mpp2025.Domain.User;
import ro.mpp2025.Service.Service;
import ro.mpp2025.Utils.MessageAlert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController {

    Service service;
    User user;

    public ObservableList<Meci> modelProbe = FXCollections.observableArrayList();


    @FXML
    private TableView<Meci> tableFilter;
    @FXML
    private TextField numeFiler;
    @FXML
    private TextField adresaFiler;
    @FXML
    private TableColumn<Client, String> col_nume_filter;
    @FXML
    private TableColumn<Meci, String> col_meci_filter;
    @FXML
    private Button btn_filter;

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

    public void setService(Service service, User org) {
        this.service = service;
        this.user = org;
        initModelProbe();
    }

    @FXML
    public void initialize() {
        initializeTableProbe();
    }

    private void initializeTableProbe() {
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
        // Pentru preț
        tabel_probe.setItems(modelProbe);
        tabel_probe.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void initModelProbe() {
        Iterable<Meci> probe = service.findAllMeci();
        List<Meci> lista_probe = StreamSupport.stream(probe.spliterator(),false).collect(Collectors.toList());
        modelProbe.setAll(lista_probe);
    }

    @FXML
    private void handleClicks(ActionEvent event)
    {
        if(event.getSource()==btn_cumpara)
            gui_cumpara_bilet();
        /*if(event.getSource()==btn_filter)
            gui_vizualizare_bilete_client();*/
    }

    private void gui_cumpara_bilet() {
        ObservableList<Meci> selected = tabel_probe.getSelectionModel().getSelectedItems();
        List<Meci> meci = new ArrayList<>();

        String nume1 = nume.getText();
        Integer nrBilete1 = Integer.valueOf(nrBilete.getText());
        String adresa1 = adresa.getText();

        Optional<Meci> meci1 = service.findOneMeciByName(selected.get(0).getNume_meci());
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
        /*else if(meci1.get().getNr_loc() - nrBilete1 == 0) {
            column.setCellFactory(param -> {
                TableCell<MyObject, String> cell = new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty && item.equals("Valoare Speciala")) {
                            setStyle("-fx-background-color: red;");
                        } else {
                            setStyle("");
                        }
                    }
                };
                return cell;
            });
        }
        else
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Nu mai sunt locuri.", "Nu mai sunt locuri , alege mai putine!");*/

       // initModelProbe();
    }

    /*private void gui_vizualizare_bilete_client() {


        col_nume_filter.setCellValueFactory(new PropertyValueFactory<>("nume"));
        col_meci_filter.setCellValueFactory(new PropertyValueFactory<>("nume_meci"));

        tabel_participanti.setItems(modelParticipanti);
    }*/
}
