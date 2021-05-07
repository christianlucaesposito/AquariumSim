package ui;

import model.Aquarium;
import model.Fish;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
 * Represents the panel for aquarium rendering;
 * holds values for tank color (clear, and dirty) and sand,
 * also contains image for fish, for dead and alive, and a timer that is the speed the tank render is being updated,
 * contains a filed with actual aquarium
 */
public class AquariumRenderPanel extends JPanel implements ActionListener {
    private static final String FISH_RENDER_ALIVE = "./render/f1.png";
    private static final String FISH_RENDER_DEAD = "./render/f1_dead.png";
    private static final Color TANK_COLOR = new Color(149, 166, 187, 255);
    private static final Color SAND_COLOR = new Color(154, 128, 89);
    private static final Color DIRTY_COLOR = new Color(107, 116, 73);
    private final BufferedImage aliveFishImage;
    private final BufferedImage deadFishImage;
    private final Timer timer;
    private Aquarium aquarium;


    // EFFECTS: constructs aquarium render panel
    public AquariumRenderPanel(Aquarium aquarium) {
        this.aquarium = aquarium;
        aliveFishImage = readImage(FISH_RENDER_ALIVE);
        deadFishImage = readImage(FISH_RENDER_DEAD);

        timer = new Timer(100, this);
        timer.start();
    }

    // MODIFIES: this and fish
    // EFFECTS: updates fish location, at timer and updates aquarium rendering
    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == timer) {
            for (Fish fish : aquarium.getFishList()) {
                fish.updateCoordinate();
            }
            repaint();
        }
    }

    // EFFECTS: reads fish image used in aquarium render
    private BufferedImage readImage(String location) {
        try {
            return ImageIO.read(new File(location));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Fish render could not be loaded",
                    "Render error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    // MODIFIES: this
    // EFFECTS: sets aquarium as given aquarium
    public void setAquarium(Aquarium aquarium) {
        this.aquarium = aquarium;
    }

    // MODIFIES: this
    // EFFECT: takes a graphic and paints/renders the aquarium, (with background and its fish)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        drawFish(g);
    }

    // MODIFIES: this
    // EFFECT:  takes a graphic and paints/renders fish facing correct direction
    private void drawFish(Graphics g) {
        for (Fish fish : aquarium.getFishList()) {
            int width = 20 * fish.getSize();
            int height = 10 * fish.getSize();
            int x = (int)(this.getWidth() * fish.getCoordinateX());
            int y = (int)(this.getHeight() * fish.getCoordinateY());

            // renders fish facing direction it is swimming
            if (fish.getDirection() == Fish.RIGHT) {
                x = x + width;
                width = -width;
            }

            // renders fish for its status (alive or dead)
            if (fish.getStatus() == Fish.ALIVE) {
                g.drawImage(aliveFishImage, x, y, width, height, null);
            } else {
                g.drawImage(deadFishImage, x, y, width, height, null);
            }
        }
    }

    // MODIFIES: this
    // EFFECT: paints background, and interpolates color of water
    private void drawBackground(Graphics g) {
        g.setColor(interpolateColor(TANK_COLOR, DIRTY_COLOR,
                1.0 - (double) aquarium.getCleanness() / Aquarium.MAX_CLEANNESS_LEVEL));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(SAND_COLOR);
        g.fillRect(0, (int)(getHeight() * 0.9), getWidth(), getHeight());
    }

    // EFFECT: linearly interpolates between a and b at amount alpha
    private int interpolate(int a, int b, double alpha) {
        return (int)(a + alpha * (b - a));
    }

    // EFFECT: linearly interpolates between color1 and color2 at amount alpha
    private Color interpolateColor(Color color1, Color color2, double alpha) {
        return new Color(
                interpolate(color1.getRed(), color2.getRed(), alpha),
                interpolate(color1.getGreen(), color2.getGreen(), alpha),
                interpolate(color1.getBlue(), color2.getBlue(), alpha)
        );
    }
}
