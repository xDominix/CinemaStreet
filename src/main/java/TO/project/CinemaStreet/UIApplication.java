package TO.project.CinemaStreet;

import TO.project.CinemaStreet.controller.UserController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UIApplication extends javafx.application.Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cinema Street");

//        set white background
        javafx.scene.layout.Pane root = new javafx.scene.layout.Pane();
        javafx.scene.Scene scene = new javafx.scene.Scene(root, 800, 600);
        scene.setFill(javafx.scene.paint.Color.WHITE);
        primaryStage.setScene(scene);

        var loader = new FXMLLoader();
        loader.setLocation(UIApplication.class.getResource("/view/UserView.fxml"));
        AnchorPane rootLayout = loader.load();
        rootLayout.setPrefSize(800, 600);

        UserController controller = loader.getController();


        configureStage(primaryStage, rootLayout);
        primaryStage.show();
    }
    private void configureStage(Stage primaryStage, AnchorPane rootLayout) {
        var scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("User List");
        primaryStage.minWidthProperty().bind(rootLayout.minWidthProperty());
        primaryStage.minHeightProperty().bind(rootLayout.minHeightProperty());
    }
}
