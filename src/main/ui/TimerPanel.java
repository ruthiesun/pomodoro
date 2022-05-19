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
    private PomodoroStatus pomodoroStatus;

    private JPanel panel;
    private JLabel time;
    private JLabel message;
    private JButton nextButton;
    private JButton dismissButton;

    private GridBagConstraints gbc;

    private Timer timer;

    // REQUIRES: duration is in seconds
    // EFFECTS: starts a timer according to the give pomodoro phase and duration
    public TimerPanel(PomodoroStatus pomodoroStatus, int duration) {
        super();
        this.pomodoroStatus = pomodoroStatus;

        switch (pomodoroStatus) {
            case WORK:
                message = new JLabel("Working...");
                break;
            case BREAK:
                message = new JLabel("Taking a break...");
                break;
            case LONG_BREAK:
                message = new JLabel("Taking a long break...");
                break;
        }

        counter = duration;
        setup();
        startTimer();
    }

    // EFFECTS: sets up GUI elements
    private void setup() {
        panel = new JPanel();

        time = new JLabel("00:00");
        time.setFont(getBigBoldFont(time.getFont()));

        nextButton = new JButton("Next");
        nextButton.setBackground(PomodoroApp.COLOUR_START);
        nextButton.addActionListener(this);
        toggleButton(nextButton);
        dismissButton = new JButton("Dismiss");
        dismissButton.setBackground(PomodoroApp.COLOUR_NEUTRAL);
        dismissButton.addActionListener(this);
        toggleButton(dismissButton);

        panel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        refresh();

        panel.setVisible(true);
    }

    // MODIFIES: b
    // EFFECTS: changes b such that nothing happens when the user clicks it and it appears greyed out
    private void toggleButton(JButton b) {
        b.setBackground(PomodoroApp.COLOUR_NEUTRAL);
        b.removeActionListener(this);
    }

    // MODIFIES: b
    // EFFECTS: changes b such that an event is fired when the user clickes it and its colour is set to c
    private void toggleButton(JButton b, Color c) {
        b.setBackground(c);
        b.addActionListener(this);
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

        String timeAsString = addZeroes(numMin) + ":" + addZeroes(numSec);
        time.setText(timeAsString);
    }

    // EFFECTS: returns bold and larger version of given font
    private Font getBigBoldFont(Font f) {
        return new Font(f.getFontName(), Font.BOLD, f.getSize() * 4);
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
        switch (pomodoroStatus) {
            case WORK:
                message.setText("Time to take a break");
                break;
            case BREAK:
                message.setText("Time to work");
                break;
            case LONG_BREAK:
                message.setText("Completed pomodoro!");
                break;
        }
        toggleButton(nextButton, PomodoroApp.COLOUR_START);
        toggleButton(dismissButton, PomodoroApp.COLOUR_NEUTRAL);
    }

    // MODIFIES: counter, time, message, filler, button, panel
    // EFFECTS: if button was pressed, notify observers. Else, update display
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            setChanged();
            notifyObservers(TimerStatus.NOTIF_NEXT); // tell observers that user has asked for the next phase
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
        panel.remove(time);
        panel.remove(message);
        panel.remove(nextButton);
        panel.remove(dismissButton);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;

        gbc.gridwidth = 2;
        gbc.gridy = 0;
        panel.add(time, gbc);
        gbc.gridy = 1;
        panel.add(message, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        panel.add(nextButton, gbc);
        gbc.gridx = 1;
        panel.add(dismissButton, gbc);

        panel.validate();
        panel.repaint();
    }
}
