package Utilities;
import java.io.File;
import javax.sound.sampled.*;

public class SoundPlayer {
    public static void playSound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
