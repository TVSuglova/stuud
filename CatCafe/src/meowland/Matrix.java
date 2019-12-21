package meowland;

public class Matrix
{
    private int n;
    private int m;
    private double[][] A;

    public int getN()
    {
        return n;
    }

    public int getM()
    {
        return m;
    }

    public double[][] getA()
    {
        return A;
    }

    public Matrix(double[][] A)
    {
        this.n = A.length;
        this.m = A[0].length;

        for (int i = 0; i < this.n; i++)
        {
            if (A[i].length != this.m)
            {
                throw new IllegalArgumentException("Невозможно создать матрицу");
            }
        }

        this.A = A;
    }

    public Matrix(double[] A)
    {
        this.n = 1;
        this.m = A.length;

        this.A = new double[n][m];
        this.A[0] = A;
    }

    public Matrix transpose()
    {
        double[][] B = new double[m][n];

        for (int i = 0; i < this.n; i++)
        {
            for (int j = 0; j < this.m; j++)
            {
                B[j][i] = this.A[i][j];
            }
        }

        return new Matrix(B);
    }

    public Matrix composition(Matrix B)
    {
        if (this.m == B.getN())
        {
            double[][] result = new double[this.n][B.getM()];
            double sum;
            for (int i = 0; i < this.n; i++)
            {
                for (int j = 0; j < B.getM(); j++)
                {
                    sum = 0;
                    for (int k = 0; k < this.m; k++)
                    {
                        sum += this.A[i][k] * B.getA()[k][j];
                    }
                    result[i][j] = sum;
                }
            }
            return new Matrix(result);
        } else
            return null;
    }
}
