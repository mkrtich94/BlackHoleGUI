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
    private int[] triangle;
    private int[] potentialValues;
    private int maxAvailable;
    private int itemNumbers[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    private GameBoard board;
    private Color color = Colors.BLUE.getColor();
    private Map<Integer, ArrayList<Integer>> neighboursMap;
    private boolean isWithGUI;
    private boolean isJuryPresent;

    public Core(Map<String, Boolean> attributes) {
        try {
            isWithGUI = !attributes.get("--no-gui");
            isJuryPresent = !attributes.get("--no-jury");
            prepareBoard();
            this.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isCommandValid(String command, int[] numbers) {
        String[] commandParts = command.split("=");
        if(command.equals("Start") || command.equals("Quit") ||
                ((Integer.parseInt(commandParts[1]) < 16 && Integer.parseInt(commandParts[1]) > 0 && numbers[Integer.parseInt(commandParts[1])] != 0)
                && (neighboursMap.get(getIndexByLabel(commandParts[0])) != null && triangle[getIndexByLabel(commandParts[0])] == 0))) {
            return true;
        }
        return false;
    }

    private void play() throws IOException {
        String command = "";
        int numbers[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        while (!(command = reader.readLine()).startsWith("Q")) {
            String[] commandParts = command.split("=");
            if(isCommandValid(command, numbers)) {
                if(commandParts.length > 1) {
                    numbers[Integer.parseInt(commandParts[1])] = 0;
                }
                this.executeCommand(command);
            } else {
                this.writeToConsole("Quit");
            }
        }
        System.exit(0);
    }

    Core(GameBoard gameBoard) {
        board = gameBoard;
        isWithGUI = true;
        try {
            prepareBoard();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareNeighboursMap() {
        neighboursMap = new HashMap<>();
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8 - i; ++j) {
                int index = i * 8 + j;
                if (neighboursMap.get(index) == null) {
                    neighboursMap.put(Integer.valueOf(index), new ArrayList<Integer>());
                }
                int index1 = index + 8;
                int index2 = index + 1;
                int index3 = index + 7;
                if (i + 1 < 8 && j < 7 - i && !neighboursMap.get(index).contains(index1)) {
                    neighboursMap.get(index).add(index1);
                }
                if (j < 7 - i && !neighboursMap.get(index).contains(index2)) {
                    neighboursMap.get(index).add(index2);
                }
                if (i + 1 < 8 && j - 1 >= 0 && !neighboursMap.get(index).contains(index3)) {
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

    private void analyzeBoardForPotentialRisks() {
        for (Integer index : neighboursMap.keySet()) {
            potentialValues[index] = 0;
            if (triangle[index] == 0) {
                for (Integer neighbourIndex : neighboursMap.get(index)) {
                    if (triangle[neighbourIndex] != 0) {
                        potentialValues[index] += triangle[neighbourIndex];
                    }
                }
            }
        }
    }

    private void prepareBoard() throws IOException {
        triangle = new int[64];
        potentialValues = new int[64];
        prepareNeighboursMap();
        if (isWithGUI || isJuryPresent) {
            prepareBrownTiles();
        } else {
            for (int i = 0; i < 5; ++i) {
                this.registerBrownTile(getIndexByLabel(reader.readLine()));
            }
        }
    }

    private void prepareBrownTiles() {
        Random random = new Random();
        for (int i = 0; i < 5; ++i) {
            int letter = random.nextInt(7);
            int number = random.nextInt(7 - letter);
            if (triangle[letter * 8 + number] != 99) {
                this.registerBrownTile(letter * 8 + number);
                String label = getLabelByIndex(letter * 8 + number);
                if(isWithGUI) {
                    board.executeCommand(Colors.JURY_BROWN.getColor(), null, label);
                } else {
                    writeToConsole(label);
                }
            } else {
                i = i > 0 ? i - 1 : 0;
            }
        }
    }

    private void registerBrownTile(int index) {
        if (index / 8 > 7 || index % 8 > 8 - index / 8 || triangle[index] == 99) {
            this.writeToConsole("Quit");
            System.exit(0);
        }
        triangle[index] = 99;
        for (int value : neighboursMap.get(index)) {
            neighboursMap.get(value).remove((Object) index);
        }
        neighboursMap.remove(index);
    }

    private void writeToConsole(String command) {
        System.out.println(command);
        System.out.flush();
        if(command.equals("Quit")) {
            System.exit(0);
        }
    }

    private void getNextMove() {
        if (getMaxAvailable(itemNumbers) == 0) { // Quit
            if(!isWithGUI) {
                writeToConsole("Quit");
            } else {
                board.executeCommand(Color.BLACK, null, "Quit");
            }
            return;
        }
        int maxPotential = 99;
        int potentialIndex = 99;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8 - i; ++j) {
                int index = i * 8 + j;
                if (maxPotential > potentialValues[index] && triangle[index] == 0) {
                    maxPotential = potentialValues[index];
                    potentialIndex = index;
                }
            }
        }
        if (maxPotential >= 0) {
            answer();
        } else if (maxPotential + getMaxAvailable(itemNumbers) > 0 && hasEmptyNeighbour(potentialIndex) != 99) {
            int minRequired = getMinAvailable(-maxPotential);
            int neighbourIndexToUse = getNeighbourWithMaximalPotentialRisk(potentialIndex);
            triangle[neighbourIndexToUse] = minRequired;
            analyzeBoardForPotentialRisks();
            if (!isWithGUI) {
                writeToConsole(getLabelByIndex(neighbourIndexToUse) + "=" + minRequired);
            } else {
                board.executeCommand(color, minRequired, getLabelByIndex(neighbourIndexToUse));
            }
        } else {
            triangle[potentialIndex] = getMinAvailable(0);
            analyzeBoardForPotentialRisks();
            if (!isWithGUI) {
                writeToConsole(getLabelByIndex(potentialIndex) + "=" + triangle[potentialIndex]);
            } else {
                board.executeCommand(color, triangle[potentialIndex], getLabelByIndex(potentialIndex));
            }
        }
    }

    private int getNeighbourWithMaximalPotentialRisk(int index) {
        int maxPotential = 99;
        int neighbourIndexWithMaxPotential = hasEmptyNeighbour(index);
        for (Integer neighbourIndexFirstLevel : neighboursMap.get(index)) {
            if (triangle[neighbourIndexFirstLevel] == 0) {
                for (Integer neighbourIndexSecondLevel : neighboursMap.get(neighbourIndexFirstLevel)) {
                    if (maxPotential > potentialValues[neighbourIndexSecondLevel] && index != neighbourIndexSecondLevel) {
                        maxPotential = potentialValues[neighbourIndexSecondLevel];
                        neighbourIndexWithMaxPotential = neighbourIndexFirstLevel;
                    }
                }
            }
        }

        return maxPotential > 0 ? index : neighbourIndexWithMaxPotential;
    }


    private int getMinAvailable(int bound) {
        int min = 99;
        for (int i = 0; i < itemNumbers.length; ++i) {
            if (itemNumbers[i] != 0 && itemNumbers[i] >= bound && min > itemNumbers[i]) {
                min = itemNumbers[i];
                itemNumbers[i] = 0;
                break;
            }
        }
        if (maxAvailable == min) {
            maxAvailable = 0;
        }
        return min;
    }

    private void answer() {
        String command = findBestWinningStrategyAndAnswer();
        String[] commandParts = command.split("=");
        if (!isWithGUI) {
            if(!command.equals("Quit")) {
                writeToConsole(command);
            } else {
                writeToConsole(this.getScore());
                System.exit(0);
            }
        } else {
            board.executeCommand(color, commandParts.length == 1 ? null : Integer.valueOf(commandParts[1]), commandParts[0]);
        }
    }

    private String getScore() {
        int score = 0;
        for(int i=0; i<8; ++i) {
            for(int j=0; j<8-i; ++j) {
                if(triangle[8*i+j]==0) {
                    score = potentialValues[8*i+j];
                    break;
                }
            }
        }
        if(this.color.equals(Colors.BLUE.getColor())) {
            return String.format("The score is : %d-%d", 75-score , 75+score);
        } else {
            return String.format("The score is : %d-%d", 75+score , 75-score);
        }
    }

    public void executeCommand(String command) {
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
            triangle[index] = -Integer.parseInt(command.split("=")[1]);
            analyzeBoardForPotentialRisks();
        }
        getNextMove();
    }

    private int getMaxAvailable(int[] array) {
        maxAvailable = array[0];
        for (int i = 1; i < array.length; ++i) {
            if (maxAvailable < array[i]) {
                maxAvailable = array[i];
            }
        }
        return maxAvailable;
    }

    private int hasEmptyNeighbour(int index) {
        if (neighboursMap.get(index) != null) {
            for (Integer neighbourIndex : neighboursMap.get(index)) {
                if (triangle[neighbourIndex] == 0) {
                    return neighbourIndex;
                }
            }
        }
        return 99;
    }

    private int findTileWithLowWinningPotentialAndEmptyNeighbour() {
        int leastWinningPotential = 99;
        int leastPotentialIndex = 99;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8 - i; ++j) {
                int index = i * 8 + j;
                if (potentialValues[index] < leastWinningPotential && hasEmptyNeighbour(index) != 99 && triangle[index] == 0) {
                    leastWinningPotential = potentialValues[index];
                    leastPotentialIndex = index;
                }
            }
        }
        return leastPotentialIndex;
    }

    private String findBestWinningStrategyAndAnswer() {
        int value = getMaxAvailable(itemNumbers);
        if (value == 0) {
            return "Quit";
        }
        itemNumbers[maxAvailable - 1] = 0;
        int index = findTileWithLowWinningPotentialAndEmptyNeighbour();
        if (index == 99) {
            return findEmptyTileWithLeastWinningPotential(value);
        } else {
            index = hasEmptyNeighbour(index);
            triangle[index] = value;
            analyzeBoardForPotentialRisks();
            return getLabelByIndex(index) + "=" + value;
        }

    }

    private String findEmptyTileWithLeastWinningPotential(int value) {
        int letter = 0;
        int z = 0;
        while (true) {
            letter = new Random().nextInt(7);
            for (z = 0; z < 8 - letter; ++z) {
                int index = 8 * letter + z;
                if (triangle[index] == 0) {
                    triangle[index] = value;
                    analyzeBoardForPotentialRisks();
                    break;
                }
            }
            if (z != 8 - letter) break;
        }
        return String.valueOf((char) ('A' + letter)) + (z + 1) + "=" + value;
    }

    private String getLabelByIndex(int index) {
        return String.valueOf(new char[]{(char) ('A' + index / 8), (char) ('1' + index % 8)});
    }

    private int getIndexByLabel(String label) {
        return (label.toCharArray()[0] - 'A')*8 + label.toCharArray()[1] - '1';
    }

}