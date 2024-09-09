import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Frame extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainPanel;
    public Frame() {

        setTitle("Flappy Bird");
        setSize(500, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        FlappyBird flappyBird = new FlappyBird(this, mainPanel, cardLayout);
        mainPanel.add(flappyBird, "game");

        MainMenu mainMenu = new MainMenu(this, mainPanel, cardLayout, flappyBird);
        mainPanel.add(mainMenu, "mainMenu");

        SkinMenu skinMenu = new SkinMenu(this, mainPanel, cardLayout, flappyBird);
        mainPanel.add(skinMenu, "skins");

        cardLayout.show(mainPanel, "mainMenu");

        add(mainPanel);
        setVisible(true);
    }



}
