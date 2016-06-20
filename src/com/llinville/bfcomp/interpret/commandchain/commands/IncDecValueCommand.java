package com.llinville.bfcomp.interpret.commandchain.commands;

import com.llinville.bfcomp.interpret.commandchain.InterpreterState;

public class IncDecValueCommand extends Command{
    private int amount;

    public IncDecValueCommand(int count){
        super(CommandType.INCDEC);
        amount += count;
    }

    public String toString(){
        return " Incn(" + amount + ") ";
    }

    public void execute(InterpreterState state){
        state.incn(amount);
    }
}
