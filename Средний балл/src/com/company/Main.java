package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class Main
{
    public static void main(String[] args)
    {
        String filename = "Баллы.csv", separator = ";";
        Vector data = ReadCSV(filename, separator);
        for (int i = 0; i < data.size(); i += 2)
            System.out.println(data.elementAt(i) + " " + data.elementAt(i + 1));
    }

    public static Vector ReadCSV(String filename, String separator)
    {
        File file = new File(filename);
        Vector data = new Vector<>();

        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(new FileInputStream(file))))
        {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null)
            {
                String[] elements = line.split(separator);
                data.add(elements[0]);
                data.add(Integer.parseInt(elements[4]));
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return data;
    }
}

