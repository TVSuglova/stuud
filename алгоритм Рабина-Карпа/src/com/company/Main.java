package com.company;

public class Main
{

    public static void main(String[] args)
    {
        String a = "Программирование";
        String b = "мир";
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
        long q = 2 ^ 64, x = 301, hashs = s[0] % q, hasht = t[0] % q;
        int n = s.length, m = t.length;
        long pow = 1;
        x %= q;
        for (int i = 1; i < m; i++)
        {
            pow *= x;
            hasht += t[i] % q * pow;
            hashs += s[i] % q * pow;
        }
        for (int i = 0; i < n - m; i++)
        {
            if (hashs == hasht)
                return i;
            hashs -= s[i] % q;
            hashs /= x;
            hashs += s[i + m] % q * pow;
        }
        if (hashs == hasht)
            return n - m;
        return -1;
    }
}
