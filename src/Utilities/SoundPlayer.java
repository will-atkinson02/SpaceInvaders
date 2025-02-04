package Utilities;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {
    private static Map<String, Clip> clips = new HashMap<>();
    
    public static void playSound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);


            

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(1/2);

            clip.start();

            clips.put(soundFilePath, clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playLoop(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(1/2);

            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

            clips.put(soundFilePath, clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopSound(String soundFilePath) {
        Clip clip = clips.get(soundFilePath);
        if (clip != null) {
            clip.stop();
            clips.remove(soundFilePath);
        }
    }
}