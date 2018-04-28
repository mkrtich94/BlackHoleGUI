package controller;

import model.Disk;
import model.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class GamePanel extends JPanel {

    private GameFrame frame;

    GamePanel(GameFrame frame) {
        super();
        this.frame = frame;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                super.mouseClicked(event);
                if (frame.getMenuPanel().isVisible()) {
                    return;
                }
                if (frame.getBoard().isFinished) {
                    showMessage("Game Finished");
                    return;
                }
                LinkedList<Disk> remainingDisks = frame.getDiskPanel().remainingDisks;
                for (int i = 0; i < remainingDisks.size(); i++) {
                    Disk disk = remainingDisks.get(i);
                    if (disk.getShape(i, remainingDisks.size()).contains(event.getPoint()) && frame.getBoard().isPlayersTurn) {
                        frame.getBoard().getDiskPanel().setSelected(disk);
                        frame.repaint();
                        return;
                    }
                }
                for (Tile tile : frame.getBoard().tileMap.values()) {
                    if (tile.getShape().contains(event.getPoint()) && frame.getBoard().isPlayersTurn) {
                        if (tile.isEmpty()) {
                            Disk selectedDisk = frame.getDiskPanel().getSelected();
                            if (selectedDisk != null) {
                                frame.getBoard().isPlayersTurn = false;
                                frame.getBoard().answer(selectedDisk, tile.getLabel());
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

    private void showMessage(String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(frame, "Message");
        dialog.setVisible(true);
    }

    public void paintComponent(Graphics g) {
        frame.getBoard().drawBoard(g);
    }

}
