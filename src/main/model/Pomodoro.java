package model;

import ui.PomodoroApp;

import java.util.Observable;
import java.util.Observer;

/*
 *  Representation of a single pomodoro.
 */
public class Pomodoro extends Observable implements Observer {
    private int numReps; // number of work periods
    private Status status; // current status

    public Pomodoro() {
        numReps = PomodoroApp.NUM_REPS;
    }

    // MODIFIES: numReps, status
    // EFFECTS: starts the pomodoro with a work phase
    public void start() {
        scheduleWork();
    }

    // MODIFIES: numReps, status
    // EFFECTS: starts a work phase
    private void scheduleWork() {
        numReps--;
        status = Status.WORK;
    }

    // MODIFIES: status
    // EFFECTS: starts a break phase
    private void scheduleBreak() {
        if (numReps <= 0) {
            status = Status.LONG_BREAK;
        } else {
            status = Status.BREAK;
        }
    }

    // MODIFIES: numReps, status
    // EFFECTS: switches to the next pomodoro phase
    private void next() {
        System.out.println("pomodoro notified");
        if (numReps < 0 && (status != Status.WORK)) { // done the pomodoro
            status = Status.DONE;
        } else if (status == Status.WORK) { // just finished a work period
            scheduleBreak();
        } else { // just finished a break period
            scheduleWork();
        }
        setChanged();
        notifyObservers(status);
    }

    // MODIFIES: status
    // EFFECTS: switches to the next pomodoro phase
    @Override
    public void update(Observable o, Object arg) {
        next();
    }
}
