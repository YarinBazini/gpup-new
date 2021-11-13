package menu;

import dtoObjects.TargetDTO;
import engineManaget.EngineManager;
import exceptions.MenuOptionException;
import java.util.LinkedHashSet;
import java.util.Scanner;

public class TargetCircleOption implements MenuOption{
    @Override
    /* the function start the menu option */
    public void start(){
        Scanner scanner = new Scanner(System.in);
        EngineManager engineManager = Menu.getEngineManager();
        try {
            engineManager.checkRunXml();
            System.out.println("Enter target name: (if you want to go back to the menu, enter -1)");
            String targetName = scanner.nextLine();
            if(!targetName.equals("-1")) {
                LinkedHashSet<TargetDTO> list = engineManager.getTargetCircle(targetName);
                printLists(list);
            }
        } catch (MenuOptionException e){
            System.out.println(e.errorInfo() + e.getMessage());
            continu(e.getMessage());
        }
    }

    /* the function print the circle path */
    private void printLists(LinkedHashSet<TargetDTO> list){
        if(list.size() == 0)
            System.out.println("There is no circle with the selected target.");
        else {
            System.out.print("The circle is: ( ");
            for (TargetDTO targetDTO : list) {
                System.out.print(targetDTO.getName() + " ");
            }
            System.out.println(")");
        }
    }

    /* the function return to the start function */
    private void continu(String error){
        if(!error.equals(MenuOptionException.getXmlLoadError()))
            start();
    }
}
