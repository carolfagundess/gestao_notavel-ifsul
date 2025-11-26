package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.service.ResponsavelService;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TelaRelatorioVoluntariosController implements Initializable {

    @FXML private TableView<Responsavel> tabelaVoluntarios;
    @FXML private TableColumn<Responsavel, String> colunaNome;
    @FXML private TableColumn<Responsavel, String> colunaHoras;
    @FXML private TableColumn<Responsavel, String> colunaCreditos;

    private final ResponsavelService responsavelService;
    private ObservableList<Responsavel> obsListaResponsaveis;

    public TelaRelatorioVoluntariosController(ResponsavelService responsavelService) {
        this.responsavelService = responsavelService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        obsListaResponsaveis = FXCollections.observableArrayList();
        configurarTabela();
        carregarDados();
    }

    private void configurarTabela() {
        tabelaVoluntarios.setItems(obsListaResponsaveis);
        tabelaVoluntarios.setPlaceholder(new Label("Nenhum responsável encontrado."));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaHoras.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.1f h", cellData.getValue().getHorasVoluntariado()))
        );
        colunaHoras.getStyleClass().add("table-cell-horas");
        colunaCreditos.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("R$ %.2f", cellData.getValue().getCreditos()))
        );
        colunaCreditos.getStyleClass().add("table-cell-creditos");
    }

    private void carregarDados() {
        try {
            List<Responsavel> responsaveis = responsavelService.buscarTodos();
            obsListaResponsaveis.setAll(responsaveis);
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar o relatório: " + e.getMessage());
        }
    }
}