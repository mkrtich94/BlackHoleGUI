import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.swing.*;

import controller.Core;
import controller.impl.CoreImpl;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import controller.GameFrame;

import static java.util.stream.Collectors.toMap;

public class Main {

    private static Map<String, Boolean> attributesMap;

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
            Core core = new CoreImpl(attributesMap);
        } else {
            final JFXPanel fxPanel = new JFXPanel();
            GameFrame frame = new GameFrame();
            frame.createBoard();
            frame.setVisible(true);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setLookAndFeel();
                    StackPane layout = new StackPane();
                    Scene scene = new Scene(layout, 800, 600);
                    fxPanel.setScene(scene);
                }
            });
        }
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
