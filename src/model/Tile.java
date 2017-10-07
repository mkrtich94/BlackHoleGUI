package model;

import controller.GameBoard;
import javafx.util.Pair;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Tile {
    private static GameBoard board;
    private boolean isFilled;
    private Color color;
    private Integer number;
    private String label;
    private int column;
    private int row;

    public Tile(String label, int column, int row) {
        this.isFilled = false;
        this.color = Color.LIGHT_GRAY;
        this.number = null;
        this.label = label;
        this.column = column;
        this.row = row;
    }

    public static void setBoard(GameBoard board) {
        Tile.board = board;
    }

    public double getColumn() {
        return (10 - row + column * 2) * (double) (board.getParentFrame().getSize().width) / 22;
    }

    public double getRow() {
        return (board.getParentFrame().getSize().height) * 0.1 + 2 * row * (double) (board.getParentFrame().getSize().width) / 22;
    }

    public Pair<int[], int[]> getCoordinates() {
        int[] ordinates = new int[6];
        int[] abscissas = new int[6];
//        for(int i = 0; i<6; ++i) {
        abscissas[0] = (int) (getRow());
        ordinates[0] = (int) (getColumn());
        abscissas[1] = (int) (getRow() + getDiameter() * 0.5);
        ordinates[1] = (int) (getColumn() - getDiameter() * 0.5);
        abscissas[2] = (int) (getRow() + getDiameter());
        ordinates[2] = (int) (getColumn());
        abscissas[3] = (int) (getRow() + getDiameter());
        ordinates[3] = (int) (getColumn() + getDiameter());
        abscissas[4] = (int) (getRow() + getDiameter() * 0.5);
        ordinates[4] = (int) (getColumn() + getDiameter() * 1.5);
        abscissas[5] = (int) (getRow());
        ordinates[5] = (int) (getColumn() + getDiameter());
//        }
        return new Pair<int[], int[]>(abscissas, ordinates);
    }

    public double getDiameter() {
        return Math.min((double) (board.getParentFrame().getSize().width) / 22, (board.getParentFrame().getSize().height) * 0.1) * 2;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getText() {
        return number.toString();
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Shape getShape() {
        return new Ellipse2D.Double(this.getColumn(), this.getRow(), this.getDiameter(), this.getDiameter());
    }

}
