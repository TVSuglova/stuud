package meowland;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;


// Class for storing information about customers.
// Field info will not save in the database.

public class GroupOfCustomer
{

    private String startTimeString = null, endTimeString = null, info = null;
    private LocalDateTime startTime, endTime;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private int number, sale, time, timer, cost;
    private int[] holidayPrice = {80, 150, 2}, weekdaysPrice = {100, 180, 2};

    public GroupOfCustomer()
    {
    }

    public GroupOfCustomer(String startTimeString, String endTimeString, int time, int sale, int cost)
    {
        this.startTime = LocalDateTime.of(LocalDate.now(), LocalTime.parse(startTimeString, dateTimeFormatter));
        this.startTimeString = startTimeString;
        this.endTime = LocalDateTime.of(LocalDate.now(), LocalTime.parse(endTimeString, dateTimeFormatter));
        this.endTimeString = endTimeString;
        this.sale = sale;
        this.time = time;
        this.cost = cost;
        this.number = 1;
    }

    public GroupOfCustomer(LocalDateTime startTime, LocalDateTime endTime, String sale, int number)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.number = number;
        switch (sale)
        {
            case "нет/заполнить позже":
            case "нет":
            case "0":
            {
                this.sale = 0;
                break;
            }
            case "именинник - бесплатно":
            case "100":
            {
                this.sale = 100;
                break;
            }
            case "10% студенческая":
            case "10% постоянный клиент":
            case "10% большая компания":
            case "10":
            {
                this.sale = 10;
                break;
            }
        }
    }

    public String getStartTimeString()
    {
        return startTimeString;
    }
    public void setStartTimeString(LocalDateTime startTime)
    {
        this.startTimeString = dateTimeFormatter.format(startTime);
    }

    public String getEndTimeString()
    {
        return endTimeString;
    }
    public void setEndTimeString(LocalDateTime endTime)
    {
        this.endTimeString = dateTimeFormatter.format(endTime);
    }

    public LocalDateTime getStartTime()
    {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime)
    {
        this.startTime = startTime;
        startTimeString = dateTimeFormatter.format(startTime);
    }

    public LocalDateTime getEndTime()
    {
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime)
    {
        this.endTime = endTime;
        endTimeString = dateTimeFormatter.format(endTime);

        time = 0;
        time += (endTime.getHour() - startTime.getHour()) * 60;
        time += endTime.getMinute() - startTime.getMinute();
        if (endTime.getDayOfWeek().equals(DayOfWeek.SUNDAY) || endTime.getDayOfWeek().equals(DayOfWeek.SATURDAY))
        {
            if (time <= 30)
                cost = holidayPrice[0];
            else if (time <= 60)
                cost = holidayPrice[1];
            else
                cost = holidayPrice[1] + (time - 60) * holidayPrice[2];
        } else
        {
            if (time <= 30)
                cost = weekdaysPrice[0];
            else if (time <= 60)
                cost = weekdaysPrice[1];
            else
                cost = weekdaysPrice[1] + (time - 60) * weekdaysPrice[2];
        }
        cost = cost - cost * this.sale / 100;
    }

    public int getNumber()
    {
        return number;
    }
    public void setNumber(int number)
    {
        this.number = number;
    }

    public String getInfo()
    {
        return info;
    }
    public void setInfo(String info)
    {
        this.info = info;
    }

    public int getTimer()
    {
        return timer;
    }

    public void setTimer(String timer)
    {
        switch (timer)
        {
            case "не напоминать":
            {
                this.timer = 0;
                break;
            }
            case "час":
            {
                this.timer = 60;
                break;
            }
            case "пол часа":
            {
                this.timer = 30;
                break;
            }
        }

        if (this.timer != 0)
        {
            Timer myTimer = new Timer();
            TimerTask timerTask = new TimerTask()
            {
                @Override
                public void run()
                {
                    Platform.runLater(() ->
                    {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("У клиента(ов) " + info + " " + number + " человек(а) " + " заканчивается время");
                        alert.setGraphic(new ImageView("file:icon.png"));
                        alert.show();
                        myTimer.cancel();
                        myTimer.purge();
                    });
                }
            };
            myTimer.schedule(timerTask, (this.timer - 5) * 60 * 1000);
        }
    }

    public int getTime()
    {
        return time;
    }
    public void setTime(int time)
    {
        this.time = time;
    }

    public int getSale()
    {
        return sale;
    }
    public void setSale(int sale)
    {
        this.sale = sale;
        if (endTime.getDayOfWeek().equals(DayOfWeek.SUNDAY) || endTime.getDayOfWeek().equals(DayOfWeek.SATURDAY))
        {
            if (time <= 30)
                cost = holidayPrice[0];
            else if (time <= 60)
                cost = holidayPrice[1];
            else
                cost = holidayPrice[1] + (time - 60) * holidayPrice[2];
        } else
        {
            if (time <= 30)
                cost = weekdaysPrice[0];
            else if (time <= 60)
                cost = weekdaysPrice[1];
            else
                cost = weekdaysPrice[1] + (time - 60) * weekdaysPrice[2];
        }
        cost = cost - cost * sale / 100;
    }
    public void setSale(String sale)
    {
        switch (sale)
        {
            case "нет/заполнить позже":
            case "нет":
            case "0":
            {
                this.sale = 0;
                break;
            }
            case "именинник - бесплатно":
            case "100":
            {
                this.sale = 100;
                break;
            }
            case "10% студенческая":
            case "10% постоянный клиент":
            case "10% большая компания":
            case "10":
            {
                this.sale = 10;
                break;
            }
        }
    }

    public int getCost()
    {
        return cost;
    }
    public void setCost(int cost)
    {
        this.cost = cost;
    }

    public String toString()
    {
        return number + " человек(а) " + dateTimeFormatter.format(startTime) + " - " + dateTimeFormatter.format(endTime) + " " + sale;
    }
}
