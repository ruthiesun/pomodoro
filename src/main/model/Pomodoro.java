package model;

import ui.PomodoroApp;

import java.util.Observable;
import java.util.Observer;

/*
 *  Representation of a single pomodoro.
 *   Schedules pomodoro components according to default settings and notifies PomodoroApp once everything is done.
 */
public class Pomodoro extends Observable implements Observer {
    private int workDuration; // duration of work period in minutes
    private int breakDuration; // duration of break period in minutes
    private int longBreakDuration; // duration of long break period in minutes
    private int numReps; // number of work periods
    private Status status;

    // EFFECTS: sets taskName to name and set default times for work, breaks, and repetitions
    public Pomodoro() {
        status = Status.BREAK;

        // set times in minutes
        workDuration = PomodoroApp.WORK_DURATION;
        breakDuration = PomodoroApp.BREAK_DURATION;
        longBreakDuration = PomodoroApp.LONG_BREAK_DURATION;
        numReps = PomodoroApp.NUM_REPS;
    }

    // MODIFIES: numReps, workTimer
    // EFFECTS: starts the timer with a work period
    public void start() {
        scheduleWork();
    }

    // MODIFIES: numReps, workTimer, currentlyWorking
    // EFFECTS: if still have work periods, starts a work timer; else, starts a long break
    private void scheduleWork() {
        numReps--;
        status = Status.WORK;
    }

    // MODIFIES: breakTimer, currentlyWorking
    // EFFECTS: starts a break timer
    private void scheduleBreak() {
        if (numReps <= 0) {
            status = Status.LONG_BREAK;
        } else {
            status = Status.BREAK;
        }
    }

    // MODIFIES: numReps, workTimer, breakTimer
    // EFFECTS: goes to next phase of the pomodoro
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

    @Override
    public void update(Observable o, Object arg) {
        next(); // switch to next timer
    }
}
