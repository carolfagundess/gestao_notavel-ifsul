package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Atendimento;
import br.com.gestaonotavel.ifsul.model.Atividade;
import br.com.gestaonotavel.ifsul.service.AtendimentoService;
import br.com.gestaonotavel.ifsul.service.AtividadeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TelaCalendarioMensalController implements Initializable {

    @FXML private Label lblMesAno;
    @FXML private GridPane gridCalendario;
    @FXML private Button btnAnterior;
    @FXML private Button btnProximo;
    @FXML private Button btnFechar;
    @FXML private Button btnNovoAgendamento;

    private YearMonth mesAtual;
    private final AtendimentoService atendimentoService;
    private final AtividadeService atividadeService;

    public TelaCalendarioMensalController(AtendimentoService atendimentoService, AtividadeService atividadeService) {
        this.atendimentoService = atendimentoService;
        this.atividadeService = atividadeService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mesAtual = YearMonth.now();
        atualizarCalendario();
    }

    @FXML
    void handleAnterior(ActionEvent event) {
        mesAtual = mesAtual.minusMonths(1);
        atualizarCalendario();
    }

    @FXML
    void handleProximo(ActionEvent event) {
        mesAtual = mesAtual.plusMonths(1);
        atualizarCalendario();
    }

    @FXML
    void handleFechar(ActionEvent event) {
        ((Stage) btnFechar.getScene().getWindow()).close();
    }

    @FXML
    private void handleNovoAgendamento(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaAgendamento.fxml"));
            // Injeta os services necessários
            loader.setControllerFactory(c -> new TelaAgendamentoController(
                new br.com.gestaonotavel.ifsul.service.PacienteService(),
                new br.com.gestaonotavel.ifsul.service.EspecialistaService(),
                new br.com.gestaonotavel.ifsul.service.AtendimentoService()
            ));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Novo Agendamento");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            // Opcional: mostrar alerta de erro
        }
    }

    private void atualizarCalendario() {
        lblMesAno.setText(mesAtual.getMonth().name() + " / " + mesAtual.getYear());
        gridCalendario.getChildren().clear();

        LocalDate inicioMes = mesAtual.atDay(1);
        LocalDate fimMes = mesAtual.atEndOfMonth();

        List<Atendimento> atendimentos = atendimentoService.listarTodos(null).stream()
                .filter(a -> !a.getDataHora().toLocalDate().isBefore(inicioMes) && !a.getDataHora().toLocalDate().isAfter(fimMes))
                .collect(Collectors.toList());

        List<Atividade> atividades = atividadeService.listarTodos().stream()
                .filter(a -> !a.getDataInicio().toLocalDate().isBefore(inicioMes) && !a.getDataInicio().toLocalDate().isAfter(fimMes))
                .collect(Collectors.toList());

        int diaSemanaInicio = mesAtual.atDay(1).getDayOfWeek().getValue() % 7;
        int diasNoMes = mesAtual.lengthOfMonth();

        int coluna = diaSemanaInicio;
        int linha = 0;

        for (int dia = 1; dia <= diasNoMes; dia++) {
            LocalDate dataAtual = mesAtual.atDay(dia);
            VBox celulaDia = criarCelulaDia(dia, dataAtual, atendimentos, atividades);
            gridCalendario.add(celulaDia, coluna, linha);

            coluna++;
            if (coluna > 6) {
                coluna = 0;
                linha++;
            }
        }
    }

    private VBox criarCelulaDia(int dia, LocalDate data, List<Atendimento> atendimentos, List<Atividade> atividades) {
        VBox box = new VBox(2);
        box.setStyle("-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-padding: 5; -fx-background-color: white;");
        box.setPrefHeight(100);
        box.setPrefWidth(100);

        Label lblDia = new Label(String.valueOf(dia));
        lblDia.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        box.getChildren().add(lblDia);

        for (Atendimento a : atendimentos) {
            if (a.getDataHora().toLocalDate().equals(data)) {
                Label lbl = new Label("• " + a.getPaciente().getNome().split(" ")[0]);
                lbl.setStyle("-fx-font-size: 10; -fx-text-fill: #1976D2;");
                box.getChildren().add(lbl);
            }
        }

        for (Atividade a : atividades) {
            if (a.getDataInicio().toLocalDate().equals(data)) {
                Label lbl = new Label("★ " + a.getNome());
                lbl.setStyle("-fx-font-size: 10; -fx-text-fill: #9C27B0; -fx-font-weight: bold;");
                box.getChildren().add(lbl);
            }
        }

        return box;
    }
}