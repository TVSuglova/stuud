package com.company;

public class Tree
{
    private Node root;
    Tree (double[] keys)
    {
        this.root = new Node (keys[Math.round(keys.length / 2)], null, null, null);
        for (int i = 0; i < keys.length; i++)
        {
            if (i != Math.round(keys.length / 2))
                add(keys[i]);
        }
    }

    public Node getRoot() {
        return root;
    }
    public void setRoot(double key) {
        this.root = new Node(key, root.parent, root.left, root.right);
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
        public double getKey() {
            return key;
        }
        public Node getParent() {
            return parent;
        }
        public Node getLeft() {
            return left;
        }
        public Node getRight() {
            return right;
        }
        public void setKey (double key) {
            this.key=key;
        }
        public void setParent (Node A) {
            this.parent=A;
        }
        public void setLeft (Node A) {
            this.left=A;
        }
        public void setRight (Node A) {
            this.right=A;
        }
        public void setNode (Node B) {
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
                this.setParent(this.parent);
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
    public void add (double key)
    {
        if (search(getRoot(), key).key > key)
            search(getRoot(), key).setLeft(new Node (key, search(getRoot(), key), null, null));
        else if (search(getRoot(), key).key < key)
            search(getRoot(), key).setRight(new Node (key, search(getRoot(), key), null, null));
        else if (search(getRoot(), key).key == key)
            System.out.println("Такой элемент уже записан(" + key + ").");
    }
    private Node findLeft(Node A)
    {
        if (A.left == null)
            return A;
        else
            return findLeft(A.left);
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
        System.out.print("Элемент удален.");
        return search(key);
    }
}
