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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaLogin.fxml"));
        loader.setControllerFactory(controllerClass -> {
            if (controllerClass == TelaLoginController.class) {
                return new TelaLoginController(serviceFactory.getUsuarioService());
            } else {
                try {
                    return controllerClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao criar controller: " + controllerClass.getName(), e);
                }
            }
        });
        Parent root = loader.load();

        // Tenta carregar CSS se existir, senão ignora
        try {
            String css = this.getClass().getResource("/styles/telalogin.css").toExternalForm();
            root.getStylesheets().add(css);
        } catch(Exception e) {
            System.out.println("Aviso: CSS telalogin.css não encontrado ou erro ao carregar.");
        }

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Gestão Notável - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        popularBancoDeDados();
        launch(args);
    }
}