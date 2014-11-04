package gui;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;

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
        FontData[] fontData = new FontData[] {new FontData ("Courier New", 13, SWT.BOLD) };
        registry.put("commandLine", fontData);
        
        fontData = new FontData[] { new FontData("Impact", 13, SWT.NORMAL) };
         registry.put("haystack title", fontData);

        fontData = new FontData[] { new FontData("Courier New", 11, SWT.NORMAL) };
        registry.put("feedback", fontData);
        
        fontData = new FontData[] { new FontData("Courier New", 9,SWT.BOLD)};
        registry.put("dates", fontData);
        
        fontData = new FontData[] { new FontData("Courier New", 10,
                SWT.BOLD | SWT.UNDERLINE_SINGLE) };
        registry.put("title", fontData);

        fontData = new FontData[] { new FontData("Courier New", 10, SWT.NONE) };
        registry.put("table", fontData);
        
        fontData = new FontData[] { new FontData("Arial", 11, SWT.NORMAL | SWT.BOLD) };
        registry.put("helpsheet", fontData);

        fontData = new FontData[] { new FontData("Courier New", 12, SWT.BOLD) };
        registry.put("list headers", fontData);
        
        fontData = new FontData[] { new FontData("Courier New", 14, SWT.CENTER) };
        registry.put("time", fontData);
        
        fontData = new FontData[] { new FontData("Courier New",
                                                 11, SWT.NORMAL) };
        registry.put("list", fontData);
                                                                      
    }
}
