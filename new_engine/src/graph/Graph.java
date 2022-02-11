package graph;

import newEnums.TargetPosition;
import target.Target;

import java.util.*;

public class Graph {
    private String graphName;
    private Map<Target, Set<Target>> map;
    private int pricePerTargetSimulation;
    private int getPricePerTargetCompilation;


    /*constructor of sub graph given a collection of targets.*/
    public Graph(Collection<String> subGr, Graph origin){
        this.graphName = origin.graphName;
        this.map = new HashMap<>();
        for (String targetName: subGr){
            Target target = origin.getTargetByName(targetName);
            Set<Target> dependsOn = new HashSet<>();
            for (Target target1 : target.getDependsOnList()){
                if(subGr.contains(target1.getName())){
                    dependsOn.add(target1);
                }
            }
            this.map.put(target, dependsOn);
        }
    }

    /*constructor of sub graph given a collection of targets.*/
    public Graph(String[] subGr, Graph origin){
        this.graphName = origin.graphName;
        this.map = new HashMap<>();
        for (String targetName: subGr){
            Target target = origin.getTargetByName(targetName);
            Set<Target> dependsOn = new HashSet<>();
            for (Target target1 : target.getDependsOnList()){
                if(isContains(subGr,target1.getName())){
                    dependsOn.add(target1);
                }
            }
            this.map.put(target, dependsOn);
        }
        this.pricePerTargetSimulation = origin.pricePerTargetSimulation;
        this.getPricePerTargetCompilation = origin.getPricePerTargetCompilation;
    }

    private boolean isContains(String[] targets, String target){
        for (String name : targets){
            if(name.equals(target)){
                return true;
            }
        }
        return false;
    }

    /* the function create new graph */
    public Graph(String name, int pricePerTargetSimulation, int pricePerTargetCompilation){
        this.graphName = name.trim();
        this.map = new HashMap<>();
        this.pricePerTargetSimulation = pricePerTargetSimulation;
        this.getPricePerTargetCompilation = pricePerTargetCompilation;
    }

    public int getPricePerTargetSimulation() {
        return pricePerTargetSimulation;
    }

    public int getPricePerTargetCompilation() {
        return getPricePerTargetCompilation;
    }

    public void setPricePerTargetSimulation(int pricePerTargetSimulation) {
        this.pricePerTargetSimulation = pricePerTargetSimulation;
    }

    /* copy constructor - create copy of depends-on sets, but same targets and serial sets remains*/
    public Graph(Graph graph){
        this.graphName = graph.getGraphName();
        this.map = new HashMap<>();
        duplicateMap(graph.getGraphMap());
    }


