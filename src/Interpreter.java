import java.io.File;
//import java.io.FileWriter;
import java.io.FileNotFoundException;
//import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
/*import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;*/
import java.util.Queue;

public class Interpreter {
    private Process currentProcess;
    private OperatingSystem OS;

    public Interpreter() {
        // constructor
        OS = new OperatingSystem();
    }

    public void setCurrentProcess(Process currentProcess) {
        this.currentProcess = currentProcess;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    public OperatingSystem getOS() {
        return OS;
    }

    public Process readProgram(String filename) { // subject to change
        try {
            Process p = new Process(filename);
            // add program to ready queue in scheduler?
            File prog = new File(filename);
            Scanner reader = new Scanner(prog);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                p.addInstruction(data);
            }
            reader.close();
            return p;
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
            return null;
        }
    }

    public void interpret(String data) {
        currentProcess.run();
        String[] keywords = data.split(" ");
        switch (keywords[0]) {
            case "print":
                print(keywords[1]);
                break;
            case "assign":
                if (keywords[2].equalsIgnoreCase("readfile"))
                    assignReadFile(keywords[1], keywords[3]);
                else
                    assign(keywords[1], keywords[2]);
                break;
            case "writeFile":
                writeFile(keywords[1], keywords[2]);
                break;
            case "readFile":
                readFile(keywords[1]);
                break;
            case "printFromTo":
                printFromTo(keywords[1], keywords[2]);
                break;
            case "semWait":
                semWait(keywords[1]);
                break;
            case "semSignal":
                semSignal(keywords[1]);
                break;
        }
    }

    public void print(String x) {
        currentProcess.printCall(currentProcess + x, OS);
    }

    public void printFromTo(String x, String y) {
        int a = Integer.parseInt(currentProcess.readDataCall(currentProcess + x, OS));
        int b = Integer.parseInt(currentProcess.readDataCall(currentProcess + y, OS));
        for (int i = a + 1; i < b; i++) {
            System.out.println(i);
        }
    }

    public ArrayList<String> readFile(String x) {
        return currentProcess.readFileCall(x, OS);
    }

    public void writeFile(String x, String y) {
        currentProcess.writeFileCall(currentProcess + x, currentProcess + y, OS);
    }

    public void assign(String x, String y) {
        if (y.equalsIgnoreCase("input")) {
            y = currentProcess.inputStringCall(OS);
        }
        if (y.matches("-?\\d+"))
            currentProcess.writeDataCall(currentProcess + x, Integer.parseInt(y), OS);

        else
            currentProcess.writeDataCall(currentProcess + x, y, OS);
    }

    public void assignReadFile(String x, String y) {
        ArrayList<String> file = readFile(currentProcess + y);
        currentProcess.writeDataCall(currentProcess + x, file, OS);
    }

    public void semSignal(String x) {
        currentProcess.semSignal(x, OS);
    }

    public void semWait(String x) {
        currentProcess.semWait(x, OS);
    }
}
