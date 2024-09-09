import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

 public class FlappyBird extends JPanel implements KeyListener, ActionListener {

    private BufferedImage birdImg, downPipeImg, upPipeImg, backgroundImg, gameOverImg;

    private final int width = 500, height = 1000;
    private final int birdX = width / 8, birdY = height / 2;

    private boolean isGameRunning = false;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private class Bird {
        int x = birdX, y = birdY;
        int birdWidth = 73, birdHeight = 52;
        BufferedImage img;

        public Bird(BufferedImage img) {
            this.img = img;
        }
    }

    private final int pipeX = width, pipeY = 0;
    private final int pipeWidth = width / 6, pipeHeight = height;

    private class Pipe {
        int x = pipeX, y = pipeY;
        int width = pipeWidth, height = pipeHeight;
        boolean passed = false;
        BufferedImage img;

        public Pipe(BufferedImage img) {
            this.img = img;
        }
    }

    private ArrayList<Pipe> pipes;
    private Random random = new Random();
    private Timer pipeTimer;

    private Bird bird;
    private int velocityX = -6, velocityY = 0;
    private int gravity = 1;

    private Timer gameTimer;
    private boolean gameOver = false;
    private double score = 0;

    public FlappyBird(JFrame frame, JPanel mainPanel, CardLayout cardLayout) {
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        addKeyListener(this);

        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        try {
            birdImg = ImageIO.read(getClass().getResourceAsStream("/images/yellow.png"));
            downPipeImg = ImageIO.read(getClass().getResourceAsStream("/images/downPipe.png"));
            upPipeImg = ImageIO.read(getClass().getResourceAsStream("/images/upPipe.png"));
            backgroundImg = ImageIO.read(getClass().getResourceAsStream("/images/background.png"));
            gameOverImg = ImageIO.read(getClass().getResourceAsStream("/images/gameover.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        bird = new Bird(birdImg);
        pipes = new ArrayList<>();

    }

    public void setSkin(BufferedImage birdSkin) {
        this.birdImg = birdSkin;
        bird = new Bird(birdImg);
    }

    public void startGame() {
        isGameRunning = true;
        pipeTimer = new Timer(1350, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });

        pipeTimer.start();

        gameTimer = new Timer(17, this);
        gameTimer.start();
    }

    public void placePipes() {
        int rdnPipeY = (int) (pipeY - pipeHeight / 3 - random.nextInt(pipeHeight / 2));
        int opening = 200;

        Pipe pipe = new Pipe(downPipeImg);
        pipe.y = rdnPipeY;
        pipes.add(pipe);

        pipe = new Pipe(upPipeImg);
        pipe.y = pipes.get(pipes.size() - 1).y + pipeHeight + opening;
        pipes.add(pipe);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        double widthScale = (double) width / backgroundImg.getWidth();
        double heightScale = (double) height / backgroundImg.getHeight();
        double scale = Math.min(widthScale, heightScale);

        int newWidth = (int) (backgroundImg.getWidth() * scale);
        int newHeight = (int) (backgroundImg.getHeight() * scale);
        int x = (width - newWidth) / 2;
        int y = (height - newHeight) / 2;

        g2d.drawImage(backgroundImg, x, y, newWidth, newHeight, this);
        if (!isGameRunning) {
            return;
        }
        for (Pipe pipe : pipes) {
            g2d.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, this);
        }

        g2d.drawImage(bird.img, bird.x, bird.y, bird.birdWidth, bird.birdHeight, this);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.drawString("Score: " + (int) score, 10, 30);

        if (gameOver) {
            g2d.drawImage(backgroundImg, x, y, newWidth, newHeight, this);

            for (Pipe pipe : pipes) {
                g2d.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, this);
            }
            g2d.drawImage(gameOverImg, width / 2 - 100, height / 2 - 200, 200, 400, this);
        }
    }

    public void move() {

        if (!isGameRunning) {
            return;
        }

        bird.y = Math.max(0, bird.y + velocityY);
        velocityY += gravity;

        for (Pipe pipe : pipes) {
            pipe.x += velocityX;

            if (isCollision(bird, pipe)) {
                gameOver = true;
            }

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5;
                pipe.passed = true;
            }
        }

        if (bird.y > height || bird.y < 0) {
            gameOver = true;
        }
    }

    boolean isCollision(Bird bird, Pipe pipe) {
        return bird.x + bird.birdWidth > pipe.x && bird.x < pipe.x + pipe.width &&
                bird.y < pipe.y + pipe.height && bird.y + bird.birdHeight > pipe.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            pipeTimer.stop();
            gameTimer.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
            if (gameOver) {
                bird.y = birdY;
                pipes.clear();
                score = 0;
                gameOver = false;
                pipeTimer.start();
                gameTimer.start();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && gameOver) {
            velocityY = -9;
            bird.y = birdY;
            pipes.clear();
            score = 0;
            gameOver = false;
            pipeTimer.start();
            gameTimer.start();
            pipeTimer.stop();
            gameTimer.stop();

            cardLayout.show(mainPanel, "mainMenu");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
