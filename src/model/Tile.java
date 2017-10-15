package model;

import controller.GameBoard;
import javafx.util.Pair;
import view.Hexagon;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Tile extends Piece {
    private boolean isFilled;
    private Color color;
    private Integer number;
    private String label;
    private int column;
    private int row;

    public Tile(String label, int column, int row) {
        super(null, Color.LIGHT_GRAY);
        this.isFilled = false;
        this.label = label;
        this.column = column;
        this.row = row;
    }

    public double getColumn() {
        return (10 - row + column * 2) * (double) (board.getParentFrame().gamePanel.getWidth() + board.getParentFrame().gamePanel.getX()) / 22;
    }

    public double getRow() {
        return (board.getParentFrame().gamePanel.getHeight()) * 0.1 + 2 * row * (double) (board.getParentFrame().gamePanel.getWidth() + board.getParentFrame().gamePanel.getX()) / 22;
    }

    public double getDiameter() {
        return Math.min((double) (board.getParentFrame().gamePanel.getWidth() + board.getParentFrame().gamePanel.getX()) / 22, (board.getParentFrame().getSize().height) * 0.1) * 2;
    }

    public boolean getIsFilled() {
        return isFilled;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    public Shape getShape() {
        double radius = this.getDiameter()/2;
        return new Hexagon(this.getColumn(), this.getRow(), radius, false).getShape();
    }

}
