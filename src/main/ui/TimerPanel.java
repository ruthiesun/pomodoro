package ui;

import model.PomodoroStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

/*
 * Timer for one phase of a pomodoro
 */
public class TimerPanel extends Observable implements ActionListener {
    public static final int SECOND = 1000; // 1 second in ms

    private int counter; // number of seconds left

    private JPanel panel;
    private JTextField time;
    private JTextField message;
    private JButton nextButton;
    private JButton dismissButton;
    private JPanel buttonPanel;

    private Timer timer;

    // REQUIRES: duration is in seconds
    // EFFECTS: starts a timer according to the give pomodoro phase and duration
    public TimerPanel(PomodoroStatus pomodoroStatus, int duration) {
        super();

        switch (pomodoroStatus) {
            case WORK:
                message = new JTextField("time to work");
                break;
            case BREAK:
                message = new JTextField("time to take a break");
                break;
            case LONG_BREAK:
                message = new JTextField("time to take a long break");
                break;
        }

        counter = duration;
        setup();
        startTimer();
    }

    // EFFECTS: sets up GUI elements
    private void setup() {
        nextButton = new JButton("next");
        nextButton.addActionListener(this);
        nextButton.setVisible(false);
        dismissButton = new JButton("dismiss");
        dismissButton.addActionListener(this);
        dismissButton.setVisible(false);
        buttonPanel = new JPanel(new GridLayout(1,0));
        buttonPanel.add(nextButton);
        buttonPanel.add(dismissButton);

        panel = new JPanel();
        panel.setLayout(new GridLayout(0,1));
        panel.add(time);
        panel.add(message);
        panel.add(buttonPanel);

        panel.setVisible(true);
    }


    // MODIFIES: timer
    // EFFECTS: starts the Swing timer
    private void startTimer() {
        timer = new Timer(0, this);
        timer.setDelay(SECOND);
        timer.start();
    }

    // MODIFIES: time
    // EFFECTS: updates time
    private void displayTime() {
        int numMin = counter/60;
        int numSec = counter%60;

        String timeAsString = addZeroes(numMin) + " : " + addZeroes(numSec);
        time = new JTextField(timeAsString);
    }

    // EFFECTS: return given number prepended by 0s such at there are at least two digits in the returned string
    private String addZeroes(int n) {
        if (n < 10) {
            if (n == 0) {
                return "00";
            }
            return "0" + n;
        } else {
            return Integer.toString(n);
        }
    }

    // MODIFIES: message, button
    // EFFECTS: makes button visible and updates message
    private void displayNotif() {
        message = new JTextField("done this phase");
        nextButton.setVisible(true);
        dismissButton.setVisible(true);
    }

    // MODIFIES: counter, time, message, filler, button, panel
    // EFFECTS: if button was pressed, notify observers. Else, update display
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            setChanged();
            notifyObservers(TimerStatus.NOTIF_OFF); // tell observers that user has asked for the next phase
        } else if (e.getSource() == dismissButton) {
            setChanged();
            notifyObservers(TimerStatus.NOTIF_DISMISSED);
        } else { // timer has ticked
            if (counter==0) {
                displayTime();
                displayNotif();
                setChanged();
                notifyObservers(TimerStatus.NOTIF_ON);
                timer.stop();
            } else if (counter>0){
                displayTime();
                counter--;
            }
            refresh();
        }
    }

    // EFFECTS: returns panel
    public JPanel getPanel() {
        return panel;
    }

    // MODIFIES: panel
    // EFFECTS: repaints panel
    public void refresh() {
        panel.removeAll();
        panel.add(time);
        panel.add(message);
        panel.add(buttonPanel);
        panel.validate();
        panel.repaint();
    }
}
