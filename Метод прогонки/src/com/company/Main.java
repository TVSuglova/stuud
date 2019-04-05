package com.company;

import java.util.*;

public class Main
{
    /*Ввод данных уравнения: функции, рассматривемый отрезок, количество точек сетки.*/
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        Functions[] fun = new Functions[]{
                x -> 0,
                x -> 1 / 0.05,
                x -> (Math.pow(Math.E, -x / Math.sqrt(0.05)) - Math.pow(Math.E, (x - 2) / Math.sqrt(0.05))) / (1 - Math.pow(Math.E, -2 / Math.sqrt(0.05)))};
        double start = 0, finish = 1;
        System.out.println("Отрезок: " + start + " " + finish);
        System.out.print("Количество точек сетки: ");
        int n = in.nextInt();
        System.out.println(Arrays.toString(krshema(start, finish, n, fun[2].arg(start), fun[2].arg(finish), fun[0], fun[1])));
        System.out.println("Ошибка: " + error(start, finish, krshema(start, finish, n, fun[2].arg(start), fun[2].arg(finish), fun[0], fun[1]), fun[2]));
    }

    /*Функция, реализующая конечно-разностную схему.
     * Входные данные: начало и конец рассматриваемого отрезка, количество точек равномерной сетки, значения искомой функции в начале и конце отрезка, известные функции данного уравнения.
     * Выходные данные: массив приблизительных значений искомой функции в точках заданной сетки.*/
    public static double[] krshema(double start, double finish, int n, double ua, double ub, Functions s, Functions g)
    {
        try
        {
            if (start == finish || n == 0)
                throw new Exception("Некорректный ввод данных.");
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        double buf = start;
        double h = ((finish - start) / n);
        double[] b = new double[n - 1], f = new double[n - 1];
        for (int i = 0; i < n - 1; i++)
        {
            buf += h;
            b[i] = 2 + h * h * g.arg(buf);
            f[i] = h * h * s.arg(buf);
        }
        f[0] += ua;
        f[n - 2] += ub;
        return progonka(b, f);
    }

    /*Подсчет максимальной ошибки.
     *На вход подаются концы отрезка, вектор получившихся значений y и функция, которая должна получиться в итоге.*/
    public static double error(double start, double finish, double[] y, Functions u)
    {
        try
        {
            if (start == finish || y.length == 0)
                throw new Exception("Некорректный ввод данных.");
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        double max = 0, buf = start;
        double h = (finish - start) / (y.length + 1);
        for (int i = 0; i < y.length; i++)
        {
            buf += h;
            if (Math.abs(u.arg(buf) - y[i]) > max)
                max = Math.abs(u.arg(buf) - y[i]);
        }
        return max;
    }

    /*Метод прогонки для решения систем уравнений с трехдиагональными матрицами.*/
    private static double[] progonka(double[] b, double[] f)
    {
        int n = f.length;
        double[] A = new double[n], B = new double[n];
        for (int i = 0; i < n - 1; i++)
        {
            A[i + 1] = 1 / (b[i] - A[i]);
            B[i + 1] = (f[i] + B[i]) / (b[i] - A[i]);
        }
        f[n - 1] = (f[n - 1] + B[n - 1]) / (b[n - 1] - A[n - 1]);
        for (int i = n - 2; i >= 0; i--)
            f[i] = A[i + 1] * f[i + 1] + B[i + 1];
        return f;
    }
}

/*Интерфейс для создания массива функций.*/
interface Functions
{
    double arg(double x);
}
