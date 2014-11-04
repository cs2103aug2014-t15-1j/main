package gui;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import parser.DateParser;

/**
 * TimeUI is the user interface that shows the system time.
 * TimeUI implementes the Observer interface, it observes logic.Processor
 * The singleton pattern is used, so every instance of this class refers to the same instance
 * @author Sharon, Yao Xiang
 */
public class TimeUI implements Observer {
    
    private StyledText time;
    private  static TimeUI timeUI;
    /**
     * Creates an instance of TimeUI
     * @param parent Composite where TimeUI is located
     */
    private TimeUI(Composite parent){
        time = new StyledText(parent, SWT.NONE);
        setLayout();
        format(parent.getDisplay()); 
        time.setText(DateParser.getCurrTimeStr());
    }
    
    /**
     * Returns an instance of timeUI, creates an instance if it does not exist
     * @param parent Composite where timeUI is located
     * @return an instance of timeUI
     */
    public static TimeUI getInstance(Composite parent){
        if(timeUI == null){
            timeUI = new TimeUI(parent);
        }
        return timeUI;
    }
    
    /**
     * Returns an instance of timeUI. This method should only be called after timeUI has been created.
     * Otherwise, an assertion failure will occur
     * @return an instance of timeUI
     */
    public static TimeUI getInstance(){
        assert(timeUI != null);
        return timeUI;
    }
    
    /**
     * updates the timeUI interface, to show the current time
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg.equals("clock")){
            String currTime = Calendar.getInstance().getTime().toString();
            time.setText(currTime);
        }
    }

    private void format(Display display) {
         setFontOfTimeUI();        
         setColour(display);
    }

    private void setColour(Display display) {
        Color color = display.getSystemColor(SWT.COLOR_WHITE);        
         time.setForeground(color);
    }

    private void setFontOfTimeUI() {
        FontRegistry registry = getFontRegistry();
        time.setFont(registry.get("time"));
    }
    
    private FontRegistry getFontRegistry(){
        return Fonts.getRegistry();
    }

    private void setLayout() {
       GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
       time.setLayoutData(gridData);
    }
}
