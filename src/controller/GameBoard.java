package controller;

import model.Colors;
import model.Tile;
import view.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameBoard {
    private final GameFrame parentFrame;
    public Map<String, Tile> tileMap;
    public Map<String, ArrayList<Tile>> neighboursMap;
    public Color playerColor;
    public boolean isFinished;
    private Core game;
//    public ArrayList<Disk> remainingDisks;

    public GameBoard(GameFrame gameFrame) {
        this.parentFrame = gameFrame;
        Tile.setBoard(this);
        init();
    }

    public GameFrame getParentFrame() {
        return parentFrame;
    }

    private void init() {
        tileMap = new HashMap<>();
        neighboursMap = new HashMap<>();
        for (int i = 0; i < 8; ++i) {
            for (int j = i; j < 8; ++j) {
                String label = new StringBuilder().append((char) (i + 'A')).append((char) (7 - j + '1')).toString();
                tileMap.put(label, new Tile(label, 7 - j, 7 - j + i));
                neighboursMap.put(label, new ArrayList<Tile>());
            }
        }
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
        isFinished = false;
        init();
        start(isPlayerFirst);
    }

    void notify(Color color, Integer number, String label) {
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
            this.parentFrame.repaint();
            for (Tile tile : this.tileMap.values()) {
                if (!tile.getIsFilled()) {
                    tile.setColor(Color.BLACK);
                    int score = getScore(tile);
                    isFinished = true;
                    showScore(String.format("The Score is %d:%d", 75 + score, 75 - score));
                }
            }
            // computeScore
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
            isFinished = false;
            init();
            this.parentFrame.showMenu();
        }
    }

    private int getScore(Tile tile) {
        int score = 0;
        for (Tile neighbour : neighboursMap.get(tile.getLabel())) {
            score += neighbour.getColor().equals(Colors.RED.getColor()) ? neighbour.getNumber() : -neighbour.getNumber();
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

    public void answer(Color color, Integer number, String label) {
        Tile tile = tileMap.get(label);
        tile.setFilled(true);
        tile.setNumber(number);
        tile.setColor(color);
        game.notify(label + "=" + number);
    }

    public void start(boolean isPlayerFirst) {
        if (!isPlayerFirst) {
            this.playerColor = Colors.BLUE.getColor();
            this.game.notify("Start");
        } else {
            this.playerColor = Colors.RED.getColor();
        }
    }
}
