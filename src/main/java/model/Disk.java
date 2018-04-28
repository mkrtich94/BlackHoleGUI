package model;

import view.Hexagon;

import java.awt.*;

public class Disk extends Piece {

    private boolean isSelected;

    public Disk(Color color, Integer number) {
        super(number, color);
    }

    public double getRadius() {
        return Math.min((double) (board.getParentFrame().getGamePanel().getWidth() + board.getParentFrame().getGamePanel().getX()) / 22, (board.getParentFrame().getSize().height) * 0.1) * 0.75;
    }

    public Shape getShape(int row, int totalCount) {
        double radius = this.getRadius();
        double y = radius * (0.6 + 2 / Math.sqrt(3)) * (row % ((totalCount + 1) / 2)) + board.tileMap.get("A1").getShape().getBounds2D().getMinY() + radius;
        double x = board.getParentFrame().getGamePanel().getWidth() * 0.01 + 2 * radius * (row / ((totalCount + 1) / 2)) + radius;
        return new Hexagon(x + (row % ((totalCount + 1) / 2)) % 2 * radius, y, radius, false).getShape();
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}