import controller.Core;
import view.GameFrame;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

    static GameFrame frame;
    static Map<String, Boolean> attributesMap;
    static Core core;

    public static void prepareAttributesMap(String[] attributes) {
        attributesMap = new HashMap<>();
        for(String attribute : attributes) {
            attributesMap.put(attribute.toLowerCase(), Boolean.TRUE);
        }
    }

    public static void main(String[] args) {
        prepareAttributesMap(args);
        if (Boolean.TRUE.equals(attributesMap.get("--no-gui"))) {
            core = new Core();
        } else {
            EventQueue.invokeLater(() -> {
                try {
                    startGame();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void startGame() {
        frame = new GameFrame();
        frame.createBoard();
        frame.setVisible(true);
    }

}
