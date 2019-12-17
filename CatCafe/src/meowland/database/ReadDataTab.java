package meowland.database;

import javafx.scene.control.Alert;
import meowland.GroupOfCustomer;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Class for parsing and saving information about previous years from Excel tables.

public class ReadDataTab
{
    public static void main(String[] args)
    {
    }

    public static void readInDatabase(Database database, String filename)
    {
        try
        {
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(filename));
            XSSFSheet sheet;

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d.M.yy H:m");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime end;
            LocalDateTime start;
            int number;
            String sale;
            int price;
            Pattern pattern;
            Matcher matcher;

            for (int i = 0; i < wb.getNumberOfSheets(); i++)
            {
                sheet = wb.getSheetAt(i);
                if (!sheet.getSheetName().matches("\\s*\\d{2}\\.\\d{2}\\.\\d{2}\\s*") ||
                        sheet.getRow(2).getCell(1).getNumericCellValue() == 0)
                    continue;

                database.setTable(sheet.getSheetName().replace(" ", ""));

                for (int j = 2; j <= sheet.getLastRowNum(); j++)
                {
                    if (sheet.getRow(j).getCell(0).getNumericCellValue() == 0 ||
                            sheet.getRow(j).getCell(1).getNumericCellValue() == 0)
                        break;

                    start = LocalDateTime.parse(sheet.getSheetName().replace(" ", "") + " " +
                            (int) sheet.getRow(j).getCell(1).getNumericCellValue() + ":"
                            + (int) sheet.getRow(j).getCell(2).getNumericCellValue(), dateTimeFormatter);
                    end = LocalDateTime.parse(sheet.getSheetName().replace(" ", "") + " " +
                            (int) sheet.getRow(j).getCell(3).getNumericCellValue() + ":"
                            + (int) sheet.getRow(j).getCell(4).getNumericCellValue(), dateTimeFormatter);

                    pattern = Pattern.compile("\\d+");
                    matcher = pattern.matcher(sheet.getRow(j).getCell(7).getStringCellValue());
                    number = 1;
                    if (matcher.find())
                        number = sheet.getRow(j).getCell(7).getStringCellValue().matches("\\d+ ч.*") ?
                                Integer.parseInt(matcher.group()) : 1;

                    sale = sheet.getRow(j).getCell(7).getStringCellValue().matches(".*10%?.*") ? "10" : "0";
                    price = (int) sheet.getRow(j).getCell(6).getNumericCellValue();

                    String[] insert = new String[5];
                    insert[0] = timeFormatter.format(start);
                    insert[1] = timeFormatter.format(end);
                    insert[2] = Integer.toString(number);
                    insert[3] = sale;
                    insert[4] = Integer.toString(price);
                    database.insert(insert);
                }
            }
        } catch (IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ошибка считывания таблицы");
            alert.show();
        }
    }

    public static void readInFile(String filename)
    {
        try
        {
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream("Таблица Котокафе октябрь-декабрь.xlsx"));
            XSSFSheet sheet;

            FileWriter writer;
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d.M.yy H:m");
            GroupOfCustomer groupOfCustomer;
            LocalDateTime end;
            LocalDateTime start;
            int number;
            String sale;
            Pattern pattern;
            Matcher matcher;

            for (int i = 0; i < wb.getNumberOfSheets(); i++)
            {
                sheet = wb.getSheetAt(i);
                if (!sheet.getSheetName().matches("\\s*\\d+\\.\\d+\\.\\d+\\s*") ||
                        sheet.getRow(2).getCell(1).getNumericCellValue() == 0)
                    continue;
                writer = new FileWriter(sheet.getSheetName().replace(" ", "") + ".txt");
                for (int j = 2; j <= sheet.getLastRowNum(); j++)
                {
                    if (sheet.getRow(j).getCell(0).getNumericCellValue() == 0 ||
                            sheet.getRow(j).getCell(1).getNumericCellValue() == 0)
                        break;

                    start = LocalDateTime.parse(sheet.getSheetName().replace(" ", "") + " " +
                            (int) sheet.getRow(j).getCell(1).getNumericCellValue() + ":"
                            + (int) sheet.getRow(j).getCell(2).getNumericCellValue(), dateTimeFormatter);
                    end = LocalDateTime.parse(sheet.getSheetName().replace(" ", "") + " " +
                            (int) sheet.getRow(j).getCell(3).getNumericCellValue() + ":"
                            + (int) sheet.getRow(j).getCell(4).getNumericCellValue(), dateTimeFormatter);

                    pattern = Pattern.compile("\\d+");
                    matcher = pattern.matcher(sheet.getRow(j).getCell(7).getStringCellValue());
                    number = 1;
                    if (matcher.find())
                        number = sheet.getRow(j).getCell(7).getStringCellValue().matches("\\d+ ч.*") ?
                                Integer.parseInt(matcher.group()) : 1;

                    sale = sheet.getRow(j).getCell(7).getStringCellValue().matches(".*10%?.*") ? "10" : null;

                    groupOfCustomer = new GroupOfCustomer(start, end, sale, number);
                    writer.write(groupOfCustomer.toString());
                    writer.append("\n");
                }
                writer.close();
            }
        } catch (IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ошибка считывания таблицы");
            alert.show();
        }
    }
}
