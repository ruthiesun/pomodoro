package model;

// TimerTaskBase for break period in a Pomodoro
public class Break extends TimerTaskBase {
    public Break(int counter) {
        super(counter);
    }

    @Override
    protected void displayTime() {
        System.out.println("time to take a break");
    }

    @Override
    protected void displayNotif() {
        System.out.println("time to work");
    }

    @Override
    public void run() {
        System.out.println("taking a break...");
        super.run();
    }
}
