import java.io.*;

public class HighScore {
    String FILE_NAME = "highscore.txt";

    public int readHighScore() {
        int highscore = 0;

        try {
            File file = new File(FILE_NAME);

            if (!file.exists()) {
                file.createNewFile();
                return highscore;
            }

            BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"));

            String line = reader.readLine();
            if (line != null) {
                highscore = Integer.parseInt(line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return highscore;
    }

    public void writeHighScore(int highscore) {
        try {
            File file = new File(FILE_NAME);

            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(String.valueOf(highscore));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}