package ui;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private JCheckBox loopCheckBox;
    private JTextField lengthWorkField;
    private JTextField lengthShortBreakField;
    private JTextField lengthLongBreakField;
    private JTextField numRepsField;

    private JLabel loopCheckLabel;
    private JLabel lengthWorkLabel;
    private JLabel lengthShortBreakLabel;
    private JLabel lengthLongBreakLabel;
    private JLabel numRepsFieldLabel;

    public SettingsPanel() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        initFields();
        setup();
    }

    private void initFields() {
        loopCheckBox = new JCheckBox();
        lengthWorkField = new JTextField();
        lengthShortBreakField = new JTextField();
        lengthLongBreakField = new JTextField();
        numRepsField = new JTextField();

        loopCheckLabel = new JLabel("loop indefinitely");
        lengthWorkLabel = new JLabel("length of work period (minutes): ");
        lengthShortBreakLabel = new JLabel("length of short break (minutes): ");
        lengthLongBreakLabel = new JLabel("length of long break (minutes): ");
        numRepsFieldLabel = new JLabel("number of repetitions: ");
    }

    private void setup() {
        JPanel loopCheckPanel = new JPanel();
        loopCheckPanel.setLayout(new FlowLayout());
        loopCheckPanel.add(loopCheckBox);
        loopCheckPanel.add(loopCheckLabel);

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new GridLayout(4,2));
        textFieldPanel.add(lengthWorkLabel);
        textFieldPanel.add(lengthWorkField);
        textFieldPanel.add(lengthShortBreakLabel);
        textFieldPanel.add(lengthShortBreakField);
        textFieldPanel.add(lengthLongBreakLabel);
        textFieldPanel.add(lengthLongBreakField);
        textFieldPanel.add(numRepsFieldLabel);
        textFieldPanel.add(numRepsField);

        this.add(loopCheckPanel);
        this.add(textFieldPanel);
    }

    private boolean fieldValid(JTextField f) {
        String input = f.getText();
        if (input.length() == 0) {
            return true;
        }

        try {
            Integer.parseInt(input);
            f.setBackground(Color.white);
            return true;
        } catch (NumberFormatException ex) {
            f.setBackground(Color.yellow);
            return false;
        }
    }

    public boolean allInputsValid() {
        boolean valid = fieldValid(lengthWorkField) && fieldValid(lengthShortBreakField) && fieldValid(lengthLongBreakField);
        if (!loopCheckBox.isSelected()) {
            valid = valid && fieldValid(numRepsField);
        }

        return valid;
    }

    // REQUIRES: input is empty or can be parsed to an int
    public int getLengthWork () {
        String input = lengthWorkField.getText();
        if (input.length() == 0) {
            return PomodoroApp.DEFAULT_WORK_DURATION;
        } else {
            return Integer.parseInt(input);
        }
    }

    // REQUIRES: input is empty or can be parsed to an int
    public int getLengthShortBreak() {
        String input = lengthShortBreakField.getText();
        if (input.length() == 0) {
            return PomodoroApp.DEFAULT_SHORT_BREAK_DURATION;
        } else {
            return Integer.parseInt(input);
        }
    }

    // REQUIRES: input is empty or can be parsed to an int
    public int getLengthLongBreak() {
        String input = lengthLongBreakField.getText();
        if (input.length() == 0) {
            return PomodoroApp.DEFAULT_LONG_BREAK_DURATION;
        } else {
            return Integer.parseInt(input);
        }
    }

    // REQUIRES: input is empty or can be parsed to an int
    public int getNumReps() {
        String input = numRepsField.getText();
        if (input.length() == 0) {
            return PomodoroApp.DEFAULT_NUM_REPS;
        } else {
            return Integer.parseInt(input);
        }
    }

    public boolean infinite() {
        return loopCheckBox.isSelected();
    }
}
