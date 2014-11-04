package logic;

import java.util.Calendar;
import java.util.TimerTask;

public class TaskScheduler extends TimerTask {
    
    @Override
    public void run() {
        Processor processor = Processor.getInstance();
        processor.notifyObservers("clock");
        Calendar currTime = Calendar.getInstance();
        if (currTime.get(Calendar.SECOND) == 0) {
            processor.notifyObservers("sidepane");
        }
    }

}
