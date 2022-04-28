package ui;

import model.Status;

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

    private int counter;

    private JPanel panel;
    private JTextField time;
    private JTextField message;
    private JButton button;

    private Timer timer;

    public TimerPanel(Status status) {
        super();

        switch (status) {
            case WORK:
                counter = PomodoroApp.WORK_DURATION;
                message = new JTextField("time to work");
                break;
            case BREAK:
                counter = PomodoroApp.BREAK_DURATION;
                message = new JTextField("time to take a break");
                break;
            case LONG_BREAK:
                counter = PomodoroApp.LONG_BREAK_DURATION;
                message = new JTextField("time to take a long break");
                break;
        }

        time = new JTextField(counter);
        button = new JButton("next");
        button.addActionListener(this);
        button.setVisible(false);

        panel = new JPanel();
        panel.setLayout(new GridLayout(0,1));
        panel.add(time);
        panel.add(message);
        panel.add(button);

        panel.setVisible(true);

        startTimer();
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
        time = new JTextField("count: " + Integer.toString(counter));
    }

    // MODFIIES: message, button
    // EFFECTS: makes button visible and updates message
    private void displayNotif() {
        message = new JTextField("done this phase");
        button.setVisible(true);
    }

    // MODIFIES: counter, time, message, filler, button, panel
    // EFFECTS: if button was pressed, notify observers. Else, update display
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            setChanged();
            notifyObservers(); // tell observers that user has dismissed notif
        } else { // timer has ticked
            System.out.println(counter);
            if (counter==0) {
                displayTime();
                displayNotif();
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
    private void refresh() {
        panel.removeAll();
        panel.add(time);
        panel.add(message);
        panel.add(button);
        panel.validate();
        panel.repaint();
    }
}
