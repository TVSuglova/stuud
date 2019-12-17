package meowland;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Test
{
    public static void main(String[] args)
    {
        StringBuilder sb = new StringBuilder();
        String[] columnKeys = {"reason", "information", "sum"};
        String table = "costs";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        String key = dateTimeFormatter.format(LocalDate.now());
        sb.append("SELECT ");
        for (String columnKey : columnKeys)
        {
            sb.append(columnKey).append(", ");
        }
        sb.delete(sb.lastIndexOf(","), sb.lastIndexOf(",") + 2);
        sb.append(" FROM ").append(table).append(" WHERE ").append("date").append(" ='").append(key).append("';");
        System.out.println(sb.toString());
    }
}
