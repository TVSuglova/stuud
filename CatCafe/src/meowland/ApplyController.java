package meowland;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
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
    private GroupOfCustomer groupOfCustomer;
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
        this.groupOfCustomer = groupOfCustomer;

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
                applyTable.getItems().add(new GroupOfCustomer(groupOfCustomer.getStartTimeString(),
                        groupOfCustomer.getEndTimeString(), groupOfCustomer.getTime(), groupOfCustomer.getSale(),
                        groupOfCustomer.getCost()));
            }
        }
        total.setText("Итог: " + cost);
    }

    public boolean isClicked()
    {
        return clicked;
    }

    @FXML
    private void enterApply(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
        {
            apply();
        }
    }

    @FXML
    private void clickApply(MouseEvent mouseEvent)
    {
        apply();
    }

    private void apply()
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

        ObservableList<Integer> kindsOfSale = FXCollections.observableArrayList();
        kindsOfSale.addAll(0, 10, 100);

        sale.setCellValueFactory(param ->
        {
            GroupOfCustomer groupOfCustomer = param.getValue();
            return new SimpleObjectProperty<>(groupOfCustomer.getSale());
        });

        sale.setCellFactory(ComboBoxTableCell.forTableColumn(kindsOfSale));

        sale.setOnEditCommit((TableColumn.CellEditEvent<GroupOfCustomer, Integer> event) ->
        {
            TablePosition<GroupOfCustomer, Integer> position = event.getTablePosition();
            GroupOfCustomer groupOfCustomer1 = event.getTableView().getItems().get(position.getRow());

            int cost = Integer.parseInt(total.getText().replace("Итог: ", "")) - groupOfCustomer1.getCost();
            groupOfCustomer1.setSale(event.getNewValue());
            cost += groupOfCustomer1.getCost();

            total.setText("Итог: " + cost);
            groupOfCustomer.setCost(cost);
            applyTable.refresh();
        });
    }
}
