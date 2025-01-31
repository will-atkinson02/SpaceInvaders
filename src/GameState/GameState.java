package GameState;

public class GameState {
    //board
    public int tileSize;
    public int rows;
    public int cols;
    public int boardHeight = tileSize*rows;
    public int boardWidth = tileSize*cols;

    public int score;
    public int level;
    public boolean gameOver;

    public GameState() {
        this.tileSize = 32;
        this.rows = 22;
        this.cols = 30;
        this.boardHeight = this.tileSize*rows;
        this.boardWidth = this.tileSize*cols;

        this.score = 0;
        this.level = 1;
        this.gameOver = false;
    }

    public void levelUp() {
        this.level += 1;
        this.score += this.level*200;
    }
}
