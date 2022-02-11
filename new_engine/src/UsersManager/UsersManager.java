package UsersManager;

import User.Admin;
import User.Worker;
import dtoObjects.UserDTO;

import javax.jws.soap.SOAPBinding;
import java.util.*;

public class UsersManager {
    private Map<String, Worker> workers;
    private Map<String, Admin> admins;

    public UsersManager(){
        this.workers = new HashMap<>();
        this.admins = new HashMap<>();
    }

    public synchronized void addWorker(String name, int numOfThreads)throws Exception{
        if(workers.containsKey(name) && workers.get(name).getIsActive())
            throw new Exception("Worker already exist");
        else if(workers.containsKey(name) && !workers.get(name).getIsActive()){
            workers.get(name).setIsActive(true);
        }
        else{
            workers.put(name, new Worker(name, numOfThreads));
        }
    }

    public synchronized void addAdmin(String name)throws Exception{
        if(admins.containsKey(name)&& admins.get(name).getIsActive())
            throw new Exception("Admin already exist");
        else if(admins.containsKey(name) && !admins.get(name).getIsActive()){
            admins.get(name).setIsActive(true);
        }
        else{
            admins.put(name, new Admin(name));
        }

    }

    public boolean isAdminExist(String name){
        return admins.containsKey(name);
    }
    public boolean isWorkerExist(String name){
        return workers.containsKey(name);
    }

    public List<UserDTO> getAllUsers(){
        List<UserDTO> users = new ArrayList<>();
        for (String worker : workers.keySet()){
            users.add(new UserDTO(worker, "Worker"));
        }
        for (String admin : admins.keySet()){
            users.add(new UserDTO(admin, "Admin"));
        }
        return users;
    }

}
