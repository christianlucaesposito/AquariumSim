package ui;


import model.Aquarium;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.Timer;

/*
 * Represents Graphical User Interface
 * contains button panel, status panel, and render panel;
 * contains an aquarium, and
 * holds a timer for passage of time on aquarium
 */
public class GUI extends JFrame implements ActionListener {
    private static final String SONG_STORE = "./media/background.wav";

    private Aquarium aquarium;
    private ButtonCommandPanel buttonsPanel;
    private StatusTablePanel statusPanel;
    private AquariumRenderPanel aquariumRender;
    private Timer passTimeTimer;
    private AudioInputStream backgroundMusic;


    // EFFECTS: constructs graphical user interface and initializes aquarium
    public GUI() {
        super("Aquarium Simulator 2020 Pandemic Edition");
        aquarium = new Aquarium();
        backgroundMusic = loadMusic(SONG_STORE);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1200, 500));
        setLayout();
        passTimeAuto();
        loopMusic(backgroundMusic);

        addButtonPanel();
        addStatusTablePanel();
        addAquariumRenderPanel();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);
    }

    // MODIFIES: this
    // EFFECTS: plays audio in loop
    private void loopMusic(AudioInputStream audio) {
        if (audio == null) {
            JOptionPane.showMessageDialog(this,
                    "Audio will not be played",
                    "Fatal Audio error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Clip clip;

        try {
            clip = loadAudioAsClip(audio);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Audio will not be played",
                    "Fatal Audio error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // EFFECTS: loads audio, returns an audioInputStream of the audio location.
    private AudioInputStream loadMusic(String audioLocation) {
        AudioInputStream audioInput;
        try {
            audioInput = AudioSystem.getAudioInputStream(new File(audioLocation));
            return audioInput;
        } catch (UnsupportedAudioFileException e) {
            JOptionPane.showMessageDialog(this,
                    "Unsupported audio file, unable to load audio file",
                    "Audio error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Loading error, unable to load music",
                    "Audio error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // EFFECTS: gets audio as a clip, and returns audioInput as clip
    private Clip loadAudioAsClip(AudioInputStream audioInput) {
        Clip clip;
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            JOptionPane.showMessageDialog(this, "Error due to resource restrictions",
                    "Audio error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        try {
            clip.open(audioInput);
            return clip;
        } catch (LineUnavailableException e) {
            JOptionPane.showMessageDialog(this,
                    "Error due to resource restrictions, unable to open audio as clip", "Audio error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Unable to open audio as clip",
                    "Audio error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // MODIFIES: this
    // EFFECTS: Constructs button panel and adds to this
    private void addButtonPanel() {
        buttonsPanel = new ButtonCommandPanel(this);
        add(buttonsPanel, generateGridBagConstraints(1, 0, 2, 1));
    }

    // MODIFIES: this
    // EFFECTS: Constructs status table panel and adds to this
    private void addStatusTablePanel() {
        statusPanel = new StatusTablePanel(aquarium);
        add(statusPanel, generateGridBagConstraints(0, 0, 1, 3));
    }

    // MODIFIES: this
    // EFFECTS: Constructs Aquarium render panel and adds to this
    private void addAquariumRenderPanel() {
        aquariumRender = new AquariumRenderPanel(aquarium);
        add(aquariumRender, generateGridBagConstraints(1, 1, 2, 2));
    }

    // MODIFIES: this
    // EFFECTS: starts time passer for 50 seconds, and passes time events every 50 second
    private void passTimeAuto() {
        passTimeTimer = new javax.swing.Timer(50000, this);
        passTimeTimer.start();
    }

    // MODIFIES: this
    // EFFECTS: Configures layout format, and sets it to gridLayout
    private void setLayout() {
        GridBagLayout gridLayout = new GridBagLayout();
        gridLayout.columnWeights = new double[]{0.5, 1.0, 1.0};
        gridLayout.rowWeights = new double[]{0.1, 1.0, 1.0};
        setLayout(gridLayout);
    }

    @Override
    // This is the method that is called when the the JButton btn is clicked
    // MODIFIES: this
    // EFFECT: processes action performed
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == passTimeTimer) {
            aquarium.passTime();
            statusPanel.updateFishTable();
        }
    }

    // MODIFIES: this
    // EFFECTS: Sets gridBag constraints for placing UI components, and returns it
    private GridBagConstraints generateGridBagConstraints(int x, int y, int width, int height) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.fill = GridBagConstraints.BOTH;
        return constraints;
    }

    // EFFECTS: returns aquarium
    public Aquarium getAquarium() {
        return aquarium;
    }

    // EFFECTS: returns status panel
    public StatusTablePanel getStatusPanel() {
        return statusPanel;
    }

    // MODIFIES: this
    // EFFECTS: sets aquarium as aquarium status panel
    public void setAquarium(Aquarium aquarium) {
        this.aquarium = aquarium;
        aquariumRender.setAquarium(aquarium);
        statusPanel.setAquarium(aquarium);
        statusPanel.updateFishTable();
    }
}

