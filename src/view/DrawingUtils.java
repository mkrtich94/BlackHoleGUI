package view;

import model.Piece;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class DrawingUtils {

    public static void drawCenteredString(Graphics2D graphics2D, Piece piece, Rectangle2D rectangle2D) {
        graphics2D.setFont(new Font(null, Font.BOLD, (int) (rectangle2D.getWidth() * 0.6)));
        FontMetrics metrics = graphics2D.getFontMetrics();
        double x = rectangle2D.getX() + (rectangle2D.getWidth() - metrics.stringWidth(piece.getNumber().toString())) / 2;
        double y = rectangle2D.getY() + ((rectangle2D.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics2D.drawString(piece.getNumber().toString(), (float) x, (float) y);
    }

}
