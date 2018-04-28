package controller.impl;

import controller.Core;
import controller.GameBoard;
import view.Colors;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CoreImpl implements Core {

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

    public CoreImpl(Map<String, Boolean> attributes) {
        try {
            isWithGUI = !attributes.get("--no-gui");
            isJuryPresent = !attributes.get("--no-jury");
            prepareBoard();
            this.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CoreImpl(GameBoard gameBoard) {
        board = gameBoard;
        isWithGUI = true;
        try {
            prepareBoard();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isCommandValid(String command, int[] numbers) {
        String[] commandParts = command.split("=");
        return command.equals("Start") || command.equals("Quit") ||
                ((Integer.parseInt(commandParts[1]) < 16 && Integer.parseInt(commandParts[1]) > 0 && numbers[Integer.parseInt(commandParts[1])] != 0)
                        && (neighboursMap.get(Core.getIndexByLabel(commandParts[0])) != null && triangle[Core.getIndexByLabel(commandParts[0])] == 0));
    }

    private void play() throws IOException {
        String command;
        int numbers[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        while (!(command = reader.readLine()).startsWith("Q")) {
            String[] commandParts = command.split("=");
            if (isCommandValid(command, numbers)) {
                if (commandParts.length > 1) {
                    numbers[Integer.parseInt(commandParts[1])] = 0;
                }
                this.executeCommand(command);
            } else {
                this.writeToConsole("Quit");
            }
        }
        System.exit(0);
    }

    private void prepareNeighboursMap() {
        neighboursMap = new HashMap<>();
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8 - i; ++j) {
                int index = i * 8 + j;
                neighboursMap.computeIfAbsent(index, k -> new ArrayList<Integer>());
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
                this.registerBrownTile(Core.getIndexByLabel(reader.readLine()));
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
                String label = Core.getLabelByIndex(letter * 8 + number);
                if (isWithGUI) {
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
        if (command.equals("Quit")) {
            System.exit(0);
        }
    }

    private int getNeighbourWithMaximalPotentialRisk(int index) {
        int maxPotential = 99;
        int neighbourIndexWithMaxPotential = getEmptyNeighbour(index);
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

    /**
     * Used to get the smallest value bigger than the bound, or the biggest value that is smaller than the bound
     *
     * @param bound
     * @return
     */
    private int getMinAvailable(int bound) {
        int min = 99;
        int index = 99;
        for (int i = 0; i < itemNumbers.length; ++i) {
            if (itemNumbers[i] != 0 && itemNumbers[i] >= bound && min > itemNumbers[i]) {
                min = itemNumbers[i];
                index = i;
                if (itemNumbers[i] > bound) {
                    break;
                }
            }
        }
        if (index == 99 && bound != 0) {
            for (int i = bound > itemNumbers.length ? itemNumbers.length - 1 : bound - 1; i > 0; --i) {
                if (itemNumbers[i] != 0) {
                    min = itemNumbers[i];
                    index = i;
                    break;
                }
            }
        }
        itemNumbers[index] = 0;
        if (maxAvailable == min) {
            maxAvailable = 0;
        }
        return min;
    }

    private void attackCommand() {
        String command = findBestWinningStrategyAndAnswer();
        String[] commandParts = command.split("=");
        if (!isWithGUI) {
            if (!command.equals("Quit")) {
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
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8 - i; ++j) {
                if (triangle[8 * i + j] == 0) {
                    score = potentialValues[8 * i + j];
                    break;
                }
            }
        }
        if (this.color.equals(Colors.BLUE.getColor())) {
            return String.format("The score is : %d-%d", 75 - score, 75 + score);
        } else {
            return String.format("The score is : %d-%d", 75 + score, 75 - score);
        }
    }

    public void executeCommand(String command) {
        char s[];
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

    private int getEmptyNeighboursCount(int index) {
        int count = 0;
        if (neighboursMap.get(index) != null) {
            for (Integer neighbourIndex : neighboursMap.get(index)) {
                if (triangle[neighbourIndex] == 0) {
                    ++count;
                }
            }
        }
        return count;
    }

    private int getEmptyNeighbour(int index) {
        if (neighboursMap.get(index) != null) {
            for (Integer neighbourIndex : neighboursMap.get(index)) {
                if (triangle[neighbourIndex] == 0) {
                    return neighbourIndex;
                }
            }
        }
        return 99;
    }

    private int findTileWithHighestPotentialDifference(int value) {
        int indexOfMaximalDifference = 99;
        int difference = -99;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8 - i; ++j) {
                int index = i * 8 + j;
                int emptyNeighbours = 0;
                if (neighboursMap.get(index) == null || triangle[index] != 0) {
                    continue;
                }
                for (Integer integer : neighboursMap.get(index)) {
                    if (triangle[integer] == 0) {
                        emptyNeighbours++;
                    }
                }
                int differenceForIndex = emptyNeighbours * value - potentialValues[index];

                if (differenceForIndex > difference) {
                    difference = differenceForIndex;
                    indexOfMaximalDifference = index;
                }
            }
        }
        return indexOfMaximalDifference;
    }

    private String findBestWinningStrategyAndAnswer() {
        int value = getMaxAvailable(itemNumbers);
        if (value == 0) {
            return "Quit";
        }
        itemNumbers[maxAvailable - 1] = 0;

        ArrayList<Integer> lonelyTiles = new ArrayList<>();
        ArrayList<Integer> socialTiles = new ArrayList<>();

        for (Integer index : filterTilesByPotential(findLeastWinningPotential(), false)) {
            if (getEmptyNeighboursCount(index) == 0) {
                lonelyTiles.add(index);
            } else {
                socialTiles.add(index);
            }
        }

        if (socialTiles.size() == 0) { // TODO: subject for change
            if (!this.socialTilesExist()) {
                triangle[lonelyTiles.get(0)] = value;
                analyzeBoardForPotentialRisks();
                return Core.getLabelByIndex(lonelyTiles.get(0)) + "=" + value;
            } else {
                socialTiles = filterTilesByPotential(0, true);
                int index = this.getMostInfluentialTile(socialTiles, false);
                triangle[index] = value;
                analyzeBoardForPotentialRisks();
                return Core.getLabelByIndex(index) + "=" + value;
            }
        } else {
            int index = this.getMostInfluentialTile(socialTiles, false);
            triangle[index] = value;
            analyzeBoardForPotentialRisks();
            return Core.getLabelByIndex(index) + "=" + value;
        }
    }

    private int findLeastWinningPotential() {
        int leastPotentialIndex = 99;
        int leastPotential = 99;
        for (Integer key : neighboursMap.keySet()) {
            if (triangle[key] == 0 && leastPotential > potentialValues[key]) {
                leastPotentialIndex = key;
                leastPotential = potentialValues[key];
            }
        }
        if (leastPotentialIndex == 99) {
            System.exit(0);
        }
        return leastPotential;
    }

    private ArrayList<Integer> filterTilesByPotential(int potential, boolean onlyTilesWithEmptyNeighbours) {
        ArrayList<Integer> tiles = new ArrayList<>();
        int minPotentialWithNeighbour = 99;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8 - i; ++j) {
                int index = i * 8 + j;
                if (triangle[index] == 0) {
                    if (potentialValues[index] <= minPotentialWithNeighbour && onlyTilesWithEmptyNeighbours && this.getEmptyNeighboursCount(index) != 0) {
                        if (potentialValues[index] < minPotentialWithNeighbour) {
                            tiles = new ArrayList<>();
                            minPotentialWithNeighbour = potentialValues[index];
                        }
                        tiles.add(index);
                    }
                    if (!onlyTilesWithEmptyNeighbours && potential == potentialValues[index]) {
                        tiles.add(index);
                    }
                }

            }
        }
        return tiles;
    }

    private boolean socialTilesExist() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8 - i; ++j) {
                int index = i * 8 + j;
                if (triangle[index] == 0 && getEmptyNeighboursCount(index) != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getNeighboursRisk(int index) {
        int risk = 0;
        if (neighboursMap.get(index) != null) {
            for (Integer neighbourIndex : neighboursMap.get(index)) {
                if (triangle[neighbourIndex] == 0 && potentialValues[neighbourIndex] < 0) {
                    risk -= potentialValues[neighbourIndex];
                }
            }
        }
        return risk;
    }

    /**
     * Method is used to get the next command
     */
    private void getNextMove() {
        if (getMaxAvailable(itemNumbers) == 0) { // Quit
            if (!isWithGUI) {
                writeToConsole("Quit");
            } else {
                board.executeCommand(Color.BLACK, null, "Quit");
            }
            return;
        }
        int maxPotentialRisk = 99;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8 - i; ++j) {
                int index = i * 8 + j;
                if (maxPotentialRisk > potentialValues[index] && triangle[index] == 0) {
                    maxPotentialRisk = potentialValues[index];
                }
            }
        }
        if (maxPotentialRisk >= 0) { // attack
            attackCommand();
        } else { // defend
            this.defenseCommand(maxPotentialRisk);
        }
    }

    private Integer getMostInfluentialTile(ArrayList<Integer> socialTiles, boolean onlyNegatives) {
        int influentialIndex = socialTiles.get(0);
        int maxNeighboursCount = 1;
        int maxRiskyNeighbours = 99;
        for (Integer index : socialTiles) {
            int neighboursCount = getEmptyNeighboursCount(index);
            int neighbourRisk = 0;
            if (onlyNegatives) {
                neighbourRisk = getNeighboursRisk(index);
            } else {
                if (neighboursMap.get(index) != null) {
                    for (Integer neighbourIndex : neighboursMap.get(index)) {
                        if (triangle[neighbourIndex] == 0) {
                            neighbourRisk += potentialValues[neighbourIndex];
                        }
                    }
                }
            }
            if (neighboursCount > maxNeighboursCount) {
                maxNeighboursCount = neighboursCount;
                maxRiskyNeighbours = neighbourRisk;
                influentialIndex = index;
            } else if (neighboursCount == maxNeighboursCount && neighbourRisk > maxRiskyNeighbours) {
                maxNeighboursCount = neighboursCount;
                maxRiskyNeighbours = neighbourRisk;
                influentialIndex = index;
            }
        }
        return influentialIndex;
    }

    private void defenseCommand(int maxPotentialRisk) {
        ArrayList<Integer> lonelyTiles = new ArrayList<>();
        ArrayList<Integer> socialTiles = new ArrayList<>();

        for (Integer index : filterTilesByPotential(maxPotentialRisk, false)) {
            if (getEmptyNeighboursCount(index) == 0) {
                lonelyTiles.add(index);
            } else {
                socialTiles.add(index);
            }
        }

        if (socialTiles.size() == 0 || -maxPotentialRisk > this.maxAvailable) {

            int potentialRiskyIndex;
            int bound = 0;
            if (socialTiles.size() == 0) {
                potentialRiskyIndex = lonelyTiles.get(0);
            } else {
                potentialRiskyIndex = getMostInfluentialTile(socialTiles, true);
                int maxNeighboursCount = getEmptyNeighboursCount(potentialRiskyIndex);
                int maxNeighboursRisk = getNeighboursRisk(potentialRiskyIndex);
                bound = maxNeighboursRisk / maxNeighboursCount;
            }
            triangle[potentialRiskyIndex] = getMinAvailable(bound);
            analyzeBoardForPotentialRisks();
            if (!isWithGUI) {
                writeToConsole(Core.getLabelByIndex(potentialRiskyIndex) + "=" + triangle[potentialRiskyIndex]);
            } else {
                board.executeCommand(color, triangle[potentialRiskyIndex], Core.getLabelByIndex(potentialRiskyIndex));
            }
        } else {
            int potentialRiskyIndex = getMostInfluentialTile(socialTiles, true);
            int maxRiskyNeighbourIndex = potentialRiskyIndex;
            int maxNeighboursCount = getEmptyNeighboursCount(potentialRiskyIndex);
            int maxNeighboursRisk = getNeighboursRisk(potentialRiskyIndex);
            for (Integer index : neighboursMap.get(potentialRiskyIndex)) {
                if (triangle[index] == 0) {
                    int currentNeighboursCount = getEmptyNeighboursCount(index);
                    int currentPotential = potentialValues[index];
                    int currentNeighboursRisk = getNeighboursRisk(index);
                    if (currentPotential < maxPotentialRisk || maxNeighboursCount < currentNeighboursCount || maxNeighboursRisk < currentNeighboursRisk) {
                        maxPotentialRisk = currentPotential;
                        maxRiskyNeighbourIndex = index;
                        maxNeighboursCount = currentNeighboursCount;
                        maxNeighboursRisk = currentNeighboursRisk;
                    }
                }
            }
            maxPotentialRisk = potentialValues[this.getNeighbourWithMaximalPotentialRisk(maxRiskyNeighbourIndex)];
            int minRequired = getMinAvailable(-maxPotentialRisk);
            triangle[maxRiskyNeighbourIndex] = minRequired;
            analyzeBoardForPotentialRisks();
            if (!isWithGUI) {
                writeToConsole(Core.getLabelByIndex(maxRiskyNeighbourIndex) + "=" + minRequired);
            } else {
                board.executeCommand(color, minRequired, Core.getLabelByIndex(maxRiskyNeighbourIndex));
            }
        }
    }

}