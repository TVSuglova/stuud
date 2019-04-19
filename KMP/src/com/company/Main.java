package com.company;

import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.*;

public class Main
{

    public static void main(String[] args)
    {
        String a = "aaaaaaa";
        String[] b = {"aaa", "fff", "a"};
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Future[] futures = new Future[b.length];
        for (int i = 0; i < b.length; i++)
            futures[i] = executor.submit(new KMP(a, b[i]));
        try
        {
            for (int i = 0; i < b.length; i++)
                System.out.println(futures[i].get());
        } catch (InterruptedException | ExecutionException ex)
        {
            ex.printStackTrace(System.err);
        }
        executor.shutdown();
    }
}

class KMP implements Callable<Vector<Integer>>
{
    String text, str;

    public KMP(String text, String str)
    {
        this.text = text;
        this.str = str;
    }

    public Vector<Integer> call()
    {
        return KnMoPr(text, str);
    }

    public static Vector<Integer> KnMoPr(String text, String str)
    {
        Vector<Integer> index = new Vector<>();
        Vector<Integer> prefix = prefix(str);
        int j = 0;
        for (int i = 0; i < text.length(); i++)
        {
            if (text.charAt(i) == str.charAt(j))
            {
                j++;
                if (j == str.length())
                {
                    index.add(i - j + 1);
                    j = prefix.elementAt(j - 1);
                }
            } else
                j = prefix.elementAt(j);
        }
        return index;
    }

    private static Vector<Integer> prefix(String str)
    {
        int len = str.length();
        Vector<Integer> p = new Vector<>();
        for (int i = 0; i < len; ++i)
        {
            p.add(0);
        }
        int j = 0;
        for (int i = 1; i < len; ++i)
        {
            while ((j > 0) && (str.charAt(j) != str.charAt(i)))
                j = p.get(j - 1);
            if (str.charAt(j) == str.charAt(i))
                ++j;
            p.set(i, j);
        }
        return p;
    }
}