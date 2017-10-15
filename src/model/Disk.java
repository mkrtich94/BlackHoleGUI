package model;

import controller.GameBoard;
import view.Hexagon;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Disk extends Piece {

    private boolean isSelected;
    public static int remaining = 15;

    public double getDiameter() {
        return Math.min((double) (board.getParentFrame().gamePanel.getWidth() + board.getParentFrame().gamePanel.getX()) / 22, (board.getParentFrame().getSize().height) * 0.1) * 1.5;
    }

    public Disk(Color color, Integer number) {
        super(number, color);
    }

    public Shape getShape(int row) {
        double radius = this.getDiameter()/2;
        double y = 2*radius*(row%((remaining+1)/2)) + board.tileMap.get("A1").getShape().getBounds2D().getMinY() + radius;
        double x = board.getParentFrame().gamePanel.getWidth()*0.01 * ((row/((remaining+1)/2))%2 + 1) + 2*radius*(row/((remaining+1)/2)) + radius;
        return new Hexagon(x + (row%((remaining+1)/2))%2*radius, y, radius, false).getShape();
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

}