package meowland.database;

import me.postaddict.instagram.scraper.Instagram;
import me.postaddict.instagram.scraper.StatelessInsta;
import me.postaddict.instagram.scraper.interceptor.ErrorInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgentInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgents;
import me.postaddict.instagram.scraper.model.Media;
import me.postaddict.instagram.scraper.model.PageObject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Class for parsing and saving information from Instagram and Excel tables.

public class StatisticsData
{
    public static void main(String[] args)
    {
    }

    public static ArrayList<double[]> networkInp(Database database, LocalDate start, LocalDate end)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        database.setTable("statistics");
        ArrayList<double[]> inps = new ArrayList<>();
        double[] inp = new double[5];

        while (start.isBefore(end))
        {
            start = start.plusDays(1);
            String check = database.get("date", dateTimeFormatter.format(start), "attendance");
            if (check == null)
                continue;

            inp[0] = Integer.parseInt(database.get("date", dateTimeFormatter.format(start), "countOfPostsByMonth"));
            inp[1] = Integer.parseInt(database.get("date", dateTimeFormatter.format(start), "countOfPostsByWeek"));
            inp[2] = Integer.parseInt(database.get("date", dateTimeFormatter.format(start), "countOfLikes"));
            inp[3] = Integer.parseInt(database.get("date", dateTimeFormatter.format(start), "maxCountOfLikes"));
            inp[4] = Integer.parseInt(database.get("date", dateTimeFormatter.format(start), "countOfComments"));

            inps.add(inp);
            inp = new double[5];
        }

