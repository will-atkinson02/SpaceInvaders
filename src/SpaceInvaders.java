//TODO
// fix level up problems
// put projectiles into own classes
// add sound effects and music
// BONUS add green shield things, alien bullet collisions with floor
// stretch change bulletArray to a linked list

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
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
    UFO ufo;

    BulletController bulletController;
    ZProjectile zProjectile;
    ZController zController;
    
    //states


    //t projectile
    ArrayList<Entity> tArray;
    ArrayList<int[]> tShapesX = new ArrayList<>();
    ArrayList<int[]> tShapesY = new ArrayList<>();

    int tAnimationInterval = 5;
    int tAnimationTimer = 0;

    int tSpawnInterval = 60;
    int tSpawnTimer = 0;

    int animationDirection = -1;
    int animationFrame = 4;

    //prevent same alien firing twice
    ArrayList<Integer> rechargingAliens;

    //miscellaneous
    JPanel titleOverlay;
    JPanel playAgainOverlay;

    Timer gameLoop;

    HighScore highScoreManager = new HighScore();
    int highscore = highScoreManager.readHighScore();

    Font customFont = FontLoader.loadFont("visitor1.ttf");

    SpaceInvaders(GameState gs) {
        this.gs = gs;

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
        ufo = new UFO(gs.tileSize, images.ufoImgs);

        tArray = new ArrayList<Entity>();
        zController = new ZController();
        bulletController = new BulletController();

        rechargingAliens = new ArrayList<Integer>();

        initaliseTSHapes();

        gameLoop = new Timer(1000/60, this);
    }

    public void initaliseTSHapes() {
        tShapesX = new ArrayList<int[]>();
        tShapesY = new ArrayList<int[]>();

        //standard T
        tShapesX.add(new int[]{0, 4, 8, 4, 4, 4, 4, 4});
        tShapesY.add(new int[]{0, 0, 0, 4, 8, 12, 16, 20});

        //Top-heavy middle cross
        tShapesX.add(new int[]{4, 4, 0, 4, 8, 4, 4, 4});
        tShapesY.add(new int[]{0, 4, 8, 8, 8, 12, 16, 20});

        //Bottom-heavy middle cross
        tShapesX.add(new int[]{4, 4, 4, 0, 4, 8, 4, 4});
        tShapesY.add(new int[]{0, 4, 8, 12, 12, 12, 16, 20});

        //upside-down T
        tShapesX.add(new int[]{4, 4, 4, 4, 4, 0, 4, 8});
        tShapesY.add(new int[]{0, 4, 8, 12, 16, 20, 20, 20});
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

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridy = 0;
        modal.add(titleLabel, gbc);
        gbc.gridy = 1;
        modal.add(startButton, gbc);

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
            clearEntities();

            ship.x = (gs.tileSize*gs.cols)/2 - gs.tileSize;
            alienArray.alienVelocityX = 8;
            
            gs.score = 0;
            ship.lives = 3;

            alienArray.alienSpriteTimer = 0;
            alienArray.alienStepRate = 24;

            alienArray.alienSpriteTimer = 0;
            alienArray.alienSpriteState = 0;

            ship.shipSpriteState = 0;

            alienArray.createAliens(gs, images.squidImgs, images.crabImgs, images.octopusImgs);

            gameLoop.start();
            gs.gameOver = false;
        }
    }

    public void clearEntities() {
        alienArray.alienArray.clear();
        bulletController.bulletArray.clear();
        tArray.clear();
        zProjectile = null;
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

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        g.drawLine(0, 75, 960, 75);
    }

    public void draw(Graphics g) {
        //render Entities
        g.drawImage(ship.imgArray.get(ship.shipSpriteState), ship.x, ship.y, ship.width, ship.height, null);
        
        EntityRenderer.renderAliens(g, alienArray.alienArray, alienArray.explodedAlienIndex, alienArray.explosionTimer, alienArray.alienSpriteState);
        
        if (ufo.isActive) {
            EntityRenderer.renderUFO(g, ufo, customFont, images.ufoImg);
        }

        EntityRenderer.renderZProjectile(g, zProjectile, gs.tileSize);
        EntityRenderer.renderTProjectile(g, tArray, tShapesX, tShapesY, animationFrame, gs.tileSize);
        
        EntityRenderer.renderBullets(g, bulletController.bulletArray);

        //render HUD
        HUDRenderer.renderHUD(g, customFont, gs, highscore, ship.lives, images.shipImgA, ship.width, ship.height);
    }

    public void move() {
        alienArray.alienMovement(gs, ship, ufo);

        zController.handleZMovement(gs, ship);

        tProjectileMovement();
        bulletMovement();

        //clear bullets
        while (bulletController.bulletArray.size() > 0 && (bulletController.bulletArray.get(0).used || bulletController.bulletArray.get(0).y < 75)) {
            bulletController.bulletArray.remove(0); 
        }
    }

    public void tProjectileMovement() {
        if (!tArray.isEmpty()) {
            for (int i = 0; i < tArray.size(); i++) {
                Entity tProjectile = tArray.get(i);

                if (tProjectile.y == gs.boardHeight) {
                    tArray.remove(i);
                } else {
                    tProjectile.y += 8;

                    if (CollisionHandler.detectCollision(tProjectile, ship)) {
                        ship.hit();
                        tArray.remove(i);
                    } 
                }
            }
        }
    }

    public void bulletMovement() {
        for (int i = 0; i < bulletController.bulletArray.size(); i++) {
            Bullet bullet = bulletController.bulletArray.get(i);
            bullet.y += bullet.velocityY;

            //bullet collision with aliens
            for (int j = 0; j < alienArray.alienArray.size(); j++) {
                Alien alien = alienArray.alienArray.get(j);
                if (!bullet.used && alien.alive && CollisionHandler.detectCollision(bullet, alien)) {
                    ship.reloadTime = 60;
                    alienArray.explodedAlienIndex = j;
                    bullet.used = true;
                    alien.alive = false;
                    alienArray.alienCount--;
                    gs.score += alien.points;
                }
            }

            //bullet collision with ufo
            if (ufo != null && CollisionHandler.detectCollision(bullet, ufo)) {
                bullet.used = true;
                ship.reloadTime = 60;
                gs.score += UFO.points();
                ufo.hit = true;
                ufo.velocityX = 0;
            }
        }
    }

    public void createTProjectile() {
        int y = alienArray.alienArray.get(alienIndex).y + gs.tileSize;
        int x = alienArray.alienArray.get(alienIndex).x + gs.tileSize*13/16;
        Entity tProjectile = new Entity(x, y, gs.tileSize*3/8, gs.tileSize*7/8, null);
        tArray.add(tProjectile);
    }

    public void nextLevel() {
        if (alienArray.alienCount == 0) {
            clearEntities();

            gs.level += 1;
            gs.score += gs.level * alienArray.alienCols * 100;

            ship.shipSpriteState = 0;

            alienArray.alienSpriteTimer = 0;
            alienArray.alienStepRate = 24 - gs.level;

            if (alienArray.alienVelocityX < 0) {
                alienArray.alienVelocityX *= -1;
            }
            alienArray.alienVelocityX += 4;

            tSpawnInterval -= 5;
            zController.zSpawnInterval -=5;

            alienArray.createAliens(gs, images.squidImgs, images.crabImgs, images.octopusImgs);
        }
    }

    public void handleTProjectileSpawn(Random r) {
        tAnimationTimer++;
        if (tAnimationTimer == tAnimationInterval) {
            handleTProjectileAnimation();
            tAnimationTimer = 0;
        }
        if (tArray.size() < 3) {
            tSpawnTimer++;
            if (tSpawnTimer == tSpawnInterval) {
                alienIndex = r.nextInt(11);
                if (alienArray.aliveSquidCount > 3) {
                    while (rechargingAliens.contains(alienIndex) || !alienArray.alienArray.get(alienIndex).alive) {

                        alienIndex = r.nextInt(11);
                    }
                    createTProjectile();
    
                    if (rechargingAliens.size() == 3) {
                        rechargingAliens.remove(0);
                    } 
                    rechargingAliens.add(alienIndex);
                } else {
                    rechargingAliens.clear();
                    if (r.nextDouble() < 0.4) {
                        while (!alienArray.alienArray.get(alienIndex).alive) {
                            alienIndex = r.nextInt(11);
                        }
                        createTProjectile();
                    }
                }
                tSpawnTimer = 0;
            }
        }
    }

    public void handleTProjectileAnimation() {
        if (animationFrame == 3 && animationDirection == 1) {
            animationDirection = -1;
        } else if (animationFrame == 0 && animationDirection == -1) {
            animationDirection = 1;
        }
        animationFrame += animationDirection;
    }

    

    @Override
    public void actionPerformed(ActionEvent e) {
        int aliveSquidCount = 0;
        if (!gs.gameOver) {
            aliveSquidCount = alienArray.countAliveSquids();
        }
        if (aliveSquidCount > 0) {
            Random r = new Random();
            if (zProjectile != null) {
                zProjectile.shuffleZConvolution();
            } else {
                zController.handleZSpawn(gs, r, alienArray, aliveSquidCount, rechargingAliens);
            }
            handleTProjectileSpawn(r);
        }
        alienArray.alienExplosionTimer();

        if (ship.reloadTime < 60) {
            ship.reloadTime++;
        }
        ship.handleShipHit(gs, alienArray);
        ship.allowShipControl(gs.boardWidth);

        ufo.incrementUFOs();
        ufo.handle(gs, images.ufoImgs);

        move();
        nextLevel();
        repaint();

        if (gs.gameOver) {
            if (gs.score > highscore) {
                highScoreManager.writeHighScore(gs.score);
            }

            if (playAgainOverlay == null) {
                initialisePlayAgainOverlay();
                showModalOverlay(playAgainOverlay); 
            }

            gameLoop.stop();
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

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
            Bullet bullet = new Bullet(gs, ship.x + ship.width*15/32, ship.y, null);
            if (ship.reloadTime == 60) {
                bulletController.bulletArray.add(bullet);
                // SoundPlayer.playSound("sounds/bullet.wav");
                ship.reloadTime = 0;
            }
        }
    }
}
