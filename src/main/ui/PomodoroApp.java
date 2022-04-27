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

    public static final int WORK_DURATION = 25;
    public static final int BREAK_DURATION = 5;
    public static final int LONG_BREAK_DURATION = 15;
    public static final int NUM_REPS = 4;

    JFrame frame;
    JPanel startPanel;
    JButton startButton;
    JTextField startTextField;

    private Pomodoro pomodoro;

    public PomodoroApp() {
        setup();
    }

    private void setup() {
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
    }

    private void startPomodoro() {
        // set up pomodoro
        TimerPanel timerPanel = new TimerPanel();
        pomodoro = new Pomodoro();

        // run pomodoro
        pomodoro.addObserver(timerPanel);
        pomodoro.addObserver(this);
        pomodoro.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((Status) arg == Status.DONE_ALL) {
            System.out.println("done one pomodoro");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("button clicked");
        startPanel.setVisible(false);
        startPomodoro();
    }
}
