
package br.com.gestaonotavel.ifsul.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

import br.com.gestaonotavel.ifsul.service.PacienteService;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.List;
import java.util.ResourceBundle;

import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author carol
 */
public class TelaPrincipalController implements Initializable {

    /**
     * Initializes the controller class.
     */

    @FXML
    private TableView<Paciente> pacientesTableView;

    @FXML
    private TableColumn<Paciente, String> colunaNome;

    @FXML
    private TableColumn<Paciente, Integer> colunaIdade;

    @FXML
    private TableColumn<Paciente, String> colunaDiagnostico;

    @FXML
    private TableColumn<Paciente, String> colunaResponsavel;

    @FXML
    private TableColumn<Paciente, String> colunaSessao;

    @FXML
    private TableColumn<Paciente, String> colunaStatus;

    @FXML
    private TableColumn<Paciente, Void> colunaAcoes;


    @FXML
    public void handleNovoCadastroButtonAction(ActionEvent event) throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/TelaCadastroPaciente.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Cadastro de Pacientes");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }catch (IOException ex){
            System.out.println(ex.getMessage());
            //AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir a tela de cadastro");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        PacienteService pacienteService = new PacienteService();

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaIdade.setCellValueFactory(cellData -> {
            LocalDate dataNascimento = cellData.getValue().getDataNascimento();
            int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
            return new SimpleIntegerProperty(idade).asObject();
        });
        colunaDiagnostico.setCellValueFactory(new PropertyValueFactory<>("diagnostico"));
        colunaResponsavel.setCellValueFactory(cellData -> {
            List<Responsavel> responsavelLista = cellData.getValue().getResponsaveisLista();
            if (responsavelLista != null && !responsavelLista.isEmpty()) {
                return new SimpleStringProperty(responsavelLista.get(0).getNome());
            }
            return new SimpleStringProperty("Nenhum");
        });
        List<Paciente> pacientes = pacienteService.listarTodos();

        pacientesTableView.setItems(FXCollections.observableArrayList(pacientes));
    }    
    
}
