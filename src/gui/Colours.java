package gui;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class Colours {
    
    private static Color deletedColor;
    private static Color todoColor;
    
    public Colours(Display display){
        createColours(display);
    }
    
    public static Color getDeletedColor(){
        return deletedColor;
    }
    
    public static Color getToDoColor(){
        return todoColor;
    }
    
    private void createColours(Display display){
        deletedColor = new Color(display,  178, 40, 7);   
        todoColor = new Color(display, 0, 133, 86);
    }
    
    public static void disposeAllColours(){
        deletedColor.dispose();
        todoColor.dispose();
    }
}   

