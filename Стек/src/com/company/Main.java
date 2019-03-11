package com.company;

import java.util.*;

public class Main
{

    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        System.out.print("Количество элементов: ");
        int n = in.nextInt();
        Vector<Double> A = new Vector<>();
        System.out.print("Введите элементы: ");
        for (int i = 0; i < n; i++)
            A.addElement(in.nextDouble());
        Steck AA = new Steck(A);
        System.out.println("Головной элемент: " + AA.peek());
        System.out.println("Удаление головного элемента: " + AA.pop());
        System.out.print("Введите элемент для добавления: ");
        System.out.println(AA.push(in.nextDouble()));
        System.out.println("Головной элемент: " + AA.peek());
        System.out.print("Введите элемент для поиска: ");
        System.out.println(AA.search(in.nextDouble()));
    }
}
