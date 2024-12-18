import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class OperatingSystem {

    private static Hashtable ram;
    private Mutex fileMutex = new Mutex();
    private Mutex userInputMutex = new Mutex();
    private Mutex userOutputMutex = new Mutex();
    private Queue<Process> generalBlocked = new LinkedList<Process>();

    private Queue<Process> readyQueue = new LinkedList<Process>();

    public OperatingSystem() {
        // constructor
        ram = new Hashtable<>();
    }

    // ---------------------------------- SYSTEM CALLS
    // -------------------------------------//

    public ArrayList<String> readFile(String x) { // 1st system call
        String filename = ((String) ram.get(x)) + ".txt";
        ArrayList<String> lines = new ArrayList<String>();
        try {
            File prog = new File(filename);
            Scanner reader = new Scanner(prog);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                lines.add(data);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
        return lines;
    }

    public void writeFile(String a, String b) { // 2nd system call
        String x = ((String) ram.get(a)) + ".txt";
        String y = ram.get(b).toString();
        try {
            File file = new File(x);
            if (file.createNewFile()) {
                FileWriter writer = new FileWriter(x);
                writer.write(y);
                writer.close();
            } else
                System.out.println("File already exists!");
        } catch (IOException e) {
            System.out.println("An error occured.");
            e.printStackTrace();
        }
    }

    public void print(String x) { // 3rd system call
        Object s = ram.get(x);
        System.out.println(s.toString());
    }

    public String inputString() { // 4th system call
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter a value");
        String input = in.nextLine();
        return input;
    }

    public String readData(String key) { // 5th system call
        return ram.get(key).toString();
    }

    public void writeData(String key, Object y) { // 6th system call (with string parameter)
        ram.put(key, y);
    }

    // -------------------------------END OF
    // SYSTEMCALLS-------------------------------------//

    public Boolean semWait(String m, Process p) {
        Boolean works;
        switch (m) {
            case "file":
                works = fileMutex.semWait(p);
                if (!(works))
                    generalBlocked.add(p);
                return works;
            case "userInput":
                works = userInputMutex.semWait(p);
                if (!(works))
                    generalBlocked.add(p);
                return works;
            case "userOutput":
                works = userOutputMutex.semWait(p);
                if (!(works))
                    generalBlocked.add(p);
                return works;
            default:
                return false;
        }
    }

    public Boolean semSignal(String m, Process p) {
        Boolean works;
        switch (m) {
            case "file":
                works = fileMutex.semSignal(p);
                if (works) {
                    generalBlocked.remove(fileMutex.getOwner());
                    if (fileMutex.getOwner() != null)
                        readyQueue.add(fileMutex.getOwner());
                }
                return works;
            case "userInput":
                works = userInputMutex.semSignal(p);
                if (works) {
                    generalBlocked.remove(userInputMutex.getOwner());
                    if (userInputMutex.getOwner() != null)
                        readyQueue.add(userInputMutex.getOwner());
                }
                return works;
            case "userOutput":
                works = userOutputMutex.semSignal(p);
                if (works) {
                    generalBlocked.remove(userOutputMutex.getOwner());
                    if (userOutputMutex.getOwner() != null)
                        readyQueue.add(userOutputMutex.getOwner());
                }
                return works;
            default:
                return false;
        }
    }

    public Queue<Process> getGeneralBlocked() {
        return generalBlocked;
    }

    public Queue<Process> getReadyQueue() {
        return readyQueue;
    }
}