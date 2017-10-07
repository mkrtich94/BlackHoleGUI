package controller;


import model.Colors;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Core {

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private byte[] triangle;
    private byte[] potentialValues;
    private int item;
    private int itemNumbers[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    private GameBoard board;
    private Color color = Colors.BLUE.getColor();
    private Map<Integer, ArrayList<Integer>> neighboursMap;


    Core(GameBoard gameBoard) {
        board = gameBoard;
        prepareBoard();
    }

    public static void main(String[] args) throws IOException {
    }

    private void prepareNeighboursMap() {
        neighboursMap = new HashMap<>();
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8 - i; ++j) {
                int index = i * 8 + j;
                neighboursMap.putIfAbsent(index, new ArrayList<>());
                int index1 = index + 8;
                int index2 = index + 1;
                int index3 = index + 7;
                if (i + 1 < 8 && j < 7 - i && !neighboursMap.get(index).contains(index1)) {
                    neighboursMap.get(index).add(index1);
                }
                if (j < 7 - i && !neighboursMap.get(index).contains(index2)) {
                    neighboursMap.get(index).add(index2);
                }
                if (i + 1 < 8 && j - 1 > 0 && !neighboursMap.get(index).contains(index3)) {
                    neighboursMap.get(index).add(index3);
                }
            }
        }
        for (Integer key : neighboursMap.keySet()) {
            for (Integer value : neighboursMap.get(key)) {
                if (!neighboursMap.get(value).contains(key)) {
                    neighboursMap.get(value).add(key);
                }
            }
        }
    }

    private void recalculatePotential(int index) {
        potentialValues[index] = 0;
        for (Integer neighbourIndex : neighboursMap.get(index)) {
            potentialValues[neighbourIndex] += triangle[index];
        }
    }

    private void prepareBoard() {
        triangle = new byte[64];
        potentialValues = new byte[64];
        prepareNeighboursMap();
        prepareBrownTiles();
    }

    private void prepareBrownTiles() {
        Random random = new Random();
        for (int i = 0; i < 5; ++i) {
            int letter = random.nextInt(7);
            int number = random.nextInt(7 - letter);
            if (triangle[letter * 8 + number] != 99) {
                triangle[letter * 8 + number] = 99;
                int index = letter * 8 + number;
                for (int value : neighboursMap.get(index)) {
                    neighboursMap.get(value).remove((Object) index);
                }
                neighboursMap.remove(index);
                board.notify(Colors.BROWN.getColor(), null, String.valueOf((char) ('A' + letter)) + String.valueOf((char) ('1' + number)));
            } else {
                i = i > 0 ? i - 1 : 0;
            }
        }
        shuffle();
    }

    private int[] getRiskyFive() {
        int riskyItems[] = new int[5];
        for(int n = 0; n < 5; ++n) {
            for(int i = 0; i < 8; ++i) {
                for(int j = 0; j < 8-i; ++j) {
//                    riskyItems
                }
            }
        }
        return  riskyItems;
    }

    private void answer() {
        String[] command = findEmpty().split("=");
        board.notify(color, command.length == 1 ? null : Integer.valueOf(command[1]), command[0]);
    }

    void notify(String command) {
        char s[] = null;
        int letter, number;
        if (command.startsWith("S")) {
            color = Colors.RED.getColor();
        } else if (command.startsWith("Q")) {
            return;
        } else {
            s = command.toCharArray();
            letter = s[0] - 'A';
            number = s[1] - '1';
            int index = letter * 8 + number;
            triangle[index] = Byte.parseByte(command.split("=")[1]);
            recalculatePotential(index);
        }
        answer();
    }

    private void shuffle() {
        Random random = new Random();
        for (int i = 0; i < 50; ++i) {
            int l = random.nextInt(14);
            int r = random.nextInt(14);
            int temp = itemNumbers[l];
            itemNumbers[l] = itemNumbers[r];
            itemNumbers[r] = temp;
        }
    }

    private String findEmpty() {
        int letter = 0;
        int z = 0;
        if (item == 15) {
            return "Quit";
        }
        int value = itemNumbers[item++];
        while (true) {
            letter = new Random().nextInt(7);
            for (; z < 8 - letter; ++z) {
                int index = 8 * letter + z;
                if (triangle[index] == 0) {
                    triangle[index] = (byte) -value;
                    recalculatePotential(index);
                    break;
                }
            }
            if (z != 8 - letter) break;
        }
        return String.valueOf((char) ('A' + letter)) + (z + 1) + "=" + value;

    }

}