package com.company;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = 10;
        Vector A = new Vector<Double>();
        for (int i = 0; i < n; i++)
            A.addElement(in.nextDouble());
        Heap AA = new Heap(A);
        System.out.println(AA);
        label:
        System.out.println("1 - добавить элемент; 2 - удалить элемент.");
        switch (in.nextInt()) {
            case 1:
                System.out.println("Введите элемент для добавления:");
                AA.add(in.nextDouble());
                break;
            case 2:
                System.out.println("1 - удалить по номеру; 2 - удалить по значению.");
                switch (in.nextInt()) {
                    case 1:
                        System.out.println("Введите номер элемента для удаления:");
                        AA.delete(in.nextInt());
                        break;
                    case 2:
                        System.out.println("Введите элемент для удаления:");
                        AA.delete(in.nextDouble());
                        break;
                    default:
                        System.out.println("Некорректный ввод.");
                        break;
                }
                break;
            default:
                System.out.println("Некорректный ввод.");
                break;

        }
        System.out.println(AA);
        AA.heapSort(A.size());
        System.out.println("Сортировка: " + AA);
    }
}

