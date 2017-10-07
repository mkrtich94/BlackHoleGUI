import view.GameFrame;

import java.awt.*;

public class Main {

    static GameFrame frame;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                startGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void startGame() {
        frame = new GameFrame();
        frame.createBoard();
        frame.setVisible(true);
    }

}
