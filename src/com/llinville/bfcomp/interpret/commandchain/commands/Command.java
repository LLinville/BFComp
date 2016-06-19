package com.llinville.bfcomp.interpret.commandchain.commands;

import com.llinville.bfcomp.interpret.commandchain.InterpreterState;
import com.llinville.bfcomp.interpret.commandchain.StateTransition;

public class Command implements StateTransition {
    private CommandType commandType;

    public enum CommandType{
        LITERAL,
        NOP;
    }

    public Command(){
        commandType = CommandType.NOP;
    }

    public Command(CommandType type) {
        commandType = type;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void execute(InterpreterState state){
        //default to doing nothing to the state
    }
}