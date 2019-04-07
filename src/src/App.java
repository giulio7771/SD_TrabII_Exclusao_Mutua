package src;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    public static final Lock processesLock = new ReentrantLock();
    public static final Lock queueLock = new ReentrantLock();
    public static ArrayDeque<Process> requestQueue = new ArrayDeque<Process>();
    public static List<Process> processes = new LinkedList<>();
    public static Process coordinator;
    public static boolean electionGoingOn = false;

    public static void main(String[] args) {
        System.out.println("Starting...");
        // Initiate simulation threads
        RequestHandler handler = new RequestHandler();
        ProcessGenerator generator = new ProcessGenerator();
        RequestMaker requestMaker = new RequestMaker();
        CoordinatorKiller coordinatorKiller = new CoordinatorKiller();
        generator.start();
        requestMaker.start();
        coordinatorKiller.start();
        handler.start();

        System.out.println("Threads started...");
        System.out.println("Showing simulation status...");

        while (true) {
            String processesList = "";
            for (Process process : processes) {
                if (processesList != "") {
                    processesList += ", ";
                }
                processesList += process.id;
            }

            System.out.println("Processes: " + processes.size());
            System.out.println("Processes: [" + processesList + "]");
            System.out.println("Master: " + (App.coordinator == null ? "null" : App.coordinator.id));

            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("\n\n");
        }

    }

    public static void election(Process requester) {
        electionGoingOn = true;
        for (Process process : processes) {
            if (process != requester) {
                if (process.id > requester.id) {
                    System.out.println("#" + requester.id + " calls election for #" + process.id);
                    System.out.println("#" + process.id + ":" + "Ok");
                    election(process);
                    return;
                }
            }
        }
        electionGoingOn = false;
        coordinator = requester;
    }

    public static void request(Process requester) {
        if (App.coordinator == null && !App.electionGoingOn) {
            App.election(requester);
            System.out.print("Process #" + requester.id + " started an election and #" + App.coordinator.id + " won.");
        } else {
            while (App.electionGoingOn);
            System.out.println("The process #" + requester.id + " made a request to the coordinator (#" + App.coordinator.id + ").");
        }

        queueLock.lock();
        requestQueue.offer(requester);
        queueLock.unlock();
    }

    public static void kill(Process process) {
        if (process != null) {
            if (coordinator == process) {
                coordinator = null;
                requestQueue = new ArrayDeque<Process>();
            }
            processes.remove(process);
        }
    }

}
