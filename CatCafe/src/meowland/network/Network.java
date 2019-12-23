package meowland.network;

import javafx.scene.control.Alert;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Network
{
    public void readAndTeach(String filename, int n)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            ArrayList<Double> out = new ArrayList<>();
            ArrayList<double[]> inp = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] elements = line.split(";");
                out.add(Double.parseDouble(elements[0]));
                line = reader.readLine();
                elements = line.split(";");
                double[] x = new double[elements.length];
                for (int i = 0; i < x.length; i++)
                    x[i] = Double.parseDouble(elements[i]);
                inp.add(x);
            }
            Double[] y = new Double[out.toArray().length];
            out.toArray(y);

            System.out.println(Arrays.toString(teach(inp, array(y), n)));
            System.out.println(Arrays.toString(predictionError(array(y), inp)));
            //System.out.println(Arrays.toString(predict(inp)));
        } catch (IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Не удалось найти файл");
            alert.show();
        }
    }

    public double[] array(Double[] d)
    {
        double[] doubles = new double[d.length];
        for (int i = 0; i < d.length; i++)
            doubles[i] = d[i];
        return doubles;
    }

    public double predict(double[] inp)
    {
        try (BufferedReader readerW = new BufferedReader(new FileReader("W.txt"));
             BufferedReader readerC = new BufferedReader(new FileReader("C.txt")))
        {
            ArrayList<Double> result = new ArrayList<>();
            ArrayList<double[]> centers = new ArrayList<>();
            double prediction = 0;
            String line;
            while ((line = readerW.readLine()) != null)
            {
                String[] elements = line.split("\\s*;\\s*");

                for (String element : elements)
                    result.add(Double.parseDouble(element));
            }

            while ((line = readerC.readLine()) != null)
            {
                String[] elements = line.split("\\s*;\\s*");

                double[] c = new double[elements.length];
                for (int i = 0; i < c.length; i++)
                    c[i] = Double.parseDouble(elements[i]);
                centers.add(c);
            }

            for (int j = 0; j < centers.size(); j++)
            {
                prediction += result.get(j) * Math.exp((-getAlpha(inp)) * getNorm(inp, centers.get(j)));
            }
            return prediction;
        } catch (IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Не удалось рассчитать коэффициенты");
            alert.show();
            return -1;
        }
    }

    public double[] predict(ArrayList<double[]> inp)
    {
        try (BufferedReader readerW = new BufferedReader(new FileReader("W.txt"));
             BufferedReader readerC = new BufferedReader(new FileReader("C.txt")))
        {
            ArrayList<Double> result = new ArrayList<>();
            ArrayList<double[]> centers = new ArrayList<>();
            double[] prediction = new double[inp.size()];
            String line;
            while ((line = readerW.readLine()) != null)
            {
                String[] elements = line.split("\\s*;\\s*");

                for (String element : elements)
                    result.add(Double.parseDouble(element));
            }

            while ((line = readerC.readLine()) != null)
            {
                String[] elements = line.split("\\s*;\\s*");

                double[] c = new double[elements.length];
                for (int i = 0; i < c.length; i++)
                    c[i] = Double.parseDouble(elements[i]);
                centers.add(c);
            }

            for (int i = 0; i < inp.size(); i++)
            {
                for (int j = 0; j < centers.size(); j++)
                {
                    prediction[i] += result.get(j) * Math.exp((-getAlpha(inp.get(i))) * getNorm(inp.get(i), centers.get(j)));
                }
            }
            return prediction;
        } catch (IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Не удалось рассчитать коэффициенты");
            alert.show();
            System.out.println(e.getMessage());
            return null;
        }
    }

    public double[] predictionError(double[] y, ArrayList<double[]> inp)
    {
        try (BufferedReader readerW = new BufferedReader(new FileReader("W.txt"));
             BufferedReader readerC = new BufferedReader(new FileReader("C.txt")))
        {
            ArrayList<Double> result = new ArrayList<>();
            ArrayList<double[]> centers = new ArrayList<>();
            double[] prediction = new double[inp.size()];
            double[] err = new double[2];
            String line;
            while ((line = readerW.readLine()) != null)
            {
                String[] elements = line.split("\\s*;\\s*");

                for (String element : elements)
                    result.add(Double.parseDouble(element));
            }

            while ((line = readerC.readLine()) != null)
            {
                String[] elements = line.split("\\s*;\\s*");

                double[] c = new double[elements.length];
                for (int i = 0; i < c.length; i++)
                    c[i] = Double.parseDouble(elements[i]);
                centers.add(c);
            }

            for (int i = 0; i < inp.size(); i++)
            {
                for (int j = 0; j < centers.size(); j++)
                {
                    prediction[i] += result.get(j) * Math.exp((-getAlpha(inp.get(i))) * getNorm(inp.get(i), centers.get(j)));
                }

                if (Math.abs(y[i] - prediction[i]) / (y[i] / 100) > err[0])
                    err[0] = Math.abs(y[i] - prediction[i]) / (y[i] / 100);
                err[1] += Math.abs(y[i] - prediction[i]) / (y[i] / 100);
            }
            err[1] /= inp.size();
            return err;
        } catch (IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Не удалось рассчитать коэффициенты");
            alert.show();
            System.out.println(e.getMessage());
            return null;
        }
    }

    public double[] teach(ArrayList<double[]> inp, double[] out, int n)
    {
        try (BufferedWriter bf = new BufferedWriter(new FileWriter("W.txt")))
        {
            double[][] centers = gaussianCenters(inp, n);
            double[][] results = new double[inp.size()][n];

            for (int i = 0; i < inp.size(); i++)
            {
                for (int j = 0; j < n; j++)
                {
                    results[i][j] = Math.exp((-getAlpha(inp.get(i)) * getNorm(inp.get(i), centers[j])));
                }
            }

            double[] W = mnk(results, out);
            for (double w : W)
                bf.write(w + ";");

            return W;
        } catch (IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Не удалось рассчитать коэффициенты");
            alert.show();
            System.out.println(e.getMessage());
            return null;
        }
    }

    public double[][] gaussianCenters(ArrayList<double[]> inp, int n)
    {
        try (BufferedWriter bf = new BufferedWriter(new FileWriter("C.txt")))
        {
            double[][] centers = new double[n][inp.get(0).length];

            for (int i = 0; i < inp.get(0).length; i++)
            {
                double max = 0;
                double min = 0;
                for (double[] doubles : inp)
                {
                    if (doubles[i] > max)
                        max = doubles[i];
                    if (doubles[i] < min)
                        min = doubles[i];
                }
                double h = (max - min) / (n - 1);
                for (int k = 0; k < n; k++)
                {
                    centers[k][i] = min;
                    min += h;
                }
            }
            for (double[] center : centers)
            {
                for (int j = 0; j < centers[0].length; j++)
                {
                    bf.write(center[j] + ";");
                }
                bf.write("\n");
            }
            return centers;
        } catch (IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Не удалось рассчитать коэффициенты");
            alert.show();
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static double getAlpha(double[] inp)
    {
        double max = 0;
        for (double x : inp)
        {
            max += x;
        }
        max /= inp.length;
        return 1 / (Math.PI / 2 * (max + 0.1));
    }

    public static double getNorm(double[] x, double[] c)
    {
        if (x.length == c.length)
        {
            double sum = 0;
            for (int i = 0; i < x.length; i++)
            {
                sum += (x[i] - c[i]) * (x[i] - c[i]);
            }
            return Math.sqrt(sum);
        } else return -1;
    }

    public double[] mnk(double[][] x, double[] y)
    {
        Matrix X = new Matrix(x);
        Matrix Y = new Matrix(y).transpose();

        return methodGausa(X.transpose().composition(X).getA(), X.transpose().composition(Y).transpose().getA()[0]);
    }

    private double[] methodGausa(double[][] x, double[] y)
    {
        for (int i = 0; i < x[0].length - 1; i++)
        {
            int max = i;
            for (int j = i + 1; j <= x.length - 1; j++)
            {
                if (Math.abs(x[j][i]) > Math.abs(x[max][i]) && x[j][i] != 0)
                    max = j;
            }
            double[] buf = x[max];
            x[max] = x[i];
            x[i] = buf;

            double buf1 = y[max];
            y[max] = y[i];
            y[i] = buf1;
            y[i] /= x[i][i];
            for (int j = i + 1; j < y.length; j++)
                y[j] -= y[i] * x[j][i];
            for (int j = x[0].length - 1; j >= i; j--)
            {
                x[i][j] /= x[i][i];
                for (int k = i + 1; k < x.length; k++)
                    x[k][j] -= x[i][j] * x[k][i];
            }
        }
        for (int i = 1; i <= x[0].length; i++)
        {
            for (int j = x[0].length - 1; j > x[0].length - i; j--)
                y[y.length - i] -= x[x.length - i][j] * y[j];
            y[y.length - i] /= x[x.length - i][x[0].length -
                    i];
        }
        return y;
    }
}
