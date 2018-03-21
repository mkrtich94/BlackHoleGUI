import controller.Core;
import view.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class Main {

    private static GameFrame frame;
    private static Map<String, Boolean> attributesMap;
    private static Core core;

    public static void prepareAttributesMap(String[] attributes) {
        attributesMap = new HashMap<>();
        attributesMap.put("--no-gui", Boolean.FALSE);
        attributesMap.put("--no-jury", Boolean.FALSE);
        for (String attribute : attributes) {
            attributesMap.put(attribute.toLowerCase(), Boolean.TRUE);
        }
    }

    public static void main(String[] args) {
        prepareAttributesMap(args);
        if (Boolean.TRUE.equals(attributesMap.get("--no-gui"))) {
            core = new Core(attributesMap);
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
        setLookAndFeel();
        frame = new GameFrame();
        frame.createBoard();
        frame.setVisible(true);
    }

    private static void setLookAndFeel() {
        Map<String, UIManager.LookAndFeelInfo> lookAndFeels = Arrays.stream(UIManager.getInstalledLookAndFeels())
                .collect(toMap(UIManager.LookAndFeelInfo::getName, Function.identity()));

        String lafClassName = UIManager.getSystemLookAndFeelClassName();

        if (System.getProperty("os.name").contains("Linux") && lookAndFeels.containsKey("GTK+")) {
            lafClassName = lookAndFeels.get("GTK+").getClassName();
        }

        try {
            UIManager.setLookAndFeel(lafClassName);
        } catch (ClassNotFoundException e) {
            System.out.println("GTK+ class not found");
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.out.println("cannot instantiate GTK+ class");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("Access exception");
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            //
        }
    }

}
