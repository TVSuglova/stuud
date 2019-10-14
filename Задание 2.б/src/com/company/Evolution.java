package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Evolution implements Iterable<Byte>
{
    private int height, width, hash;
    private byte[][] field;
    private ArrayList<Evolution> history = new ArrayList<>();

    Evolution(int height, int width)
    {
        this.height = height;
        this.width = width;
        this.field = new byte[height][width];
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
                field[i][j] = (byte) Math.round(Math.random());
        }
        this.hash = Arrays.deepHashCode(field);
    }

    private Evolution(byte[][] field, int hash)
    {
        this.height = field.length;
        this.width = field[0].length;
        this.field = new byte[height][];
        for (int i = 0; i < height; i++)
            this.field[i] = field[i].clone();
        this.field = field;
        this.hash = hash;
    }

    public Evolution clone()
    {
        return new Evolution(field, hash);
    }

    public void print()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
                sb.append("|").append(field[i][j]);
            sb.append("|\n");
        }
        System.out.println(sb);
    }

    public void chenging() throws Exception
    {
        byte life;
        byte[][] buf = new byte[height][width];
        while (this.check())
        {
            for (int i = 0; i < height; i++)
            {
                for (int j = 0; j < width; j++)
                {
                    life = 0;
                    if (i > 0)
                    {
                        life += field[i - 1][j];
                        if (j < width - 1)
                            life += field[i - 1][j + 1];
                        if (j > 0)
                            life += field[i - 1][j - 1];
                    }
                    if (j > 0)
                    {
                        life += field[i][j - 1];
                        if (i < height - 1)
                            life += field[i + 1][j - 1];
                    }
                    if (i < height - 1)
                    {
                        life += field[i + 1][j];
                        if (j < width - 1)
                            life += field[i + 1][j + 1];
                    }
                    if (j < width - 1)
                    {
                        life += field[i][j + 1];
                    }
                    if (life == 2)
                        buf[i][j] = field[i][j];
                    else if (life == 3)
                        buf[i][j] = 1;
                    else buf[i][j] = 0;
                }
            }
            field = buf;
            hash = Arrays.deepHashCode(field);
        }
    }

    private boolean check() throws Exception
    {
        this.print();
        for (Byte e : this)
        {
            if (e > 0)
            {
                if (history.size() == 0)
                {
                    history.add(this.clone());
                    return true;
                }
                for (Evolution ev : history)
                {
                    if (this.equals(ev))
                    {
                        if (this.equals(history.get(history.size() - 1)))
                            throw new Exception("Стабилизация состояния.");
                        throw new Exception("Произошло зацикливание.");
                    }
                }
                history.add(this.clone());
                return true;
            }
        }
        throw new Exception("На поле не осталось живых клеток.");
    }

    /*private long hash()
    {
        StringBuilder sb = new StringBuilder();
        for (Byte e: this)
        {
            sb.append(e);
        }
        return Long.parseLong(sb.toString(), 2);
    }*/

    private boolean equals(Evolution ev)
    {
        if (this.hash != ev.hash)
            return false;
        else
        {
            for (int i = 0; i < height; i++)
            {
                for (int j = 0; j < width; j++)
                {
                    if (this.field[i][j] != ev.field[i][j])
                        return false;
                }
            }
        }
        return true;
    }

    @Override
    public Iterator<Byte> iterator()
    {
        return new Iterator<>()
        {
            int i1 = 0, i2 = -1;

            @Override
            public boolean hasNext()
            {
                return i1 * i2 < (height - 1) * (width - 1);
            }

            @Override
            public Byte next()
            {
                if (hasNext())
                {
                    i2++;
                    if (i2 < field[0].length)
                        return field[i1][i2];
                    else
                    {
                        i2 = 0;
                        i1++;
                        return field[i1][i2];
                    }
                }
                return null;
            }
        };
    }
}

