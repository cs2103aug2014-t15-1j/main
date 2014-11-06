package gui;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class Colours {
    
    private static Color deletedColor;
    private static Color todoColor;
    private static Color tabFolderColor;
    private static Color overdueColor;
    
    public Colours(Display display){
        createColours(display);
    }
    
    public static Color getDeletedColor(){
        return deletedColor;
    }
    
    public static Color getToDoColor(){
        return todoColor;
    }
    
    public static Color getTabFolderColour(){
        return tabFolderColor;
    }
    
    public static Color getOverdueColour(){
        return overdueColor;
    }
 
    private void createColours(Display display){
        deletedColor = new Color(display,  178, 40, 7);   
        todoColor = new Color(display, 0, 133, 86);
        tabFolderColor = new Color(display, 220, 220, 220);
        overdueColor = new Color(display, 255, 0, 0);
    }
    
    public static void disposeAllColours(){
        deletedColor.dispose();
        todoColor.dispose();
        tabFolderColor.dispose();
    }
}   

