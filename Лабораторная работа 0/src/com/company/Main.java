package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        ArrayList<String> data = new ArrayList<>();
        try (Scanner in = new Scanner(new File("Текст.txt")))
        {
            while (in.hasNext())
                data.add(in.nextLine());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        print(data);
    }
    public static void print(ArrayList<String> data)
    {
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-3s|%-57s|%-11s|%-56s%n", "№", "                           Слово", " Палиндром", "                 Количество информации");
        System.out.println("    |                                                         |           |----------------------------------------------------------");
        System.out.printf("%-4s|%-57s|%-11s|%-11s|%14s|%14s|%14s%n", " ", " ", " ", "  Кол-во", "Байт, размер ", "Байт,    ", "Байт,    ");
        System.out.printf("%-4s|%-57s|%-11s|%-11s|%14s|%14s|%14s%n", " ", " ", " ", " символов", "в программе  ", "по Хартли  ", "по Шенону  ");
        for(int i = 0; i < data.size(); i++)
        {
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-4d|%-57s|%-11s|%-11d|%14d|%14.2f|%14.9f%n", i+1, data.get(i), pol(data.get(i)), data.get(i).length(), 2*data.get(i).length(), Hartly(data.get(i)), Shenon(data.get(i)));
        }
    }

    public static char pol(String string)
    {
        string = string.toLowerCase();
        for (int i = 0; i < string.length() - i; i++)
        {
            if (string.charAt(i) != string.charAt(string.length() - i - 1))
                return '-';
        }
        return '+';
    }
    public static double Hartly(String string)
    {
        String unique = string.replaceAll("(.)(?=.*?\\1)", "");
        return string.length()*Math.log(unique.length())/Math.log(2);
    }
    public static double Shenon(String string)
    {
        String unique = string.replaceAll("(.)(?=.*?\\1)", "");
        double result = 0;
        char[] str = string.toCharArray();
        float[] index = new float[unique.length()];
        for (int i = 0; i < index.length; i++)
            index[i] = 1;
        int j = 0;
        Arrays.sort(str);
        for(int i = 1; i < str.length; i++)
        {
            if (str[i-1] == str[i])
            {
                index[j]++;
            }
            else
            {
                result += (index[j]/str.length)*Math.log(index[j]/str.length)/Math.log(2);
                j++;
            }
        }
        result += (index[j]/str.length)*Math.log(index[j]/str.length)/Math.log(2);
        result = -result;
        return result;
    }
}
