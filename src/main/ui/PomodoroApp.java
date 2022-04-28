package ui;

import model.Pomodoro;
import model.Status;

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
    // window dimensions
    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;

    // duration of pomodoro phases in seconds
    public static final int WORK_DURATION = 66;
    public static final int BREAK_DURATION = 2;
    public static final int LONG_BREAK_DURATION = 2;
    public static final int NUM_REPS = 4;

    private JFrame frame;
    private JPanel startPanel;
    private JButton startButton;
    private JTextField startTextField;

    private TimerPanel timerPanel;
    private Pomodoro pomodoro;

    public PomodoroApp() {
        // set up window
        frame = new JFrame("Pomodoro Timer");
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set up starting menu contents
        startButton = new JButton("go!");
        startButton.addActionListener(this);
        startTextField = new JTextField("start new pomodoro?");

        startPanel = new JPanel();
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.PAGE_AXIS));
        startPanel.add(startTextField);
        startPanel.add(startButton);

        frame.add(startPanel);
        frame.setVisible(true);
        refresh();
    }

    // MODIFIES: pomodoro, timerPanel
    // EFFECTS: starts a pomodoro
    private void startPomodoro() {
        // set up new timer
        pomodoro = new Pomodoro();
        pomodoro.addObserver(this);
        newTimer(Status.WORK);
        pomodoro.start();
    }

    // REQUIRES: pomodoro is initialized
    // MODIFIES: timerPanel, frame
    // EFFECTS: starts a timer for a pomodoro phase
    private void newTimer(Status status) {
        timerPanel = new TimerPanel(status);
        frame.add(timerPanel.getPanel());
        timerPanel.addObserver(pomodoro);
        timerPanel.refresh();
        refresh();
    }

    // REQUIRES: observable object notifies with a Status as an argument
    // MODIFIES: timerPanel, frame
    // EFFECTS: either creates a new timer or reopens the starting menu
    @Override
    public void update(Observable o, Object arg) {
        frame.remove(timerPanel.getPanel());
        switch ((Status) arg) {
            case WORK:
                newTimer(Status.WORK);
                break;
            case BREAK:
                newTimer(Status.BREAK);
                break;
            case LONG_BREAK:
                newTimer(Status.LONG_BREAK);
                break;
            case DONE:
                frame.add(startPanel);
                refresh();
                break;
        }
    }

    // MODIFIES: frame
    // EFFECTS: repaints the window
    private void refresh() {
        frame.validate();
        frame.repaint();
    }

    // MODIFIES: startPanel
    // EFFECTS: makes the starting menu invisible and starts a pomodoro
    @Override
    public void actionPerformed(ActionEvent e) {
        frame.remove(startPanel);
        startPomodoro();
    }
}
