package src;

import java.util.Random;

public class RequestMaker extends Thread {
// This class makes a request every 25 seconds

    @Override
    public void run() {
        while (true) {
            App.processesLock.lock();
            if (!App.processes.isEmpty()) {
                try {
                    Random random = new Random();
                    long sequence = random.nextInt(App.processes.size());
                    Process requester = App.processes.get((int) sequence);
                    App.request(requester);
                } finally {
                    App.processesLock.unlock();
                }
            }
            try {
                App.processesLock.unlock();
            } catch (java.lang.IllegalMonitorStateException ex) {
            }
            try {
                Thread.sleep(10000 + new Random().nextInt(15000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
