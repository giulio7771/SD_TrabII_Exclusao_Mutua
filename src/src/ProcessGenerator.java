package src;

public class ProcessGenerator extends Thread {
//This class generates a new process every 30 seconds

    @Override
    public void run() {
        while (true) {
            App.processesLock.lock();
            try {
                Process p = new Process();
                App.processes.add(p);
                System.out.println("New process started (#" + p.id + ").");
            } finally {
                App.processesLock.unlock();
            }
            try {
                Thread.sleep(40000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
