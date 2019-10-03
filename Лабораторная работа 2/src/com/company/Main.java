package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main
{

    public static void main(String[] args)
    {   //Пункт 1
        {
            long n = 123456789;
            Stack<Short> stack = recS(n);
            while (!stack.isEmpty())
            {
                System.out.print(stack.pop());
            }
            System.out.println();
            stack = revRecS(n);
            while (!stack.isEmpty())
            {
                System.out.print(stack.pop());
            }
            System.out.println();
            ArrayDeque<Short> qeque = recQ(n);
            while (!qeque.isEmpty())
            {
                System.out.print(qeque.remove());
            }
            System.out.println();
            qeque = revRecQ(n);
            while (!qeque.isEmpty())
            {
                System.out.print(qeque.remove());
            }
            System.out.println("\n");
        }
        //Пункт 2
        {
            Stack<Info> stack = new Stack<>();
            ArrayDeque<Info> qeque = new ArrayDeque<>();
            String buf1;
            Info buf2;
            try (BufferedReader bf = new BufferedReader(new FileReader("File.txt")))
            {
                while ((buf1 = bf.readLine()) != null)
                {
                    stack.add(new Info(buf1));
                    qeque.add(new Info(buf1));
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            System.out.println("    Люди младше 40: ");
            while (!stack.isEmpty())
            {
                buf2 = stack.pop();
                if (buf2.getAge() < 40)
                    System.out.println(buf2);
            }
            System.out.println("    Остальные: ");
            while (!qeque.isEmpty())
            {
                buf2 = qeque.remove();
                if (buf2.getAge() > 40)
                    System.out.println(buf2);
            }
            System.out.println("\n");
        }
        //Пункт 3
        if (balance("{x*(-1)-[4*x+5-(2+y)+90]}"))
            System.out.println("Баланс скобок не нарушен");
        else System.out.println("Баланс скобок нарушен");
        if (balance("{x*(-1]-[4*x+5-(2+y)+90]}"))
            System.out.println("Баланс скобок не нарушен");
        else System.out.println("Баланс скобок нарушен");
        //Пункт 5
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader bf = new BufferedReader(new FileReader("Текст.txt")))
        {
            String buf;
            while ((buf = bf.readLine()) != null)
                list.add(buf);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        list.sort(Comparator.comparingInt(String::length));
        System.out.println("\n    По длине строки:");
        for (String string : list)
            System.out.println(string);
        list.sort(String::compareTo);
        System.out.println("\n    В лексикографическом порядке:");
        for (String string : list)
            System.out.println(string);
        list.sort(Comparator.comparingInt(x ->
        {
            int index = 0;
            for (int i = 0; i < x.length(); i++)
                if (x.charAt(i) >= 'A' && x.charAt(i) <= 'Z')
                    index++;
            return -index;
        }));
        System.out.println("\n    По количеству заглавных букв в строке:");
        for (String string : list)
            System.out.println(string);
        System.out.println("\n");
        //Пункт 6
        ArrayList<Point2D> points = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            points.add(new Point2D(new double[]{i, Math.random() * 10 + i}));
        BrokenLine brokenLine = new BrokenLine(points);
        System.out.println(brokenLine);
    }

    static boolean balance(String s)
    {
        Stack<Character> stack = new Stack<>();
        char sym;
        for (int i = 0; i < s.length(); i++)
        {
            sym = s.charAt(i);
            if (sym == '{' || sym == '(' || sym == '[')
                stack.add(sym);
            else if (!stack.isEmpty() && (sym == '}' && stack.peek() == '{' || sym == ')' && stack.peek() == '(' || sym == ']' && stack.peek() == '['))
                stack.pop();
            else if (sym == '}' || sym == ')' || sym == ']')
                return false;
        }
        return true;
    }

    public static int count(long n)
    {
        return (n == 0) ? 1 : (int) Math.ceil(Math.log10(n + 0.5));
    }

    public static Stack<Short> recS(long n)
    {
        Stack<Short> num = new Stack<>();
        while (n > 0)
        {
            num.push((short) (n % 10));
            n /= 10;
        }
        return num;
    }

    public static Stack<Short> revRecS(long n)
    {
        Stack<Short> num = new Stack<>();
        long determ = (long) Math.pow(10, count(n) - 1);
        while (determ > 0)
        {
            num.push((short) (n / determ % 10));
            determ /= 10;
        }
        return num;
    }

    public static ArrayDeque<Short> recQ(long n)
    {
        ArrayDeque<Short> num = new ArrayDeque<>();
        while (n > 0)
        {
            num.add((short) (n % 10));
            n /= 10;
        }
        return num;
    }

    public static ArrayDeque<Short> revRecQ(long n)
    {
        ArrayDeque<Short> num = new ArrayDeque<>();
        long determ = (long) Math.pow(10, count(n) - 1);
        while (determ > 0)
        {
            num.add((short) (n / determ % 10));
            determ /= 10;
        }
        return num;
    }
}
