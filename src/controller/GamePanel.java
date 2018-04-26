package controller;

import model.Disk;
import model.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class GamePanel extends JPanel {
    public GameFrame frame;

    public GamePanel(GameFrame frame) {
        super();
        this.frame = frame;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                super.mouseClicked(event);
                if (frame.menuPanel.isVisible()) {
                    return;
                }
                if (frame.board.isFinished) {
                    showMessage("Game Finished");
                    return;
                }
                LinkedList<Disk> remainingDisks = frame.board.getDiskPane().remainingDisks;
                for (int i = 0; i < remainingDisks.size(); i++) {
                    Disk disk = remainingDisks.get(i);
                    if (disk.getShape(i).contains(event.getPoint()) && frame.board.isPlayersTurn) {
                        frame.board.getDiskPane().setSelected(disk);
                        frame.repaint();
                        return;
                    }
                }
                for (Tile tile : frame.board.tileMap.values()) {
                    if (tile.getShape().contains(event.getPoint()) && frame.board.isPlayersTurn) {
                        if (!tile.getIsFilled()) {
                            Disk selectedDisk = frame.board.getDiskPane().getSelected();
                            if (selectedDisk != null) {
                                frame.board.isPlayersTurn = false;
                                frame.board.answer(selectedDisk, tile.getLabel());
                            } else {
                                showMessage("Please select a disk");
                            }
                        } else {
                            showMessage("Tile is already filled with another disc");
                        }
                        return;
                    }
                }
            }
        });
    }

    public void showMessage(String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(frame, "Message");
        dialog.setVisible(true);
    }

    public void paintComponent(Graphics g) {
        frame.drawBoard(g);
    }

}
