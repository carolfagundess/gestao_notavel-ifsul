package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Usuario;
import br.com.gestaonotavel.ifsul.service.UsuarioService;
import br.com.gestaonotavel.ifsul.service.factory.ServiceFactory;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import br.com.gestaonotavel.ifsul.util.MaskUtil;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;
import br.com.gestaonotavel.ifsul.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert; // Import Importante
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TelaLoginController implements Initializable {

    @FXML private TextField cpfTextField;
    @FXML private TextField senhaPasswordField;

    final UsuarioService usuarioService;

    public TelaLoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MaskUtil.cpfField(cpfTextField);
    }

    @FXML
    private void handleEntrarButtonAction(ActionEvent event) {
        try {
            String cpf  = cpfTextField.getText();
            String senha = senhaPasswordField.getText();

            Usuario usuario = usuarioService.autenticarUsuario(cpf, senha);
            SessionManager.getInstance().iniciarSessao(usuario);

            System.out.println("Login bem-sucedido! Bem-vindo, " + usuario.getNome());
            abrirTelaPrincipal();
        }catch (RegraDeNegocioException erro){
            // Correção: Garantindo que Alert.AlertType seja reconhecido
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro de autenticação", erro.getMessage());
        }
    }

    public void abrirTelaPrincipal(){
        try {
            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaPrincipal.fxml"));

            loader.setControllerFactory(controller ->
                    new TelaPrincipalController(
                            serviceFactory.getPacienteService(),
                            serviceFactory.getAuditoriaLogService()
                    )
            );
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestão Notável - Painel Principal");
            stage.setScene(new Scene(root));
            stage.show();

            Stage loginStage = (Stage) cpfTextField.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao abrir a tela principal.");
        }
    }
}