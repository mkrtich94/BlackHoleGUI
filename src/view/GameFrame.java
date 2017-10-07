package view;

import controller.GameBoard;
import model.Colors;
import model.Tile;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GameFrame extends JFrame {
    private static final int HEIGHT = 800;
    private static final int WIDTH = 800;
    public GameBoard board;
    public MenuPanel menuPanel;
    public GamePanel gamePanel;

    public GameFrame() throws HeadlessException {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icon.png"));
        this.setIconImage(icon.getImage());
        this.setTitle("Black Hole");
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.white);
        this.getContentPane().add(this.gamePanel = new GamePanel(this));
        this.menuPanel = new MenuPanel(this);
        this.setGlassPane(this.menuPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.showMenu();
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new JMenu("Game"), 0);
        menuBar.getMenu(0).add(new JMenuItem("Restart"), 0);
        menuBar.getMenu(0).add(new JMenuItem("Menu"), 1);
        menuBar.getMenu(0).add(new JMenuItem("Exit"), 2);
        menuBar.getMenu(0).getItem(0).addActionListener((event)->{
            this.board.restart(this.board.playerColor.equals(Colors.RED.getColor()));
        });
        menuBar.getMenu(0).getItem(1).addActionListener((event)->{
            this.board.restart(true);
            this.showMenu();
        });
        menuBar.getMenu(0).getItem(2).addActionListener((event)->{
            this.dispose();
        });
        this.setJMenuBar(menuBar);
    }

    public void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("menu.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public void showMenu() {
        this.menuPanel.setVisible(true);
        this.playSound();
    }

    public void createBoard() {
        this.board = new GameBoard(this);
    }

    public void drawBoard(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        for (Tile tile : this.board.tileMap.values()) {
            graphics2D.setColor(tile.getColor());
            graphics2D.fill(tile.getShape());
            graphics2D.setColor(Color.BLACK);
            graphics2D.draw(tile.getShape());
            if(tile.getNumber() != null) {
                graphics2D.setFont(new Font(null, Font.BOLD, (int) (tile.getDiameter()*0.6)));
                int textWidth = graphics2D.getFontMetrics().stringWidth(tile.getText());
                int height = graphics2D.getFontMetrics().getHeight();
                graphics2D.drawString(tile.getText(), (float)(tile.getColumn() - (tile.getText().length() == 2 ? tile.getDiameter()*0.5 : 0) +textWidth), (float)(tile.getRow() + height));
            }
        }
    }
}
