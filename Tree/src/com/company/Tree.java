package com.company;

import java.util.Arrays;

public class Tree
{
    private Node root;
    Tree (double[] keys)
    {
        Arrays.sort(keys);
        this.root = new Node(keys[Math.round((keys.length - 1) / 2)], null, null, null);
        buildTree(keys);
    }

    public void buildTree(double[] keys)
    {
        if (keys.length <= 2)
        {
            for (int i = 0; i < keys.length; i++)
                buildAdd(keys[i]);
            return;
        }
        double[] left = Arrays.copyOfRange(keys, 0, Math.round((keys.length - 1) / 2));
        double[] right = Arrays.copyOfRange(keys, Math.round((keys.length - 1) / 2) + 1, keys.length);
        if (keys[Math.round((keys.length - 1) / 2)] != root.key)
            buildAdd(keys[Math.round((keys.length - 1) / 2)]);
        buildTree(left);
        buildTree(right);
    }

    public Node getRoot() {
        return root;
    }
    public void setRoot(double key) {
        this.root = new Node(key, null, root.left, root.right);
    }
    public void setRoot(Node root) {
        this.root = new Node(root.key, null, root.left, root.right);
    }

    public class Node
    {
        private double key;
        private Node parent, left, right;

        Node(double key, Node parent, Node left, Node right)
        {
            this.key = key;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        public double getKey()
        {
            return key;
        }

        public Node getParent()
        {
            return parent;
        }

        public Node getLeft()
        {
            return left;
        }

        public Node getRight()
        {
            return right;
        }

        public void setKey(double key)
        {
            this.key = key;
        }

        public void setParent(Node A)
        {
            this.parent = A;
        }

        public void setLeft(Node A)
        {
            this.left = A;
        }

        public void setRight(Node A)
        {
            this.right = A;
        }

        public void setNode(Node B)
        {
            if (search(key).key == root.key)
                Tree.this.setRoot(B);
            if (B != null)
                B.setParent(this.getParent());
            if (this.parent != null)
            {
                if (this == this.parent.getLeft())
                    this.parent.setLeft(B);
                else if (this == this.parent.getRight())
                    this.parent.setRight(B);
            }
        }
        public String toString()
        {
            String s2 = "\nРодитель: нет", s3 = "\nЛевый потомок: нет", s4 = "\nПравый потомок: нет";
            if (parent != null)
                s2 = "\nРодитель:" + parent.key;
            if (left != null)
                s3 = "\nЛевый потомок:" + left.key;
            if (right != null)
                s4 = "\nПравый потомок:" + right.key;
            return "\nЭлемент:" + key + s2 + s3 + s4;
        }

        public Node clone()
        {
            return new Node(this.key, this.parent, this.left, this.right);
        }
    }

    private Node search (Node A, double key)
    {
        if (A == null || A.key == key)
            return A;
        if (key < A.key)
        {
            if (A.left == null)
                return A;
            return search(A.left, key);
        }
        else
        {
            if (A.right == null)
                return A;
            return search(A.right, key);
        }
    }
    public Node search (double key)
    {
        if (search(getRoot(), key).key == key)
            return search(getRoot(), key);
        System.out.print("\nЭлемент не найден: ");
        return null;
    }

    public void buildAdd(double key)
    {
        if (search(getRoot(), key).key > key)
            search(getRoot(), key).setLeft(new Node(key, search(getRoot(), key), null, null));
        else if (search(getRoot(), key).key < key)
            search(getRoot(), key).setRight(new Node(key, search(getRoot(), key), null, null));
        else if (search(getRoot(), key).key == key)
        {
            System.out.println("Такой элемент уже записан(" + key + ").");
        }
    }
    public void add (double key)
    {
        if (search(getRoot(), key).key > key)
            search(getRoot(), key).setLeft(new Node (key, search(getRoot(), key), null, null));
        else if (search(getRoot(), key).key < key)
            search(getRoot(), key).setRight(new Node (key, search(getRoot(), key), null, null));
        else if (search(getRoot(), key).key == key)
        {
            System.out.println("Такой элемент уже записан(" + key + ").");
            return;
        }
        callBalance(root);
    }
    private Node findLeft(Node A)
    {
        if (A.left == null)
            return A;
        else
            return findLeft(A.left);
    }

    private Node findRight(Node A)
    {
        if (A.right == null)
            return A;
        else
            return findRight(A.right);
    }
    public Node delete(double key)
    {
        if (search(key) != null)
        {
            if (search(key).left == null && search(key).right == null)
            {
                search(key).setNode(null);
            } else if (search(key).left != null && search(key).right == null)
            {
                search(key).setNode(search(key).left);
            } else if (search(key).left == null && search(key).right != null)
            {
                search(key).setNode(search(key).right);
            } else if (search(key).left != null && search(key).right != null)
            {
                if (search(key).right.getLeft() == null)
                {
                    search(key).right.setLeft(search(key).left);
                    search(key).left.setParent(search(key).right);
                    search(key).setNode(search(key).right);
                }
                else
                {
                    Node A = findLeft(search(key).right);
                    delete(findLeft(search(key).right).getKey());
                    search(key).left.setParent(A);
                    search(key).right.setParent(A);
                    search(key).setNode(A);
                }
            }
        }
        callBalance(root);
        System.out.print("Элемент удален.");
        return search(key);
    }

    private int checkLeft(Node A)
    {
        int l = 0;
        while (A != null && A.left != null)
        {
            l++;
            A = A.left;
        }
        return l;
    }

    private int checkRight(Node A)
    {
        int r = 0;
        while (A != null && A.right != null)
        {
            r++;
            A = A.right;
        }
        return r;
    }

    public void callBalance(Node A)
    {
        if (findLeft(A) == A && findRight(A) == A)
            balance(A);
        else
        {
            callBalance(findRight(findLeft(A)));
            callBalance(findLeft(findRight(A)));
        }
    }

    public void balance(Node A)
    {
        if (A == null)
            return;
        int l = checkLeft(A.clone()), r = checkRight(A.clone());
        Node B = A;
        Node C = A;
        for (int i = 1; i <= l; i++)
        {
            if (B != null && B.left != null && checkRight(B.left.clone()) + i > l)
                l = i + checkLeft(B.left.clone());
            B = B.left;
        }
        for (int i = 1; i <= r; i++)
        {
            if (C != null && C.right != null && checkLeft(C.right.clone()) + i > r)
                r = i + checkLeft(C.right.clone());
            C = C.right;
        }
        if (A.parent == null)
        {
            if (l - r > 1)
                turnRight(A, l, r);
            else if (r - l > 1)
                turnLeft(A, l, r);
            return;
        }
        if (l - r > 1)
            turnRight(A, l, r);
        else if (r - l > 1)
            turnLeft(A, l, r);
        balance(A.parent);
    }

    private void turnRight(Node A, int l, int r)
    {
        Node B = A;
        for (int i = 0; i < Math.round((l - r) / 2); i++)
            B = B.left;
        A.setParent(findRight(B));
        findRight(B).setRight(A);
        A.setNode(B);
    }

    private void turnLeft(Node A, int l, int r)
    {
        Node B = A;
        for (int i = 0; i < Math.round((r - l) / 2); i++)
            B = B.right;
        A.setParent(findLeft(B));
        findLeft(B).setLeft(A);
        A.setNode(B);
    }
}
