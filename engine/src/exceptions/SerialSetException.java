package exceptions;

public class SerialSetException extends GeneralException{

    /* the function return the error information */
    public String errorInfo(){
        return "Error in serial set - ";
    }

    /* the function create new xml exception */
    public SerialSetException(String message){
        super(message);
    }
}
