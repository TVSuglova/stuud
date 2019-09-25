package com.company;

import static java.lang.Math.*;

public class Bisection
{
    public static void main(String[] args)
    {
        Functions[] equations = new Functions[]{                                               // Пункт 4
                x -> x * sin(x) - 0.5,
                x -> log10(pow(x, 2) - 3 * x + 2),
                x -> 0.5 * tan((2.0 / 3) * (x + PI / 4)) - 1};
        double e = 0.0001;
        System.out.println("Корень уравнения а: " + bisec(equations[0], 0, PI, e));
        System.out.println("Корень уравнения б: " + bisec(equations[1], 0, 0.9, e));
        System.out.println("Корень уравнения в: " + bisec(equations[1], 2.1, 5, e));
        System.out.println("Корень уравнения г: " + bisec(equations[2], PI, 2 * PI, e));
    }

    public static double bisec(Functions fun, double a, double b, double e)
    {
        double h, mid = 0;
        if (fun.arg(a) == 0)
            return a;
        else if (fun.arg(b) == 0)
            return b;
        while (b - a > e)
        {
            h = (b - a) / 2;
            mid = a + h;
            if (signum(fun.arg(a)) == signum(fun.arg(mid)))
                a = mid;
            else b = mid;
        }
        return mid;
    }
}
