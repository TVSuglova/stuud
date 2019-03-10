package com.company;

import Jama.Matrix;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Решение уравнения типа\n 1. Y=b(0)+b(1)*ln(X(1)^2)+b(2)*ln(X(2))\n 2. Y=a+b*t");
        if (in.nextInt()==1)
        {
            Pre[] fun = new Pre[]{
                    x -> Math.log(x*x),
                    x -> Math.log(x)};
            System.out.println("Длина векторов X:");
            int m=in.nextInt();
            double[][] x=new double[m][2];
            for (int i=0; i<x[0].length; i++)
            {
                System.out.println("Введите вектор X"+(i+1)+":");
                for (int j=0; j<x.length; j++)
                    x[j][i]=in.nextDouble();
            }
            System.out.println("Введите вектор Y:");
            double[] y=new double[x.length];
            for (int i=0; i<y.length; i++)
                y[i]=in.nextDouble();
            System.out.println(Arrays.toString(test(prepare(x, fun).clone(),y.clone())));
            System.out.println(error(x, y, test(x.clone(),y.clone())));
        }
        else if (in.nextInt()==2)
        {
            System.out.println("Длина вектора Y:");
            double[] y=new double[in.nextInt()];
            Pre[] fun = new Pre[]{
                    x -> x};
            System.out.println("Введите вектор Y:");
            for (int i=0; i<y.length; i++)
                y[i]=in.nextDouble();
            double[][] x =new double[1][y.length];
            for (int i=0; i<y.length; i++)
                x[0][i]=i+1;
            System.out.println(Arrays.toString(test(prepare(x, fun).clone(),y.clone())));
            System.out.println(error(x, y, test(x.clone(),y.clone())));
        }
    }

    private static double[] methodGausa (double[][]x, double[]y)
    {
        for (int i=0; i<x[0].length-1; i++)
        {
            int max=i;
            for (int j=i+1; j<=x.length-1; j++)
            {
                if (Math.abs(x[j][i]) > Math.abs(x[max][i]) && x[j][i]!=0)
                    max = j;
            }
            double[] buf = x[max];
            x[max] = x[i];
            x[i] = buf;

            double buf1 = y[max];
            y[max] = y[i];
            y[i] = buf1;
            y[i]/=x[i][i];
            for (int j=i+1; j<y.length; j++)
                y[j]-=y[i]*x[j][i];
            for (int j=x[0].length-1; j>=i; j--)
            {
                x[i][j]/=x[i][i];
                for (int k=i+1; k<x.length; k++)
                    x[k][j]-=x[i][j]*x[k][i];
            }
        }
        for (int i=1; i<=x[0].length; i++)
        {
            for (int j=x[0].length-1; j>x[0].length-i; j--)
                y[y.length-i] -= x[x.length - i][j]*y[j];
            y[y.length-i]/=x[x.length-i][x[0].length-i];
        }
        return y;
    }

    public static double[][] prepare (double[][] a, Pre[] fun)
    {
        double[][] x = new double[a.length][a[0].length+1];
        for (int i=0; i<x.length; i++)
            x[i][0]=1;
        for (int i=1; i<x[0].length; i++)
        {
            for (int j=0; j<x.length; j++)
                x[j][i]=fun[i-1].arg(a[j][i-1]);
        }
        return x;
    }

    public static double[] test (double[][]x, double[]y)
    {
        Matrix X=new Matrix(x);
        Matrix Y=new Matrix(y,y.length);
        return methodGausa(X.transpose().times(X).getArray(), X.transpose().times(Y).transpose().getArray()[0]);
    }

    public static double error (double[][] x, double[] y, double[] b)
    {
        double e=0;
        for (int i=0; i<y.length; i++)
        {
            double f=0;
            for (int j=0; j<b.length; j++)
                f+=b[j]*x[i][j];
            e+=(y[i]-f)*(y[i]-f);
        }
        return e;
    }
}
interface Pre
{
    double arg (double x);
}
