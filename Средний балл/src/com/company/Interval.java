package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

// По образцу Interval2.
public class Interval
{

    private static Vector<String> names = new Vector<>();
    private static Vector<Integer> points = new Vector<>();
    public static String filename = "Баллы.csv";

    private static final String separator = ";";

    public static void main(String[] args)
    {
        ReadCSV(filename);
        for (int i = 0; i < names.size(); i++)
            System.out.println(names.elementAt(i) + " " + points.elementAt(i));
    }

    public static void ReadCSV(String filename)
    {
        File file = new File(filename);
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(new FileInputStream(file))))
        {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null)
            {
                String[] elements = line.split(separator);
                names.add(elements[0]);
                points.add(Integer.parseInt(elements[4]));
            }
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

