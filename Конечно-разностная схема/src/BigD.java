import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.concurrent.*;

public class BigD
{
    /* Ввод данных : функции, рассматривемый отрезок, n(количество отрезков сетки), М2(значение функции в конце отрезка).*/
    public static void main(String[] args)
    {
        int K = 4;
        Functions answer = r -> BigDecimal.valueOf(1).subtract(r.pow(K));
        Functions[] fun = new Functions[]{
                r -> BigDecimal.valueOf(1),  //k
                r -> BigDecimal.valueOf(0),  //q
                r -> BigDecimal.valueOf(K*K*Math.pow(r.doubleValue(), K-2))}; //f
        BigDecimal finish = BigDecimal.valueOf(1), M2 = BigDecimal.valueOf(0);
        int n = 1000000;
        BigDecimal[] result = krshema(finish, n, M2, fun[0], fun[1], fun[2]);
        System.out.println(Arrays.toString(result));
        System.out.println(error(finish, result, answer));
        /*ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Future<Double>[] futures = new Future[1];
        for (int i = 999999; i < n; i++)
        {
            futures[0] = executor.submit(new KRS(start, finish, i+5, M2, fun, ans));
        }
        try
        {
            for (int i = 0; i < 1; i++)
                System.out.println(futures[0].get());
        } catch (InterruptedException | ExecutionException ex)
        {
            ex.printStackTrace(System.err);
        }
        executor.shutdown();*/
    }

    /* Метод, реализующий разностную схему.
     * Входные данные: точки начала и конца отрезка, количество отрезков сетки, значение функции в конце отрезка, известные функции.
     * Выходные данные: вектор приближенных решений.*/
    private static BigDecimal[] krshema(BigDecimal finish, int n, BigDecimal M2, Functions k, Functions q, Functions f)
    {
        BigDecimal h = finish.setScale(25, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(n), RoundingMode.HALF_UP).setScale(25, RoundingMode.HALF_UP),
                buf = BigDecimal.valueOf(0);
        BigDecimal[] C = new BigDecimal[n - 1], A = new BigDecimal[n], F = new BigDecimal[n - 1];
        BigDecimal h2 = h.multiply(h), hhalf = BigDecimal.valueOf(0.5).multiply(h);
        A[0] = hhalf.add(buf).multiply(k.arg(hhalf.add(buf))).divide(h2, RoundingMode.HALF_UP).setScale(25, RoundingMode.HALF_UP);
        for (int i = 0; i < n - 1; i++)
        {
            buf = buf.add(h);
            BigDecimal r = hhalf.add(buf);
            A[i + 1] = r.multiply(k.arg(r)).divide(h2, RoundingMode.HALF_UP).setScale(25, RoundingMode.HALF_UP);
            C[i] = BigDecimal.valueOf(0).subtract(q.arg(buf).multiply(buf).add(A[i]).add(A[i+1])).setScale(25, RoundingMode.HALF_UP);
            F[i] = BigDecimal.valueOf(0).subtract(f.arg(buf).multiply(buf)).setScale(25, RoundingMode.HALF_UP);
        }
        buf = BigDecimal.valueOf(0);
        C[0] = k.arg(hhalf).multiply(A[0]).divide(k.arg(hhalf).add(h2.multiply(q.arg(buf)).divide(BigDecimal.valueOf(4), RoundingMode.HALF_UP)), RoundingMode.HALF_UP).add(C[0]).setScale(25, RoundingMode.HALF_UP);
        F[0] = F[0].subtract(h2.multiply(f.arg(buf)).multiply(A[0]).divide(BigDecimal.valueOf(4), RoundingMode.HALF_UP).divide(k.arg(hhalf).add(h2.multiply(q.arg(buf)).divide(BigDecimal.valueOf(4), RoundingMode.HALF_UP)), RoundingMode.HALF_UP)).setScale(25, RoundingMode.HALF_UP);
        F[n - 2] = F[n - 2].subtract(A[n - 2].multiply(M2)).setScale(25, RoundingMode.HALF_UP);
        return progonka(A, C, F);
    }

    /*Метод прогонки для решения СЛАУ с трехдиагональными матрицами с диагональным преобладанием.*/
    private static BigDecimal[] progonka(BigDecimal[] a, BigDecimal[] c, BigDecimal[] f) {
        int n = f.length;
        BigDecimal[] x = new BigDecimal[n];
        BigDecimal[] A = new BigDecimal[n], B = new BigDecimal[n];
        A[0] = BigDecimal.valueOf(0);
        B[0] = BigDecimal.valueOf(0);
        for (int i = 0; i < n - 1; i++) {
            BigDecimal denom = a[i].multiply(A[i]).add(c[i]);
            A[i + 1] = BigDecimal.valueOf(0).subtract(a[i+1].divide(denom, RoundingMode.HALF_UP)).setScale(25, RoundingMode.HALF_UP);
            B[i + 1] = f[i].subtract(a[i].multiply(B[i])).divide(denom, RoundingMode.HALF_UP).setScale(25, RoundingMode.HALF_UP);
        }
        x[n - 1] = f[n - 1].subtract(a[n - 1].multiply(B[n - 1])).divide(a[n - 1].multiply(A[n - 1]).add(c[n - 1]), RoundingMode.HALF_UP).setScale(25, RoundingMode.HALF_UP);
        for (int i = n - 2; i >= 0; i--)
            x[i] = A[i + 1].multiply(x[i + 1]).add(B[i + 1]).setScale(25, RoundingMode.HALF_UP);
        return x;
    }

