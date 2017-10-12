//package model;
//
//import javax.swing.*;
//import javax.swing.border.LineBorder;
//import java.awt.*;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.awt.event.MouseMotionListener;
//
//public class Disk extends JLayer {
//
//    private volatile int screenX = 0;
//    private volatile int screenY = 0;
//    private volatile int myX = 0;
//    private volatile int myY = 0;
//
//    private Color color;
//    private Integer number;
//
//    public Color getColor() {
//        return color;
//    }
//
//    public void setColor(Color color) {
//        this.color = color;
//    }
//
//    public Integer getNumber() {
//        return number;
//    }
//
//    public void setNumber(Integer number) {
//        this.number = number;
//    }
//
//    public Disk(Color color, Integer number) {
//
//        this.color = color;
//        this.number = number;
//
//        setBorder(new LineBorder(Color.BLACK, 1));
//        setBackground(color);
//        setBounds(0, 0, 100, 100);
//        setVisible(true);
//        setOpaque(false);
//
//        addMouseListener(new MouseListener() {
//
//            @Override
//            public void mouseClicked(MouseEvent e) {
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//                screenX = e.getXOnScreen();
//                screenY = e.getYOnScreen();
//
//                myX = getX();
//                myY = getY();
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//            }
//
//        });
//        addMouseMotionListener(new MouseMotionListener() {
//
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                int deltaX = e.getXOnScreen() - screenX;
//                int deltaY = e.getYOnScreen() - screenY;
//
//                setLocation(myX + deltaX, myY + deltaY);
//            }
//
//            @Override
//            public void mouseMoved(MouseEvent e) {
//            }
//
//        });
//    }
//
//}