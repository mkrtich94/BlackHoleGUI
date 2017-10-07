package model;

import java.awt.*;

public enum Colors {
    RED(0x80, 0x00, 0x00),
    BLUE(0x7A, 0xAB,0xEB),
    BROWN(0x8B, 0x45, 0x13);

    private Color color;

    Colors(int r, int g, int b){
        this.color = new Color(r,g,b);
    }

    public Color getColor() {
        return color;
    }
}
