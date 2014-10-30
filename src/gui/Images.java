package gui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.widgets.Composite;

public class Images {

    private static ImageRegistry registry;

    public Images(Composite parent) {
        registry = new ImageRegistry(parent.getDisplay());
        addImagesFromFile();
    }

    public static void disposeAllImages() {
        registry.dispose();
    }

    public static ImageRegistry getRegistry() {
        return registry;
    }

    private void addImagesFromFile() {
        ImageDescriptor id = ImageDescriptor
                .createFromFile(getClass(), "/resource/mainbg.png");
        registry.put("main", id);

        id = ImageDescriptor.createFromFile(getClass(),
                                            "/resource/resultBg.png");
        registry.put("result", id);

        id = ImageDescriptor.createFromFile(getClass(),
                                            "/resource/sidepanelbg.png");
        registry.put("sidepane", id);

        id = ImageDescriptor
                .createFromFile(getClass(), "/resource/Someday.png");
        registry.put("someday", id);
        id = ImageDescriptor.createFromFile(getClass(),
                                            "/resource/UpcomingTask.png");
        registry.put("upcoming", id);
    }

}
