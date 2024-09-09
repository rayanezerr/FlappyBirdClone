import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.image.BufferedImage;


public class MainMenu extends JPanel {

    private BufferedImage backgroundImg;

    public MainMenu(JFrame frame, JPanel mainPanel, CardLayout cardLayout, FlappyBird gamePanel) {

        try {
            backgroundImg = ImageIO.read(getClass().getResourceAsStream("/images/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());
        JLabel title = new JLabel("Flappy Bird", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 50));
        add(title, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1));

        JButton playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.PLAIN, 30));
        buttonPanel.add(playButton);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "game");
                gamePanel.startGame();
                gamePanel.requestFocusInWindow();
            }
        });

        JButton skinButton = new JButton("Skins");
        skinButton.setFont(new Font("Arial", Font.PLAIN, 30));
        buttonPanel.add(skinButton);
        skinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "skins");
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 30));
        buttonPanel.add(exitButton);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        double widthScale = (double) 500 / backgroundImg.getWidth();
        double heightScale = (double) 1000 / backgroundImg.getHeight();
        double scale = Math.min(widthScale, heightScale);
        int newWidth = (int) (backgroundImg.getWidth() * scale);
        int newHeight = (int) (backgroundImg.getHeight() * scale);
        g2d.drawImage(backgroundImg, 0, 0, newWidth, newHeight, this);

    }
}