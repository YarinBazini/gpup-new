package graph;

import target.Target;
import java.util.*;

public class Graph {
    private String graphName;
    private String workingDirectory;
    private Map<Target, Set<Target>> map;

    /* the function create new graph */
    public Graph(String name, String workingDirectory){
        this.graphName = name;
        this.workingDirectory = workingDirectory;
        this.map = new HashMap<>();
    }


    /* copy constructor */
    public Graph(Graph graph){
        this.graphName = graph.getGraphName();
        this.workingDirectory = graph.getWorkingDirectory();
        duplicateMap(graph.getGraphMap());
    }

    /* the function duplicate the graph map */
    private void duplicateMap(Map<Target, Set<Target>> other){
        for(Map.Entry<Target, Set<Target>> entry : other.entrySet()){
            Target target = new Target(entry.getKey());
            this.map.put(target, new HashSet<>(target.getDependsOnList()));
        }
    }

    /* the function return graph name */
    public String getGraphName() {
        return graphName;
    }

    /* the function return graph working directory */
    public String getWorkingDirectory() {
        return workingDirectory;
    }

    /* the function return the graph map */
    public Map<Target, Set<Target>> getGraphMap(){
        return this.map;
    }

    /* the function return the target from graph map according to target name */
    public Target getTargetByName(String name){
        for(Target target: map.keySet()){
            if(target.getName().equals(name))
                return target;
        }
        return null;
    }

    /* the function add the target to the graph map */
    public void addToGr(Target target){
        this.map.put(target, new HashSet<>(target.getDependsOnList()));
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
    public String getGraphInfo(){
        int countRoots = 0, countMiddle = 0, countLeaf = 0, countIndependents = 0;
        String info = new String();
        info += "There are " + map.size() +" targets on the graph. ";
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
        info += "There are " + countRoots + " Roots, " + countMiddle + " Middles, " + countLeaf + " Leafs, " + countIndependents
                + " Indpendents targets on the graph.";
        return info;
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





    /* ******************************** */
    // handle sort graph - doesnt use
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
