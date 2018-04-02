package CommonCommand;

import java.io.Serializable;


public enum CommandType implements Serializable{
    LOGIN,SIGNUP,STARTPROJECT,MYPROJECT,ALLPROJECT,GETPROJECT,GETBRANCH,GETCOMMITS,SUCCESS,ALREADY_EXISTS;  
}
