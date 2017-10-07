package model;

import java.awt.*;

public class Disk {
    private Color color;
    private Integer number;

    public Disk(Color color, Integer number) {
        this.color = color;
        this.number = number;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
