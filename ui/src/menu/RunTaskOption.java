package menu;

import Enums.SimulationEntryPoint;
import dtoObjects.SimulationSummeryDTO;
import engineManager.EngineManager;
import exceptions.MenuOptionException;
import exceptions.TaskException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class RunTaskOption implements MenuOption{

    @Override
    /* the function start the menu option */
    public void start() {
        Consumer<String> consumer = s-> System.out.println(s);
        Scanner scanner = new Scanner(System.in);
        EngineManager engineManager = Menu.getEngineManager();
        try {
            double chanceTargetSuccess = 0, chanceTargetWarning = 0;
            int processTime = 0;
            String isRandomString, entryPointString;
            boolean isRandom = false;
            SimulationSummeryDTO simulationSummeryDTO;
            engineManager.checkRunXml();
            processTime = askForProcessTime(scanner);
            scanner.nextLine();
            isRandomString = askForRandomString(scanner);
            chanceTargetSuccess = askForChanceTargetSuccess(scanner);
            chanceTargetWarning = askForChanceTargetWarning(scanner);
            scanner.nextLine();
            entryPointString = askForEntryPointString(scanner);
//            /* ************* */
//            ExecutorService threadExecutor = Executors.newFixedThreadPool(1);
//
//
//
//
//
//
//
//
//            /* ************ */
            if (isRandomString.equalsIgnoreCase("Y"))
                isRandom = true;
            if (entryPointString.equalsIgnoreCase("Y"))
                simulationSummeryDTO = engineManager.runSimulate(processTime, chanceTargetSuccess, chanceTargetWarning, isRandom, SimulationEntryPoint.FROM_SCRATCH, consumer);
            else
                simulationSummeryDTO = engineManager.runSimulate(processTime, chanceTargetSuccess, chanceTargetWarning, isRandom, SimulationEntryPoint.INCREMENTAL,consumer);

            /* ********************** */
            System.out.println(simulationSummeryDTO.toString());
            /* ********************** */
        } catch (MenuOptionException e) {
            System.out.println(e.errorInfo() + e.getMessage());
            continu(e.getMessage());
        }
            catch (TaskException e){
                System.out.println(e.errorInfo() + e.getMessage());
        }
    }
    

    /* the function ask for process time */
    private int askForProcessTime(Scanner scanner) {
        try{
            int proccessTime;
            System.out.println("Enter processing time for all targets (positive integer):");
            proccessTime = scanner.nextInt();
            if(proccessTime < 0){
                throw new Exception();
            }else{
                return proccessTime;
            }
        }catch (InputMismatchException e){
            System.out.println("Process time need to be positive integer");
            scanner.next();
            return askForProcessTime(scanner);
        }catch (Exception e){
            System.out.println("Process time need to be positive integer");
            return askForProcessTime(scanner);
        }
    }

    /* the function ask for random string */
    private String askForRandomString(Scanner scanner) {
        String isRandomString;
        System.out.println("Enter if processing time is random (Y/N):");
        isRandomString = scanner.nextLine();
        while(!isRandomString.equalsIgnoreCase("Y") && !isRandomString.equalsIgnoreCase("N")){
            System.out.println("Please enter valid char for random process time (Y/N)");
            isRandomString = scanner.nextLine();
        }
        return isRandomString;
    }

    /* the function ask for chance target success */
    private double askForChanceTargetSuccess(Scanner scanner) {
        try{
            double chance;
            System.out.println("Enter the probability that it will end successfully (between 0 to 1):");
            chance = scanner.nextDouble();
            if(chance<0 || chance>1){
                throw new Exception();
            }else{
                return chance;
            }

        }catch (InputMismatchException e){
            System.out.println("The probability that it will end successfully need to be between 0 to 1");
            scanner.next();
            return askForChanceTargetSuccess(scanner);
        }
        catch (Exception e){
            System.out.println("The probability that it will end successfully need to be between 0 to 1");
            return askForChanceTargetSuccess(scanner);
        }
    }

    /* the function ask for chance target warning */
    private double askForChanceTargetWarning(Scanner scanner) {
        try{
            double chance;
            System.out.println("If end with success, enter the probability that it will end with warning (between 0 to 1):");
            chance = scanner.nextDouble();
            if(chance<0 || chance>1){
                throw new Exception();
            }else{
                return chance;
            }

        }catch (InputMismatchException e){
            System.out.println("The probability that it will end with warning need to be between 0 to 1");
            scanner.next();
            return askForChanceTargetSuccess(scanner);
        }
        catch (Exception e){
            System.out.println("The probability that it will end with warning need to be between 0 to 1");
            return askForChanceTargetSuccess(scanner);
        }
    }

    /* the function ask for entry point string */
    private String askForEntryPointString(Scanner scanner) {
        String entryPointString;
        System.out.println("Do you want to start running from scratch? (Y/N):");
        entryPointString = scanner.nextLine();
        while(!entryPointString.equalsIgnoreCase("Y") && !entryPointString.equalsIgnoreCase("N")){
            System.out.println("Please enter valid char for start from scratch (Y/N)");
            entryPointString = scanner.nextLine();
        }
        return entryPointString;
    }

    /* the function return to the start function */
    private void continu(String error){
        if(!error.equals(MenuOptionException.getXmlLoadError()))
            start();
    }
}
