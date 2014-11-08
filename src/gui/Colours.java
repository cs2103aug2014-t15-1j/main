package gui;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
/**
 * This class contains colours that was used throughout the application.
 */
//@author A0118846W
public class Colours {
    
    private static Color deletedColor;
    private static Color todoColor;
    private static Color tabFolderColor;
    private static Color overdueColor;
    private static Color blockDateColor;
    private static Color doneColor;
    
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
 
    public static Color getBlockDateColor(){
        return blockDateColor;
    }
    
    public static Color getDoneColor(){
        return doneColor;
    }
    
    private void createColours(Display display){
        deletedColor = new Color(display,  255, 0, 0);   
        todoColor = new Color(display, 146, 217, 110);
        tabFolderColor = new Color(display, 220, 220, 220);
        overdueColor = new Color(display, 255, 49, 117);
        blockDateColor = new Color(display, 107, 229, 252);
        doneColor = new Color(display, 252, 183, 117);
    } 
    
    public static void disposeAllColours(){
        deletedColor.dispose();
        todoColor.dispose();
        tabFolderColor.dispose();
        overdueColor.dispose();
        blockDateColor.dispose();
        doneColor.dispose();
    }
}   

