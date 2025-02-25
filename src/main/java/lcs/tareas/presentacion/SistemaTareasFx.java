package lcs.tareas.presentacion;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lcs.tareas.TareasApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class SistemaTareasFx extends Application {

    /*public static void main(String[] args) {
        launch(args);
    }*/

    private ConfigurableApplicationContext appContext;

    @Override
    public void init(){
        this.appContext = new SpringApplicationBuilder(TareasApplication.class).run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(TareasApplication.class.getResource("/templates/index.fxml"));
        loader.setControllerFactory(appContext::getBean);
        Scene escena = new Scene(loader.load());
        stage.setScene(escena);
        stage.show();

    }

    public void stop(){
        appContext.close();
        Platform.exit();
    }
}
