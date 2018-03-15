package controller;

import model.Colors;
import model.Disk;
import view.DrawingUtils;

import java.awt.*;
import java.util.LinkedList;

public class DiskPane {

    public LinkedList<Disk> remainingDisks;

    DiskPane(GameBoard board) {
        remainingDisks = new LinkedList<>();
        for(int i=0; i< 15; ++i) {
            remainingDisks.add(new Disk(board.playerColor, i+1));
        }
    }

    void removeSelected() {
        remainingDisks.remove(getSelected());
        Disk.remaining = remainingDisks.size();
    }

    public Disk getSelected() {
        for (Disk disk : remainingDisks) {
            if (disk.getIsSelected()) {
                return disk;
            }
        }
        return null;
    }

    public void setSelected(Disk selectedDisk) {
        for (Disk disk : remainingDisks) {
            if (!disk.equals(selectedDisk)) {
                disk.setIsSelected(false);
            }
        }
        selectedDisk.setIsSelected(!selectedDisk.getIsSelected());
    }

    public void drawDisks(Graphics2D graphics2D) {
        graphics2D.setStroke(new BasicStroke(1.0f));
        for (int i = 0; i < remainingDisks.size(); i++) {
            Disk disk = remainingDisks.get(i);
            Shape shape = disk.getShape(i);
            DrawingUtils.drawShapeWithBorder(graphics2D, shape, disk.getColor());
            if (disk.getIsSelected() && disk.getColor().equals(Colors.RED.getColor()) || (!disk.getIsSelected() && !disk.getColor().equals(Colors.RED.getColor()))) {
                graphics2D.setColor(Color.WHITE);
            } else {
                graphics2D.setColor(Color.BLACK);
            }
            DrawingUtils.drawCenteredString(graphics2D, disk.getNumber().toString(), shape.getBounds2D());
        }
    }
}
