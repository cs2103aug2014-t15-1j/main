package gui;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class Colours {
    
    private static Color deletedColor;
    private static Color todoColor;
    private static Color tabFolderColor;
    
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
 
    private void createColours(Display display){
        deletedColor = new Color(display,  178, 40, 7);   
        todoColor = new Color(display, 0, 133, 86);
        tabFolderColor = new Color(Display.getDefault(), 220, 220, 220);
    }
    
    public static void disposeAllColours(){
        deletedColor.dispose();
        todoColor.dispose();
        tabFolderColor.dispose();
    }
}   

