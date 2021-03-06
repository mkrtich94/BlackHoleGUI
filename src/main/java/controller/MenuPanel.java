package controller;

import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static java.awt.Image.SCALE_SMOOTH;

public class MenuPanel extends JPanel {

    private GameFrame frame;

    MenuPanel(GameFrame frame) {
        super();
        this.frame = frame;
        JButton start = new JButton("Start");
        Icon dialogIcon = new ImageIcon(frame.getIconImage().getScaledInstance(25, 25, SCALE_SMOOTH));
        Icon companyIcon = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("company.png"))).getImage().getScaledInstance(75, 50, SCALE_SMOOTH));
        start.addActionListener(e -> {
            Object[] options = {"Red",
                    "Blue"};
            int dialogResult = JOptionPane.showOptionDialog(frame,
                    "Do you want to play as Red or Blue?",
                    "Choose",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    dialogIcon,
                    options,
                    null);
            if (dialogResult == JOptionPane.YES_OPTION) {
                this.startGame(true);
            } else {
                this.startGame(false);
            }
        });
        JButton about = new JButton("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Property of Naymada Corp.",
                "About",
                JOptionPane.INFORMATION_MESSAGE,
                companyIcon));
        JButton exit = new JButton("Exit");
        exit.addActionListener(e -> {
            int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to leave the game?", "Confirm", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    dialogIcon);
            if (dialogResult == JOptionPane.YES_OPTION) {
                this.frame.dispose();
                Platform.exit();
            }
        });
        start.setAlignmentX(CENTER_ALIGNMENT);
        this.add(start);
        about.setAlignmentX(CENTER_ALIGNMENT);
        this.add(about);
        exit.setAlignmentX(CENTER_ALIGNMENT);
        this.add(exit);
    }

    private void startGame(boolean isPlayerFirst) {
        this.setVisible(false);
        frame.getBoard().start(isPlayerFirst);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
    }

}
