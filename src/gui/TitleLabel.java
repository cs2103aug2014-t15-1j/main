package gui;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * This Composite contains the title of the program.
 */
public class TitleLabel extends Composite {

    private static final String LABEL_TEXT = "      Haystack";
    private static final int NUM_COLS_SCREEN = 2;
    private FontRegistry registry;

    /**
     * Creates an instance of titleLabel
     * @param parent Composite where TitleLabel is located
     * @param style  Style that TitleLabel has
     */
    // @author A0118846W
    public TitleLabel(Composite parent, int style) {
        super(parent, SWT.NONE);
        setLayout(parent);
        createContents(parent);
    }
    /**
     * Sets the layout of the TitleLabel Composite
     * @param parent Composite where the Title Label is located
     */
    // @author A0118846W
    private void setLayout(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.numColumns = NUM_COLS_SCREEN;
        this.setLayout(layout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        //gridData.horizontalSpan = NUM_COLS_SCREEN;
        gridData.horizontalAlignment = SWT.LEFT;
        this.setLayoutData(gridData);
    }
    
    // @author A0118846W
    private void createContents(Composite parent) {
        createTitle(parent);
        createCloseButton(parent);
    }

    /**
     * Creates a close button for the program.
     * 
     * @author Yeo Zi Xian, Justin & Ter Yao Xiang
     */
    private void createCloseButton(Composite parent) {
        Button button = new Button(parent, SWT.PUSH);
        button.setText(" X ");
        button.setCursor(new Cursor(Display.getCurrent(), SWT.CURSOR_HAND));
        button.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        
        button.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                System.exit(0);
            }
            
            public void widgetDefaultSelected(SelectionEvent event) {
                System.exit(0);
            }
        });
        
    }
    
   // @author A0118846W
    private void createTitle(Composite parent) {
        getFontRegistry();
        Label title = new Label(this, SWT.SINGLE | SWT.CENTER);
        title.setText(LABEL_TEXT);
        title.setFont(registry.get("haystack title"));
        Color color;
        color = parent.getDisplay().getSystemColor(SWT.COLOR_BLACK);
        title.setForeground(color);
        color.dispose();
    }
    
     // @author A0118846W
    private void getFontRegistry() {
        registry = Fonts.getRegistry();
    }

}
