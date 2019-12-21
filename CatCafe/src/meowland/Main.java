package meowland;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application
{

    static private Stage primaryStage;
    static private Stage dialogStage;
    static private Stage applyingStage;
    private FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/customersInfo.fxml"));
    private FXMLLoader loader1 = new FXMLLoader(Main.class.getResource("fxml/applying.fxml"));
    Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/identification.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        primaryStage.setTitle("Meowland");
        primaryStage.getIcons().addAll(
                new Image(getClass().getResourceAsStream("pictures/icon32x32.png")),
                new Image(getClass().getResourceAsStream("pictures/icon48x48.png")),
                new Image(getClass().getResourceAsStream("pictures/icon64x64.png")));
        primaryStage.setScene(new Scene(root, 900, 600));
        Main.primaryStage = primaryStage;
        primaryStage.show();
    }

    @Override
    public void stop()
    {
        controller.database.close();
        System.exit(0);
    }

    public boolean showDialogWindow(GroupOfCustomer groupOfCustomer) throws IOException
    {
        if (dialogStage == null)
        {
            Parent root = loader.load();
            dialogStage = new Stage();
            dialogStage.setTitle("Анкета посетителей");
            dialogStage.getIcons().addAll(
                    new Image(getClass().getResourceAsStream("pictures/icon32x32.png")),
                    new Image(getClass().getResourceAsStream("pictures/icon48x48.png")),
                    new Image(getClass().getResourceAsStream("pictures/icon64x64.png")));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(root, 700, 200));
        }

        DialogWindowController controller = loader.getController();
        controller.setStage(dialogStage);
        controller.setGroupOfCustomer(groupOfCustomer);

        dialogStage.showAndWait();
        return controller.isClicked();
    }

    public boolean showApplyingWindow(GroupOfCustomer groupOfCustomer) throws IOException
    {
        if (applyingStage == null)
        {
            Parent root = loader1.load();
            applyingStage = new Stage();
            applyingStage.setTitle("Итог");
            applyingStage.getIcons().addAll(
                    new Image(getClass().getResourceAsStream("pictures/icon32x32.png")),
                    new Image(getClass().getResourceAsStream("pictures/icon48x48.png")),
                    new Image(getClass().getResourceAsStream("pictures/icon64x64.png")));
            applyingStage.initModality(Modality.WINDOW_MODAL);
            applyingStage.initOwner(primaryStage);
            applyingStage.setScene(new Scene(root, 700, 500));
        }

        ApplyController controller = loader1.getController();
        controller.setStage(applyingStage);
        controller.setGroupOfCustomer(groupOfCustomer);

        applyingStage.showAndWait();
        return controller.isClicked();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
