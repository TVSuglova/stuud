package com.company;

import java.util.*;

public class Steck
{
    private Vector<Double> data;

    public Steck(Vector<Double> data)
    {
        this.data = data;
    }

    public Steck clone()
    {
        return new Steck(data);
    }

    public double push(double a)
    {
        data.add(a);
        return a;
    }

    public double pop()
    {
        double a = this.peek();
        data.remove(data.size() - 1);
        return a;
    }

    public double peek()
    {
        if (data.isEmpty())
        {
            System.out.println("Стек пуст.");
            throw new EmptyStackException();
        }
        return data.lastElement();
    }

    public double search(double a)
    {
        Steck A = this.clone();
        while (A.peek() != a)
            this.pop();
        return a;
    }
}
