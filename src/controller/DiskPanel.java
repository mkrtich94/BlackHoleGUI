package controller;

import view.Colors;
import model.Disk;
import view.DrawingUtils;

import java.awt.*;
import java.util.LinkedList;

class DiskPanel {

    LinkedList<Disk> remainingDisks;
    private GameFrame frame;

    DiskPanel(GameFrame frame) {
        this.frame = frame;
    }

    void removeSelected() {
        remainingDisks.remove(getSelected());
    }

    void init() {
        remainingDisks = new LinkedList<>();
        for(int i=0; i < 15; ++i) {
            remainingDisks.add(new Disk(frame.getBoard().playerColor, i+1));
        }
    }

    Disk getSelected() {
        for (Disk disk : remainingDisks) {
            if (disk.getIsSelected()) {
                return disk;
            }
        }
        return null;
    }

    void setSelected(Disk selectedDisk) {
        for (Disk disk : remainingDisks) {
            if (!disk.equals(selectedDisk)) {
                disk.setIsSelected(false);
            }
        }
        selectedDisk.setIsSelected(!selectedDisk.getIsSelected());
    }

    void drawDisks(Graphics2D graphics2D) {
        graphics2D.setStroke(new BasicStroke(1.0f));
        for (int i = 0; i < remainingDisks.size(); i++) {
            Disk disk = remainingDisks.get(i);
            Shape shape = disk.getShape(i, remainingDisks.size());
            DrawingUtils.drawShapeWithBorder(graphics2D, shape, disk.getColor());
            if (disk.getIsSelected() && Colors.RED.getColor().equals(disk.getColor()) || (!disk.getIsSelected() && !Colors.RED.getColor().equals(disk.getColor()))) {
                graphics2D.setColor(Color.WHITE);
            } else {
                graphics2D.setColor(Color.BLACK);
            }
            DrawingUtils.drawCenteredString(graphics2D, disk.getNumber().toString(), shape.getBounds2D());
        }
    }
}
