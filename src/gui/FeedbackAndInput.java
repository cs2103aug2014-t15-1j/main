//@author A0118846W
package gui;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * FeedbackAndInput Composite contains controls that are involved in taking user input and
 * displaying feedback. The children of FeedbackAndInput are BlockLabelUI, commandLine and feedback
 */
public class FeedbackAndInput extends Composite{

    private static final String MESSAGE_TYPE_HERE = "Welcome to Haystack! Type here.";
    private static final String WELCOME_MESSAGE = "Don't know where to start? Press F1 for help!";
    
    private FontRegistry registry;
    private static Text commandLine;
    private static StyledText feedback;
    
    /**
     * Creates the FeedbackAndInput Composite and its children
     * @param parent Composite where FeedbackInput Composite is located
     * @param style  Style that FeedbackInput Composite will use
     */
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
        feedback.setForeground(color);
        
        feedback.setEnabled(false);
    }
    

    private void formatCommandLine() {
        Display display = this.getDisplay();
        Color black = display.getSystemColor(SWT.COLOR_BLACK);
        Color bg = new Color(Display.getCurrent(), 242, 255, 237);
        commandLine.setBackground(bg);
        commandLine.setForeground(black);
        commandLine.setFont(registry.get("commandLine"));
        bg.dispose();
    }
}
