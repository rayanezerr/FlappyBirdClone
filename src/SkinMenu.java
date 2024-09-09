import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class SkinMenu extends JPanel {
    private ArrayList<BufferedImage> skins;
    private BufferedImage selectedSkin;
    private JLabel selectedSkinLabel;
    private BufferedImage backgroundImg;

    public SkinMenu(JFrame frame, JPanel mainPanel, CardLayout cardLayout, FlappyBird gamePanel) {
        try {
            backgroundImg = ImageIO.read(getClass().getResourceAsStream("/images/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());
        setOpaque(false);

        JLabel title = new JLabel("Skins", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 50));
        add(title, BorderLayout.NORTH);

        JPanel skinPanel = new JPanel();
        skinPanel.setLayout(new FlowLayout());
        skinPanel.setOpaque(false);

        skins = new ArrayList<>();

        try {
            skins.add(ImageIO.read(getClass().getResource("/images/black.png")));
            skins.add(ImageIO.read(getClass().getResource("/images/cyan.png")));
            skins.add(ImageIO.read(getClass().getResource("/images/purple.png")));
            skins.add(ImageIO.read(getClass().getResource("/images/green.png")));
            skins.add(ImageIO.read(getClass().getResource("/images/red.png")));
            skins.add(ImageIO.read(getClass().getResource("/images/yellow.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (BufferedImage skin : skins) {
            JLabel skinLabel = new JLabel(new ImageIcon(skin));
            skinLabel.setIcon(new ImageIcon(skin.getScaledInstance(209, 148, Image.SCALE_SMOOTH)));

            skinLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("Skin selected");
                    selectedSkin = skin;
                    selectedSkinLabel.setIcon(new ImageIcon(selectedSkin));
                    repaint();
                }
            });
            skinPanel.add(skinLabel);
        }

        add(skinPanel, BorderLayout.CENTER);
        selectedSkinLabel = new JLabel("Selected Skin", JLabel.CENTER);
        selectedSkinLabel.setFont(new Font("Arial", Font.BOLD, 30));
        selectedSkinLabel.setOpaque(false);
        add(selectedSkinLabel, BorderLayout.SOUTH);


        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(new Font("Arial", Font.PLAIN, 30));
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedSkin == null) {
                    JOptionPane.showMessageDialog(frame, "Please select a skin", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                gamePanel.setSkin(selectedSkin);
                cardLayout.show(mainPanel, "mainMenu");
            }
        });
        add(confirmButton, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        double widthScale = (double) getWidth() / backgroundImg.getWidth();
        double heightScale = (double) getHeight() / backgroundImg.getHeight();
        double scale = Math.min(widthScale, heightScale);
        int newWidth = (int) (backgroundImg.getWidth() * scale);
        int newHeight = (int) (backgroundImg.getHeight() * scale);
        int x = (getWidth() - newWidth) / 2;
        int y = (getHeight() - newHeight) / 2;

        g2d.drawImage(backgroundImg, x, y, newWidth, newHeight, this);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        g2d.drawString("Selected skin : ", 120, 600);


        if (selectedSkin == null) {
            g2d.drawImage(skins.get(5), 145, 650, 210, 148, this);
        }
        else {
            g2d.drawImage(selectedSkin, 145, 650, 210, 148, this);
        }
    }
}
