package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main
{

    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите строку: ");
        String string = in.nextLine();
        System.out.println("Пункт a:\n" + string.matches("abcd1{7}02019"));
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(string);
        int number = 0;
        {
            ArrayList<Long> nums1 = new ArrayList<>();
            long sum = 0, max = 0;
            while (matcher.find())
                nums1.add(Long.parseLong(string.substring(matcher.start(), matcher.end())));
            for (Long n : nums1)
            {
                sum += n;
                if (n > max)
                {
                    max = n;
                    number = nums1.indexOf(n);
                }
            }
            System.out.println("Пункт б:\n" + nums1);
            System.out.println("sum = " + sum);
            System.out.println("max = " + max + " number = " + number);
        }
        {
            ArrayList<Double> nums2 = new ArrayList<>();
            pattern = Pattern.compile("\\d+[.,]\\d+");
            matcher = pattern.matcher(string);
            while (matcher.find())
                nums2.add(Double.parseDouble(string.substring(matcher.start(), matcher.end()).replace(',', '.')));
            System.out.println("Пункт в:\n" + nums2);
        }
        //В пункте г заменяются только английские и русские слова от 10 букв.
        System.out.println("Пункт г:\n" + string.replaceAll("([a-zA-Z]|[а-яА-я]){10,}", "*"));
        {
            StringBuilder sb = new StringBuilder();
            pattern = Pattern.compile("([a-zA-Z]|[а-яА-я]){10,}");
            matcher = pattern.matcher(string);
            while (matcher.find())
            {
                number = matcher.start();
                matcher.appendReplacement(sb, Character.toString(string.charAt(number)));
            }
            matcher.appendTail(sb);
            System.out.println(sb.length() == 0 ? string : sb);
            sb = new StringBuilder();
            matcher.reset();
            while (matcher.find())
            {
                number = matcher.start();
                matcher.appendReplacement(sb, "");
                for (int i = number; i != matcher.end(); i++)
                    sb.append(string.charAt(number));
            }
            matcher.appendTail(sb);
            System.out.println(sb.length() == 0 ? string : sb);
        }
        System.out.println("Пункт д:\n" + string.matches("[a-fA-F\\d]{8}-(([a-zA-Z]|[\\d]){4}-){3}[a-zA-Z\\d]{12}"));
        System.out.println("Пункт е:\n" + string.matches("#[A-F\\d]{8}"));
        System.out.println("Пункт ж:\n" + Arrays.toString(string.split(":*\\\\")));
        System.out.println("Пункт з:\n" + string.replaceAll("ик", ""));
        {
            System.out.println("Пункт и:\n");
            ArrayList<Double> USD = new ArrayList<>(), RUR = new ArrayList<>(), EU = new ArrayList<>();
            pattern = Pattern.compile("\\d+(\\.\\d{1,2})?\\s?(USD|RUR|EU)");
            //В выражении ценой считается цифра с не более чем двумя знаками после точки без или с пробелом перед названием валюты (Например 4RUR, 5.6 USD, 23.99EU).
            matcher = pattern.matcher(string);
            System.out.print("Введите целевой курс: ");
            String currency = in.nextLine();
            double curU = 1, curR = 1, curE = 1;
            switch (currency)
            {
                case "USD":
                    System.out.println("\nВведите курсы обмена RUR и EU к USD: ");
                    curR = in.nextInt();
                    curE = in.nextInt();
                    break;
                case "RUR":
                    System.out.println("\nВведите курсы обмена USD и EU к RUR: ");
                    curU = in.nextInt();
                    curE = in.nextInt();
                    break;
                case "EU":
                    System.out.println("\nВведите курсы обмена USD и RUR к EU: ");
                    curU = in.nextInt();
                    curR = in.nextInt();
                    break;
            }
            StringBuilder sb = new StringBuilder();
            while (matcher.find())
            {
                number = matcher.end();
                if (string.charAt(number - 1) == 'D')
                {
                    USD.add(Double.parseDouble(string.substring(matcher.start(), number).replaceAll("\\s?(USD|RUR|EU)", "")));
                    matcher.appendReplacement(sb, USD.get(USD.size() - 1) * curU + currency);
                } else if (string.charAt(number - 1) == 'R')
                {
                    RUR.add(Double.parseDouble(string.substring(matcher.start(), number).replaceAll("\\s?(USD|RUR|EU)", "")));
                    matcher.appendReplacement(sb, RUR.get(RUR.size() - 1) * curR + currency);
                } else if (string.charAt(number - 1) == 'U')
                {
                    EU.add(Double.parseDouble(string.substring(matcher.start(), number).replaceAll("\\s?(USD|RUR|EU)", "")));
                    matcher.appendReplacement(sb, EU.get(EU.size() - 1) * curE + currency);
                }
            }
            matcher.appendTail(sb);
            System.out.println(sb.length() == 0 ? string : sb);
        }
    }
}

