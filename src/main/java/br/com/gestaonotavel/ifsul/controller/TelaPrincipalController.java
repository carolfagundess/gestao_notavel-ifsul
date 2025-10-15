
package br.com.gestaonotavel.ifsul.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

import br.com.gestaonotavel.ifsul.service.PacienteService;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.List;
import java.util.ResourceBundle;

import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TableColumn<Paciente, Void> colunaAcoes; //
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        PacienteService pacienteService = new PacienteService();

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaIdade.setCellValueFactory(cellData -> {
            LocalDate dataNascimento = cellData.getValue().getDataNascimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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
