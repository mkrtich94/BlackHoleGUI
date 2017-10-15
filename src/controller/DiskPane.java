package controller;

import model.Disk;

import java.util.LinkedList;

public class DiskPane {

    public LinkedList<Disk> remainingDisks;

    public DiskPane(GameBoard board) {
        remainingDisks = new LinkedList<>();
        for(int i=0; i< 15; ++i) {
            remainingDisks.add(new Disk(board.playerColor, i+1));
        }
    }

    public void removeDisk() {
        remainingDisks.remove(getSelected());
        Disk.remaining--;
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
}
