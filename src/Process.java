import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

public class Process {
    enum State {
        READY,
        RUNNING,
        BLOCKED
    }

    private String name;
    private State state;
    private Queue<String> instructions = new LinkedList<String>();

    public Process(String Name) {
        // constructor
        name = Name;
        state = State.READY;
    }

    @Override
    public String toString() {
        return name;
    }

    public void addInstruction(String x) { // queues next instruction
        instructions.add(x);
    }

    public String nextInstruction() { // removes/dequeues next instruction and returns it
        return instructions.remove();
    }

    public String peekNextInstruction() { // returns next instructions WITHOUT removing it
        return instructions.peek();
    }

    public Boolean isDone() {
        return instructions.isEmpty();
    }

    public void setState(State state) {
        this.state = state;
    }

    public void block() {
        state = State.BLOCKED;
    }

    public void unblock() {
        state = State.READY;
    }

    public void run() {
        state = State.RUNNING;
    }

    public ArrayList<String> readFileCall(String x, OperatingSystem OS) {
        return OS.readFile(x);
    }

    public void writeFileCall(String a, String b, OperatingSystem OS) {
        OS.writeFile(a, b);
    }

    public void printCall(String x, OperatingSystem OS) {
        OS.print(x);
    }

    public String inputStringCall(OperatingSystem OS) {
        return OS.inputString();
    }

    public String readDataCall(String key, OperatingSystem OS) {
        return OS.readData(key);
    }

    public void writeDataCall(String key, Object y, OperatingSystem OS) {
        OS.writeData(key, y);
    }

    public Boolean semWait(String mutex, OperatingSystem OS) {
        return OS.semWait(mutex, this);
    }

    public Boolean semSignal(String mutex, OperatingSystem OS) {
        return OS.semSignal(mutex, this);
    }
}