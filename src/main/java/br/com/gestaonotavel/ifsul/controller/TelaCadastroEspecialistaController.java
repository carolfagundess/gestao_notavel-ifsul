package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Especialista;
import br.com.gestaonotavel.ifsul.service.EspecialistaService;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TelaCadastroEspecialistaController implements Initializable {

    @FXML private TextField txtNome;
    @FXML private TextField txtEspecialidade;
    @FXML private TextField txtRegistro;
    @FXML private TextField txtValorSessao;
    @FXML private TextField txtDuracao;
    @FXML private TextField txtMaxPacientes;
    @FXML private Button btnSalvar;
    @FXML private Button btnCancelar;

    private final EspecialistaService especialistaService;

    public TelaCadastroEspecialistaController(EspecialistaService especialistaService) {
        this.especialistaService = especialistaService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    void handleSalvarButtonAction(ActionEvent event) {
        try {
            Especialista especialista = new Especialista();
            especialista.setNome(txtNome.getText());
            especialista.setEspecialidade(txtEspecialidade.getText());
            especialista.setRegistroProfissional(txtRegistro.getText());

            // Conversões e validações básicas
            try {
                double valor = Double.parseDouble(txtValorSessao.getText().replace(",", "."));
                especialista.setValorSessao(valor);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Valor da sessão inválido.");
            }

            try {
                int duracao = Integer.parseInt(txtDuracao.getText());
                especialista.setDuracao(duracao);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Duração deve ser um número inteiro (minutos).");
            }

            try {
                int max = Integer.parseInt(txtMaxPacientes.getText());
                especialista.setMaxPacientes(max);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Máximo de pacientes deve ser um número inteiro.");
            }

            especialista.setPacientesAtuais(0); // Inicia com 0

            especialistaService.salvar(especialista);
            AlertUtil.showAlert(javafx.scene.control.Alert.AlertType.INFORMATION, "Sucesso", "Especialista cadastrado com sucesso!");
            ((Stage) btnSalvar.getScene().getWindow()).close();

        } catch (Exception e) {
            AlertUtil.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    void handleCancelarButtonAction(ActionEvent event) {
        ((Stage) btnCancelar.getScene().getWindow()).close();
    }
}