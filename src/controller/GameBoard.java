package controller;

import model.Colors;
import model.Disk;
import model.Tile;
import view.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import model.Disk;

public class GameBoard {
    private final GameFrame parentFrame;
    public Map<String, Tile> tileMap;
    public Map<String, ArrayList<Tile>> neighboursMap;
    public Color playerColor;
    public boolean isFinished;
    public boolean isPlayersTurn;
    private Core game;
    private DiskPane diskPane;

    public GameBoard(GameFrame gameFrame) {
        this.parentFrame = gameFrame;
    }

    public GameFrame getParentFrame() {
        return parentFrame;
    }

    private void init() {
        Tile.setBoard(this);
        Disk.setBoard(this);
        Disk.remaining = 15;
        tileMap = new HashMap<>();
        neighboursMap = new HashMap<>();
        for (int i = 0; i < 8; ++i) {
            for (int j = i; j < 8; ++j) {
                String label = new StringBuilder().append((char) (i + 'A')).append((char) (7 - j + '1')).toString();
                tileMap.put(label, new Tile(label, 7 - j, 7 - j + i));
                neighboursMap.put(label, new ArrayList<Tile>());
            }
        }
        this.diskPane = new DiskPane(this);
        prepareNeighboursMap();
        this.game = new Core(this);
    }

    private void prepareNeighboursMap() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8 - i; ++j) {
                String label = new StringBuilder().append((char) (i + 'A')).append((char) ('1' + j)).toString();
                char info[] = label.toCharArray();
                String label1 = String.valueOf(new char[]{(char) (info[0] + 1), info[1]});
                String label2 = String.valueOf(new char[]{info[0], (char) (info[1] + 1)});
                String label3 = String.valueOf(new char[]{(char) (info[0] + 1), (char) (info[1] - 1)});
                if (tileMap.get(label1) != null && !neighboursMap.get(label).contains(tileMap.get(label1))) {
                    neighboursMap.get(label).add(tileMap.get(label1));
                }
                if (tileMap.get(label2) != null && !neighboursMap.get(label).contains(tileMap.get(label2))) {
                    neighboursMap.get(label).add(tileMap.get(label2));
                }
                if (tileMap.get(label3) != null && !neighboursMap.get(label).contains(tileMap.get(label3))) {
                    neighboursMap.get(label).add(tileMap.get(label3));
                }
            }
        }
        for (String key : neighboursMap.keySet()) {
            for (Tile t : neighboursMap.get(key)) {
                if (!neighboursMap.get(t.getLabel()).contains(tileMap.get(key))) {
                    neighboursMap.get(t.getLabel()).add(tileMap.get(key));
                }
            }
        }
    }

    public void restart(boolean isPlayerFirst) {
        start(isPlayerFirst);
    }

    void executeCommand(Color color, Integer number, String label) {
        if (!label.equals("Quit")) {
            Tile tile = tileMap.get(label);
            tile.setFilled(true);
            tile.setNumber(number);
            tile.setColor(color);
            if (number == null) {
                for (Tile t : neighboursMap.get(label)) {
                    neighboursMap.get(t.getLabel()).remove(tile);
                }
                neighboursMap.remove(label);
            }
            this.parentFrame.repaint();
        }
        if (label.equals("Quit") || this.checkGameEnded()) {
            for (Tile tile : this.tileMap.values()) {
                if (!tile.getIsFilled()) {
                    tile.setColor(Color.BLACK);
                    isFinished = true;
                    this.parentFrame.repaint();
                    break;
                }
            }
        }
        this.isPlayersTurn = true;
        if (isFinished) {
            int score = getScore();
            showScore(String.format("The Score is %d:%d", 75 + score, 75 - score));
        }
    }

    private void showScore(String score) {

        int dialogResult = JOptionPane.showConfirmDialog(this.parentFrame,
                score + "\nDo you want to play again?",
                "GameFinished",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (dialogResult == JOptionPane.YES_OPTION) {
            Object[] options = {"Red",
                    "Blue"};
            int result = JOptionPane.showOptionDialog(this.parentFrame,
                    "Which Color?",
                    "Choose",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options, JOptionPane.YES_OPTION);
            this.restart(result == JOptionPane.YES_OPTION);
        } else {
            this.parentFrame.showMenu();
        }
    }

    private int getScore() {
        int score = 0;
        for (Tile tile : tileMap.values()) {
            if (tile.getColor().equals(Color.BLACK)) {
                for (Tile neighbour : neighboursMap.get(tile.getLabel())) {
                    if (neighbour.getNumber() != null) {
                        score += neighbour.getColor().equals(Colors.RED.getColor()) ? neighbour.getNumber() : -neighbour.getNumber();
                    }
                }
                break;
            }
        }
        return score;
    }

    private boolean checkGameEnded() {
        int emptyTiles = 0;
        for (Tile tile : this.tileMap.values()) {
            if (!tile.getIsFilled()) {
                emptyTiles++;
            }
        }
        return emptyTiles < 2;
    }

    public void answer(Disk disk, String label) {
        this.diskPane.removeSelected();
        Tile tile = tileMap.get(label);
        tile.setFilled(true);
        tile.setNumber(disk.getNumber());
        tile.setColor(disk.getColor());
        this.getParentFrame().repaint();
        game.executeCommand(label + "=" + disk.getNumber());
    }

    public void start(boolean isPlayerFirst) {
        isPlayersTurn = isPlayerFirst;
        this.playerColor = !isPlayerFirst ? Colors.BLUE.getColor() : Colors.RED.getColor();
        isFinished = false;
        init();
        if (!isPlayerFirst) {
            this.game.executeCommand("Start");
        }
    }

    public DiskPane getDiskPane() {
        return diskPane;
    }

}
