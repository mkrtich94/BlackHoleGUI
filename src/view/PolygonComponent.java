package view;

import model.Colors;
import model.TextColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class PolygonComponent extends JComponent {

    private Colors backgroundColor;
    private TextColor textColor;
    private boolean isSelected;
    private double outerRadius;
    private double innerRadius;

    private String text = "";

    public PolygonComponent(Colors backgroundColor, TextColor textColor, double radius, boolean isOuterRadius) {
        addPropertyChangeListener("select", evt -> setSelected((Boolean) evt.getNewValue()));
        if (isOuterRadius) {
            this.outerRadius = radius;
            this.innerRadius = outerRadius * Math.sqrt(3) / 2;
        } else {
            this.innerRadius = radius;
            this.outerRadius = innerRadius * 2 / Math.sqrt(3);
        }
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    return;
                }
                firePropertyChange("select", isSelected, !isSelected);
                repaint();
            }
        });
    }

    @Override
    public boolean contains(Point p) {
        return getShape().contains(p);
    }

    @Override
    public boolean contains(int x, int y) {
        return getShape().contains(x, y);
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public Color getBackgroundColor() {
        return backgroundColor.getColor();
    }

    public Color getTextColor() {
        return isSelected ? textColor.invert().getColor() : textColor.getColor();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        Shape shape = getShape();
        graphics2D.setColor(getBackgroundColor());
        graphics2D.fill(shape);
        graphics2D.setColor(Colors.BORDER.getColor());
        graphics2D.draw(shape);
        Rectangle2D rectangle2D = shape.getBounds2D();
        graphics2D.setFont(new Font(null, Font.BOLD, (int) (rectangle2D.getWidth() * 0.6)));
        FontMetrics metrics = graphics2D.getFontMetrics();
        double x = rectangle2D.getX() + (rectangle2D.getWidth() - metrics.stringWidth(text)) / 2;
        double y = rectangle2D.getY() + ((rectangle2D.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics2D.setColor(getTextColor());
        graphics2D.drawString(text, (float) x, (float) y);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Frame");
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        PolygonComponent component1 = new PolygonComponent(Colors.RED, TextColor.BLACK, 50, false);
        component1.setText("15");
        component1.setBounds(0, 40, 150, 150);
        PolygonComponent component2 = new PolygonComponent(Colors.BLUE, TextColor.WHITE, 50, false);
        component2.setText("7");
        component2.setBounds(100, 40, 150, 150);
        PolygonComponent component3 = new PolygonComponent(Colors.RED, TextColor.WHITE, 50, false);
        component3.setText("5");
        component3.setBounds(100, 40, 20, 20);


        frame.getContentPane().add(component1);
        frame.getContentPane().add(component2);
        frame.getContentPane().add(component3);
        frame.setVisible(true);
    }

    private Shape getShape() {
        Dimension dimension = this.getSize();
        double centerX = dimension.getWidth() / 2;
        double centerY = dimension.getHeight() / 2;
        int[] ordinates = new int[6];
        int[] abscissas = new int[6];

        abscissas[0] = (int) (centerX);
        ordinates[0] = (int) (centerY - outerRadius);

        abscissas[1] = (int) (centerX + innerRadius);
        ordinates[1] = (int) (centerY - outerRadius / 2);

        abscissas[2] = (int) (centerX + innerRadius);
        ordinates[2] = (int) (centerY + outerRadius / 2);

        abscissas[3] = (int) (centerX);
        ordinates[3] = (int) (centerY + outerRadius);

        abscissas[4] = (int) (centerX - innerRadius);
        ordinates[4] = (int) (centerY + outerRadius / 2);

        abscissas[5] = (int) (centerX - innerRadius);
        ordinates[5] = (int) (centerY - outerRadius / 2);

        return new Polygon(abscissas, ordinates, 6);
    }
}
