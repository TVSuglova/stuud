package com.company;

import java.util.Arrays;
import java.util.concurrent.*;

public class Main
{
    /* Ввод данных : функции, рассматривемый отрезок, n(количество отрезков сетки), М2(значение функции в конце отрезка).*/
    public static void main(String[] args)
    {
        double K = 4.5, M2 = 0, finish = 1;
        Functions answer = r -> 1 - Math.pow(r, K);
        Functions[] fun = new Functions[]{
                r -> 1,  //k
                r -> 0,  //q
                r -> K*K*Math.pow(r, K-2)}; //f
        int n = 8;
        double[] result = progonka(finish, n, M2, fun[0], fun[1], fun[2]);
        System.out.println(Arrays.toString(result));
        System.out.println(error(finish, result, answer));
        /*ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Future<Double>[] futures = new Future[1];
        for (int i = 99; i < n; i++)
        {
            futures[0] = executor.submit(new KRS(start, finish, i+1, M2, fun, ans));
        }
        try
        {
            for (int i = 0; i < 1; i++)
                System.out.println(futures[0].get());
        } catch (InterruptedException | ExecutionException ex)
        {
            ex.printStackTrace(System.err);
        }
        executor.shutdown();*/
    }
    /* Метод, реализующий разностную схему.
    * Входные данные: точки начала и конца отрезка, количество отрезков сетки, значение функции в конце отрезка, известные функции.
    * Выходные данные: вектор приближенных решений.
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
    }*/
    /*Метод прогонки для решения СЛАУ с трехдиагональными матрицами с диагональным преобладанием.*/
    private static double[] progonka(double finish, int n, double M2, Functions k, Functions q, Functions f) {
        double h = finish / n, buf = 0, denom;
        double[] x = new double[n + 1];
        double[] alpha = new double[n], beta = new double[n];
        alpha[0] = k.arg(0.5*h) / (k.arg(0.5*h) + h*h*q.arg(0)/4);
        beta[0] = h*h*f.arg(0) / 4*(k.arg(0.5*h) + h*h*q.arg(0)/4);
        for (int i = 0; i < n - 1; i++)
        {
            buf += h;
            denom = (buf - 0.5*h)*k.arg(buf - 0.5*h)*(1 - alpha[i]) + (buf + 0.5*h)*k.arg(buf + 0.5*h) + h*h*buf*q.arg(buf);
            alpha[i + 1] = (buf + 0.5*h)*k.arg(buf + 0.5*h) / denom;
            beta[i + 1] = (beta[i]*(buf - 0.5*h)*k.arg(buf - 0.5*h) + f.arg(buf)*buf*h*h) / denom;
        }
        x[n] = M2;
        for (int i = n - 1; i > -1; i--)
            x[i] = alpha[i] * x[i + 1] + beta[i];
        return x;
    }
    /*Метод, вычисляющий максимальную ошибку полученного приближенного решения.*/
    private static double error(double finish, double[] y, Functions u)
    {
        double max = 0, buf = 0;
        double h = finish / (y.length - 1);
        for (double aY : y)
        {
            if (Math.abs(u.arg(buf) - aY) > max)
                max = Math.abs(u.arg(buf) - aY);
            buf += h;
        }
        return max;
    }
}
/*Интерфейс для создания массива функций*/
interface Functions
{
    double arg(double r);
}

class KRS implements Callable<Double>
{
    private Functions[] fun;
    private Functions ans;
    private double start, finish, M2;
    private int n;
    public KRS (double start, double finish, int n, double M2, Functions[] fun, Functions ans)
    {
        this.start = start;
        this.finish = finish;
        this.n = n;
        this.M2 = M2;
        this.fun = fun;
        this.ans = ans;
    }

    public Double call()
    {
        return error(start, finish, krshema(start, finish, n, M2, fun[0], fun[1], fun[2]), ans);
    }

    private static double[] krshema(double start, double finish, int n, double M2, Functions k, Functions q, Functions f)
    {
        double h = ((finish - start) / n), buf = start;
        double[] C = new double[n - 1], A = new double[n], F = new double[n - 1];
        A[0] = (buf + 0.5*h)*k.arg(buf + 0.5*h)/h/h;
        for (int i = 0; i < n - 1; i++)
        {
            buf += h;
            A[i + 1] = (buf + 0.5*h)*k.arg(buf + 0.5*h)/h/h;
            C[i] = -(A[i] + A[i + 1] + q.arg(buf)*buf);
            F[i] = -f.arg(buf)*buf;
        }
        C[0] += A[0]*k.arg(start + 0.5*h)/(k.arg(start + 0.5*h) + h*h*q.arg(start)/4);
        F[0] -= A[0]*(h*h*f.arg(start)/4)/(k.arg(start + 0.5*h) + h*h*q.arg(start)/4);
        F[n - 2] -= A[n - 2]*M2;
        return progonka(A, C, F);
    }

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

    private static double error(double start, double finish, double[] y, Functions u)
    {
        double max = 0, buf = start;
        double h = (finish - start) / (y.length + 1);
        for (double aY : y)
        {
            buf += h;
            if (Math.abs(u.arg(buf) - aY) > max)
                max = Math.abs(u.arg(buf) - aY);
        }
        return max;
    }
}
