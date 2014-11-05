package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

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

import database.BlockDate;
/**
 * FeedbackAndInput Composite contains controls that are involved in taking user input and
 * displaying feedback. The children of FeedbackAndInput are BlockLabelUI, commandLine and feedback
 */
//@author A0118846W
public class FeedbackAndInput extends Composite{

    private static final String MESSAGE_TYPE_HERE = "Type here";
    private static final String WELCOME_MESSAGE = "Welcome.";
    
    private FontRegistry registry;
    private static FeedbackAndInput feedbackAndInput;
    
    private static Text commandLine;
    private static StyledText feedback;

    public FeedbackAndInput(Composite parent, int style) {
        super(parent, style);
        setLayout();
        buildControls();
    }
    
    public static Text getCommandLine(){
        assert(commandLine!=null);
        return commandLine;
    }
    
    public static StyledText getFeedback(){
        assert(feedback!= null);
        return feedback;
    }
    
    private void setLayout() {
        GridLayout layout = new GridLayout();
        this.setLayout(layout);
        this.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }
    
    private void buildControls() {
        getRegistry();
        BlockLabelUI.getInstance(this);
        buildCommandLineUI();
        buildFeedbackUI();
    }

    private void getRegistry() {
        registry = Fonts.getRegistry();
    }
    
    private void buildCommandLineUI() {
        commandLine = new Text(this, SWT.SINGLE);

        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        commandLine.setLayoutData(gridData);

        commandLine.setText(MESSAGE_TYPE_HERE);
        formatCommandLine();
        commandLine.setFocus();
    }

    private void buildFeedbackUI() {
        feedback = new StyledText(this, SWT.MULTI);
        feedback.setText(WELCOME_MESSAGE);

        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        feedback.setLayoutData(gridData);

        feedback.setFont(registry.get("feedback"));
        Color color = this.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        
        feedback.setEnabled(false);
        
        feedback.setForeground(color);
    }
    

    private void formatCommandLine() {
        Display display = this.getDisplay();
        Color black = display.getSystemColor(SWT.COLOR_BLACK);
        Color white = display.getSystemColor(SWT.COLOR_WHITE);
        commandLine.setBackground(black);
        commandLine.setForeground(white);
        commandLine.setFont(registry.get("commandLine"));
    }
}
