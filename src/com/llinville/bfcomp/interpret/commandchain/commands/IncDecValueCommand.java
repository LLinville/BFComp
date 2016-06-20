package com.llinville.bfcomp.interpret.commandchain.commands;

import com.llinville.bfcomp.interpret.commandchain.InterpreterState;

public class IncDecValueCommand extends Command{
    int amount;

    public IncDecValueCommand(int amount){
        this.amount += amount;
    }

    public String toString(){
        return " Incn(" + amount + ") ";
    }

    public void execute(InterpreterState state){
        state.incn(amount);
    }
}
