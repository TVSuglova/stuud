package com.company;

import java.util.Scanner;

class Node
{
    int key;
    int deep;
    Node left;
    Node right;
    Node parent;
}

public class Formation
{

    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        int dataArray[] = {20, 18, 19, 15, 17, 16, 3, 25, 22, 27, 26, 28, 21, 23};
        Node root = null;
        try
        {
            System.out.println(root.key);
        } catch (NullPointerException ex)
        {
            System.out.println(ex.getMessage());
        }
        try
        {
            int a = 0, b = 1 / a;
        } catch (ArithmeticException ex)
        {
            System.out.println(ex.getMessage());
        }
        try
        {
            System.out.println("Введите число:");
            double c = in.nextDouble();
        } catch (NumberFormatException ex)
        {
            System.out.println(ex.getMessage());
        }

        try
        {
            Node z = new Node();
            z.key = dataArray[0];
            z.parent = null;
            root = insert(z, root);
            try
            {
                for (int i = 1; i <= dataArray.length; i++)
                {
                    z = new Node();
                    z.key = dataArray[i];
                    insert(z, root);
                }
            } catch (IndexOutOfBoundsException ex)
            {
                System.out.println(ex.getMessage());
            }
            try
            {
                z = new Node();
                z.key = 19;
                insert(z, root);
            } catch (IllegalArgumentException ex)
            {
                System.out.println(ex.getMessage());
            }
        } catch (NodeBallanceException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public static Node insert(Node z, Node x) throws NodeBallanceException
    {
        if (x == null)
        {
            x = z;
        } else if (x.key == z.key)
            throw new IllegalArgumentException();
        else if (z.key < x.key)
        {
            x.left = insert(z, x.left);
            x.left.parent = x;
        } else
        {
            x.right = insert(z, x.right);
            x.right.parent = x;
        }
        if (x.left == null)
            x.deep = 0;
        else if (x.deep <= x.left.deep)
            x.deep = x.left.deep + 1;
        if (x.right != null && x.deep <= x.right.deep)
            x.deep = x.right.deep + 1;
        if (x.right != null && x.left != null && Math.abs(x.left.deep - x.right.deep) > 1)
            throw new NodeBallanceException();
        else if (x.right != null && x.right.deep > 1)
            throw new NodeBallanceException();
        else if (x.left != null && x.left.deep > 1)
            throw new NodeBallanceException();
        return x;
    }

    public static Node search(Node x, int k)
    {
        if ((x == null) || (k == x.key))
            return x;
        if (k < x.key)
            return search(x.left, k);
        else
            return search(x.right, k);
    }


    public static void findDeep(Node a) throws NodeBallanceException
    {
        if (a.right != null && a.left != null)
        {
            if (Math.abs(a.left.deep - a.right.deep) > 1)
                throw new NodeBallanceException();
            findDeep(a.right);
            findDeep(a.left);
        } else if (a.left != null)
        {
            if (a.left.deep > 0)
                throw new NodeBallanceException();
            findDeep(a.left);
        } else if (a.right != null)
        {
            if (a.right.deep > 0)
                throw new NodeBallanceException();
            findDeep(a.right);
        }
    }
}

class NodeBallanceException extends Exception
{
    public String getMessage()
    {
        return "NodeBallanceException";
    }
}
