package view;

import model.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class GamePanel extends JLayeredPane {
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
                for (Tile tile : frame.board.tileMap.values()) {
                    if (tile.getShape().contains(event.getPoint()) && frame.board.isPlayersTurn) {
                        if (!tile.getIsFilled()) {
                            frame.board.isPlayersTurn = false;
                            frame.board.answer(frame.board.playerColor, new Random().nextInt(14) + 1, tile.getLabel());
                        } else {
                            showMessage("Tile is already filled with another disc");
                        }
                        break;
                    }
                }
            }
        });
        this.add(new JButton("Next Song"), 0);
        this.getComponent(0).addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                super.mouseClicked(event);
                frame.getMediaPlayer().nextSong();
            }
        });
    }

    public void showMessage(String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(frame, "Message");
        dialog.setVisible(true);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dialog.setVisible(false);
    }

    public void paint(Graphics g) {
        frame.drawBoard(g);
    }

}
