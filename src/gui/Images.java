//@author A0118846W
package gui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
/**
 * The Images class adds all file images into a registry to be used by the application
 */
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
        // (Any queries ask Justin/A0116208N)
        if (screenWidth<1920) {
            id = ImageDescriptor
                    .createFromFile(getClass(), "/resource/bg1280.png");
            registry.put("main", id);
            
            id = ImageDescriptor.createFromFile(getClass(), "/resource/helpsheet1280.png");
            registry.put("help", id);
        } else if (screenWidth<3840) {
            id = ImageDescriptor
                    .createFromFile(getClass(), "/resource/bg1920.png");
            registry.put("main", id);
            
            id = ImageDescriptor.createFromFile(getClass(), "/resource/helpsheet1920.png");
            registry.put("help", id);
        } else {
            id = ImageDescriptor
                    .createFromFile(getClass(), "/resource/bg1920.png");
            registry.put("main", id);
            

            id = ImageDescriptor.createFromFile(getClass(), "/resource/helpsheet3840.png");
            registry.put("help", id);
        }
        

        id = ImageDescriptor
                .createFromFile(getClass(), "/resource/Icon.gif");
        registry.put("icon", id);
        
    }

}
