package com.company;

import java.util.Arrays;

public class Check
{
    /* Ввод данных : функции, рассматриваемый отрезок, n(количество отрезков сетки), М2(значение функции в конце отрезка). Вывод ответа на консоль.*/
    public static void main(String[] args)
    {
        double K = 4.5, M2 = 0, finish = 1;
        Functions answer = r -> 1 - Math.pow(r, K);
        Functions[] fun = new Functions[]{
                r -> 1,  //k
                r -> 0,  //q
                r -> K*K*Math.pow(r, K-2)}; //f
        int n = 10;
        double[] result = krshema(finish, n, M2, fun[0], fun[1], fun[2]);
        System.out.println(Arrays.toString(result));
        System.out.println(error(finish, result, answer));
    }

    /* Метод, реализующий разностную схему.
     * Входные данные: точки начала и конца отрезка, количество отрезков сетки, значение функции в конце отрезка, известные функции.
     * Выходные данные: вектор приближенных решений.*/
    private static double[] krshema(double finish, int n, double M2, Functions k, Functions q, Functions f)
    {
        double h = finish / n, buf = 0;
        double[] C = new double[n - 1], A = new double[n], F = new double[n - 1];
        A[0] = (buf + 0.5*h)*k.arg(buf + 0.5*h)/h/h;
        for (int i = 0; i < n - 1; i++)
        {
            buf += h;
            A[i + 1] = (buf + 0.5*h)*k.arg(buf + 0.5*h)/h/h;
            C[i] = -(A[i] + A[i + 1] + q.arg(buf)*buf);
            F[i] = -f.arg(buf)*buf;
        }
        C[0] += A[0]*k.arg(0.5*h)/(k.arg(0.5*h) + h*h*q.arg(0)/4);
        F[0] -= A[0]*(h*h*f.arg(0)/4)/(k.arg(0.5*h) + h*h*q.arg(0)/4);
        F[n - 2] -= A[n - 2]*M2;
        return progonka(A, C, F);
    }

    /*Метод прогонки для решения СЛАУ с трехдиагональными матрицами с диагональным преобладанием.*/
    private static double[] progonka(double[] a, double[] b, double[] f) {
        int n = f.length;
        double[] x = new double[n];
        double[] A = new double[n], B = new double[n];
        for (int i = 0; i < n - 1; i++) {
            A[i + 1] = -a[i+1] / (b[i] + a[i] * A[i]);
            B[i + 1] = (f[i] - a[i] * B[i]) / (b[i] + a[i] * A[i]);
        }
        x[n - 1] = (f[n - 1] - a[n - 1] * B[n - 1]) / (b[n - 1] + a[n - 1] * A[n - 1]);
        for (int i = n - 2; i >= 0; i--)
            x[i] = A[i + 1] * x[i + 1] + B[i + 1];
        return x;
    }
    /*Метод, вычисляющий максимальную ошибку полученного приближенного решения.*/
    private static double error(double finish, double[] y, Functions u)
    {
        double max = 0, buf = 0;
        double h = finish / (y.length + 1);
        for (double aY : y)
        {
            buf += h;
            if (Math.abs(u.arg(buf) - aY) > max)
                max = Math.abs(u.arg(buf) - aY);
        }
        return max;
    }
}

/*Интерфейс для создания массива функций*/