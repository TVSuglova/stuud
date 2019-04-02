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
        long q = 2 ^ 64, x = 301, hashs, hasht = t[0] % q;
        int n = s.length, m = t.length;
        long[] pow = new long[m + 1];
        pow[0] = 1;
        for (int i = 1; i < m; i++)
        {
            pow[i] = pow[i - 1] * x;
            hasht += (t[i] * pow[i]) % q;
        }
        for (int i = 0; i <= n - m; i++)
        {
            hashs = 0;
            for (int j = 0; j <= m - 1; j++)
            {
                hashs += (s[i + j] * pow[j]) % q;
            }
            if (hashs == hasht)
                return i;
        }
        return -1;
    }
}
