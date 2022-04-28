package model;

import ui.PomodoroApp;

import java.util.Observable;
import java.util.Observer;

/*
 *  Representation of a single pomodoro.
 */
public class Pomodoro extends Observable implements Observer {
    private int numReps; // number of work periods
    private PomodoroStatus pomodoroStatus; // current pomodoroStatus

    public Pomodoro() {
        numReps = PomodoroApp.NUM_REPS;
    }

    // MODIFIES: numReps, pomodoroStatus
    // EFFECTS: starts the pomodoro with a work phase
    public void start() {
        scheduleWork();
    }

    // MODIFIES: numReps, pomodoroStatus
    // EFFECTS: starts a work phase
    private void scheduleWork() {
        numReps--;
        pomodoroStatus = PomodoroStatus.WORK;
    }

    // MODIFIES: pomodoroStatus
    // EFFECTS: starts a break phase
    private void scheduleBreak() {
        if (numReps <= 0) {
            pomodoroStatus = PomodoroStatus.LONG_BREAK;
        } else {
            pomodoroStatus = PomodoroStatus.BREAK;
        }
    }

    // MODIFIES: numReps, pomodoroStatus
    // EFFECTS: switches to the next pomodoro phase
    private void next() {
        if (numReps <= 0 && (pomodoroStatus != PomodoroStatus.WORK)) { // done the pomodoro
            pomodoroStatus = PomodoroStatus.DONE;
        } else if (pomodoroStatus == PomodoroStatus.WORK) { // just finished a work period
            scheduleBreak();
        } else { // just finished a break period
            scheduleWork();
        }
        setChanged();
        notifyObservers(pomodoroStatus);
    }

    // MODIFIES: pomodoroStatus
    // EFFECTS: switches to the next pomodoro phase
    @Override
    public void update(Observable o, Object arg) {
        switch ((TimerStatus) arg) {
            case NOTIF_OFF:
                next();
        }
    }
}
