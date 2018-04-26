package view;

import java.awt.*;

public enum Colors {
    RED(0xE4, 0x33, 0x26),
    BLUE(0x2F, 0x41, 0xA5),
    JURY_BROWN(0x69, 0x45, 0x38),
    EMPTY(0xDD, 0xDD, 0xDD),
    BORDER(0x34, 0x32, 0x43);

    private Color color;

    Colors(int r, int g, int b) {
        this.color = new Color(r, g, b);
    }

    public Color getColor() {
        return color;
    }
}
