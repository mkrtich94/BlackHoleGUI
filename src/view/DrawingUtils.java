package view;

import model.Colors;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class DrawingUtils {

    public static void drawCenteredString(Graphics2D graphics2D, String text, Rectangle2D rectangle2D) {
        graphics2D.setFont(new Font(null, Font.BOLD, (int) (rectangle2D.getWidth() * 0.6)));
        FontMetrics metrics = graphics2D.getFontMetrics();
        double x = rectangle2D.getX() + (rectangle2D.getWidth() - metrics.stringWidth(text)) / 2;
        double y = rectangle2D.getY() + ((rectangle2D.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics2D.drawString(text, (float) x, (float) y);
    }

    public static void drawShapeWithBorder(Graphics2D graphics2D, Shape shape, Color color) {
        Color c = graphics2D.getColor();
        graphics2D.setColor(color);
        graphics2D.fill(shape);
        graphics2D.setColor(Colors.BORDER.getColor());
        graphics2D.draw(shape);
        graphics2D.setColor(c);
    }

}
