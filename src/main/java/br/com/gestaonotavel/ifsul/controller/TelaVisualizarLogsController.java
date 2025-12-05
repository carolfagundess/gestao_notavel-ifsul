package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.AuditoriaLog;
import br.com.gestaonotavel.ifsul.service.AuditoriaLogService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TelaVisualizarLogsController implements Initializable {

    @FXML private TableView<AuditoriaLog> tabelaLogs;
    @FXML private TableColumn<AuditoriaLog, String> colunaData;
    @FXML private TableColumn<AuditoriaLog, String> colunaUsuario;
    @FXML private TableColumn<AuditoriaLog, String> colunaAcao;

    private final AuditoriaLogService logService;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public TelaVisualizarLogsController(AuditoriaLogService logService) {
        this.logService = logService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colunaData.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTimestamp().format(fmt)));
        colunaUsuario.setCellValueFactory(new PropertyValueFactory<>("usuarioNome"));
        colunaAcao.setCellValueFactory(new PropertyValueFactory<>("acao"));

        tabelaLogs.setItems(FXCollections.observableArrayList(logService.listarLogs()));
    }
}