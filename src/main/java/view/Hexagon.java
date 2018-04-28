package view;

import java.awt.*;

public class Hexagon {

    private double outerRadius;
    private double innerRadius;
    private Shape shape;

    public Hexagon(double centerX, double centerY, double radius, boolean isOuterRadius) {
        if (isOuterRadius) {
            this.outerRadius = radius;
            this.innerRadius = outerRadius * Math.sqrt(3) / 2;
        } else {
            this.innerRadius = radius;
            this.outerRadius = innerRadius * 2 / Math.sqrt(3);
        }
        setShape(centerX, centerY);
    }

    public double getOuterRadius() {
        return outerRadius;
    }

    public void setOuterRadius(double radius) {
        this.outerRadius = radius;
        this.innerRadius = outerRadius * Math.sqrt(3) / 2;

        setShape(getShape().getBounds2D().getCenterX(), getShape().getBounds2D().getCenterY());
    }

    public double getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(double radius) {
        this.innerRadius = radius;
        this.outerRadius = innerRadius * 2 / Math.sqrt(3);

        setShape(getShape().getBounds2D().getCenterX(), getShape().getBounds2D().getCenterY());
    }

    public Shape getShape() {
        return shape;
    }

    private void setShape(double centerX, double centerY) {
        int[] ordinates = new int[6];
        int[] abscissas = new int[6];

        abscissas[0] = (int) (centerX);
        ordinates[0] = (int) (centerY - outerRadius);

        abscissas[1] = (int) (centerX + innerRadius);
        ordinates[1] = (int) (centerY - outerRadius / 2);

        abscissas[2] = (int) (centerX + innerRadius);
        ordinates[2] = (int) (centerY + outerRadius / 2);

        abscissas[3] = (int) (centerX);
        ordinates[3] = (int) (centerY + outerRadius);

        abscissas[4] = (int) (centerX - innerRadius);
        ordinates[4] = (int) (centerY + outerRadius / 2);

        abscissas[5] = (int) (centerX - innerRadius);
        ordinates[5] = (int) (centerY - outerRadius / 2);

        this.shape = new Polygon(abscissas, ordinates, 6);
    }
}
