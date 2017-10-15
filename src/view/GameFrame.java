package view;

import controller.GameBoard;
import controller.MediaPlayer;
import model.Colors;
import model.Disk;
import model.Piece;
import model.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class GameFrame extends JFrame {
    private static final int HEIGHT = 800;
    private static final int WIDTH = 800;
    public GameBoard board;
    public MenuPanel menuPanel;
    public GamePanel gamePanel;
    private MediaPlayer mediaPlayer;

    public GameFrame() throws HeadlessException {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icon.png"));
        this.setIconImage(icon.getImage());
        this.setTitle("Black Hole");
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.white);
        mediaPlayer = new MediaPlayer();
//        this.setRootPane(this.gamePanel = new GamePanel(this));
//        this.getContentPane().add(new SidePane(this), BorderLayout.WEST);
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
        menuBar.getMenu(0).getItem(0).addActionListener((event) -> {
            this.board.restart(this.board.playerColor.equals(Colors.RED.getColor()));
        });
        menuBar.getMenu(0).getItem(1).addActionListener((event) -> {
            this.board.restart(true);
            this.showMenu();
        });
        menuBar.getMenu(0).getItem(2).addActionListener((event) -> {
            this.dispose();
        });
        this.setJMenuBar(menuBar);
    }

    public void showMenu() {
        this.menuPanel.setVisible(true);
//        this.getMediaPlayer().play(1);
    }

    public MediaPlayer getMediaPlayer() {
        return this.mediaPlayer;
    }

    public void createBoard() {
        this.board = new GameBoard(this);
    }

    public void drawCenteredString(Graphics2D graphics2D, Piece piece , Rectangle2D rectangle2D) {
        graphics2D.setFont(new Font(null, Font.BOLD, (int) (rectangle2D.getWidth() * 0.6)));

        FontMetrics metrics = graphics2D.getFontMetrics();

        // Determine the X coordinate for the text
        double x = rectangle2D.getX() + (rectangle2D.getWidth() - metrics.stringWidth(piece.getNumber().toString())) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        double y = rectangle2D.getY() + ((rectangle2D.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        // Draw the String
        graphics2D.drawString(piece.getNumber().toString(), (float)x, (float)y);
    }

    public void drawBoard(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        for (Tile tile : this.board.tileMap.values()) {
            Shape shape = tile.getShape();
            graphics2D.setColor(tile.getColor());
            graphics2D.fill(shape);
            graphics2D.setStroke(new BasicStroke(1.5f));
            graphics2D.setColor(Color.BLACK);
            graphics2D.draw(shape);
            if (tile.getNumber() != null) {
                graphics2D.setFont(new Font(null, Font.BOLD, (int) (tile.getDiameter() * 0.6)));
                int textWidth = graphics2D.getFontMetrics().stringWidth(tile.getNumber().toString());
                int height = graphics2D.getFontMetrics().getHeight();
                drawCenteredString(graphics2D, tile, shape.getBounds2D());
//                .getNumber().toString(), (float) (shape.getBounds2D().getCenterX() - textWidth/2), (float) (shape.getBounds2D().getCenterY() + height/2));
            }
        }
        int i = 0;
        for (Disk disk : this.board.getDiskPane().remainingDisks) {
            graphics2D.setColor(disk.getColor());
            Shape shape = disk.getShape(i);
            graphics2D.fill(shape);
            if(disk.getIsSelected()) {
                graphics2D.setColor(Color.white);
            } else {
                graphics2D.setColor(Color.BLACK);
            }
            graphics2D.setStroke(new BasicStroke(1.0f));
            graphics2D.draw(shape);
            graphics2D.setFont(new Font(null, Font.BOLD, (int) (disk.getDiameter() * 0.6)));
            int textWidth = graphics2D.getFontMetrics().stringWidth(disk.getNumber().toString());
            int height = graphics2D.getFontMetrics().getHeight();
            drawCenteredString(graphics2D, disk, shape.getBounds2D());
//            graphics2D.drawString(disk.getNumber().toString(), (float) (shape.getBounds2D().getCenterX() - textWidth/2), (float) (shape.getBounds2D().getCenterY() + height/2));
            i++;
        }
    }
}
