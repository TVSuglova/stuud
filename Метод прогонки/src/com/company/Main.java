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
                x -> (Math.pow(Math.E, (-x / Math.sqrt(0.05))) - Math.pow(Math.E, ((x - 2) / Math.sqrt(0.05)))) / (1 - Math.pow(Math.E, (-2 / Math.sqrt(0.05))))};
        System.out.print("Отрезок: ");
        double start = in.nextDouble();
        double finish = in.nextDouble();
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
        if (start == finish || n == 0)
            System.out.println("Некорректный ввод данных.");
        double buf = start;
        double[] a = new double[n - 1];
        double[] c = new double[n - 1];
        double[] b = new double[n - 1];
        double[] f = new double[n - 1];
        double h = ((finish - start) / n);
        for (int i = 0; i < n - 1; i++)
        {
            buf += h;
            a[i] = -1;
            c[i] = -1;
            b[i] = 2 + h * h * g.arg(buf);
            f[i] = h * h * s.arg(buf);
        }
        f[0] += ua;
        f[n - 2] += ub;
        return progonka(a, b, c, f);
    }

    /*Подсчет максимальной ошибки.
     *На вход подаются концы отрезка, вектор получившихся значений y и функция, которая должна получиться в итоге.*/
    public static double error(double start, double finish, double[] y, Functions u)
    {
        if (start == finish || y.length == 0)
            System.out.println("Некорректный ввод данных.");
        double max = 0;
        double h = (finish - start) / (y.length + 1);
        double buf = start;
        for (int i = 0; i < y.length; i++)
        {
            buf += h;
            if (Math.abs(u.arg(buf) - y[i]) > max)
                max = Math.abs(u.arg(buf) - y[i]);
        }
        return max;
    }

    /*Метод прогонки для решения систем уравнений с трехдиагональными матрицами.*/
    public static double[] progonka(double[] a, double[] b, double[] c, double[] f)
    {
        int n = f.length;
        try
        {
            if (f.length != a.length || f.length != b.length || f.length != c.length)
                throw new Exception("Некорректный ввод данных.");
            for (int i = 0; i < n; i++)
            {
                if (Math.abs(a[i]) + Math.abs(c[i]) > Math.abs(b[i]))
                    throw new Exception("Матрица без диагонального преобладания.");
            }
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        double[] x = new double[n];
        double[] A = new double[n], B = new double[n];
        for (int i = 0; i < n - 1; i++)
        {
            A[i + 1] = -c[i] / (b[i] + a[i] * A[i]);
            B[i + 1] = (f[i] - a[i] * B[i]) / (b[i] + a[i] * A[i]);
        }
        x[n - 1] = (f[n - 1] - a[n - 1] * B[n - 1]) / (b[n - 1] + a[n - 1] * A[n - 1]);
        for (int i = n - 2; i >= 0; i--)
            x[i] = A[i + 1] * x[i + 1] + B[i + 1];
        return x;
    }
}

/*Интерфейс для создания массива функций.*/
interface Functions
{
    double arg(double x);
}
