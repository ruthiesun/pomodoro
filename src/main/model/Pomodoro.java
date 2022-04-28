package model;

import ui.PomodoroApp;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;

/*
 *  Representation of a single pomodoro.
 *   Schedules pomodoro components according to default settings and notifies PomodoroApp once everything is done.
 */
public class Pomodoro implements Observer {
    private Timer timer;

    private int workDuration; // duration of work period in minutes
    private int breakDuration; // duration of break period in minutes
    private int longBreakDuration; // duration of long break period in minutes
    private int numReps; // number of work periods
    private boolean currentlyWorking;
    //public static Work workTimer;
    //public static Break breakTimer;

    // EFFECTS: sets taskName to name and set default times for work, breaks, and repetitions
    public Pomodoro() {
        timer = new Timer();
        currentlyWorking = false;

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
    // EFFECTS: starts a work timer
    private void scheduleWork() {
        numReps--;
        if (numReps < 0) {
            scheduleLongBreak();
        } else {
            currentlyWorking = true;
            notifyObservers(Status.DONE_BREAK);
            workTimer = new Work(workDuration);
            timer.scheduleAtFixedRate(workTimer, 0, 1000);
        }
    }

    // MODIFIES: breakTimer, currentlyWorking
    // EFFECTS: starts a short break timer
    private void scheduleShortBreak() {
        currentlyWorking = false;
        notifyObservers(Status.DONE_WORK);
        breakTimer = new Break(breakDuration);
        timer.scheduleAtFixedRate(breakTimer, 0, 1000);
    }

    // MODIFIES: breakTimer, currentlyWorking
    // EFFECTS: starts a long break timer
    private void scheduleLongBreak() {
        currentlyWorking = false;
        notifyObservers(Status.DONE_WORK);
        breakTimer = new Break(longBreakDuration);
        timer.scheduleAtFixedRate(breakTimer, 0, 1000);

    }

    public void updateTime() {

    }

    // MODIFIES: numReps, workTimer, breakTimer
    // EFFECTS: watches for notification dismissals in GUI and schedules next appropriate task
    public void next() {
        // if user dismissed notif...
        if (numReps < 0 && !currentlyWorking) { // done the pomodoro
            workTimer.cancel();
            breakTimer.cancel();
            notifyObservers(Status.DONE_ALL);
        } else if (currentlyWorking) { // just finished a work period
            workTimer.cancel();
            scheduleShortBreak();
        } else { // just finished a break period
            breakTimer.cancel();
            scheduleWork();
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
