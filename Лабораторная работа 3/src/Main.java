import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main
{

    public static void main(String[] args)
    {
        readAndPrintFile1();
        readAndPrintFile2();
    }

    public static void readAndPrintFile2()
    {
        ArrayList<Sportsman> sportsmen = new ArrayList<>();
        try
        {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse("File2.xml");
            Node root = document.getDocumentElement();
            NodeList bufSportsmen = root.getChildNodes();
            for (int i = 0; i < bufSportsmen.getLength(); i++)
            {
                if (bufSportsmen.item(i).getNodeType() != Node.TEXT_NODE)
                {
                    sportsmen.add(new Sportsman(bufSportsmen.item(i)));
                }
            }

        } catch (ParserConfigurationException | IOException | SAXException e)
        {
            e.printStackTrace();
        }
        for (Sportsman human : sportsmen)
        {
            if (human.getS().compareTo("м") == 0)
                System.out.println(human.getName() + " " + human.getBirthday());
        }
        System.out.println();
        for (Sportsman human : sportsmen)
        {
            if (human.getBirthday().isBefore(LocalDate.of(1985, 12, 31)) && human.getS().compareTo("ж") == 0)
                System.out.println(human.getName() + " " + human.getBirthday() + "\nКоличество медалей: " + human.getEvents().size());
        }
        System.out.println();
        for (Sportsman human : sportsmen)
        {
            for (Sportsman.Event event : human.getEvents())
            {
                if (event.getYear() == 2002 && event.getPlace().compareTo("москва") == 0)
                    System.out.println(human.getName() + "\nРезультат: " + event.getResult());
            }
        }
        System.out.println();
        Scanner in = new Scanner(System.in);
        Sportsman newSportsmen = new Sportsman();
        System.out.println("Введите имя и пол(м, ж): ");
        newSportsmen.setName(in.next());
        newSportsmen.setS(in.next());
        System.out.println("Введите дату рождения через пробел(год, месяц, число): ");
        newSportsmen.setBirthday(LocalDate.of(in.nextInt(), in.nextInt(), in.nextInt()));
        System.out.println("Введите количество соревнований: ");
        int n = in.nextInt();
        ArrayList<Sportsman.Event> events = new ArrayList<>();
        for (int i = 0; i < n; i++)
        {
            System.out.println("Введите место(без пробелов) и год проведения соревнования: ");
            events.add(new Sportsman.Event());
            events.get(i).setPlace(in.next());
            events.get(i).setYear(in.nextInt());
            System.out.println("Введите результат и медаль: ");
            events.get(i).setResult(in.nextInt());
            events.get(i).setAward(in.next());
        }
        newSportsmen.setEvents(events);
        sportsmen.add(newSportsmen);
        newTree(sportsmen);
    }

    public static void newTree(ArrayList<Sportsman> sportsmen)
    {
        try
        {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.newDocument();
            Node root = document.appendChild(document.createElement("team"));
            root.appendChild(document.createTextNode("\n"));
            for (Sportsman human : sportsmen)
            {
                Element pers = document.createElement("sportsman");
                pers.setAttribute("name", human.getName());
                pers.setAttribute("numberOfEvents", Integer.toString(human.getEvents().size()));
                int result = 0;
                for (Sportsman.Event event : human.getEvents())
                {
                    result += event.getResult();
                }
                pers.setAttribute("results", Integer.toString(result));
                root.appendChild(pers);
                root.appendChild(document.createTextNode("\n"));
            }
            writeDocument(document);
        } catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
    }

    private static void writeDocument(Document document) throws TransformerFactoryConfigurationError
    {
        try
        {
            Transformer tr = TransformerFactory.newInstance().newTransformer();

            DOMSource source = new DOMSource(document);
            FileOutputStream fos = new FileOutputStream("New.xml");
            StreamResult result = new StreamResult(fos);
            tr.transform(source, result);
        } catch (TransformerException | IOException e)
        {
            e.printStackTrace(System.out);
        }
    }

    public static void readAndPrintFile1()
    {
        try
        {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse("File1.xml");
            Node root = document.getDocumentElement();
            System.out.println("List of autors:\n===========>>>>");
            NodeList autors = root.getChildNodes();
            for (int i = 0; i < autors.getLength(); i++)
            {
                if (autors.item(i).getNodeType() != Node.TEXT_NODE)
                {
                    System.out.println(autors.item(i).getNodeName() + ":\n" + autors.item(i).getAttributes().item(0).getTextContent());
                    NodeList books = autors.item(i).getChildNodes();
                    for (int j = 0; j < books.getLength(); j++)
                    {
                        if (books.item(j).getNodeType() != Node.TEXT_NODE)
                            System.out.println(books.item(j).getNodeName() + ":\nНазвание книги: " + books.item(j).getAttributes().item(0).getTextContent() + "\nКоличество страниц: " + books.item(j).getAttributes().item(1).getTextContent());
                    }
                    System.out.println("===========>>>>");
                }
            }

        } catch (ParserConfigurationException | IOException | SAXException e)
        {
            e.printStackTrace();
        }
    }
}