package edu.virginia.engine.sound;
import java.io.*;
import sun.audio.*;

/**
 * Created by BrandonSangston on 2/22/17.
 */
public class SoundManager {

    public SoundManager () {

    }

    public void loadSoundEffect (String id, String filename) {
        try {
            InputStream in = new FileInputStream("resources" + File.separator + filename);
            AudioStream audioStream = new AudioStream(in);
            AudioPlayer.player.start(audioStream);
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found");
        } catch (java.io.IOException e) {
            System.out.println("IO Exception");
        }
    }

    public void playSoundEffect (String id) {

    }

    public void loadMusic (String id, String filename) {
        try {
            InputStream in = new FileInputStream("resources" + File.separator + filename);
            AudioStream audioStream = new AudioStream(in);
            AudioPlayer.player.start(audioStream);
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found");
        } catch (java.io.IOException e) {
            System.out.println("IO Exception");
        }
    }

    public void playMusic (String id) {

    }

}
