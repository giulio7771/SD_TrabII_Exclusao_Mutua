package src;

public class CoordinatorKiller extends Thread {
    // This class kills a random process every 80 seconds

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            App.processesLock.lock();
            try {
                if (App.coordinator != null) {
                    System.out.println("The coordinator died (#" + App.coordinator.id + ").");
                }
                App.kill(App.coordinator);
            } finally {
                App.processesLock.unlock();
            }
        }
    }
}
