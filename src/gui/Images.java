package gui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
/**
 * The Images class adds all file images into a registry to be used by the application
 */
//@author A0118846W
public class Images {

    private static ImageRegistry registry;
    
    /**
     * Initializes the images registry and adds images into the registry 
     */
    public Images(Shell shell) {
        registry = new ImageRegistry(shell.getDisplay());
        addImagesFromFile();
    }

    public static void disposeAllImages() {
        registry.dispose();
    }

    public static ImageRegistry getRegistry() {
        return registry;
    }

    private void addImagesFromFile() {
        int screenWidth = Display.getCurrent().getBounds().width;
        ImageDescriptor id;
        
        // Background Image is free for commercial use.
        // Taken from http://pixabay.com/en/tinker-color-share-many-colorful-451275/
        if (screenWidth<=1280) {
            id = ImageDescriptor
                    .createFromFile(getClass(), "/resource/bg1280.png");
            registry.put("main", id);
        } else if (screenWidth<=1920) {
            id = ImageDescriptor
                    .createFromFile(getClass(), "/resource/bg1920.png");
            registry.put("main", id);
        } else {
            id = ImageDescriptor
                    .createFromFile(getClass(), "/resource/bg1920.png");
            registry.put("main", id);
        }
        
        id = ImageDescriptor.createFromFile(getClass(), "/resource/Helpsheet.png");
        registry.put("help", id);
    }

}
