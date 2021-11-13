package target;

import dtoObjects.SimulationSummeryDTO;
import Enums.DependencyTypes;
import Enums.TargetPosition;
import Enums.TargetRunStatus;
import Enums.TargetStatus;
import graph.Graph;
import scema.generated.GPUPTargetDependencies;
import exceptions.XmlException;
import java.util.Random;


import java.util.*;

public class Target {
    private String name;
    private TargetRunStatus runStatus;
    private TargetStatus status;
    private Set<Target> dependsOnList;
    private Set<Target> requiredForList;
    private String generalInfo;


    public void setRunStatus(TargetRunStatus runStatus) {
        this.runStatus = runStatus;
    }

    public void setStatus(TargetStatus status) {
        this.status = status;
    }

    /* the function create new target */
    public Target(String name){
        this.name = name;
        dependsOnList = new HashSet<>();
        requiredForList = new HashSet<>();
    }

    /* copy constructor */
    public Target(Target other){
        this.name = other.getName();
        this.runStatus = other.getRunStatus();
        this.status = other.getStatus();
        this.generalInfo = other.getGeneralInfo();
        duplicateDependsOnList(other.getDependsOnList());
        duplicateRequiredForList(other.getRequiredForList());
    }

    /* the function duplicate dependsOn list of target */
    private void duplicateDependsOnList(Set<Target> other){
        for(Target target: other){
            Target targetToAdd = new Target(target);
            this.dependsOnList.add(targetToAdd);
        }
    }

    /* the function duplicate RequiredFor list of target */
    private void duplicateRequiredForList(Set<Target> other){
        for(Target target: other){
            Target targetToAdd = new Target(target);
            this.requiredForList.add(targetToAdd);
        }
    }

    /* the function update the general info of target */
    public void updateGeneralInfo(String generalInfo){
        this.generalInfo = generalInfo;
    }

    /* the function return target run status */
    public TargetRunStatus getRunStatus() {
        return runStatus;
    }

    /* the function return target status */
    public TargetStatus getStatus() {
        return status;
    }

    /* the function return target general information */
    public String getGeneralInfo() {
        return generalInfo;
    }

    /* the function load the dependsOnList and requiredForList of the target */
   public void load(GPUPTargetDependencies gpupTargetDependencies, Graph graph, Set<String> validTargetExist, Set<String> errorTargetsDependencies) throws XmlException{
       for (GPUPTargetDependencies.GPUGDependency gpupDependency : gpupTargetDependencies.getGPUGDependency()) {
           Target target;
           if (!graph.getGraphMap().containsKey(new Target(gpupDependency.getValue()))) {
               target = new Target(gpupDependency.getValue());
               graph.addToGr(target);
               validTargetExist.add(target.getName());
           } else {
               target = graph.getTargetByName(gpupDependency.getValue());
           }
           if (gpupDependency.getType().equals(DependencyTypes.REQUIRED_FOR.toString())) {
               if(!this.requiredForList.contains(target))
                   this.addToRequiredForList(target);
               if(!target.dependsOnList.contains(this))
                   target.addToDependsOnList(this);
               checkConflictDependencies(target, DependencyTypes.REQUIRED_FOR, errorTargetsDependencies);
           } else if (gpupDependency.getType().equals(DependencyTypes.DEPENDS_ON.toString())) {
               if(!this.dependsOnList.contains(target))
                   this.addToDependsOnList(target);
               if(!target.requiredForList.contains(this))
                   target.addToRequiredForList(this);
               checkConflictDependencies(target, DependencyTypes.DEPENDS_ON,errorTargetsDependencies);
           }
       }
   }

   /* the function return true if there is conflict between the types of dependencies of targets and false else */
    void checkConflictDependencies(Target target, DependencyTypes dependencyTypes, Set<String> errorTargetsDependencies) throws XmlException{
        if (dependencyTypes == DependencyTypes.DEPENDS_ON){
            if (this.dependsOnList.contains(target) && target.dependsOnList.contains(this))
                errorTargetsDependencies.add("target " + this.getName() + " and target " + target.getName() + " can not be dependent on each other");
        } else if(dependencyTypes == DependencyTypes.REQUIRED_FOR){
            if (this.requiredForList.contains(target) && target.requiredForList.contains(this))
                errorTargetsDependencies.add("target " + this.getName() + " and target " + target.getName() + " can not be required for each other");
        }
    }

