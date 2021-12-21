package serialSet;


import exceptions.SerialSetException;
import target.Target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class SerialSet {
    private String name;
    private boolean isBlock;
    private ArrayList<Target> executable;
    private ArrayList<Target> notExecutable;
    private HashSet<Target> generalSet;

    public SerialSet(String name, Collection generalSet){
        this.name = name;
        this.isBlock = false;
        setGeneralSet(generalSet);
        this.executable = new ArrayList();
        this.notExecutable = new ArrayList();
    }

    public String getName() {
        return name;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public Collection<Target> getExecutable() {
        return executable;
    }

    public Collection<Target> getNotExecutable() {
        return notExecutable;
    }

    public Collection<Target> getGeneralSet() {
        return generalSet;
    }

    public void setGeneralSet(Collection<Target> generalSet) {
        this.generalSet = new HashSet<>(generalSet);
    }

    public void moveFromExecutableToNot(Target target){
        this.executable.remove(target);
        this.notExecutable.add(target);
    }

    public void moveFromNotToExecutable(Target target){
        this.notExecutable.remove(target);
        this.executable.add(target);
    }

    public void setBlock(boolean isBlock){
        this.isBlock = isBlock;
    }

    public boolean getBlock(){
        return this.isBlock;
    }

    public Target getTargetToExe() throws SerialSetException {
        if (isBlock) {
            throw new SerialSetException("Serial set " + this.name + " is blocked");
        } else {
            if(this.executable.isEmpty()) {
                throw new SerialSetException("Serial set " + this.name + " is empty from executable");
            }else{
                Target target = this.executable.get(0);
                executable.remove(0);
                return target;
            }
        }
    }

}
