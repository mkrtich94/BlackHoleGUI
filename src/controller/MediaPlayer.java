package controller;

import com.sun.media.sound.DirectAudioDeviceProvider;

import javax.sound.sampled.*;
import javax.sound.sampled.spi.MixerProvider;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaPlayer {

    List<AudioInputStream> playlist;
    Clip clip;
    int currentSong;
    int state; // 0 if stopped, 1 if playing

    public MediaPlayer() {
        loadPlaylist();
    }

    public void nextSong() {
        stop();
        if(currentSong == playlist.size()-2) {
            currentSong = 0;
        } else {
            currentSong++;
        }
        play();
    }

    public void previousSong() {
        clip.stop();
        if(currentSong == 0) {
            currentSong = playlist.size()-2;
        } else {
            currentSong--;
        }
        clip.start();
    }

    public void play() {
        try {
            clip.open(playlist.get(currentSong));
            clip.setFramePosition(0);
            clip.start();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        clip.close();
        clip.stop();
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    void loadPlaylist() {
        playlist = new ArrayList<AudioInputStream>();
        try {
            this.clip = AudioSystem.getClip();
            for(int i = 1; i < new File("resources"+ File.separator+"music").listFiles().length; ++i) {
                playlist.add(AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("music/" + i + ".wav")));
            }
            playlist.add(AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("music/extras.wav")));
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play(int song) {
        stop();
        currentSong = song%playlist.size();
        play();
    }

}
