package com.company;

import java.util.Arrays;
import java.util.Vector;

public class Main
{

    public static void main(String[] args)
    {
        String a = "aaaaaaa";
        String b = "aaa";
        System.out.println(Arrays.toString(KnMoPr(a, b).toArray()));
    }

    private static Vector<Integer> KnMoPr(String text, String str)
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

    public static Vector<Integer> prefix(String str)
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