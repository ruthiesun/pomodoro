package ui;

import model.Pomodoro;
import model.PomodoroStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/*
 * Manages pomodoro timers
 */
public class PomodoroApp implements Observer, ActionListener {
    // minimum window dimensions
    public static final int WIDTH = 400;
    public static final int HEIGHT = 150;

    // default duration of pomodoro phases in seconds
    public static final int DEFAULT_WORK_DURATION = 25*60;
    public static final int DEFAULT_SHORT_BREAK_DURATION = 5*60;
    public static final int DEFAULT_LONG_BREAK_DURATION = 15*60;
    public static final int DEFAULT_NUM_REPS = 4;

    private JFrame frame;
    private JPanel startPanel;
    private SettingsPanel settingsPanel;
    private JButton startButton;
    private JButton exitButton;

    private TimerPanel timerPanel;
    private Pomodoro pomodoro;

    // EFFECTS: starts the pomodoro app
    public PomodoroApp() {
        // set up window
        frame = new JFrame("Pomodoro Timer");
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setUndecorated(true);
        frame.setOpacity(0.9F);
        //setLocationToBotRight(frame);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(1,0)); //!!!

        exitButton = new JButton("x");
        exitButton.addActionListener(this);

        // set up starting menu contents
        settingsPanel = new SettingsPanel();

        startButton = new JButton("go!");
        startButton.addActionListener(this);

        startPanel = new JPanel();
        startPanel.setLayout(new BorderLayout());
        startPanel.add(settingsPanel, BorderLayout.CENTER);
        startPanel.add(startButton, BorderLayout.PAGE_END);

        frame.add(startPanel); //!!!
        frame.setVisible(true);
        refresh();
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


        frame.add(timerPanel.getPanel());
        timerPanel.addObserver(this);
        timerPanel.addObserver(pomodoro);
        timerPanel.refresh();
        refresh();
    }

    // MODIFIES: timerPanel, frame
    // EFFECTS: creates a new timer, reopens the starting menu, or gives user a notification
    @Override
    public void update(Observable o, Object arg) {
        if (o.getClass() == Pomodoro.class) {
            frame.remove(timerPanel.getPanel());
            switch ((PomodoroStatus) arg) {
                case WORK:
                    newTimer(PomodoroStatus.WORK);
                    break;
                case BREAK:
                    newTimer(PomodoroStatus.BREAK);
                    break;
                case LONG_BREAK:
                    newTimer(PomodoroStatus.LONG_BREAK);
                    break;
                case DONE:
                    frame.add(startPanel);
                    refresh();
                    break;
            }
        } else if (o.getClass() == TimerPanel.class) {
            switch ((TimerStatus) arg) {
                case NOTIF_ON:
                    frameToFront(frame);
                    refresh();
                    break;
                case NOTIF_DISMISSED:
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

    // MODIFIES: frame
    // EFFECTS: repaints the window
    private void refresh() {
        frame.add(exitButton);
        frame.validate();
        frame.repaint();
    }

    // MODIFIES: startPanel
    // EFFECTS: makes the starting menu invisible and starts a pomodoro
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            if (settingsPanel.allInputsValid()) {
                frame.remove(startPanel);
                startPomodoro();
            }
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }
}
