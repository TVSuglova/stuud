package com.company;

public class Main
{

    public static void main(String[] args)
    {
        String a = "Карл у Клары украл кораллы, Клара у Карла украла кларнет.";
        String b = "нет.";
        char[] text = a.toCharArray();
        int r = RabinKarp(text, b.toCharArray());
        if (r == -1)
            System.out.println(r);
        else
        {
            System.out.println(r);
            for (int i = 0; i < a.length(); i++)
                if (i < r || i > r + b.length() - 1)
                    System.out.print(Character.toLowerCase(text[i]));
                else
                    System.out.print(Character.toUpperCase(text[i]));
        }
    }

    static int RabinKarp(char[] s, char[] t)
    {
        int n = s.length, m = t.length;
        long q = (long) Math.pow(2, 61) - 1, x = 2, hashs = s[m - 1], hasht = t[m - 1];
        long pow = 1;
        for (int i = m - 2; i > -1; i--)
        {
            pow *= x;
            pow %= q;
            hasht += t[i] * pow;
            hashs += s[i] * pow;
        }
        hasht %= q;
        hashs %= q;
        for (int i = 0; i < n - m; i++)
        {
            if (hashs == hasht)
                return i;
            hashs = ((hashs - s[i] * pow % q) * x + s[i + m]) % q;
        }
        if (hashs == hasht)
            return n - m;
        return -1;
    }
}
