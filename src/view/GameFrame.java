package view;

import controller.GameBoard;
import controller.Player;
import javafx.application.Platform;
import model.Colors;
import model.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GameFrame extends JFrame {
    private static final int HEIGHT = 700;
    private static final int WIDTH = 800;
    public GameBoard board;
    public MenuPanel menuPanel;
    public GamePanel gamePanel;
    private Player mediaPlayer;

    public GameFrame() throws HeadlessException {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icon.png")));
        this.setIconImage(icon.getImage());
        this.setTitle("Black Hole");
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.white);
        mediaPlayer = new Player();
        this.getContentPane().add(this.gamePanel = new GamePanel(this));
        this.menuPanel = new MenuPanel(this);
        this.setGlassPane(this.menuPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.showMenu();
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new JMenu("Game"), 0);
        menuBar.getMenu(0).add(new JMenuItem("Restart"), 0);
        menuBar.getMenu(0).add(new JMenuItem("Menu"), 1);
        menuBar.getMenu(0).add(new JMenuItem("Exit"), 2);
        menuBar.getMenu(0).getItem(0).addActionListener((event) -> this.board.restart(this.board.playerColor.equals(Colors.RED.getColor())));
        menuBar.getMenu(0).getItem(1).addActionListener((event) -> this.showMenu());
        menuBar.getMenu(0).getItem(2).addActionListener((event) -> {
            this.dispose();
            Platform.exit();
        });
        this.setJMenuBar(menuBar);
    }

    public void showMenu() {
        this.menuPanel.setVisible(true);
        this.getMediaPlayer().play();
    }

    public Player getMediaPlayer() {
        return this.mediaPlayer;
    }

    public void createBoard() {
        this.board = new GameBoard(this);
    }

    public void drawBoard(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        for (Tile tile : this.board.tileMap.values()) {
            Shape shape = tile.getShape();
            graphics2D.setStroke(new BasicStroke(1.5f));
            DrawingUtils.drawShapeWithBorder(graphics2D, shape, tile.getColor());
            if (tile.getNumber() != null) {
                graphics2D.setColor(tile.getColor().equals(Colors.RED.getColor()) ? Color.BLACK : Color.WHITE);
                DrawingUtils.drawCenteredString(graphics2D, tile.getNumber().toString(), shape.getBounds2D());
            }
        }
        board.getDiskPane().drawDisks(graphics2D);
//        board.getScorePane().showScore(graphics2D);
    }
}
