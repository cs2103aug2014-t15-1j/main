package gui;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.CTabFolder;

import database.BlockDate;

public class DateTableUI {

    public void update(List<BlockDate> dates) {
        TableViewer table = getTable();
        table.setInput(dates);
        setFocus();
    }

    private TableViewer getTable() {
        SetUp setUp = SetUp.getInstance();
        TableViewer viewer = setUp.getDateViewer();
        return viewer;
    }

    private void setFocus() {
        SetUp setUp = SetUp.getInstance();
        CTabFolder tabs = setUp.getTabFolder();
        tabs.setSelection(1);
    }

}
