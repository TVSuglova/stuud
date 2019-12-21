package meowland;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.time.LocalDateTime;

// Controller of the window with customers information fields.

public class DialogWindowController
{
    private Stage stage;
    private GroupOfCustomer groupOfCustomer;
    private boolean clicked = false;
    @FXML
    private TextField info, number;
    @FXML
    private ComboBox<String> sale, timer;

    public void setStage(Stage dialogStage)
    {
        this.stage = dialogStage;
    }

    public void setGroupOfCustomer(GroupOfCustomer groupOfCustomer)
    {
        this.groupOfCustomer = groupOfCustomer;
    }

    public boolean isClicked()
    {
        return clicked;
    }

    @FXML
    private void clickSave(MouseEvent mouseEvent)
    {
        if (check())
        {
            save();
        }
    }

    @FXML
    private void enterSave(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER && check())
        {
            save();
        }
    }

    private void save()
    {
        groupOfCustomer.setInfo(info.getText());
        groupOfCustomer.setSale(sale.getValue());
        groupOfCustomer.setTimer(timer.getValue());
        groupOfCustomer.setNumber(Integer.parseInt(number.getText()));
        groupOfCustomer.setStartTime(LocalDateTime.now());

        clicked = true;
        stage.close();

        info.clear();
        number.clear();
        timer.getSelectionModel().clearSelection();
        timer.setButtonCell(new ListCell<>()
        {
            @Override
            protected void updateItem(String item, boolean empty)
            {
                super.updateItem(item, empty);
                setText(item);
            }
        });
        sale.getSelectionModel().clearSelection();
        sale.setButtonCell(new ListCell<>()
        {
            @Override
            protected void updateItem(String item, boolean empty)
            {
                super.updateItem(item, empty);
                setText(item);
            }
        });
        info.requestFocus();
    }

    private boolean check()
    {
        try
        {
            if (number.getText().equals(""))
                number.setText("1");
            Integer.parseInt(number.getText());
            if (info.getText().equals(""))
                info.setText("Посетители");
            if (sale.getValue() == null)
                sale.setValue("нет/заполнить позже");
            if (timer.getValue() == null)
                timer.setValue("не напоминать");
            return true;
        } catch (NumberFormatException ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Количество людей должно быть числом");
            alert.show();
            return false;
        } catch (Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ошибка ввода");
            alert.show();
            return false;
        }
    }

    @FXML
    private void chancel(MouseEvent mouseEvent)
    {
        stage.close();
        info.clear();
        number.clear();
        timer.getSelectionModel().clearSelection();
        timer.setButtonCell(new ListCell<>()
        {
            @Override
            protected void updateItem(String item, boolean empty)
            {
                super.updateItem(item, empty);
                setText(item);
            }
        });
        sale.getSelectionModel().clearSelection();
        sale.setButtonCell(new ListCell<>()
        {
            @Override
            protected void updateItem(String item, boolean empty)
            {
                super.updateItem(item, empty);
                setText(item);
            }
        });
        info.requestFocus();
    }

    @FXML
    private void enterChancel(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
        {
            stage.close();
            info.clear();
            number.clear();
            timer.getSelectionModel().clearSelection();
            timer.setButtonCell(new ListCell<>()
            {
                @Override
                protected void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);
                    setText(item);
                }
            });
            sale.getSelectionModel().clearSelection();
            sale.setButtonCell(new ListCell<>()
            {
                @Override
                protected void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);
                    setText(item);
                }
            });
            info.requestFocus();
        }
    }

    @FXML
    private void infoHandler(KeyEvent keyEvent)
    {
        switch (keyEvent.getCode())
        {
            case DOWN:
            {
                number.requestFocus();
                break;
            }
            case RIGHT:
            {
                Platform.runLater(() -> sale.requestFocus());
                sale.show();
                break;
            }
            case UP:
            case LEFT:
                break;
            default:
                keyEvent.consume();
        }
    }

    @FXML
    private void numberHandler(KeyEvent keyEvent)
    {
        switch (keyEvent.getCode())
        {
            case DOWN:
            {
                Platform.runLater(() -> timer.requestFocus());
                timer.show();
                break;
            }
            case UP:
            {
                info.requestFocus();
                break;
            }
            case RIGHT:
            case LEFT:
                break;
            default:
                keyEvent.consume();
        }
    }

    @FXML
    private void timerHandler(KeyEvent keyEvent)
    {
        switch (keyEvent.getCode())
        {
            case UP:
            {
                number.requestFocus();
                break;
            }
            case RIGHT:
            {
                Platform.runLater(() -> sale.requestFocus());
                sale.show();
                keyEvent.consume();
                break;
            }
            case DOWN:
            case LEFT:
                break;
            default:
                keyEvent.consume();
        }
    }
}
