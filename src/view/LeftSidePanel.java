package view;

import model.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class LeftSidePanel extends JPanel {
    public GameFrame frame;

    public LeftSidePanel(GameFrame frame) {
        super();
        this.frame = frame;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                super.mouseClicked(event);
                if(frame.menuPanel.isVisible()) {
                    return;
                }
                if(frame.board.isFinished) {
                    showMessage("Game Finished");
                    return;
                }
                for (Tile tile : frame.board.tileMap.values()) {
                    if (tile.getShape().contains(event.getPoint())) {
                        if(!tile.getIsFilled()) {
                            frame.board.answer(frame.board.playerColor, new Random().nextInt(14)+1, tile.getLabel());
                        } else {
                            showMessage("Tile is already filled with another disc");
                        }
                        break;
                    }
                }
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
//        g.drawPolygon(new int[]{0, 400, 800}, new int[]{800, 0, 800}, 3);
//
////        g.fillRoundRect(100, 100, 400, 400, 50, 50);
////        for (int i = 100; i <= 400; i += 100) {
////            for (int j = 100; j <= 400; j += 100) {
////                g.clearRect(i, j, 50, 50);
////            }
////        }
////
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j <= i; j++) {
//                if (i % 2 != 0) {
//                    g.fillOval(400-i*50, i * 100, 100, 100);
//                } else {
//                    g.fillOval(i * 100, i * 100, 100, 100);
//                }
//            }
//        }
    }
}
