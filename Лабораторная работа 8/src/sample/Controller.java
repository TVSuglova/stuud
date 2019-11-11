package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Controller
{
    @FXML
    private TextField lineX1, lineY1, lineX2, lineY2, rectangleX, rectangleY, rectangleL, rectangleW,
            ellipseX, ellipseY, ellipseW, ellipseL, triangleX, triangleY, recursion, treeXY, treeAngle, treeScale;
    @FXML
    private Canvas canvasLine, canvasRectangle, canvasEllipse, canvasTriangle, canvasCat, canvasTree;
    private GraphicsContext gc;
    @FXML
    private CheckBox fillRectangle, fillEllipse, fillTriangle, varicoloredTree;
    @FXML
    private ColorPicker treeColor;

    @FXML
    private void newLine(MouseEvent event)
    {
        double x1 = Double.parseDouble(lineX1.getText()), y1 = Double.parseDouble(lineY1.getText()),
                x2 = Double.parseDouble(lineX2.getText()), y2 = Double.parseDouble(lineY2.getText());

        gc = canvasLine.getGraphicsContext2D();
        gc.setLineWidth(0.5);
        gc.strokeLine(x1, y1, x2, y2);
    }

    @FXML
    private void newRectangle(MouseEvent event)
    {
        double x = Double.parseDouble(rectangleX.getText()), y = Double.parseDouble(rectangleY.getText()),
                w = Double.parseDouble(rectangleW.getText()), l = Double.parseDouble(rectangleL.getText());

        gc = canvasRectangle.getGraphicsContext2D();
        if (fillRectangle.isSelected())
        {
            gc.setFill(Color.color(Math.random(), Math.random(), Math.random()));
            gc.fillRect(x, y, w, l);
        } else
        {
            gc.setLineWidth(0.5);
            gc.strokeRect(x, y, w, l);
        }
    }

    @FXML
    private void newEllipse(MouseEvent event)
    {
        double x = Double.parseDouble(ellipseX.getText()), y = Double.parseDouble(ellipseY.getText()),
                w = Double.parseDouble(ellipseW.getText()), l = Double.parseDouble(ellipseL.getText());

        gc = canvasEllipse.getGraphicsContext2D();
        if (fillEllipse.isSelected())
        {
            gc.setFill(Color.color(Math.random(), Math.random(), Math.random()));
            gc.fillOval(x, y, w, l);
        } else
        {
            gc.setLineWidth(0.5);
            gc.strokeOval(x, y, w, l);
        }
    }

    @FXML
    private void newTriangle(MouseEvent event)
    {
        double[] x = new double[3], y = new double[3];
        String[] X = triangleX.getText().split("\\s*;\\s*"), Y = triangleY.getText().split("\\s*;\\s*");
        x[0] = Double.parseDouble(X[0]);
        x[1] = Double.parseDouble(X[1]);
        x[2] = Double.parseDouble(X[2]);
        y[0] = Double.parseDouble(Y[0]);
        y[1] = Double.parseDouble(Y[1]);
        y[2] = Double.parseDouble(Y[2]);

        gc = canvasTriangle.getGraphicsContext2D();
        if (fillTriangle.isSelected())
        {
            gc.setFill(Color.color(Math.random(), Math.random(), Math.random()));
            gc.fillPolygon(x, y, 3);
        } else
        {
            gc.setLineWidth(0.5);
            gc.strokePolygon(x, y, 3);
        }
    }

    @FXML
    private void newCat(MouseEvent event)
    {
        gc = canvasCat.getGraphicsContext2D();
        Image img = new Image("file:Walking_Cat.png");
        gc.drawImage(img, Math.random() * 250, Math.random() * 150, (Math.random() + 0.05) * 250, (Math.random() + 0.05) * 250);
    }

    public void newTree(MouseEvent mouseEvent)
    {
        int depth = Integer.parseInt(recursion.getText());
        double x = Double.parseDouble(treeXY.getText().split("\\s*;\\s*")[0]),
                y = Double.parseDouble(treeXY.getText().split("\\s*;\\s*")[1]),
                angle = Double.parseDouble(treeAngle.getText());
        double scale = Double.parseDouble(treeScale.getText());

        GraphicsContext gc = canvasTree.getGraphicsContext2D();
        gc.setStroke(treeColor.getValue());
        drawTree(gc, x, y, angle, depth, scale);
    }

    private void drawTree(GraphicsContext gc, double x1, double y1, double angle, int depth, double scale)
    {
        if (depth == 0)
            return;
        double x2 = x1 + (Math.cos(angle) * depth * 5);
        double y2 = y1 + (Math.sin(angle) * depth * 5);
        if (varicoloredTree.isSelected())
            gc.setStroke(Color.color(Math.random(), Math.random(), Math.random()));
        gc.strokeLine(x1 * scale, y1 * scale, x2 * scale, y2 * scale);
        drawTree(gc, x2, y2, angle - Math.PI / 8, depth - 1, scale);
        drawTree(gc, x2, y2, angle + Math.PI / 8, depth - 1, scale);
    }
}
