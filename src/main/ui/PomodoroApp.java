package ui;

import model.Pomodoro;
import model.PomodoroStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

/*
 * Manages pomodoro timers
 */
public class PomodoroApp implements Observer, ActionListener {
    // default duration of pomodoro phases in seconds
    public static final int DEFAULT_WORK_DURATION = 25*60;
    public static final int DEFAULT_SHORT_BREAK_DURATION = 5*60;
    public static final int DEFAULT_LONG_BREAK_DURATION = 15*60;
    public static final int DEFAULT_NUM_REPS = 4;

    public static final Color COLOUR_NEUTRAL = new Color(182, 192, 214);
    public static final Color COLOUR_START = new Color(191, 214, 182);
    public static final Color COLOUR_STOP = new Color(214, 185, 182);

    private JFrame frame;
    private JPanel startPanel;
    private SettingsPanel settingsPanel;
    private JButton startButton;
    private JLabel exitButton;

    private TimerPanel timerPanel;
    private Pomodoro pomodoro;
    private PomodoroStatus pomodoroStatus;

    private GridBagConstraints gbc;

    // EFFECTS: starts the pomodoro app
    public PomodoroApp() {
        // set up window
        frame = new JFrame("Pomodoro Timer");
        frame.setUndecorated(true);
        frame.setOpacity(0.9F);
        frame.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, UIManager.getColor(frame.getBackground())));
        //setLocationToBotRight(frame);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout());

        exitButton = new JLabel(new ImageIcon("./data/exitButton.png"));
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        setupStartMenu();

        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;

        frame.setVisible(true);
        addFrameComponents(startPanel);
    }

    // EFFECTS: helper for constructor; sets up start menu GUI components
    private void setupStartMenu() {
        settingsPanel = new SettingsPanel();

        startButton = new JButton("Go!");
        startButton.setBackground(PomodoroApp.COLOUR_START);
        startButton.addActionListener(this);

        startPanel = new JPanel();
        startPanel.setLayout(new BorderLayout());
        startPanel.add(settingsPanel, BorderLayout.CENTER);
        startPanel.add(startButton, BorderLayout.PAGE_END);
    }

    // MODIFIES: f
    // EFFECTS: sets location of f to bottom right of screen
    // adapted from https://stackoverflow.com/questions/50498314/how-can-i-set-jframe-location-at-right-of-screen
    private static void setLocationToBotRight(JFrame f) {
        GraphicsConfiguration config = f.getGraphicsConfiguration();
        Rectangle bounds = config.getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(config);

        int x = bounds.x + bounds.width - insets.right - f.getWidth();
        int y = bounds.y + bounds.height - insets.top - f.getHeight();
        f.setLocation(x, y);
    }

    // MODIFIES: pomodoro, timerPanel
    // EFFECTS: starts a pomodoro based on current state of the settings panel
    private void startPomodoro() {
        pomodoro = new Pomodoro(settingsPanel.getNumReps(), settingsPanel.infinite());
        pomodoro.addObserver(this);
        newTimer(PomodoroStatus.WORK);
        pomodoro.start();
    }

    // REQUIRES: pomodoro is initialized
    // MODIFIES: timerPanel, frame
    // EFFECTS: starts a timer for a pomodoro phase
    private void newTimer(PomodoroStatus pomodoroStatus) {
        this.pomodoroStatus = pomodoroStatus;
        switch(pomodoroStatus) {
            case WORK:
                int work = settingsPanel.getLengthWork();
                timerPanel = new TimerPanel(pomodoroStatus, work);
                break;
            case BREAK:
                int shortBreak = settingsPanel.getLengthShortBreak();
                timerPanel = new TimerPanel(pomodoroStatus, shortBreak);
                break;
            case LONG_BREAK:
                int longBreak = settingsPanel.getLengthLongBreak();
                timerPanel = new TimerPanel(pomodoroStatus, longBreak);
                break;
        }

        frame.remove(exitButton);
        addFrameComponents(timerPanel.getPanel());
        timerPanel.addObserver(this);
        timerPanel.addObserver(pomodoro);
        timerPanel.refresh();
    }

    // MODIFIES: timerPanel, frame
    // EFFECTS: creates a new timer, reopens the starting menu, or gives user a notification
    @Override
    public void update(Observable o, Object arg) {
        if (o.getClass() == Pomodoro.class) {
            frame.remove(timerPanel.getPanel());
            switch ((PomodoroStatus) arg) {
                case WORK:
                    frame.setAlwaysOnTop(false);
                    newTimer(PomodoroStatus.WORK);
                    break;
                case BREAK:
                    frame.setAlwaysOnTop(true);
                    newTimer(PomodoroStatus.BREAK);
                    break;
                case LONG_BREAK:
                    frame.setAlwaysOnTop(true);
                    newTimer(PomodoroStatus.LONG_BREAK);
                    break;
                case DONE:
                    frame.setAlwaysOnTop(false);
                    frame.remove(exitButton);
                    addFrameComponents(startPanel);
                    break;
            }
        } else if (o.getClass() == TimerPanel.class) {
            switch ((TimerStatus) arg) {
                case NOTIF_ON: // timer has hit 0
                    frameToFront(frame);
                    refresh();
                    break;
                case NOTIF_NEXT: // user clicked next
                    refresh();
                    break;
                case NOTIF_DISMISSED: // user clicked dismiss
                    frame.setAlwaysOnTop(false);
                    frame.toBack();
                    refresh();
                    break;
            }
        }
    }

    // MODIFIES: f
    // EFFECTS: sets frame to be always on top of VM display
    // adapted from https://stackoverflow.com/questions/309023/how-to-bring-a-window-to-the-front
    private void frameToFront(JFrame f) {
        int sta = f.getExtendedState() & ~JFrame.ICONIFIED & JFrame.NORMAL;

        f.setExtendedState(sta);
        f.setAlwaysOnTop(true);
        f.toFront();
        f.requestFocus();
        //f.setAlwaysOnTop(false);
    }

    // EFFECTS: adds given panel plus the exit button; repaints at the end
    private void addFrameComponents(JPanel p) {
        gbc.gridx = 0;
        gbc.gridheight = 10;
        gbc.gridwidth = 10;
        frame.add(p, gbc);

        gbc.gridx = 10;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        frame.add(exitButton, gbc);

        refresh();
    }

    // MODIFIES: frame
    // EFFECTS: repaints the window
    private void refresh() {
        frame.validate();
        frame.repaint();
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    // MODIFIES: startPanel
    // EFFECTS: if startButton was clicked, makes the starting menu invisible and starts a pomodoro
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            if (settingsPanel.allInputsValid()) {
                frame.remove(startPanel);
                startPomodoro();
            }
        }
    }
}
