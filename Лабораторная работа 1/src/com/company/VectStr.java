package com.company;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;

public class VectStr
{
    public static void main(String[] args)                                                      // Пункт 6
    {
        Predicate<String>[] predics = new Predicate[3];
        predics[0] = x -> x.length() == 5;
        predics[1] = x -> x.charAt(0) == 'W';                     // Считаются слова, начинающиеся только на прописную 'W'
        predics[2] = x ->
        {
            x = x.toLowerCase();                   // Поиск полиндромов осуществляется вне зависимости от регистра букв в нем
            for (int i = 0; i < x.length() - i; i++)
            {
                if (x.charAt(i) != x.charAt(x.length() - i - 1))
                    return false;
            }
            return true;
        };
        ArrayList<String> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Текст.txt")))
        {
            String line;
            while ((line = br.readLine()) != null)
                data.add(line);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("Количество пятибуквенных слов = " + quantity(data, predics[0]));
        System.out.println("Количество палиндромов = " + quantity(data, predics[2]));
        System.out.println("Количество слов на букву W = " + quantity(data, predics[1]));
        System.out.println(list(data, predics[1]));
    }

    public static int quantity(ArrayList<String> data, Predicate<String> predic)
    {
        int n = 0;
        for (String sentance : data)
        {

            String[] words = sentance.split("\\W+");        // Разделение строки на слова осуществляется по не букво-цифренным символам и работает только для английского языка
            for (String word : words)
            {
                if (predic.test(word))
                {
                    n++;
                }
            }
        }
        return n;
    }

    public static ArrayList<String> list(ArrayList<String> data, Predicate<String> predic)
    {
        ArrayList<String> checked = new ArrayList<>();
        for (String sentance : data)
        {
            String[] words = sentance.split("\\W+");
            for (String word : words)
            {
                if (predic.test(word))
                {
                    checked.add(word);
                }
            }
        }
        return checked;
    }
}