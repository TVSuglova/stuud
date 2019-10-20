import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SAXparserMain
{
    private static ArrayList<Sportsman> sportsmen = new ArrayList<>();

    public static void main(String[] args)
    {
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            HandlerXML handler = new HandlerXML();
            parser.parse("File2.xml", handler);
            for (Sportsman sportsman : sportsmen)
                System.out.println(sportsman);
            FileWriter writer = new FileWriter("New2.json", false);
            writer.write("{");
            for (Sportsman sportsman : sportsmen)
            {
                writer.write(sportsman.toJSON());
                if (sportsman != sportsmen.get(sportsmen.size() - 1))
                    writer.write("\n    },");
                else writer.write("\n   }");
            }
            writer.write("\n}");
            writer.flush();
        } catch (ParserConfigurationException | SAXException | IOException e)
        {
            e.printStackTrace();
        }
    }

    private static class HandlerXML extends DefaultHandler
    {
        private String lastElementName;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
        {
            if (qName.equals("sportsman") && attributes != null)
                sportsmen.add(new Sportsman(attributes.getValue(0), attributes.getValue(2), attributes.getValue(1), new ArrayList<>()));
            else if (qName.equals("event"))
                sportsmen.get(sportsmen.size() - 1).events.add(new Sportsman.Event(attributes.getValue(0), attributes.getValue(1)));
            lastElementName = qName;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException
        {
            String information = new String(ch, start, length);
            information = information.replace("\n", "").trim();
            int n;
            if (!information.isEmpty())
            {
                n = sportsmen.get(sportsmen.size() - 1).events.size() - 1;
                if (lastElementName.equalsIgnoreCase("result"))
                    sportsmen.get(sportsmen.size() - 1).events.get(n).setResult(Integer.parseInt(information));
                else if (lastElementName.equalsIgnoreCase("award"))
                    sportsmen.get(sportsmen.size() - 1).events.get(n).setAward(information);
            }
        }
    }
}
