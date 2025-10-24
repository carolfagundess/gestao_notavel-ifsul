package br.com.gestaonotavel.ifsul;

import br.com.gestaonotavel.ifsul.controller.TelaLoginController;
import br.com.gestaonotavel.ifsul.service.factory.ServiceFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static br.com.gestaonotavel.ifsul.util.DataInitializer.popularBancoDeDados;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        // Carrega o arquivo FXML da tela de login
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaLogin.fxml"));
        loader.setControllerFactory(controllerClass -> {
            if (controllerClass == TelaLoginController.class) {
                // Injeta o serviço do nosso contêiner
                return new TelaLoginController(serviceFactory.getUsuarioService());
            } else {
                // Se não for o controller que esperamos, deixe o JavaFX tentar
                try {
                    return controllerClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao criar controller: " + controllerClass.getName(), e);
                }
            }
        });
        Parent root = loader.load();

        // Carrega o arquivo CSS
        String css = this.getClass().getResource("/styles/telalogin.css").toExternalForm();
        root.getStylesheets().add(css);

        // Cria a cena com o conteúdo da tela
        Scene scene = new Scene(root, 800, 600);

        // Define o título da janela
        stage.setTitle("Gestão Notável - Login");

        // Define a cena na janela
        stage.setScene(scene);

        // Exibe a janela
        stage.show();
    }

    public static void main(String[] args) {
        popularBancoDeDados();
        launch(args);
    }

}