    /* thr function calculate and return the target position */
    public TargetPosition getPosition(){
        int sizeDepends = this.dependsOnList.size(), sizeRequired = this.requiredForList.size();
        if(sizeDepends == 0 && sizeRequired == 0)
            return TargetPosition.INDEPENDENT;
        else if(sizeDepends == 0)
            return TargetPosition.LEAF;
        else if(sizeRequired == 0)
            return TargetPosition.ROOT;
        else
            return TargetPosition.MIDDLE;

    }

    /* the function return the target name */
    public String getName(){
        return this.name;
    }

    //************
    @Override
    /* the function return information about the target */
    public String toString() {
//        return "name: " + name + " position: " + position.toString() + " list of depends on: **" + " list of required from: **" +
//                " general information: " + generalInfo;
        return name;
    }

    @Override
    /* equals function between targets */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Target target = (Target) o;
        return Objects.equals(name, target.name);
    }

    @Override
    /* hash code function for target */
    public int hashCode() {
        return Objects.hash(name);
    }

    /* the function return thr dependsOn list of target */
    public Set<Target> getDependsOnList() {
        return dependsOnList;
    }

    /* the function return thr requiredFor list of target */
    public Set<Target> getRequiredForList() {
        return requiredForList;
    }

    //TO-DO- for check if input is invalid - check if already is in the other list
    /* the function add the target to dependsOn list */
    public void addToDependsOnList(Target targetToAdd){
        this.dependsOnList.add(targetToAdd);
    }

    /* the function add the target to requiredFor list */
    public void addToRequiredForList(Target targetToAdd){
        this.requiredForList.add(targetToAdd);
    }

    /* the function run target */
    public boolean run(int time, double chanceSuccess, double chanceWarning,boolean isRandom, SimulationSummeryDTO simulationSummeryDTO){
        try{
            simulationSummeryDTO.addOutput("Target "+ name + " start run.");
            if(generalInfo != null){
                simulationSummeryDTO.addOutput("General info of the target: " + generalInfo);
            }
            this.status = TargetStatus.IN_PROCESS;
            Random random = new Random();
            if(isRandom){
                time = random.nextInt(time);
            }
            long chanceInt = Math.round(chanceSuccess*10);
            boolean isSuccess = (random.nextInt(9)<= chanceInt);
            simulationSummeryDTO.addOutput("Target "+ name+ " start sleep");
            Thread.sleep(time);
            simulationSummeryDTO.addOutput("Target "+ name+ " done sleep");
            this.status = TargetStatus.FINISHED;
            if(isSuccess){
                if(random.nextInt(9)<= Math.round(chanceWarning*10)){
                    this.runStatus = TargetRunStatus.WARNING;
                }else{
                    this.runStatus = TargetRunStatus.SUCCESS;
                }
            }else{
                this.runStatus = TargetRunStatus.FAILURE;
            }
            simulationSummeryDTO.addOutput("Target "+ name+ " run done. run status: " + runStatus.toString());
            return isSuccess;
        }catch (Exception e){
        }
        return false;
    }

    /* the function remove the target from requiredFor list */
    public void removeTargetFromRequiredList(Target targetToRemove){
        this.requiredForList.remove(targetToRemove);
    }

    /* the function remove the target from dependsOn list */
    public void removeTargetFromDependsList(Target targetToRemove){
        this.dependsOnList.remove(targetToRemove);
    }

    /* the function return true if target exist on dependsOn list */
    public boolean isInDependsOnList(Target target){
        return dependsOnList.contains(target);
    }

    /* the function return true if target exist on requiredFor list */
    public boolean isInRequiredForList(Target target){
        return requiredForList.contains(target);
    }

}
