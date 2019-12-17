package meowland;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import meowland.database.Database;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    @FXML
    private Button start;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    public Database database;


    @FXML
    private void clickLogIn(MouseEvent mouseEvent)
    {
        logIn(mouseEvent);
    }

    @FXML
    private void enterLogIn(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
        {
            logIn(keyEvent);
        }
    }

    private void logIn(Event event)
    {
        try
        {
            database.setTable("users");
            MessageDigest mdigest = MessageDigest.getInstance("SHA-256");
            byte[] mdBytes = mdigest.digest(passwordField.getText().getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte mdByte : mdBytes)
            {
                String s = Integer.toHexString(0xff & mdByte);
                s = (s.length() == 1) ? "0" + s : s;
                sb.append(s);
            }

            if (database.get("Name", loginField.getText(), "Password") != null &&
                    database.get("Name", loginField.getText(), "Password").equals(sb.toString()))
            {
                database.set("LastSession", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yy")), "Name", loginField.getText());

                FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/workspace1.fxml"));
                loader.setController(new WorkspaceController(database));
                Parent root = loader.load();
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).setTitle("Meowland");
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(new Scene(root,
                        ((Node) event.getSource()).getScene().getWindow().getWidth(),
                        ((Node) event.getSource()).getScene().getWindow().getHeight()));
            } else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Неверный логин или пароль");
                alert.show();
            }
        } catch (NoSuchAlgorithmException | IOException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void logInHandler(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.DOWN)
        {
            passwordField.requestFocus();
        } else
        {
            keyEvent.consume();
        }
    }

    @FXML
    private void PasswordHandler(KeyEvent keyEvent)
    {
        switch (keyEvent.getCode())
        {
            case UP:
            {
                loginField.requestFocus();
                break;
            }
            case DOWN:
            {
                start.requestFocus();
                break;
            }
            default:
                keyEvent.consume();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        String url = "jdbc:mysql://localhost/catcafe?serverTimezone=Europe/Moscow&useSSL=false";
        String username = "root", password = "Tan4iK56!";
        database = new Database(url, username, password);
    }
}
