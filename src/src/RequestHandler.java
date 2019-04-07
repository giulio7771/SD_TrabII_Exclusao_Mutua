package src;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestHandler extends Thread {

    @Override
    public void run() {
        while (true) {
            App.queueLock.lock();
            if (App.requestQueue.isEmpty()) {
                App.queueLock.unlock();
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                }
                continue;
            }
            try {
                App.queueLock.unlock();
            } catch (Exception ex) {
            }

            try {
                Thread.sleep(5000 + new Random().nextInt(10000));
            } catch (InterruptedException ex) {
                Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            }
            App.queueLock.lock();
            App.requestQueue.poll();
            App.queueLock.unlock();
        }
    }

}
