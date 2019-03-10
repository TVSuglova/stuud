package com.company;

import java.util.*;

public class Heap {
    private Vector<Double> data;

    public Heap(Vector<Double> data)
    {
        this.data = data;
        for (int j = data.size() / 2; j >= 0; j--) {
            heapify(j, data.size());
        }
    }

    public String toString() {
        return Arrays.toString(data.toArray());
    }

    public void heapify(int i, int end) {
        int largest = i, left = 2 * i + 1, right = 2 * i + 2;
        double buf;
        if (left < end && data.elementAt(left) > data.elementAt(largest))
            largest = left;
        if (right < end && data.elementAt(right) > data.elementAt(largest))
            largest = right;
        if (largest == i)
            return;
        buf = data.elementAt(i);
        data.setElementAt(data.elementAt(largest), i);
        data.setElementAt(buf, largest);
        heapify(largest, end);
    }

    public void delete(int i) {
        if (i < data.size()) {
            data.remove(i);
            for (int j = data.size() / 2; j >= 0; j--)
                heapify(j, data.size());
        } else
            System.out.println("Элемент c таким номером не содержится в куче.");
    }

    public void delete(double a) {
        if (data.contains(a))
            delete(data.indexOf(a));
        else
            System.out.println("Элемент не содержится в куче.");
    }

    public void add(double a) {
        data.add(a);
        for (int j = data.size() / 2; j >= 0; j--) {
            heapify(j, data.size());
        }
    }

    public void heapSort(int end) {
        if (end < 2)
            return;
        double buf;
        for (int i = end / 2 - 1; i >= 0; i--)
            heapify(i, end);
        buf = data.elementAt(0);
        data.setElementAt(data.elementAt(end - 1), 0);
        data.setElementAt(buf, end - 1);
        heapSort(end - 1);
    }
}
