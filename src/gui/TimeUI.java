package gui;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import logic.Processor;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import parser.DateParser;

public class TimeUI implements Observer {
    
    private StyledText time;
    
    public TimeUI(Composite parent){
        time = new StyledText(parent, SWT.NONE);
        setLayout();
        format(parent.getDisplay()); 
        time.setText(DateParser.getCurrTimeStr());
    }

    private void format(Display display) {
         setFont(display);        
         setColour(display);
    }

    private void setColour(Display display) {
        Color color = display.getSystemColor(SWT.COLOR_WHITE);        
         time.setForeground(color);
    }

    private void setFont(Display display) {
        FontRegistry registry = setFontRegistry(display);
         time.setFont(registry.get("time"));
    }
    
    private FontRegistry setFontRegistry(Display display){
        FontRegistry registry = new FontRegistry(display);
        FontData font = new FontData("Courier New", 14, SWT.NORMAL);
        FontData[] fontData = new FontData[] { font };
        registry.put("time", fontData);
        return registry;
    }

    private void setLayout() {
       GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
       time.setLayoutData(gridData);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg.equals("clock")){
            String currTime = Calendar.getInstance().getTime().toString();
            time.setText(currTime);
        }
    }

}
