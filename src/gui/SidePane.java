package gui;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;

public class SidePane extends Composite {

    private static final int MIN_HEIGHT_SIDE_PANE = 500;
    private ImageRegistry imageRegistry;
    private FontRegistry fontRegistry;

    public SidePane(Composite parent, int style) {
        super(parent, style);
        setDimensions(parent);
        setLayout(parent);
        createChildren();
    }

    private void setDimensions(Composite parent) {
        int shellWidth = parent.getSize().x;
        this.setSize(shellWidth / 3, MIN_HEIGHT_SIDE_PANE);
    }

    private void setLayout(Composite parent) {
        GridLayout sidePaneLayout = new GridLayout();
        sidePaneLayout.numColumns = 1;
        this.setLayout(sidePaneLayout);
        this.setLayoutData(new GridData(GridData.FILL_VERTICAL));
    }

    private void createChildren() {
        formatRegistry();
        addCalendar();
        buildUpcomingLabel();
        UpcomingTaskList.getInstance(this, this.getStyle());
        buildSomedayLabel();
        FloatingTaskList.getInstance(this, this.getStyle());
        buildDateLabel();
    }

    private void addCalendar() {
        DateTime cal = new DateTime(this, SWT.CALENDAR);
    }

    private void formatRegistry() {
        getImageRegistry();
        formatFontRegistry();
    }

    private void getImageRegistry() {
        imageRegistry = Images.getRegistry();
    }

    private void formatFontRegistry() {
        fontRegistry = new FontRegistry(this.getDisplay());
        FontData[] fontData = new FontData[] { new FontData("Times New Roman",
                14, SWT.NORMAL) };
        fontRegistry.put("list headers", fontData);
    }

    private void buildUpcomingLabel() {
        Label upcomingTask = new Label(this, SWT.NONE);

        upcomingTask.setImage(imageRegistry.get("upcoming"));
        GridData centeredGridData = new GridData(SWT.CENTER, SWT.FILL, true,
                true);
        upcomingTask.setLayoutData(centeredGridData);
    }

    private void buildSomedayLabel() {
        Label floatingTask = new Label(this, SWT.SINGLE);
        floatingTask.setImage(imageRegistry.get("someday"));
        GridData centeredGridData = new GridData(SWT.CENTER, SWT.FILL, true,
                true);
        floatingTask.setLayoutData(centeredGridData);
    }

    private void buildDateLabel() {
        StyledText todaysDate = new StyledText(this, SWT.READ_ONLY |
                                                     SWT.SINGLE | SWT.RIGHT |
                                                     SWT.BOLD);
        todaysDate.setFont(fontRegistry.get("list headers"));

        todaysDate.setText(getTodaysDate());
        todaysDate.setForeground(getTextColour());

    }

    private String getTodaysDate() {
        Date date = new Date();
        String now = new SimpleDateFormat("EEE MMM d, yyyy").format(date);
        return now;
    }

    private Color getTextColour() {
        return this.getDisplay().getSystemColor(SWT.COLOR_WHITE);
    }

}
