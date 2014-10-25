package gui;

import gui.org.eclipse.wb.swt.SWTResourceManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * For Reference???
 * 
 * @author Yao Xiang
 *
 */
public class AltMainScreen {
    
    static Boolean btnMouseDown = false;
    static int xCoor = 0;
    static int yCoor = 0;

    public static void main(String[] args) {
        
        Display display = new Display();
        Image bg = new Image(display, ".\\images\\mainbg.png");
        final Shell shell = new Shell(display, SWT.NO_TRIM | SWT.ON_TOP);
        shell.setBackgroundImage(bg);
        shell.setBackgroundMode(SWT.INHERIT_FORCE);
        
        //Mouseover codes
        //Referenced from:
        //http://stackoverflow.com/questions/18949073/how-to-create-a-moveable-swt-shell-without-title-bar-close-button
        shell.addMouseListener(new MouseListener() {

            @Override
            public void mouseUp(MouseEvent arg0) {
                // TODO Auto-generated method stub
                btnMouseDown=false;
            }

            @Override
            public void mouseDown(MouseEvent e) {
                // TODO Auto-generated method stub
                btnMouseDown=true;
                xCoor=e.x;
                yCoor=e.y;
            }

            @Override
            public void mouseDoubleClick(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }
        });
        shell.addMouseMoveListener(new MouseMoveListener() {

            @Override
            public void mouseMove(MouseEvent e) {
                // TODO Auto-generated method stub
                if(btnMouseDown){

                    shell.setLocation(shell.getLocation().x+(e.x-xCoor),shell.getLocation().y+(e.y-yCoor));
                }
            }
        });
        //END OF MOUSEMOVEMENT
        
        shell.setMinimumSize(921, 642);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        gridLayout.marginTop = 70;
        gridLayout.marginBottom = 10;
        shell.setLayout(gridLayout);
        
        //Banner
        /*Label haystack = new Label(shell, SWT.NONE);
        GridData bannerGrid = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        bannerGrid.horizontalSpan = 3;
        haystack.setText("Haystack");
        haystack.setLayoutData(bannerGrid);
        */
        
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
        gridData.widthHint = 671;
        gridData.heightHint = 452;
        gridData.horizontalSpan = 2;
        
        
        //ResultDisplay (671, 452)
        Composite resultDisplay = new Composite(shell, SWT.NONE);
        resultDisplay.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        resultDisplay.setLayoutData(gridData);
        Image resultBg = new Image(display, ".\\images\\resultbg.png");
        resultDisplay.setBackgroundImage(resultBg);
        
        //SidePanel - 217,523
        GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
        gridData2.verticalSpan = 2;
        gridData2.horizontalSpan = 1;
        gridData2.widthHint = 217;
        gridData2.heightHint = 523;
        Composite sidePanelDisplay = new Composite(shell, SWT.NONE);
        Image sidePanelBg = new Image(display, ".\\images\\sidepanelbg.png");
        sidePanelDisplay.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        sidePanelDisplay.setBackgroundImage(sidePanelBg);
        sidePanelDisplay.setLayoutData(gridData2);
        
        //CommandLine
        GridData gridData3 = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        gridData3.horizontalSpan = 2;
        Text commandLine = new Text(shell, SWT.SINGLE | SWT.BORDER);
        commandLine.setLayoutData(gridData3);
        Color black = display.getSystemColor(SWT.COLOR_BLACK);
        Color white = display.getSystemColor(SWT.COLOR_WHITE);
        commandLine.setBackground(black);
        commandLine.setForeground(white);
        
        shell.pack();
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }

        display.dispose();
    }
}
