package com.company;

public class Main
{

    public static void main(String[] args)
    {
        Evolution e = new Evolution(3, 3);
        try
        {
            e.chenging();
        } catch (Exception e1)
        {
            System.out.println(e1.getMessage());
        }
    }
}
