package com.llinville.bfcomp.interpret.commandchain.commands;

import com.llinville.bfcomp.interpret.commandchain.InterpreterState;

public class ZeroCellCommand extends Command{
    public ZeroCellCommand(){
        super(CommandType.ZERO);
    }

    public String toString(){
        return " Zero ";
    }

    public void execute(InterpreterState state){
        state.setCellValue(0);
        state.incCommandCounter();
    }

    public void executeNTimes(InterpreterState state, int n){
        execute(state);
    }

}
