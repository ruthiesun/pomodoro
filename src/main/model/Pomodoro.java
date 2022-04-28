package model;

import ui.TimerStatus;

import java.util.Observable;
import java.util.Observer;

/*
 *  Representation of a single pomodoro.
 */
public class Pomodoro extends Observable implements Observer {
    private int numReps; // number of work periods
    private int numRepsInitial;
    private PomodoroStatus pomodoroStatus; // current pomodoroStatus
    private boolean infinite;

    public Pomodoro(int numReps, boolean infinite) {
        this.numReps = numReps;
        this.numRepsInitial = numReps;
        this.infinite = infinite;
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
            if (infinite) {
                numReps = numRepsInitial;
                start();
            } else {
                pomodoroStatus = PomodoroStatus.DONE;
            }
        } else if (pomodoroStatus == PomodoroStatus.WORK) { // just finished a work period
            scheduleBreak();
        } else { // just finished a break period
            scheduleWork();
        }
        setChanged();
        notifyObservers(pomodoroStatus);
    }

    // REQUIRES: arg is a TimerStatus
    // MODIFIES: pomodoroStatus
    // EFFECTS: switches to the next pomodoro phase
    @Override
    public void update(Observable o, Object arg) {
        switch ((TimerStatus) arg) {
            case NOTIF_OFF:
                next();
                break;
        }
    }
}
