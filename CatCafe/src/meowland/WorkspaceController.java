package meowland;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
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
import meowland.database.StatisticsData;
import meowland.network.Network;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.*;

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
    private Label currentDate, oldDate, predictions;
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
    private String prediction;
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

            TitledPane titledPane = new TitledPane(groupOfCustomer.getInfo() + " " + groupOfCustomer.getNumber() + " человек(а)", gridPane);
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
        if (startDate.getValue() == null || endDate.getValue() == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Выберите временной промежуток");
            alert.show();
            return;
        }

        chart.getData().clear();
        chartTable.getItems().clear();
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        attendanceCol.setCellValueFactory(new PropertyValueFactory<>("attendance"));
        ObservableList<DayStatistics> list = FXCollections.observableArrayList();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        database.setTable("statistics");

        Network network = new Network();

        try
        {
            Callable<String> callable1 = null;
            if (prediction == null)
            {
                callable1 = () ->
                {
                    double[] inpForPrediction = StatisticsData.readToDatabase(database);
                    double n = network.predict(inpForPrediction);
                    return (int) Math.floor(n) + " - " + (int) Math.ceil(n) + " человек(а)";
                };
            }

            Callable<XYChart.Series<String, Number>[]> callable2 = () ->
            {
                {
                    XYChart.Series<String, Number>[] series = new XYChart.Series[5];

                    series[0] = new XYChart.Series<>();
                    series[0].setName("Посещаемость");

                    series[1] = new XYChart.Series<>();
                    series[1].setName("Посты за последнюю неделю");

                    series[2] = new XYChart.Series<>();
                    series[2].setName("Посты за последний месяц");

                    series[3] = new XYChart.Series<>();
                    series[3].setName("Лайки за последнюю неделю");

                    ArrayList<double[]> inp = StatisticsData.networkInp(database, startDate.getValue(), endDate.getValue());
                    double[] approximation = network.predict(inp);
                    int j = 0;
                    series[4] = new XYChart.Series<>();
                    series[4].setName("По прогнозу сети");

                    LocalDate localDate = startDate.getValue();
                    while (localDate.isBefore(endDate.getValue()))
                    {
                        localDate = localDate.plusDays(1);
                        String point = database.get("date", dateTimeFormatter.format(localDate), "attendance");
                        if (point == null)
                            continue;

                        list.add(new DayStatistics(dateTimeFormatter.format(localDate), point));
                        series[0].getData().add(new XYChart.Data<>(dateTimeFormatter.format(localDate), Integer.parseInt(point)));
                        series[1].getData().add(new XYChart.Data<>(dateTimeFormatter.format(localDate),
                                Integer.parseInt(database.get("date", dateTimeFormatter.format(localDate), "countOfPostsByWeek"))));
                        series[2].getData().add(new XYChart.Data<>(dateTimeFormatter.format(localDate),
                                Integer.parseInt(database.get("date", dateTimeFormatter.format(localDate), "countOfPostsByMonth"))));
                        series[3].getData().add(new XYChart.Data<>(dateTimeFormatter.format(localDate),
                                Integer.parseInt(database.get("date", dateTimeFormatter.format(localDate), "countOfLikes"))));
                        series[4].getData().add(new XYChart.Data<>(dateTimeFormatter.format(localDate), Math.abs(approximation[j])));
                        j++;
                    }
                    chartTable.setItems(list);

                    return series;
                }
            };

            ExecutorService executorService = Executors.newFixedThreadPool(2);
            Future<String> future1;
            if (callable1 != null)
            {
                future1 = executorService.submit(callable1);
                prediction = future1.get();
            }
            Future<XYChart.Series<String, Number>[]> future2 = executorService.submit(callable2);
            XYChart.Series<String, Number>[] series = future2.get();

            if (instaChart.isSelected())
                chart.getData().addAll(series);
            else
                chart.getData().addAll(series[0], series[4]);

            chart.getXAxis().setLabel("Дата");
            chart.getYAxis().setLabel("Количество");

            String color = colorPicker.getValue().toString().replace("0x", "#").substring(0, 7);
            XYChart.Data<String, Number> dataPoint;
            series[0].getNode().lookup(".chart-series-line").setStyle("-fx-stroke: " + color + ";");
            for (int i = 0; i < series[0].getData().size(); i++)
            {
                dataPoint = series[0].getData().get(i);
                dataPoint.getNode().lookup(".chart-line-symbol").setStyle("-fx-background-color: " + color + ";");
            }

            predictions.setText(prediction);

            executorService.shutdown();
        } catch (ExecutionException | InterruptedException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Не удается получить данные для прогноза\n Проверьте интернет-соединение");
            alert.show();
            predictions.setText("Не удается получить данные для прогноза");
        } catch (Exception e)
        {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Что-то пошло не так");
            alert.show();
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
        } else oldTable.getItems().clear();
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
