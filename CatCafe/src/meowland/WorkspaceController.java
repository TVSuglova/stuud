package meowland;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import meowland.database.Costs;
import meowland.database.Database;
import meowland.database.DayStatistics;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WorkspaceController implements Initializable
{
    @FXML
    private VBox list;
    @FXML
    private TableView<Costs> currentTable, oldTable;
    @FXML
    private TableColumn<Costs, String> tableCosts_1, tableCostsInfo_1, tableCosts_2, tableCostsInfo_2;
    @FXML
    private TableColumn<Costs, Integer> tableValue_1, tableValue_2;
    @FXML
    private Label currentDate, oldDate;
    @FXML
    private TextField costsInfo, value;
    @FXML
    private ComboBox<String> costs;
    @FXML
    private DatePicker startDate, endDate, datePicker;
    @FXML
    private TableView<DayStatistics> chartTable;
    @FXML
    private TableColumn<DayStatistics, String> dateCol, attendanceCol;
    @FXML
    private LineChart<String, Number> chart;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private CheckBox instaChart;

    private Main main = new Main();
    private String table;
    Database database;

    public WorkspaceController(Database database)
    {
        this.database = database;
    }

    @FXML
    private void clickAddCustomer(MouseEvent mouseEvent) throws IOException
    {
        addCustomer();
    }

    @FXML
    private void enterAddCustomer(KeyEvent keyEvent) throws IOException
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
        {
            addCustomer();
        }
    }

    private void addCustomer() throws IOException
    {
        GroupOfCustomer groupOfCustomer = new GroupOfCustomer();
        boolean clicked = main.showDialogWindow(groupOfCustomer);
        if (clicked)
        {
            database.setTable(table);

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            GridPane gridPane = new GridPane();
            ColumnConstraints col0 = new ColumnConstraints();
            col0.setPercentWidth(70);
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(15);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(15);
            gridPane.getColumnConstraints().addAll(col0, col1, col2);

            Button delete = new Button("Удалить");
            Button calculate = new Button("Рассчитать");

            gridPane.add(new Label(groupOfCustomer.getNumber() + " человек(а)"), 0, 0);
            gridPane.add(new Label("Время прихода: " + dateTimeFormatter.format(groupOfCustomer.getStartTime())), 0, 1);
            gridPane.add(delete, 1, 1);
            gridPane.add(calculate, 2, 1);

            TitledPane titledPane = new TitledPane(groupOfCustomer.getInfo(), gridPane);
            list.getChildren().add(titledPane);

            delete.setOnMouseClicked(event -> list.getChildren().remove(titledPane));
            calculate.setOnMouseClicked(event ->
            {
                groupOfCustomer.setEndTime(LocalDateTime.now());
                try
                {
                    boolean clicked1 = main.showApplyingWindow(groupOfCustomer);
                    if (clicked1)
                        if (groupOfCustomer.getTime() != 0)
                        {
                            database.setTable(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yy")));
                            String[] values = {groupOfCustomer.getStartTimeString(), groupOfCustomer.getEndTimeString(),
                                    Integer.toString(groupOfCustomer.getNumber()), Integer.toString(groupOfCustomer.getSale()),
                                    Integer.toString(groupOfCustomer.getCost())};
                            database.insert(values);
                            list.getChildren().remove(titledPane);
                        } else
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Нельзя рассчитать человека, который провел в кафе меньше минуты");
                            alert.show();
                        }
                } catch (IOException e)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Ошибка загрузки");
                    alert.show();
                }
            });
        }
    }

    @FXML
    private void getChart(MouseEvent mouseEvent)
    {
        chart.getData().clear();
        chartTable.getItems().clear();
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        attendanceCol.setCellValueFactory(new PropertyValueFactory<>("attendance"));
        ObservableList<DayStatistics> list = FXCollections.observableArrayList();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        database.setTable("statistics");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Дата");
        yAxis.setLabel("Количество посетителей");

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Посещаемость");
        ObservableList<XYChart.Data<String, Number>> data1 = FXCollections.observableArrayList();

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Посты за последнюю неделю");
        ObservableList<XYChart.Data<String, Number>> data2 = FXCollections.observableArrayList();

        XYChart.Series<String, Number> series3 = new XYChart.Series<>();
        series3.setName("Посты за последний месяц");
        ObservableList<XYChart.Data<String, Number>> data3 = FXCollections.observableArrayList();

        XYChart.Series<String, Number> series4 = new XYChart.Series<>();
        series4.setName("Лайки за последнюю неделю");
        ObservableList<XYChart.Data<String, Number>> data4 = FXCollections.observableArrayList();

        LocalDate localDate = startDate.getValue();
        while (localDate.isBefore(endDate.getValue()))
        {
            localDate = localDate.plusDays(1);
            String point = database.get("date", dateTimeFormatter.format(localDate), "attendance");
            if (point == null)
                continue;

            list.add(new DayStatistics(dateTimeFormatter.format(localDate), point));
            data1.add(new XYChart.Data<>(dateTimeFormatter.format(localDate), Integer.parseInt(point)));
            data2.add(new XYChart.Data<>(dateTimeFormatter.format(localDate),
                    Integer.parseInt(database.get("date", dateTimeFormatter.format(localDate), "countOfPostsByWeek"))));
            data3.add(new XYChart.Data<>(dateTimeFormatter.format(localDate),
                    Integer.parseInt(database.get("date", dateTimeFormatter.format(localDate), "countOfPostsByMonth"))));
            data4.add(new XYChart.Data<>(dateTimeFormatter.format(localDate),
                    Integer.parseInt(database.get("date", dateTimeFormatter.format(localDate), "countOfLikes"))));
        }

        chartTable.setItems(list);

        if (instaChart.isSelected())
        {
            series1.setData(data1);
            series2.setData(data2);
            series3.setData(data3);
            series4.setData(data4);
            chart.getData().addAll(series1, series2, series3, series4);
        } else
        {
            series1.setData(data1);
            chart.getData().add(series1);
        }

        String color = colorPicker.getValue().toString().replace("0x", "#").substring(0, 7);
        XYChart.Data<String, Number> dataPoint;

        series1.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: " + color + ";");
        for (int i = 0; i < series1.getData().size(); i++)
        {
            dataPoint = series1.getData().get(i);
            dataPoint.getNode().lookup(".chart-line-symbol").setStyle("-fx-background-color: " + color + ";");
        }
    }

    @FXML
    private void enterAddCost(KeyEvent keyEvent)
    {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
        {
            addCost();
        }
    }

    @FXML
    private void clickAddCost(MouseEvent mouseEvent)
    {
        addCost();
    }

    public void addCost()
    {
        database.setTable("costs");
        String[] values = {table, costs.getValue(), costsInfo.getText(), value.getText()};
        database.insert(values);

        currentTable.getItems().add(new Costs(costs.getValue(), costsInfo.getText(), Integer.parseInt(value.getText())));
        currentTable.refresh();
    }

    @FXML
    private void clickGet(MouseEvent mouseEvent)
    {
        get();
    }

    @FXML
    private void enterGet(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
        {
            get();
        }
    }

    private void get()
    {
        ObservableList<Costs> list = FXCollections.observableArrayList();
        database.setTable("costs");
        String[] columnKeys = {"reason", "information", "sum"};

        ArrayList<String> results = database.get("date", datePicker.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yy")), columnKeys);
        oldDate.setText(datePicker.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yy")));

        if (results != null)
        {
            for (int i = 0; i < results.size(); i += 3)
            {
                list.add(new Costs(results.get(i), results.get(i + 1), Integer.parseInt(results.get(i + 2))));
            }
            oldTable.setItems(list);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        LocalDate today = LocalDate.now();
        table = dateTimeFormatter.format(today);

        tableCosts_1.setCellValueFactory(new PropertyValueFactory<>("reason"));
        tableCosts_2.setCellValueFactory(new PropertyValueFactory<>("reason"));
        tableCostsInfo_1.setCellValueFactory(new PropertyValueFactory<>("info"));
        tableCostsInfo_2.setCellValueFactory(new PropertyValueFactory<>("info"));
        tableValue_1.setCellValueFactory(new PropertyValueFactory<>("sum"));
        tableValue_2.setCellValueFactory(new PropertyValueFactory<>("sum"));

        ObservableList<Costs> list1 = FXCollections.observableArrayList();
        database.setTable("costs");
        String[] columnKeys = {"reason", "information", "sum"};

        ArrayList<String> results = database.get("date", table, columnKeys);
        currentDate.setText(table);
        currentTable.getItems().clear();

        if (results != null)
        {
            for (int i = 0; i < results.size(); i += 3)
            {
                list1.add(new Costs(results.get(i), results.get(i + 1), Integer.parseInt(results.get(i + 2))));
            }
            currentTable.setItems(list1);
        }

        ObservableList<Costs> list2 = FXCollections.observableArrayList();
        results = null;

        for (int i = 1; results == null; i++)
        {
            results = database.get("date", dateTimeFormatter.format(today.minusDays(i)), columnKeys);
            oldDate.setText(dateTimeFormatter.format(today.minusDays(i)));
        }
        for (int i = 0; i < results.size(); i += 3)
        {
            list2.add(new Costs(results.get(i), results.get(i + 1), Integer.parseInt(results.get(i + 2))));
        }
        oldTable.setItems(list2);
    }
}
