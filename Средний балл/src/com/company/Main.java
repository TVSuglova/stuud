package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class Main
{

    public static void main(String[] args)
    {

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("Баллы.csv"));
            String line = reader.readLine();
            Scanner scanner;
            int index = 0;
            Vector<String> names = new Vector<>();
            Vector<Integer> points = new Vector<>();

            while ((line = reader.readLine()) != null)
            {
                scanner = new Scanner(line);
                scanner.useDelimiter(";");
                while (scanner.hasNext())
                {
                    String data = scanner.next();
                    if (index == 0)
                        names.add(data);
                    else if (index == 4)
                    {
                        points.add(Integer.parseInt(data));
                    }
                    index++;
                }
                index = 0;

            }
            reader.close();
            for (int i = 0; i < names.size(); i++)
                System.out.println(names.elementAt(i) + " " + points.elementAt(i));

        } catch (FileNotFoundException ex)
        {
            ex.getMessage();
        } catch (IOException ex)
        {
            ex.getMessage();
        }
    }
}