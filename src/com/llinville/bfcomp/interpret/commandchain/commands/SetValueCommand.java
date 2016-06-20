package com.llinville.bfcomp.interpret.commandchain.commands;

import com.llinville.bfcomp.interpret.commandchain.InterpreterState;

public class SetValueCommand extends Command{
    int value;

    public SetValueCommand(int value){
        this.value = value;
    }

    public void execute(InterpreterState state){
        state.setCellValue(value);
    }

}
