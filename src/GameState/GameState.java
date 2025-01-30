package GameState;

public class GameState {
    //board
    public int tileSize = 32;
    public int rows = 22;
    public int cols = 30;
    public int boardHeight = tileSize*rows;
    public int boardWidth = tileSize*cols;

    public int score = 0;
    public int level = 0;
    public boolean gameOver;

    public GameState() {
        this.tileSize = 32;
        this.rows = 22;
        this.cols = 30;
        this.boardHeight = this.tileSize*rows;
        this.boardWidth = this.tileSize*cols;

        this.score = 0;
        this.level = 0;
        this.gameOver = false;
    }
}
