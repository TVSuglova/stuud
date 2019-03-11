package com.company;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Количество элементов: ");
        int n = in.nextInt();
        double[] key = new double[n];
        for (int i=0; i<n; i++)
        {
            key[i]=in.nextDouble();
        }
        Tree A = new Tree (key);
        System.out.println(A.getRoot());
        System.out.println("\nВведите элемент для удаления:");
        System.out.println(A.delete(in.nextDouble()));
    }
}
