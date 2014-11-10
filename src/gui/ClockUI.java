package gui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * ClockUI is the user interface that shows the system time.
 */
// @author A0118846W
public class ClockUI{
    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");
    private StyledText time;
    
    /**
     * Creates an instance of ClockUI
     * @param parent Composite where ClockUI is located
     */
    public ClockUI(final Composite parent){
        time = new StyledText(parent, SWT.CENTER | SWT.MULTI | SWT.READ_ONLY);
        setLayout();
        time.setWordWrap(true);
       time.setText("Time now is   :  10 : 45 " + LINE_SEPARATOR +" Saturday  date place holder");
        format(parent.getDisplay());
        startTimer(parent);
    }
    
    /**
     * Starts and runs the clock continuously until the program is closed
     * @param parent shell where the timer is located
     */
    private void startTimer(final Composite parent) {
        Runnable timer = new Runnable(){
            public void run(){
                if(parent.isDisposed() || parent.getShell().isDisposed()){
                return;
                }
                parent.getDisplay().timerExec(1000, this);
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat formatTime = new SimpleDateFormat("EEEE h:mm:ss");
                SimpleDateFormat formatDate = new SimpleDateFormat("dd MMMM YYYY zz");
                Date now = cal.getTime();
                String timeNow = formatTime.format(now).toString();
                String dateNow = formatDate.format(now);
                String dateTime = timeNow + LINE_SEPARATOR + dateNow;
                time.setText(dateTime);
                
                if (cal.get(Calendar.SECOND) == 0) {
                    
                    (new TableManagement()).refreshTables();
                }
                
            }
        };
        if(!parent.getShell().isDisposed()){
        parent.getDisplay().timerExec(1000, timer);
        }
    }
    
    private void format(Display display) {
         setFontOfClockUI();        
         setColour(display);
    }

    private void setColour(Display display) {
        Color color = display.getSystemColor(SWT.COLOR_BLACK);
        time.setForeground(color);
        Color bg = new Color(Display.getCurrent(),252,252,247);
        time.setBackground(bg);
    }

    private void setFontOfClockUI() {
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
