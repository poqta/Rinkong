package ni.edu.uam.rinkong;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PrincipalApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(
                PrincipalApplication.class.getResource("principal-view.fxml")
        );Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Rinkong - Administración Hotelera");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
