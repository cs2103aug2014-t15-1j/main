package gui;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * This class contains all the fonts used through out the application.
 */
// @author A0118846W
public class Fonts {
    private static FontRegistry registry;

    public Fonts(Composite parent) {
        registry = new FontRegistry(parent.getDisplay());
        addSystemFonts();
    }
    
    public static FontRegistry getRegistry() {
        assert(registry!= null);
        return registry;
    }
/**
 * Adds the fonts from the system that will be used in the application
 */
    private void addSystemFonts() {
        FontData[] fontData = new FontData[] {new FontData ("Consolas", 12, SWT.BOLD) };
        registry.put("commandLine", fontData);

        fontData = new FontData[] { new FontData("Consolas", 11, SWT.NORMAL) };
        registry.put("feedback", fontData);
        
        fontData = new FontData[] { new FontData("Consolas", 9,SWT.BOLD)};
        registry.put("dates", fontData);
        
        fontData = new FontData[] { new FontData("Courier New", 10,
                SWT.BOLD | SWT.UNDERLINE_SINGLE) };
        registry.put("title", fontData);
        
        // adds fonts specific to screen resolution
        int screenWidth = Display.getCurrent().getBounds().width;
        if (screenWidth>= 1920) {
            fontData = new FontData[] { new FontData("Impact", 18, SWT.NORMAL) };
            registry.put("haystack title", fontData);
            
            // TODO: Re-check font compatibility
            fontData = new FontData[] { new FontData("Consolas", 10, SWT.NONE) };
            registry.put("table", fontData);
            
            fontData = new FontData[] { new FontData("Consolas", 10, SWT.BOLD) };
            registry.put("table status", fontData);
        } else {
            fontData = new FontData[] { new FontData("Impact", 16, SWT.NORMAL) };
            registry.put("haystack title", fontData);
            
            fontData = new FontData[] { new FontData("Consolas", 8, SWT.NONE) };
            registry.put("table", fontData);
            
            fontData = new FontData[] { new FontData("Consolas", 8, SWT.BOLD) };
            registry.put("table status", fontData);
        }
        
        fontData = new FontData[] { new FontData("Arial", 11, SWT.NORMAL | SWT.BOLD) };
        registry.put("helpsheet", fontData);

        fontData = new FontData[] { new FontData("Verdana", 12, SWT.BOLD) };
        registry.put("list headers", fontData);
        
        fontData = new FontData[] { new FontData("Tahoma", 10, SWT.CENTER) };
        registry.put("time", fontData);
        
        fontData = new FontData[] { new FontData("Consolas",
                                                 11, SWT.NORMAL) };
        registry.put("list", fontData);
                                                                      
    }
}
