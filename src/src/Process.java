package src;

import java.util.Random;

public class Process {
// This class represents a process

    public long id;

    public Process() {
        Random random = new Random();
        long id = random.nextInt();
        if (id < 0) {
            id *= -1;
        }
        boolean idInUse;
        App.processesLock.lock();
        do {
            idInUse = false;
            for (Process p : App.processes) {
                if (p.id == id) {
                    idInUse = true;
                    break;
                }
            }
            if (idInUse) {
                id = random.nextInt();
                if (id < 0) {
                    id *= -1;
                }
            }
        } while (idInUse);
        App.processesLock.unlock();
        this.id = id;
    }

}