    /*Метод, вычисляющий максимальную ошибку полученного приближенного решения.*/
    private static BigDecimal error(BigDecimal finish, BigDecimal[] y, Functions u)
    {
        BigDecimal h = finish.setScale(25, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(y.length + 1),RoundingMode.HALF_UP);
        BigDecimal max = BigDecimal.valueOf(0).setScale(25, RoundingMode.HALF_UP), buf = BigDecimal.valueOf(0);
        for (BigDecimal aY : y)
        {
            buf = buf.add(h);
            if ((u.arg(buf).subtract(aY)).abs().compareTo(max) > 0)
                max = (u.arg(buf).subtract(aY)).abs();
        }
        return max;
    }
}

/*Интерфейс для создания массива функций*/
interface Functions
{
    BigDecimal arg(BigDecimal r);
}

/*class KRS implements Callable<Double>
{
    private Functions[] fun;
    private Functions ans;
    private double start, finish, M2;
    private int n;
    public KRS (double start, double finish, int n, double M2, Functions[] fun, Functions ans)
    {
        this.start = start;
        this.finish = finish;
        this.n = n;
        this.M2 = M2;
        this.fun = fun;
        this.ans = ans;
    }

    public Double call()
    {
        return error(start, finish, krshema(BigDecimal.valueOf(start), BigDecimal.valueOf(finish), n, M2, fun[0], fun[1], fun[2]), ans).doubleValue();
    }

    private static BigDecimal[] krshema(BigDecimal start, BigDecimal finish, int n, double M2, Functions k, Functions q, Functions f)
    {
        BigDecimal h = BigDecimal.valueOf(finish.subtract(start).doubleValue() / n).setScale(25, RoundingMode.HALF_UP),
                buf = start;
        BigDecimal[] C = new BigDecimal[n - 1], A = new BigDecimal[n], F = new BigDecimal[n - 1];
        A[0] = BigDecimal.valueOf(0.5).multiply(h).add(buf).multiply(k.arg(BigDecimal.valueOf(0.5).multiply(h).add(buf))).divide(h.multiply(h), RoundingMode.HALF_UP).setScale(25, RoundingMode.HALF_UP);

        for (int i = 0; i < n - 1; i++)
        {
            buf = buf.add(h);
            A[i + 1] = BigDecimal.valueOf(0.5).multiply(h).add(buf).multiply(k.arg(BigDecimal.valueOf(0.5).multiply(h).add(buf))).divide(h.multiply(h), RoundingMode.HALF_UP).setScale(25, RoundingMode.HALF_UP);
            C[i] = BigDecimal.valueOf(0).subtract(q.arg(buf).multiply(buf).add(A[i]).add(A[i+1])).setScale(25, RoundingMode.HALF_UP);
            F[i] = BigDecimal.valueOf(0).subtract(f.arg(buf).multiply(buf)).setScale(25, RoundingMode.HALF_UP);
        }
        C[0] = k.arg(BigDecimal.valueOf(0.5).multiply(h).add(start)).divide(k.arg(BigDecimal.valueOf(0.5).multiply(h).add(start)).add(h.multiply(h).multiply(q.arg(start)).divide(BigDecimal.valueOf(4), RoundingMode.HALF_UP)), RoundingMode.HALF_UP).multiply(A[0]).add(C[0]).setScale(25, RoundingMode.HALF_UP);
        F[0] = F[0].subtract(h.multiply(h).multiply(q.arg(start)).multiply(A[0]).divide(BigDecimal.valueOf(4), RoundingMode.HALF_UP).divide(k.arg(BigDecimal.valueOf(0.5).multiply(h).add(start)).add(h.multiply(h).multiply(q.arg(start)).divide(BigDecimal.valueOf(4), RoundingMode.HALF_UP)), RoundingMode.HALF_UP)).setScale(25, RoundingMode.HALF_UP);
        F[n - 2] = F[n - 2].subtract(A[n - 2].multiply(BigDecimal.valueOf(M2))).setScale(25, RoundingMode.HALF_UP);
        return progonka(A, C, F);
    }

    private static BigDecimal[] progonka(BigDecimal[] a, BigDecimal[] b, BigDecimal[] f) {
        int n = f.length;
        BigDecimal[] x = new BigDecimal[n];
        BigDecimal[] A = new BigDecimal[n], B = new BigDecimal[n];
        A[0] = BigDecimal.valueOf(0);
        B[0] = BigDecimal.valueOf(0);
        for (int i = 0; i < n - 1; i++) {
            A[i + 1] = BigDecimal.valueOf(0).subtract(a[i+1].divide(a[i].multiply(A[i]).add(b[i]), RoundingMode.HALF_UP)).setScale(25, RoundingMode.HALF_UP);
            B[i + 1] = f[i].subtract(a[i].multiply(B[i])).divide(a[i].multiply(A[i]).add(b[i]), RoundingMode.HALF_UP).setScale(25, RoundingMode.HALF_UP);
        }
        x[n - 1] = f[n - 1].subtract(a[n - 1].multiply(B[n - 1])).divide(a[n - 1].multiply(A[n - 1]).add(b[n - 1]), RoundingMode.HALF_UP).setScale(25, RoundingMode.HALF_UP);
        for (int i = n - 2; i >= 0; i--)
            x[i] = A[i + 1].multiply(x[i + 1]).add(B[i + 1]).setScale(25, RoundingMode.HALF_UP);
        return x;
    }
    private static BigDecimal error(double start, double finish, BigDecimal[] y, Functions u)
    {
        BigDecimal max = BigDecimal.valueOf(0), buf = BigDecimal.valueOf(start);
        BigDecimal h = BigDecimal.valueOf((finish - start) / (y.length + 1));
        for (BigDecimal aY : y)
        {
            buf = buf.add(h);
            if ((u.arg(buf).subtract(aY)).abs().compareTo(max) > 0)
                max = (u.arg(buf).subtract(aY)).abs();
        }
        return max;
}*/
