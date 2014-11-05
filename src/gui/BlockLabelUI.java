package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import logic.Processor;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import database.BlockDate;
/**
 * The blockLabelUI is the user interface which displays the top five dates blocked by the user. 
 * It also implements the Observer interface, it observers logic.Processor
 * The singleton pattern is applied so that any instance of the class refers to the same instance.
 */

// @author A0118846W
public class BlockLabelUI implements Observer{
    
    private static BlockLabelUI blockLabelUI;
    private StyledText blockDateLabel;
    private FontRegistry registry;
    
    /**
     * Creates the blockLabelUI and sets the text to be shown at startup
     * @param parent Composite where the interface is located in
     */
    private BlockLabelUI(Composite parent){
        createContents(parent);
    }
    
    /**
     * This method returns an instance of the BlockTableUI, it creates a new instance if it does not exist
     * @param parent Composite where the interface is located in
     * @return an instance of the BlockLabelUI class
     */
    public static BlockLabelUI getInstance(Composite parent){
        if(blockLabelUI == null){
            blockLabelUI = new BlockLabelUI(parent);
        }
        return blockLabelUI;
    }
    
    /**
     * This method returns an instance of the BlockTableUI and
     * should only be called after the BlockTableUI has been created. 
     * Otherwise, an assertion failure will occur.
     * @param parent
     * @return an instance of the BlockLabelUI class
     */
    public static BlockLabelUI getInstance(){
        assert(blockLabelUI != null);
        return blockLabelUI;
    }
    
    /**
     * Updates the sequence of blockDates being shown
     */
    @Override
    public void update(Observable itemBeingObserved, Object argumentReturned) {
        blockDateLabel.setText(getDateLabelText());
    }
    

    private void createContents(Composite parent) {
        getFontRegistry();
        buildLabel(parent);
    }
    
    private void getFontRegistry() {
        registry = Fonts.getRegistry();
    }

    private void buildLabel(Composite parent){
       blockDateLabel = new StyledText(parent, SWT.SINGLE | SWT.READ_ONLY);
        String topFiveDates = getDateLabelText();
        
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        blockDateLabel.setLayoutData(gridData);
        
        blockDateLabel.setFont(registry.get("dates"));
        Color color = parent.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        
        blockDateLabel.setForeground(color);
        
        blockDateLabel.setText(topFiveDates);
        
        blockDateLabel.setEnabled(false);
       }
    
    private String getDateLabelText() {
        List<BlockDate> dates = getTopFiveBlockedDates();
        return changeDatesToString(dates);
    }

    private String changeDatesToString(List<BlockDate> dates) {
       int size = dates.size();
       String dateString = "";
       for(int index = 0; index < size; index++){
           dateString = dateString + "< "+ dates.get(index) + " > ";
       }
        return dateString;
    }

    private List<BlockDate> getTopFiveBlockedDates() {
       List<BlockDate> dates = getBlockDates();
        int size = dates.size();
        if(size < 5 ){
            return dates;
        }
        List<BlockDate> topfiveDates = new ArrayList<BlockDate>();
        for(int index = 0; index < 5; index++){
            topfiveDates.add(dates.get(index));
        }
        
        return topfiveDates;
    }

    private List<BlockDate> getBlockDates() {
        Processor processor = Processor.getInstance();
        List<BlockDate> dates =  processor.fetchBlockedDate();
        return dates;
    }


    
}
