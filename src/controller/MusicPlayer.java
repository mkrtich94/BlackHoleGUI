package controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicPlayer {

    private List<File> playlist;
    private MediaPlayer player;
    private int currentSong;

    public MusicPlayer() {
        loadPlaylist();
        currentSong = -1;
    }

    public void play() {
        if (player == null || !player.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            playNext();
        }
    }

    public void playNext() {
        if (currentSong == playlist.size() - 1) {
            currentSong = 0;
        } else {
            currentSong++;
        }
        stop();
        Media hit = new Media(playlist.get(currentSong).toURI().toString());
        player = new MediaPlayer(hit);
        player.play();
        player.setOnEndOfMedia(this::playNext);
    }

    public void stop() {
        if(player != null) {
            player.stop();
            player.dispose();
        }
    }

    //TODO error on jar
    private void loadPlaylist() {
        playlist = new ArrayList<>();
        File[] files = new File[0];
        try {
            files = new File(getClass().getClassLoader().getResource("./").toURI()).listFiles(file -> file.getName().toLowerCase().endsWith(".mp3"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (files != null) {
            playlist.addAll(Arrays.asList(files));
        }
    }
}
