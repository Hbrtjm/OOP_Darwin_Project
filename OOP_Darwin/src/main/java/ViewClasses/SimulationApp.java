package ViewClasses;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.List;

public class SimulationApp extends Application {

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Darwin Simulation Application");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
    @Override
    public void start(Stage primaryStage) throws IOException
    {
        List<String> args = getParameters().getRaw();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("SimulationParametersController.fxml"));
        BorderPane viewRoot = loader.load();
        String[] argsArray = args.toArray(new String[0]);
        primaryStage.show();
        configureStage(primaryStage,viewRoot);
    }
}
