package gui;

import logic.Processor;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class FeedbackAndInput extends Composite {

    private static final String MESSAGE_TYPE_HERE = "Type here";
    private static final String WELCOME_MESSAGE = "Welcome.";

    private static final String ASK_CONFIRM_DELETE = "This will erase all data, PERMANENTLY.  Key 'y' to continue or 'n' to abort";
    private static final String INVALID_INPUT = "Invalid Input.";
    private static final String SUCCESSFUL_DELETE_ALL = "Erased all data!";
    private static final String UNSUCCESSFUL_DELETE_ALL = "Did not delete anything";

    private static final String CONFIRM = "yes";
    private static final String NO_CONFIRM = "no";

    private static String CODE_EXIT = "exit";

    private FontRegistry registry;
    private Text commandLine;
    private StyledText feedback;
    private boolean isReplyToConfrimation = false;
    private static ResultGenerator resultGenerator = ResultGenerator
            .getInstance();

    public FeedbackAndInput(Composite parent, int style) {
        super(parent, style);
        this.setToolTipText("I am a composite");
        setLayout(parent);
        buildControls();
    }

    private void setLayout(Composite parent) {
        GridLayout layout = new GridLayout();
        this.setLayout(layout);
        this.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    private void buildControls() {
        formatRegistry();
        buildCommandLineUI();
        buildFeedbackUI();
        addListeners();
    }

    private void formatRegistry() {
        registry = new FontRegistry(this.getDisplay());
        FontData font = new FontData("New Courier", 11, SWT.NORMAL);
        FontData[] fontData = new FontData[] { font };
        registry.put("type box", fontData);

        fontData = new FontData[] { new FontData("Arial", 11, SWT.NORMAL) };
        registry.put("feedback", fontData);
    }

    private void buildCommandLineUI() {
        commandLine = new Text(this, SWT.SINGLE);

        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        commandLine.setLayoutData(gridData);

        commandLine.setText(MESSAGE_TYPE_HERE);

        formatText(commandLine);
        commandLine.setFocus();

    }

    private void formatText(Text commandLine) {
        Display display = this.getDisplay();
        Color black = display.getSystemColor(SWT.COLOR_BLACK);
        Color white = display.getSystemColor(SWT.COLOR_WHITE);
        commandLine.setBackground(black);

        commandLine.setForeground(white);
        commandLine.setFont(registry.get("type box"));
    }

    private void buildFeedbackUI() {
        feedback = new StyledText(this, SWT.MULTI);
        feedback.setText(WELCOME_MESSAGE);

        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        feedback.setLayoutData(gridData);

        feedback.setFont(registry.get("feedback"));
        Color color = this.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        feedback.setForeground(color);
        feedback.setEnabled(false);
    }

    private void addListeners() {
        // This listener will only be activated at the start of the program
        commandLine.addListener(SWT.KeyDown, new Listener() {
            @Override
            public void handleEvent(Event event) {

                switch (event.type) {
                    case SWT.KeyDown:
                        commandLine.setText("");
                        commandLine.removeListener(SWT.KeyDown, this);
                        break;
                }
            }
        });

        // these listeners are activated throughout the program
        commandLine.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String input = commandLine.getText();
                if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN) {
                    String output = "";
                    switch (e.keyCode) {
                        case SWT.ARROW_UP:
                            output = Processor.getInstance().fetchInputUpKey();
                            break;
                        case SWT.ARROW_DOWN:
                            output = Processor.getInstance()
                                    .fetchInputDownKey();
                            break;
                        default:
                            // ignore
                    }
                    commandLine.setText(output);
                }
                giveSuggestions(input);
            }
        });

        commandLine.addListener(SWT.DefaultSelection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                String input = commandLine.getText();
                commandLine.setText("");

                if (isReplyToConfrimation) {
                    processReply(input);
                } else if (input.trim().isEmpty()) {
                    // do nothing
                } else {
                    processInput(input);
                }
            }
        });
    }

    protected void processReply(String input) {
        if (!isValidDeleteAll(input)) {
            feedback.setText(INVALID_INPUT);

        }
        if (resultGenerator.processDeleteAll(input)) {
            feedback.setText(SUCCESSFUL_DELETE_ALL);
        } else {
            feedback.setText(UNSUCCESSFUL_DELETE_ALL);
        }

        isReplyToConfrimation = false;
    }

    private boolean isValidDeleteAll(String input) {
        String inputLowerCase = input.toLowerCase();
        if (inputLowerCase.equals(CONFIRM) || inputLowerCase.equals("y")) {
            return true;
        }

        if (inputLowerCase.equals(NO_CONFIRM) || inputLowerCase.equals("n")) {
            return true;
        }

        return false;
    }

    protected void processInput(String input) {
        String output = resultGenerator.sendInput(input);

        if (output.equals(CODE_EXIT)) {
            exitProgram();
        } else if (output.equals(ASK_CONFIRM_DELETE)) {
            feedback.setText(ASK_CONFIRM_DELETE);
            isReplyToConfrimation = true;

        } else if (output.equals("Help")) {
            new HelpDialog(this.getShell());

        } else {

            feedback.setText(output);
        }
    }

    private void exitProgram() {
        System.exit(0);
    }

    protected void giveSuggestions(String input) {

        if (input.isEmpty()) {
            return;
        }

        Character firstLetter = input.charAt(0);
        switch (Character.toLowerCase(firstLetter)) {
            case 'a':
                feedback.setText("add [name] due [DD/MM/YYYY hhmm] start [DD/MM/YYYY hhmm] #tag");
                return;
            case 'b':
                feedback.setText("block [DD/MM/YYYY hhmm] to [DD/MM/YYYY hhmm]");
                return;
            case 'd':
                if (!isOutOfBounds(input, 1) &&
                    Character.toLowerCase(input.charAt(1)) == 'i') {
                    feedback.setText("display");
                    return;
                } else if (!isOutOfBounds(input, 1) &&
                           Character.toLowerCase(input.charAt(1)) == 'o') {
                    feedback.setText("done");
                    return;
                }
                feedback.setText("delete [id] ");

                return;
            case 'e':
                if (!isOutOfBounds(input, 1) &&
                    Character.toLowerCase(input.charAt(1)) == 'x') {
                    feedback.setText("exit");
                    return;
                }
                feedback.setText("edit [id] <parameter>  [value]");
                return;
            case 'h':
                feedback.setText("help");
                return;
            case 'r':
                if (!isOutOfBounds(input, 2) &&
                    Character.toLowerCase(input.charAt(2)) == 'd') {
                    feedback.setText("redo [id]");
                    return;
                } else if (!isOutOfBounds(input, 3) &&
                           (Character.toLowerCase(input.charAt(3)) == 'e')) {
                    feedback.setText("reset");
                    return;
                }
                feedback.setText("restore [id]");

                return;
            case 's':
                if (!isOutOfBounds(input, 1) &&
                    Character.toLowerCase(input.charAt(1)) == 'h') {
                    feedback.setText("show");
                    return;
                }
                feedback.setText("search [id]");
                return;
            case 't':
                feedback.setText("todo [id]");
                return;
            case 'u':
                if (!isOutOfBounds(input, 2) &&
                    (Character.toLowerCase(input.charAt(2)) == 'b')) {
                    feedback.setText("unblock [DD/MM/YYYY hhmm] to [DD/MM/YYYY hhmm]");
                    return;
                }
                feedback.setText("undo");
                return;

        }
    }

    protected boolean isOutOfBounds(String input, int index) {
        if (input.length() - 1 < index) {
            return true;
        }
        return false;
    }

}
