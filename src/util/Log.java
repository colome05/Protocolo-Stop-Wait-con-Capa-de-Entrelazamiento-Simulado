package util;

import java.util.concurrent.locks.ReentrantLock;

public class Log {

    // Reset
    public static final String RESET  = "\033[0m";     // Text Reset

    // Regular Colors
    public static final String BLACK  = "\033[0;30m";  // BLACK
    public static final String RED    = "\033[0;31m";  // RED
    public static final String GREEN  = "\033[0;32m";  // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE   = "\033[0;34m";  // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN   = "\033[0;36m";  // CYAN
    public static final String WHITE  = "\033[0;37m";  // WHITE
    
    ReentrantLock lock = new ReentrantLock();
    private static Log instance;
    private static long startTime;

    public static Log getLog() {

        if (instance == null) {
            instance = new Log();
            startTime = System.currentTimeMillis();
        }
        return instance;
    }

    private void out(String str) {
        lock.lock();
        try {
            // Small delay to avoid identical time outputs
            Thread.sleep(1);
            //System.out.println((System.currentTimeMillis() - startTime) + " " + str);
            System.out.println(str);
        } catch (InterruptedException ex) {
          ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void printRED(String str){
      out(RED+str+RESET);
    }
    
    public void printBLACK(String str){
      out(BLACK+str+RESET);
    }
    
    public void printBLUE(String str){
      out(BLUE+str+RESET);
    }
    
    public void printPURPLE(String str){
      out(PURPLE+str+RESET);
    }
    
    public void printGREEN(String str){
      out(GREEN+str+RESET);
    }
}
