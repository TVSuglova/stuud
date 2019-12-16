package meowland;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ApplyController implements Initializable
{
    private Stage stage;
    private boolean clicked = false;
    @FXML
    private TableView<GroupOfCustomer> applyTable;
    @FXML
    private TableColumn<GroupOfCustomer, String> startTime, endTime;
    @FXML
    private TableColumn<GroupOfCustomer, Integer> sale, time, cost;
    @FXML
    private Label total;

    public void setStage(Stage dialogStage)
    {
        this.stage = dialogStage;
    }

    public void setGroupOfCustomer(GroupOfCustomer groupOfCustomer)
    {
        int cost = 0;
        if (groupOfCustomer.getNumber() == 1)
        {
            cost += groupOfCustomer.getCost();
            applyTable.getItems().add(groupOfCustomer);
        } else
        {
            for (int i = 0; i < groupOfCustomer.getNumber(); i++)
            {
                cost += groupOfCustomer.getCost();
                applyTable.getItems().add(groupOfCustomer);
            }
        }
        total.setText("Итог: " + cost);
    }

    public boolean isClicked()
    {
        return clicked;
    }

    public void enterApply(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
        {
            clicked = true;
            stage.close();
            applyTable.getItems().clear();
            total.setText("Итог: 0");
        }
    }

    public void apply(MouseEvent mouseEvent)
    {
        clicked = true;
        stage.close();
        applyTable.getItems().clear();
        total.setText("Итог: 0");
    }

    @FXML
    private void chancel(MouseEvent mouseEvent)
    {
        clicked = false;
        stage.close();
        applyTable.getItems().clear();
        total.setText("Итог: 0");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        startTime.setCellValueFactory(new PropertyValueFactory<>("startTimeString"));
        endTime.setCellValueFactory(new PropertyValueFactory<>("endTimeString"));
        sale.setCellValueFactory(new PropertyValueFactory<>("sale"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
    }
}
