import org.w3c.dom.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class Sportsman
{
    private String name, s;
    private LocalDate birthday;
    ArrayList<Event> events;

    Sportsman(Node info)
    {
        name = info.getAttributes().getNamedItem("name").getTextContent();
        s = info.getAttributes().getNamedItem("s").getTextContent();
        String[] i = info.getAttributes().getNamedItem("birthday").getTextContent().split("-");
        birthday = LocalDate.of(Integer.parseInt(i[0]), Integer.parseInt(i[1]), Integer.parseInt(i[2]));
        events = new ArrayList<>();
        NodeList event = info.getChildNodes();
        for (int j = 0; j < event.getLength(); j++)
        {
            if (event.item(j).getNodeType() != Node.TEXT_NODE)
                events.add(new Event(event.item(j)));
        }
    }
    Sportsman()
    {
    }

    Sportsman(String name, String s, String birthday, ArrayList<Event> events)
    {
        this.name = name;
        this.s = s;
        String[] i = birthday.split("-");
        this.birthday = LocalDate.of(Integer.parseInt(i[0]), Integer.parseInt(i[1]), Integer.parseInt(i[2]));
        this.events = events;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" ").append(birthday).append(" ").append(s).append("\n");
        for (Event e : events)
            sb.append(e.place).append(" ").append(e.year).append("\nРезультат: ").append(e.result).append(" ").append(e.award).append("\n");
        return sb.toString();
    }
    public String getS()
    {
        return s;
    }

    public String getName()
    {
        return name;
    }

    public LocalDate getBirthday()
    {
        return birthday;
    }

    public ArrayList<Event> getEvents()
    {
        return events;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setS(String s)
    {
        this.s = s;
    }

    public void setBirthday(LocalDate birthday)
    {
        this.birthday = birthday;
    }

    public void setEvents(ArrayList<Event> events)
    {
        this.events = events;
    }

    public String toJSON()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\n \"sportsmen\":\n  {\"name\":\"").append(name).append("\",\n").append("\n   \"birthday\":\"").append(birthday).append("\",\n").append("\n\"s\":\"").append(s).append("\",\n");
        for (Event e : events)
        {
            if (e != events.get(events.size() - 1))
                sb.append("\n   \"event\":\n{").append("\n    \"place\":\"").append(e.place).append("\",\n").append("\n    \"year\":\"").append(e.year).append("\",\n").append("\n    \"result\":\"").append(e.result).append("\",\n").append("\n    \"award\":\"").append(e.award).append("\"\n},");
            else
                sb.append("\n   \"event\":\n{").append("\n    \"place\":\"").append(e.place).append("\",\n").append("\n    \"year\":\"").append(e.year).append("\",\n").append("\n    \"result\":\"").append(e.result).append("\",\n").append("\n    \"award\":\"").append(e.award).append("\"\n}");
        }
        return sb.toString();
    }

    static class Event
    {
        private String place, award;
        private int result, year;

        Event(Node info)
        {
            place = info.getAttributes().getNamedItem("place").getTextContent();
            year = Integer.parseInt(info.getAttributes().getNamedItem("year").getTextContent());
            NodeList data = info.getChildNodes();
            for (int j = 0; j < data.getLength(); j += 2)
            {
                result = Integer.parseInt(data.item(1).getTextContent());
                award = data.item(3).getTextContent();
            }
        }

        Event()
        {
        }

        Event(String place, String year)
        {
            this.place = place;
            this.year = Integer.parseInt(year);
        }
        public int getYear()
        {
            return year;
        }

        public String getPlace()
        {
            return place;
        }

        public int getResult()
        {
            return result;
        }

        public String getAward()
        {
            return award;
        }

        public void setYear(int year)
        {
            this.year = year;
        }

        public void setPlace(String place)
        {
            this.place = place;
        }

        public void setResult(int result)
        {
            this.result = result;
        }

        public void setAward(String award)
        {
            this.award = award;
        }
    }
}
