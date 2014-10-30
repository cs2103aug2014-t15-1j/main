package gui;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;

public class TableComposite extends Composite {

    private static final String TASK_TABLE_TAB_LABEL = "Tasks";
    private static final String DATE_TABLE_TAB_LABEL = "Blocked Dates";
    private FontRegistry registry;

    public TableComposite(Composite parent, int style) {
        super(parent, style);
        setLayout();
        createContents(parent);
    }

    private void setLayout() {
        GridLayout layout = new GridLayout();
        this.setLayout(layout);
        this.setLayoutData(new GridData(GridData.FILL_BOTH));

        setColour(this);
    }

    private void setColour(Composite parent) {
        Color white = parent.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        this.setBackground(white);
    }

    private void createContents(Composite parent) {
        formatRegistry(parent);
        CTabFolder tabFolder = new CTabFolder(this, SWT.BORDER);
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

        CTabItem taskTable = new CTabItem(tabFolder, SWT.BORDER);
        taskTable.setText(TASK_TABLE_TAB_LABEL);
        Table taskTableUI = TaskTableUI.getInstance(tabFolder).getTableViewer()
                .getTable();
        taskTable.setControl(taskTableUI);

        CTabItem dateTable = new CTabItem(tabFolder, SWT.NONE);
        dateTable.setText(DATE_TABLE_TAB_LABEL);
        Table dateTableUI = DateTableUI.getInstance(tabFolder).getTableViewer()
                .getTable();
        dateTable.setControl(dateTableUI);

        addListener(tabFolder);
    }

    private void formatRegistry(Composite parent) {
        registry = new FontRegistry(parent.getDisplay());

        FontData[] fontData = new FontData[] { new FontData("Arial", 10,
                SWT.BOLD | SWT.UNDERLINE_SINGLE) };
        registry.put("title", fontData);
    }

    private void addListener(CTabFolder folder) {
        Display display = folder.getDisplay();
        display.addFilter(SWT.KeyDown, new Listener() {

            public void handleEvent(Event event) {
                if (((event.stateMask & SWT.CTRL) == SWT.CTRL) &&
                    (event.keyCode == 'd')) {
                    int index = folder.getSelectionIndex();
                    if (index == 0) {
                        folder.setSelection(1);
                    } else {
                        folder.setSelection(0);
                    }
                } else if (event.keyCode == SWT.F1) {
                    new HelpDialog(folder.getShell());
                }
            }
        });
    }
}
