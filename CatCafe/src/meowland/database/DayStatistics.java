package meowland.database;

public class DayStatistics
{
    private String date, attendance;

    public DayStatistics(String date, String attendance)
    {
        this.date = date;
        this.attendance = attendance;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getAttendance()
    {
        return attendance;
    }

    public void setAttendance(String attendance)
    {
        this.attendance = attendance;
    }
}
