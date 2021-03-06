//@author A0110751W
package logic;

import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;

public class Log {

    /** Logger for monitoring purposes */
    public final static boolean LOGGING_ENABLED = true;
    private static final Logger log = Logger.getLogger("Logger");
    
    protected static void initialiseLogger() {
        if (LOGGING_ENABLED) {
            try {
                FileHandler fh = new FileHandler("System.log", true);
                Formatter format = new LogFormatter();
                fh.setFormatter(format);
                log.addHandler(fh);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static Logger getLogger() {
        return log;
    }

}
