import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

import Controllers.*;
import Entities.*;
import GameState.GameState;
import Rendering.*;
import Utilities.*;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {
    GameState gs;
    ImageLoader images;

    Ship ship;
    AlienArray alienArray;
    UFOController ufoController;

    BulletController bulletController;
    ZController zController;
    TController tController;

    JPanel titleOverlay;
    JPanel playAgainOverlay;

    Timer gameLoop;
    long lastTime = System.nanoTime();
    double TARGET_FPS = 60.0;
    double NANOSECONDS_IN_SECOND = 1000000000.0;

    HighScore highScoreManager = new HighScore();
    int highscore = highScoreManager.readHighScore();

    Font customFont = FontLoader.loadFont("visitor1.ttf");

    SpaceInvaders(GameState gs) {
        this.gs = gs;

        // window settings
        setPreferredSize(new Dimension(gs.boardWidth, gs.boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        initialiseTitleModalOverlay();
        showModalOverlay(titleOverlay);

        images = new ImageLoader();

        alienArray = new AlienArray();
        alienArray.createAliens(gs, images.squidImgs, images.crabImgs, images.octopusImgs);
        ship = new Ship(gs, images.shipImgs);

        ufoController = new UFOController();

        tController = new TController();
        zController = new ZController();
        bulletController = new BulletController();

        gameLoop = new Timer(1000 / 60, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        long currentTime = System.nanoTime();
        double deltaTime = (currentTime - lastTime) / NANOSECONDS_IN_SECOND;  // Time in seconds
        lastTime = currentTime;

        updateGame(deltaTime);
    }

    public void updateGame(double deltaTime) {
        alienArray.alienExplosionTimer();
        alienArray.countAliveSquids();
        handleAlienProjectiles();

        ship.reload();
        ship.handleShipHit(gs, alienArray, tController, zController);
        ship.allowShipControl(gs.boardWidth);

        ufoController.handle(gs, images.ufoImgs);

        moveAllEntities();
        checkNextLevel();
        checkGameOver();

        repaint();
    }

    public void initialiseTitleModalOverlay() {
        initialiseJPanel("SPACE INVADERS", "Play", true);
    }

    public void initialisePlayAgainOverlay() {
        String text = "Score: " + Integer.toString(gs.score);
        initialiseJPanel(text, "Play again", false);
    }

    private void initialiseJPanel(String labelText, String buttonText, boolean isTitle) {
        JPanel modal = CreateJComponents.createJPanel(gs.boardWidth, gs.boardHeight);

        JLabel titleLabel = CreateJComponents.createTitleLabel(labelText, customFont);
        JButton startButton = CreateJComponents.createJButton(buttonText, customFont, isTitle);
        startButton.addActionListener(e -> handleButtonAction(buttonText, isTitle));

        CreateJComponents.setLayout(modal, titleLabel, startButton);

        if (isTitle) {
            this.titleOverlay = modal;
        } else {
            this.playAgainOverlay = modal;
        }
    }

    public void handleButtonAction(String buttonText, boolean isTitle) {
        if (isTitle) {
            removeModalOverlay(this.titleOverlay);
            gameLoop.start();
        } else {
            removeModalOverlay(this.playAgainOverlay);
            restartGame();
            
        }
    }

    private void showModalOverlay(JPanel modal) {
        add(modal);
        repaint();
        revalidate();
    }

    private void removeModalOverlay(JPanel modal) {
        remove(modal);
        repaint();
        revalidate();
        playAgainOverlay = null;
    }

    public void clearEntities() {
        alienArray.alienArray.clear();
        bulletController.bulletArray.clear();
        tController.tArray.clear();
        zController.zProjectile = null;
        ufoController.ufo = null;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // render Entities
        g.drawImage(ship.imageArray.get(ship.shipSpriteState), ship.x, ship.y, ship.width, ship.height, null);
        EntityRenderer.renderAliens(g, alienArray.alienArray, alienArray.explodedAlienIndex, alienArray.explosionTimer,
                alienArray.alienSpriteState);

        EntityRenderer.renderUFO(g, ufoController, customFont, images.ufoImg);

        EntityRenderer.renderZProjectile(g, zController.zProjectile, gs.tileSize);
        EntityRenderer.renderTProjectile(g, tController.tArray, gs.tileSize);
        EntityRenderer.renderBullets(g, bulletController.bulletArray);

        // render HUD
        HUDRenderer.renderHUD(g, customFont, gs, highscore, ship.lives, images.shipImgA, ship.width, ship.height);
    }

    public void handleAlienProjectiles() {
        if (alienArray.aliveSquidCount > 0) {
            if (zController.zProjectile != null) {
                zController.zProjectile.shuffleZConvolution();
            } else if (zController.allowSpawn) {
                zController.handleZSpawn(gs, new Random(), alienArray, alienArray.aliveSquidCount);
            }

            if (!tController.tArray.isEmpty()) {
                tController.handleAllTAnimations();
            }
            tController.handleTProjectileSpawn(gs, new Random(), alienArray);
        }
    }

    public void moveAllEntities() {
        alienArray.alienMovement(gs, ship, ufoController);

        zController.handleZMovement(gs, ship);
        tController.handleTMovement(gs, ship);
        bulletController.handleMovement(gs, alienArray, ship, ufoController.ufo);

        bulletController.clearBullets();
    }

    public void checkNextLevel() {
        if (alienArray.alienCount == 0 && ufoController.ufo == null) {
            clearEntities();

            gs.levelUp();

            ship.shipSpriteState = 0;

            alienArray.alienSpriteTimer = 0;
            alienArray.alienStepRate = 24 - gs.level;
            if (alienArray.alienVelocityX < 0) {
                alienArray.alienVelocityX *= -1;
            }
            alienArray.createAliens(gs, images.squidImgs, images.crabImgs, images.octopusImgs);

            ufoController.ufo = null;

            if (tController.spawnInterval > 0) {
                tController.spawnInterval -= gs.level;
            }
            if (zController.spawnInterval > 0) {
                zController.spawnInterval -= gs.level;
            }
        }
    }

    public void checkGameOver() {
        if (gs.gameOver) {
            if (gs.score > highscore) {
                highScoreManager.writeHighScore(gs.score);
            }

            if (playAgainOverlay == null) {
                initialisePlayAgainOverlay();
                showModalOverlay(playAgainOverlay);
            }

            gameLoop.stop();
        }
    }

    public void restartGame() {
        clearEntities();

        alienArray = new AlienArray();
        alienArray.createAliens(gs, images.squidImgs, images.crabImgs, images.octopusImgs);
        ship = new Ship(gs, images.shipImgs);

        ufoController = new UFOController();

        gs.reset();
        gameLoop.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x > 0) {
            ship.movingLeft = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width < gs.boardWidth) {
            ship.movingRight = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x > 0) {
            ship.movingLeft = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width < gs.boardWidth) {
            ship.movingRight = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE && !ship.shipHit) {
            Bullet bullet = new Bullet(gs, ship.x + ship.width * 15 / 32, ship.y);
            if (ship.reloadTime == 60) {
                bulletController.bulletArray.add(bullet);
                SoundPlayer.playSound("../assets/sounds/bullet.wav");
                ship.reloadTime = 0;
            }
        }
    }
}
