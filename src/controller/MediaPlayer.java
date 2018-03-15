package controller;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MediaPlayer {

    private List<File> playlist;
    private Clip clip;
    private int currentSong;

    public MediaPlayer() {
        loadPlaylist();
        currentSong = 0;
    }

    public void playNext() {
        stop();
        if (currentSong == playlist.size()-1) {
            currentSong = 0;
        } else {
            currentSong++;
        }
        play(playlist.get(currentSong));
    }

    public void playPrevious() {
        stop();
        if (currentSong == 0) {
            currentSong = playlist.size() - 1;
        } else {
            currentSong--;
        }
        play(playlist.get(currentSong));
    }

    public void play() {
        if(!clip.isOpen()) {
            play(playlist.get(currentSong));
        }
    }

    private void stop() {
        clip.close();
        clip.stop();
    }

    public void play(File file) {
        try {
            stop();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.addLineListener(event -> {
                if(event.getType().equals(LineEvent.Type.STOP) && event.getFramePosition() == MediaPlayer.this.clip.getFramePosition()) {
                    MediaPlayer.this.playNext();
                }
            });
            clip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPlaylist() {
        playlist = new ArrayList<>();
        try {
            this.clip = AudioSystem.getClip();
            File[] files = new File(getClass().getClassLoader().getResource("").toURI()).listFiles(file -> file.getName().toLowerCase().endsWith(".wav"));
            playlist.addAll(Arrays.asList(files));
        } catch (LineUnavailableException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
