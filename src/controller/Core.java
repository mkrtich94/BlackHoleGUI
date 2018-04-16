package controller;

public interface Core {

    void executeCommand(String command);

    // _______________________________ UTILS ______________________________ //

    static String getLabelByIndex(int index) {
        return String.valueOf(new char[]{(char) ('A' + index / 8), (char) ('1' + index % 8)});
    }

    static int getIndexByLabel(String label) {
        return (label.toCharArray()[0] - 'A') * 8 + label.toCharArray()[1] - '1';
    }

}