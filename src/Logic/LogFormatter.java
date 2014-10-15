package Logic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
    
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    
    @Override
    public String format(LogRecord record) {
        String date = df.format(new Date(record.getMillis())).toString();
        String className = record.getSourceClassName();
        String methodName = record.getSourceMethodName();
        String level = record.getLevel().toString();
        String message = record.getMessage();
        return "[" + date + "] - " + level + ": " + message + " ::: "+ className + " - " + methodName + "\n"; 
    }
}