    /* the function duplicate the graph map */
    private void duplicateMap(Map<Target, Set<Target>> other){
        for(Map.Entry<Target, Set<Target>> entry : other.entrySet()){
            this.map.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
    }

    /* the function return graph name */
    public String getGraphName() {
        return graphName;
    }


    /* the function return the graph map */
    public Map<Target, Set<Target>> getGraphMap(){
        return this.map;
    }

    /* the function return the target from graph map according to target name */
    public Target getTargetByName(String name){
        for(Target target: map.keySet()){
            if(target.getName().equalsIgnoreCase(name))
                return target;
        }
        return null;
    }

    /* the function add the target to the graph map */
    public void addToGr(Target target){
        this.map.put(target, new HashSet<>(target.getDependsOnList()));
    }

    public void addToGraphWithEmptyList(Target target){
        this.map.put(target, new HashSet<>());
    }

    public void addToGraphWithoutList(Target target){
        this.map.put(target, new HashSet<>());
    }

    /* the function return true if graph map is empty and false else */
    public boolean isEmpty(){
        return this.map.isEmpty();
    }

    /* the function return all the targets that independents or leaves */
    public List<Target> getRunnableTargets(){
        List<Target> runnableTargetsList = new ArrayList<>();
        for(Map.Entry<Target, Set<Target>> entry: this.map.entrySet()){
            if(entry.getValue().size() == 0){
                runnableTargetsList.add(entry.getKey());
            }
        }
        return runnableTargetsList;
    }

    /* the function remove the target from graph map */
    public void removeTarget(Target target){
        this.map.remove(target);
    }

    /* the function return the graph information */
    public Map<TargetPosition, Integer> getGraphInfo(){
        int countRoots = 0, countMiddle = 0, countLeaf = 0, countIndependents = 0;
        Map<TargetPosition, Integer> targetMap = new HashMap<>();
        for(Target target: map.keySet()){
            switch (target.getPosition()){
                case ROOT:
                    countRoots++;
                    break;
                case MIDDLE:
                    countMiddle++;
                    break;
                case LEAF:
                    countLeaf++;
                    break;
                case INDEPENDENT:
                    countIndependents++;
                    break;
            }
        }
        targetMap.put(TargetPosition.ROOT, countRoots);
        targetMap.put(TargetPosition.MIDDLE, countMiddle);
        targetMap.put(TargetPosition.LEAF, countLeaf);
        targetMap.put(TargetPosition.INDEPENDENT, countIndependents);
        return targetMap;
    }

    /* the function return all the paths between target src to target des */
    public List<List<Target>> findAllPaths (Target src, Target des){
        List<List<Target>> allPaths = new ArrayList<>();
        Set<Target> visited = new LinkedHashSet<>();
        allPaths.add(new ArrayList<>());
        findAllPathsRec(src, des, visited,allPaths);
        allPaths.remove(allPaths.size()-1);
        return allPaths;
    }

    /* the function return all the paths between target src to target des - Recursion*/
    private void findAllPathsRec (Target src, Target des, Set<Target> visited, List<List<Target>> allPaths){
        if (src.equals(des)) {
            update(visited, allPaths.get(allPaths.size()-1));
            allPaths.get(allPaths.size()-1).add(src);
            allPaths.add(new ArrayList<>());
            return;
        }
        visited.add(src);
        for(Target target: src.getDependsOnList()){
            if(!visited.contains(target)){
                findAllPathsRec(target, des, visited, allPaths);
            }
        }
        visited.remove(src);
    }

    /* the function add all the targets from visited set to path list */
    private void update( Set<Target> visited, List<Target> path){
        for (Target target:visited)
            path.add(target);
    }

    /* the function return linkedHashSet of the circle that include the target */
    public LinkedHashSet<Target> findCircle(Target target){
        LinkedHashSet<Target> circle = new LinkedHashSet<>();
        for (Target target1: target.getDependsOnList()){
            findCircleRec(target1, target, circle);
            if(circle.contains(target)){
                return circle;
            }else{
                circle.clear();
            }
        }
        return circle;
    }

    /* the function find the circle that include the target - Recursion */
    private void findCircleRec(Target src, Target dss, LinkedHashSet<Target> path){
        if(src.equals(dss)){
            path.add(src);
            return;
        }
        path.add(src);
        for (Target target : src.getDependsOnList()) {
            if (!path.contains(target)) {
                findCircleRec(target, dss, path);
                if(!path.contains(dss)){
                    path.remove(target);
                }else{
                    return;
                }
            }
        }
    }

    /* the function return if given target is runnable */
    public boolean isRunnable(Target target){
        return map.get(target).size()==0;
    }

    /* the function remove connection from target1 to target 2 in graph */
    public synchronized void removeConnection(Target target1, Target target2){
        try{
            Set<Target> list = map.get(target1);
            if(list !=  null)
                list.remove(target2);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /* the function remove target from graph */
    public synchronized void removeFromGraph(Target target){
        map.remove(target);
        for (Set<Target> set : this.map.values()){
            if(set.contains(target)){
                set.remove(target);
            }
        }
    }

    /*add connection between target and targetToAdd in the graph*/
    public void addConnection(Target targetToAdd, Target target){
        if(map.get(target) != null){
            this.map.get(target).add(targetToAdd);
        }
    }

    public void removeConnectionIfExist(Target target1, Target target2){
       Set<Target> targets= this.map.get(target1);
       if(targets != null){
           targets.remove(target2);
       }
    }

    public boolean isContainTarget(Target target){
        return this.map.containsKey(target);
    }

    /* ******************************** for sort graph */
    public void printOrderMap(Map<Target, List<Target>> map){
        for (Map.Entry<Target, List<Target>> entry : map.entrySet()){
            System.out.println(entry.getKey().toString());
        }
    }

    public void sortMap(){
        List<Target> roots = new ArrayList<>();
        List<Target> independents = new ArrayList<>();

        Map<Target, List<Target>> orderMap = new LinkedHashMap<>();
        for (Map.Entry<Target, Set<Target>> entry : this.map.entrySet()) {
            if(entry.getKey().getRequiredForList().size() == 0){
                if(entry.getValue().size() == 0){
                    independents.add(entry.getKey());
                }else{
                    roots.add(entry.getKey());
                }
            }
        }
        insertListToOrderMap(orderMap, independents);
        sortMapUtil(orderMap, roots);
        printOrderMap(orderMap);

    }

    private void insertListToOrderMap(Map<Target, List<Target>> orderMap, List<Target> listToAdd){
        for (Target target : listToAdd){
            List<Target> value = new ArrayList<>();
            orderMap.put(target,value);
        }
    }

    private void sortMapUtil(Map<Target, List<Target>> orderMap,  List<Target> roots){
        for(Target target : roots){
            sortMapUtilRec(orderMap,target);
        }
    }

    private void sortMapUtilRec(Map<Target, List<Target>> orderMap, Target root){
        Set<Target> dependsOnList = root.getDependsOnList();
        for (Target target : dependsOnList){
            sortMapUtilRec(orderMap, target);
        }

        orderMap.put(root,new ArrayList<>(root.getDependsOnList()));
    }
}
