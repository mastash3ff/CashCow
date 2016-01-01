package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Logging {
	final String FNAME = "cashcow";
    private final Logger logger = Logger.getLogger(Logging.class
            .getName());
    private FileHandler fh = null;

    public Logging() {
        SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
        try {
            fh = new FileHandler(FNAME
                + format.format(Calendar.getInstance().getTime()) + ".log");
        } catch (Exception e) {
            e.printStackTrace();
        }

        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
    }
    
    public void warn(String msg){
    	logger.warning(msg);
    }
}   