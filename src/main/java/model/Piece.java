package model;

import controller.GameBoard;

import java.awt.*;

public abstract class Piece {

    static GameBoard board;
    private Integer number;
    private Color color;

    Piece(Integer number, Color color) {
        this.number = number;
        this.color = color;
    }

    public static void setBoard(GameBoard gameBoard) {
        board = gameBoard;
    }

    public final Integer getNumber() {
        return number;
    }

    public final void setNumber(Integer number) {
        this.number = number;
    }

    public final Color getColor() {
        return color;
    }

    public final void setColor(Color color) {
        this.color = color;
    }

    public abstract double getRadius();

}
