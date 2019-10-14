package com.company;

import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class Point
{
    protected int dim;
    protected double[] x;

    public String toString()
    {
        StringBuilder P = new StringBuilder();
        P.append("(");
        for (int i = 0; i < dim - 1; i++)
        {
            P.append(x[i]).append(" ; ");
        }
        P.append(x[dim - 1]).append(")");
        return P.toString();
    }

    public Point(int dim)
    {
        this.dim = dim;
        this.x = new double[dim];
    }

    public Point(int dim, double[] x)
    {
        this.dim = dim;
        this.x = x.clone();
    }

    public Point clone()
    {
        return new Point(dim, x);
    }

    public int GetDim()
    {
        return dim;
    }

    public double[] GetX()
    {
        return x;
    }

    public double GetX(int i)
    {
        return x[i];
    }

    public void SetX(double[] x)
    {
        this.x = x.clone();
    }

    public void SetX(int i, double x)
    {
        this.x[i] = x;
    }

    public double Abs()
    {
        double a = 0;
        for (double i : x)
        {
            a += i * i;
        }
        return Math.sqrt(a);
    }

    public Point Add(Point B)
    {
        if (dim != B.GetDim())
        {
            throw new IllegalArgumentException("Точки несовместимы.");
        }
        Point buf = this.clone();
        for (int i = 0; i < dim; i++)
        {
            buf.SetX(i, this.GetX(i) + B.GetX(i));
        }
        return buf;
    }

    public static Point Add(Point A, Point B)
    {
        if (A.GetDim() != B.GetDim())
        {
            throw new IllegalArgumentException("Точки несовместимы.");
        }
        double[] x = new double[A.GetDim()];
        for (int i = 0; i < A.GetDim(); i++)
        {
            x[i] = A.GetX(i) + B.GetX(i);
        }
        return new Point(A.GetDim(), x);
    }

    public Point Sub(Point B)
    {
        if (dim != B.GetDim())
        {
            throw new IllegalArgumentException("Точки несовместимы.");
        }
        Point buf = this.clone();
        for (int i = 0; i < dim; i++)
        {
            buf.SetX(i, this.GetX(i) - B.GetX(i));
        }
        return buf;
    }

    public static Point Sub(Point A, Point B)
    {
        if (A.GetDim() != B.GetDim())
        {
            throw new IllegalArgumentException("Точки несовместимы.");
        }
        double[] x = new double[A.GetDim()];
        for (int i = 0; i < A.GetDim(); i++)
        {
            x[i] = A.GetX(i) - B.GetX(i);
        }
        return new Point(A.GetDim(), x);
    }

    public Point Mult(double r)
    {
        Point buf = this.clone();
        for (int i = 0; i < dim; i++)
        {
            buf.SetX(i, this.GetX(i) * r);
        }
        return buf;
    }

    public static Point Mult(Point A, double r)
    {
        double[] x = new double[A.GetDim()];
        for (int i = 0; i < A.GetDim(); i++)
        {
            x[i] = A.GetX(i) * r;
        }
        return new Point(A.GetDim(), x);
    }

    public double Multi(Point B)
    {
        if (dim != B.GetDim())
        {
            throw new IllegalArgumentException("Точки несовместимы.");
        }
        double a = 0;
        for (int i = 0; i < dim; i++)
        {
            a += x[i] * B.GetX(i);
        }
        return a;
    }

    public static double Multi(Point A, Point B)
    {
        if (A.GetDim() != B.GetDim())
        {
            throw new IllegalArgumentException("Точки несовместимы.");
        }
        double a = 0;
        for (int i = 0; i < A.GetDim(); i++)
        {
            a += A.GetX(i) * B.GetX(i);
        }
        return a;
    }

    public Point Simm(int n)
    {
        Point buf = this.clone();
        for (int i = 0; i < dim; i++)
        {
            buf.SetX(i, -this.GetX(i));
        }
        buf.SetX(n, this.GetX(n));
        return buf;
    }
}

class Point2D extends Point
{
    public Point2D()
    {
        super(2);
    }

    public Point2D(double x, double y)
    {
        super(2, new double[]{x, y});
    }

    public Point2D Rot(double phi)
    {
        return new Point2D(this.x[0] * Math.cos(phi) - this.x[1] * Math.sin(phi), this.x[0] * Math.sin(phi) + this.x[1] * Math.cos(phi));
    }
}

class BrokenLine implements Iterable
{
    private ArrayList<Point2D> Points;
    public BrokenLine(ArrayList<Point2D> Points)
    {
        this.Points = Points;
    }

    public Iterator iterator()
    {
        return Points.listIterator();
    }
    public String toString()
    {
        StringBuilder P = new StringBuilder();
        P.append("(");
        for (int i = 0; i < Points.size(); i++)
        {
            P.append(Points.get(i)).append(" ");
        }
        return P.toString();
    }

    public void addNewPoint(Point2D point)
    {
        Points.add(point);
    }

    public BrokenLine rot(double phi)
    {
        ArrayList<Point2D> P = new ArrayList<>();
        for (int i = 0; i < Points.size(); i++)
        {
            P.add(Points.get(i).Rot(phi));
        }
        return new BrokenLine(P);
    }

    public BrokenLine shift(Point2D a)
    {
        ArrayList<Point2D> P = new ArrayList<>();
        for (int i = 0; i < Points.size(); i++)
        {
            P.add((Point2D) Points.get(i).Add(a));
        }
        return new BrokenLine(P);
    }

    public BrokenLine simm(int a)
    {
        ArrayList<Point2D> P = new ArrayList<>();
        for (int i = 0; i < Points.size(); i++)
        {
            P.add((Point2D) Points.get(i).Simm(a));
        }
        return new BrokenLine(P);
    }
}


