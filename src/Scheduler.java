import java.util.Queue;
import java.util.LinkedList;

public class Scheduler {
    private Queue<Process> readyQueue; // = new LinkedList<Process>();
    private int quantum;
    private int instructionCntr = 0;
    private int time = 0;
    private int Prog1_arrival;
    private int Prog2_arrival;
    private int Prog3_arrival;
    /*
     * private Boolean Prog1_done = false;
     * private Boolean Prog2_done = false;
     * private Boolean Prog3_done = false;
     */
    private int done = 0;
    private Interpreter inter;

    public Scheduler(int q, int a1, int a2, int a3) {
        this.quantum = q;
        this.Prog1_arrival = a1;
        this.Prog2_arrival = a2;
        this.Prog3_arrival = a3;
        this.inter = new Interpreter();

        readyQueue = inter.getOS().getReadyQueue();
    }

    public void runSched() {
        while (done < 3) {
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.print("TIME: ");
            System.out.println(time);
            for (Process p1 : readyQueue) {
                for (Process p2 : inter.getOS().getGeneralBlocked()) {
                    if (p1.equals(p2))
                        readyQueue.remove(p1);
                }
            }
            if (time == Prog1_arrival) {
                readyQueue.add(inter.readProgram("Program_1.txt"));
            } else if (time == Prog2_arrival) {
                readyQueue.add(inter.readProgram("Program_2.txt"));
            } else if (time == Prog3_arrival) {
                readyQueue.add(inter.readProgram("Program_3.txt"));
            }
            if (!(readyQueue.isEmpty()) && instructionCntr == 0) {
                Process crntProc = readyQueue.remove();
                System.out.println(crntProc);
                inter.setCurrentProcess(crntProc);
            }
            if (inter.getCurrentProcess() == null) {
                time++;
                continue;
            }
            if (inter.getCurrentProcess().isDone()) {
                done++;
                instructionCntr = 0;
                System.out.println(inter.getCurrentProcess() + " DONE");
                inter.setCurrentProcess(null);
                continue;
            }
            String ins = inter.getCurrentProcess().nextInstruction();
            if (instructionCntr < quantum - 1) {
                System.out.println(inter.getCurrentProcess() + " | instruction #" + instructionCntr + " | " + ins);
                inter.interpret(ins);
                instructionCntr++;
            } else {
                System.out.println(inter.getCurrentProcess() + " | instruction #" + instructionCntr + " | " + ins);
                inter.interpret(ins);
                readyQueue.add(inter.getCurrentProcess());
                instructionCntr = 0;
            }
            time++;
            output();
        }
    }

    public void output() {
        System.out.print("READY QUEUE: ");
        for (Process p : readyQueue) {
            System.out.print(p + ",");
        }
        System.out.println();
        System.out.print("BLOCKED QUEUE: ");
        for (Process p : inter.getOS().getGeneralBlocked()) {
            System.out.print(p + ",");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scheduler schedule = new Scheduler(2, 0, 8, 14);
        schedule.runSched();
    }

}
