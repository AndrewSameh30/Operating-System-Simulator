import java.util.Queue;
import java.util.LinkedList;
public class Mutex {
    private Boolean value;
    private Queue<Process> blockedQueue;
    private Process owner;
    
    public Mutex(){
        value = false;
        blockedQueue = new LinkedList<Process>();
        owner = null;
    }

    public Boolean semWait(Process p){
        if(!(this.value)){
            owner = p;
            value = true;
            return true;
        }
        else {
            blockedQueue.add(p);
            return false;
        }
    }

    public Boolean semSignal(Process p){
        if(p.equals(this.owner)){
            if(blockedQueue.isEmpty()){
                value = false;
                owner = null;
            } else {
                owner = blockedQueue.remove();
            }
            return true;
        }
        return false;
    }
    public Process getOwner() {
        return owner;
    }

}