        return inps;
    }

    public static void readFromTableToDatabase(Database database, String filename)
    {
        try
        {
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(filename));
            XSSFSheet sheet;
            database.setTable("statistics");

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d.M.yy H:m");

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(loggingInterceptor)
                    .addInterceptor(new UserAgentInterceptor(UserAgents.OSX_CHROME))
                    .addInterceptor(new ErrorInterceptor())
                    .build();
            StatelessInsta client = new Instagram(httpClient);
            client.basePage();

            PageObject<Media> medias = client.getMedias("kotokafe_meowland", 10);
            Collection<Media> mediaList = medias.getNodes();

            for (int i = 0; i < wb.getNumberOfSheets(); i++)
            {
                sheet = wb.getSheetAt(i);
                if (!sheet.getSheetName().matches("\\s*\\d{2}\\.\\d{2}\\.\\d{2}\\s*") || sheet.getRow(2).getCell(1).getNumericCellValue() == 0)
                    continue;

                int countOfPostsByWeek = 0;
                int countOfPostsByMonth = 0;
                int countOfLikes = 0;
                int maxCountOfLikes = 0;
                int countOfComments = 0;
                int countOfClients = 0;
                String[] values = new String[7];
                int number;
                Pattern pattern;
                Matcher matcher;

                for (int j = 2; j <= sheet.getLastRowNum(); j++)
                {
                    if (sheet.getRow(j).getCell(0).getNumericCellValue() == 0 ||
                            sheet.getRow(j).getCell(1).getNumericCellValue() == 0)
                        break;
                    number = 1;
                    pattern = Pattern.compile("\\d+");
                    matcher = pattern.matcher(sheet.getRow(j).getCell(7).getStringCellValue());
                    if (matcher.find())
                        number = sheet.getRow(j).getCell(7).getStringCellValue().matches("\\d+ ч.*") ?
                                Integer.parseInt(matcher.group()) : 1;
                    countOfClients += number;
                }

                values[0] = sheet.getSheetName().replace(" ", "");
                values[1] = Integer.toString(countOfClients);
                LocalDateTime today = LocalDateTime.parse(sheet.getSheetName().replace(" ", "") + " 0:0", dateTimeFormatter);

                for (Media media : mediaList)
                {
                    if (DateUtil.toLocalDateTime(media.getCreated()).isBefore(today.minusDays(30)))
                        break;
                    if (DateUtil.toLocalDateTime(media.getCreated()).isBefore(today)
                            && DateUtil.toLocalDateTime(media.getCreated()).isAfter(today.minusDays(7)))
                    {
                        countOfComments += media.getCommentCount();
                        countOfLikes += media.getLikeCount();
                        maxCountOfLikes = media.getLikeCount() > maxCountOfLikes ? media.getLikeCount() : maxCountOfLikes;
                        countOfPostsByWeek++;
                    }
                    if (DateUtil.toLocalDateTime(media.getCreated()).isBefore(today)
                            && DateUtil.toLocalDateTime(media.getCreated()).isAfter(today.minusDays(30)))
                        countOfPostsByMonth++;
                }

                values[2] = Integer.toString(countOfPostsByWeek);
                values[3] = Integer.toString(countOfPostsByMonth);
                values[4] = Integer.toString(countOfLikes);
                values[5] = Integer.toString(maxCountOfLikes);
                values[6] = Integer.toString(countOfComments);

                database.insert(values, 0);

                countOfPostsByWeek = 0;
                countOfPostsByMonth = 0;
                countOfLikes = 0;
                maxCountOfLikes = 0;
                countOfComments = 0;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static double[] readToDatabase(Database database) throws IOException
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(new UserAgentInterceptor(UserAgents.OSX_CHROME))
                .addInterceptor(new ErrorInterceptor())
                .build();
        StatelessInsta client = new Instagram(httpClient);
        client.basePage();
        PageObject<Media> medias = client.getMedias("kotokafe_meowland", 1);
        Collection<Media> mediaList = medias.getNodes();

        LocalDate localDate = LocalDate.now();
        LocalDateTime today = LocalDateTime.now();
        double[] inp = new double[5];
        String lastDate = database.getLastRowCell("costs", "date");
        while (!dateTimeFormatter.format(localDate).equals(lastDate))
        {
            if (localDate.equals(today.toLocalDate()))
            {
                database.setTable("statistics");
                int countOfPostsByWeek = 0;
                int countOfPostsByMonth = 0;
                int countOfLikes = 0;
                int maxCountOfLikes = 0;
                int countOfComments = 0;

                for (Media media : mediaList)
                {
                    if (DateUtil.toLocalDateTime(media.getCreated()).isBefore(today.minusDays(30)))
                        break;
                    if (DateUtil.toLocalDateTime(media.getCreated()).isBefore(today)
                            && DateUtil.toLocalDateTime(media.getCreated()).isAfter(today.minusDays(7)))
                    {
                        countOfComments += media.getCommentCount();
                        countOfLikes += media.getLikeCount();
                        maxCountOfLikes = media.getLikeCount() > maxCountOfLikes ? media.getLikeCount() : maxCountOfLikes;
                        countOfPostsByWeek++;
                    }
                    if (DateUtil.toLocalDateTime(media.getCreated()).isBefore(today)
                            && DateUtil.toLocalDateTime(media.getCreated()).isAfter(today.minusDays(30)))
                        countOfPostsByMonth++;
                }

                inp[0] = countOfPostsByMonth;
                inp[1] = countOfPostsByWeek;
                inp[2] = countOfLikes;
                inp[3] = maxCountOfLikes;
                inp[4] = countOfComments;
            }

            if (database.isThereTable(dateTimeFormatter.format(localDate)))
            {
                database.setTable(dateTimeFormatter.format(localDate));
                int countOfClients = 0;
                ArrayList<String> list = database.getAll("Number");
                if (list == null)
                {
                    localDate = localDate.minusDays(1);
                    continue;
                }
                for (String n : list)
                    countOfClients += Integer.parseInt(n);

                database.setTable("statistics");
                int countOfPostsByWeek = 0;
                int countOfPostsByMonth = 0;
                int countOfLikes = 0;
                int maxCountOfLikes = 0;
                int countOfComments = 0;
                String[] values = new String[7];

                values[0] = dateTimeFormatter.format(localDate);
                values[1] = Integer.toString(countOfClients);

                for (Media media : mediaList)
                {
                    if (DateUtil.toLocalDateTime(media.getCreated()).isBefore(today.minusDays(30)))
                        break;
                    if (DateUtil.toLocalDateTime(media.getCreated()).isBefore(today)
                            && DateUtil.toLocalDateTime(media.getCreated()).isAfter(today.minusDays(7)))
                    {
                        countOfComments += media.getCommentCount();
                        countOfLikes += media.getLikeCount();
                        maxCountOfLikes = media.getLikeCount() > maxCountOfLikes ? media.getLikeCount() : maxCountOfLikes;
                        countOfPostsByWeek++;
                    }
                    if (DateUtil.toLocalDateTime(media.getCreated()).isBefore(today)
                            && DateUtil.toLocalDateTime(media.getCreated()).isAfter(today.minusDays(30)))
                        countOfPostsByMonth++;
                }

                values[2] = Integer.toString(countOfPostsByWeek);
                values[3] = Integer.toString(countOfPostsByMonth);
                values[4] = Integer.toString(countOfLikes);
                values[5] = Integer.toString(maxCountOfLikes);
                values[6] = Integer.toString(countOfComments);

                database.insert(values, 0);

                countOfPostsByWeek = 0;
                countOfPostsByMonth = 0;
                countOfLikes = 0;
                maxCountOfLikes = 0;
                countOfComments = 0;
            }
            localDate = localDate.minusDays(1);
        }
        return inp;
    }
}
