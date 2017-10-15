package model;

import view.Hexagon;

import java.awt.*;

public class Disk extends Piece {

    public static int remaining = 15;
    private boolean isSelected;

    public Disk(Color color, Integer number) {
        super(number, color);
    }

    public double getRadius() {
        return Math.min((double) (board.getParentFrame().gamePanel.getWidth() + board.getParentFrame().gamePanel.getX()) / 22, (board.getParentFrame().getSize().height) * 0.1) * 0.75;
    }

    public Shape getShape(int row) {
        double radius = this.getRadius();
        double y = radius * (0.6 + 2 / Math.sqrt(3)) * (row % ((remaining + 1) / 2)) + board.tileMap.get("A1").getShape().getBounds2D().getMinY() + radius;
        double x = board.getParentFrame().gamePanel.getWidth() * 0.01 + 2 * radius * (row / ((remaining + 1) / 2)) + radius;
        return new Hexagon(x + (row % ((remaining + 1) / 2)) % 2 * radius, y, radius, false).getShape();
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}