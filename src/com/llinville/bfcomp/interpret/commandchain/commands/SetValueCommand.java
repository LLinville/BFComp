package com.llinville.bfcomp.interpret.commandchain.commands;

import com.llinville.bfcomp.interpret.commandchain.InterpreterState;

public class SetValueCommand extends Command{
    int value;

    public SetValueCommand(int value){
        super(CommandType.SET);
        this.value = value;
    }

    public void execute(InterpreterState state){
        state.setCellValue(value);
    }

    public void executeNTimes(InterpreterState state, int n){
        execute(state);
    }

}
