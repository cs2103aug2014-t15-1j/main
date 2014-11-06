package gui;

import java.util.Calendar;
import java.util.Date;
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
 * The singleton pattern is used, so every instance of this class refers to the same instance
 */
public class TimeUI{
    // use swt timer
    // refresh table every minute
    // key up
    // toggle visibility
    private StyledText time;
    private  static TimeUI timeUI;
    /**
     * Creates an instance of TimeUI
     * @param parent Composite where TimeUI is located
     */
    private TimeUI(final Composite parent){
        time = new StyledText(parent, SWT.CENTER | SWT.MULTI | SWT.H_SCROLL);
        setLayout();
        time.setWordWrap(true);
        time.setText("  Time now is   :  ");
        format(parent.getDisplay());
        Runnable timer = new Runnable(){
            public void run(){
                if(!parent.getShell().isDisposed()){
                    
                parent.getDisplay().timerExec(1000, this);
                Calendar cal = Calendar.getInstance();
                String timeNow = cal.getTime().toString();
                time.setText(timeNow);
                
                if (cal.get(Calendar.SECOND) == 0) {
                    
                    (new TableManagement()).refreshTables();
                }
                
                }
            }
        };
        if(!parent.getShell().isDisposed()){
        parent.getDisplay().timerExec(1000, timer);
        }
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

    private void format(Display display) {
         setFontOfTimeUI();        
         setColour(display);
    }

    private void setColour(Display display) {
        Color color = display.getSystemColor(SWT.COLOR_BLACK);
        time.setForeground(color);
        //color = display.getSystemColor(SWT.COLOR_WHITE);
        //time.setBackground(color);
        Color bg = new Color(Display.getCurrent(),252,252,247);
        time.setBackground(bg);
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
       gridData.horizontalAlignment = SWT.CENTER;
       time.setLayoutData(gridData);
    }
}
