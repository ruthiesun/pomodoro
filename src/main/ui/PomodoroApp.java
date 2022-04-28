package ui;

import model.Pomodoro;
import model.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

// Manages pomodoros
public class PomodoroApp implements Observer, ActionListener {
    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;

    public static final int WORK_DURATION = 3;
    public static final int BREAK_DURATION = 2;
    public static final int LONG_BREAK_DURATION = 1;
    public static final int NUM_REPS = 2;

    private JFrame frame;
    private JPanel startPanel;
    private JButton startButton;
    private JTextField startTextField;

    private TimerPanel timerPanel;
    private Pomodoro pomodoro;

    // EFFECTS: constructor
    public PomodoroApp() {
        // set up window
        frame = new JFrame("Pomodoro Timer");
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));

        // set up initial contents
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

    // EFFECTS: starts a pomodoro
    private void startPomodoro() {
        // set up new timer
        pomodoro = new Pomodoro();
        pomodoro.addObserver(this);
        newTimer(Status.WORK);
        pomodoro.start();
    }

    private void newTimer(Status status) {
        timerPanel = new TimerPanel(status);
        frame.add(timerPanel.panel);
        timerPanel.addObserver(pomodoro);
        refresh();
    }

    @Override
    public void update(Observable o, Object arg) {
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
                timerPanel.panel.setVisible(false);
                startPanel.setVisible(true);
                refresh();
                break;
        }
    }

    private void refresh() {
        frame.validate();
        frame.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("button clicked");
        startPanel.setVisible(false);
        startPomodoro();
    }
}
