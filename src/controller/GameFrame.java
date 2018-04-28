package controller;

import javafx.application.Platform;
import view.Colors;
import model.Tile;
import view.DrawingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GameFrame extends JFrame {
    private static final int HEIGHT = 700;
    private static final int WIDTH = 800;

    private GameBoard board;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private MusicPlayer musicPlayer;
    private DiskPanel diskPanel;

    public GameFrame() throws HeadlessException {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icon.png")));
        this.setIconImage(icon.getImage());
        this.setTitle("Black Hole");
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.white);
        musicPlayer = new MusicPlayer();
        this.initializePanels();
        this.getContentPane().add(this.gamePanel);
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
        this.createBoard();
        this.setVisible(true);
    }

    private void initializePanels() {
        this.gamePanel = new GamePanel(this);
        this.menuPanel = new MenuPanel(this);
        this.diskPanel = new DiskPanel(this);
    }

    void showMenu() {
        this.menuPanel.setVisible(true);
        this.getMusicPlayer().play();
    }

    private MusicPlayer getMusicPlayer() {
        return this.musicPlayer;
    }

    private void createBoard() {
        this.board = new GameBoard(this);
    }

    DiskPanel getDiskPanel() {
        return this.diskPanel;
    }

    GameBoard getBoard() {
        return board;
    }

    MenuPanel getMenuPanel() {
        return menuPanel;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
