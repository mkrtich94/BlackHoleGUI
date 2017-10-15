package model;

import view.Hexagon;

import java.awt.*;

public class Tile extends Piece {
    private boolean isFilled;
    private String label;
    private int column;
    private int row;

    public Tile(String label, int column, int row) {
        super(null, Colors.EMPTY.getColor());
        this.isFilled = false;
        this.label = label;
        this.column = column;
        this.row = row;
    }

    private double getColumn() {
        return (10 - row + column * 2) * (double) (board.getParentFrame().getWidth() + board.getParentFrame().gamePanel.getX()) / 22;
    }

    private double getRow() {

        return (board.getParentFrame().gamePanel.getHeight()) * 0.15 + row * getRadius() * (0.6 + 2 / Math.sqrt(3));
    }

    public double getRadius() {
        return Math.min((double) (board.getParentFrame().gamePanel.getWidth() + board.getParentFrame().gamePanel.getX()) / 22
                , (board.getParentFrame().getSize().height) * 0.1);
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
        return new Hexagon(this.getColumn(), this.getRow(), this.getRadius(), false).getShape();
    }

}
