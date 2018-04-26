package view;

import java.awt.*;

public enum TextColor {
    BLACK(0x00, 0x00, 0x00) {
        @Override
        public TextColor invert() {
            return WHITE;
        }
    },
    WHITE(0xff, 0xff, 0xff) {
        @Override
        public TextColor invert() {
            return BLACK;
        }
    };
    private final Color color;

    TextColor(int r, int g, int b) {
        this.color = new Color(r, g, b);
    }

    public Color getColor() {
        return color;
    }

    public abstract TextColor invert();

}
