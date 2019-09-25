package com.company;

import java.util.Arrays;

import static java.lang.Math.*;

public class Main
{

    public static void main(String[] args)
    {
        int otric = 0, buf = 0;                                                    // Пункты 1, 2.a, 3
        double d = PI / 6, a = -2 * PI, b = 2 * PI;
        Functions[] fourfun = new Functions[]{
                x -> 2 * sin(x) + 1,
                x -> pow(((x / PI) - 1), 2),
                x -> -pow(x / PI, 2) - 2 * x + 5 * PI,
                x -> 0.5 * pow(cos(x), 2) + 1};
        double[] answers;
        for (int i = 0; i < fourfun.length; i++)
        {
            answers = tab(fourfun[i], d, a, b);
            for (double answer : answers)
            {
                if (answer < 0)
                    otric++;
            }
            System.out.println("Количество отрицательных значений функции №" + (i + 1) + " = " + (otric - buf));
            buf = otric;
        }
        System.out.println("\nКоличество отрицательных значений всех функций = " + otric);

        int n1 = 10;                                                               // Пункт 2.b
        double[] dot = new double[n1];
        for (int i = 0; i < n1; i++)
            dot[i] = random() * 20 - 10;
        System.out.println("\nМассив случайных точек: " + Arrays.toString(dot) + "\n");
        for (int i = 0; i < fourfun.length; i++)
        {
            answers = tab(fourfun[i], dot);
            System.out.println("Наибольшее и наименьшее значение функци №" + (i + 1) + " соответственно равны " + min(answers) + " и " + max(answers));
        }

        int n2 = 500;                                                              //Пункт 5
        System.out.println("\nИнтегал функци №" + 1 + " на отрезке от -PI до PI = " + integral(fourfun[0], -PI, PI, n2));
        System.out.println("Интегал функци №" + 3 + " на отрезке от -PI до PI = " + integral(fourfun[2], -PI, PI, n2));
        System.out.println("Интегал функци №" + 4 + " на отрезке от -PI до PI = " + integral(fourfun[3], -PI, PI, n2));
    }

    public static double[] tab(Functions fun, double d, double a, double b)
    {
        double[] results = new double[(int) ((b - a) / d)];
        for (int i = 0; i < results.length; i += 1)
        {
            results[i] = fun.arg(a);
            a += d;
        }
        return results;
    }

    public static double[] tab(Functions fun, double[] toch)
    {
        for (int i = 0; i < toch.length; i += 1)
            toch[i] = fun.arg(toch[i]);
        return toch;
    }

    public static double min(double[] data)
    {
        double min = data[0];
        for (int j = 1; j < data.length; j++)
        {
            if (data[j] < min)
                min = data[j];
        }
        return min;
    }

    public static double max(double[] data)
    {
        double max = data[0];
        for (int j = 1; j < data.length; j++)
        {
            if (data[j] > max)
                max = data[j];
        }
        return max;
    }

    public static double integral(Functions fun, double a, double b, int n)
    {
        double result = 0, h = (b - a) / n;
        for (int i = 0; i < n; i++)
        {
            result += fun.arg(a + h * i + 0.5);
        }
        result *= h;
        return result;
    }
}

interface Functions
{
    double arg(double x);
}
