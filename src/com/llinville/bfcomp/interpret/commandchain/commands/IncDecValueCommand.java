package com.llinville.bfcomp.interpret.commandchain.commands;

import com.llinville.bfcomp.interpret.commandchain.InterpreterState;

public class IncDecValueCommand extends Command implements Balanced{
    private int amount;

    public IncDecValueCommand(int count){
        super(CommandType.INCDEC);
        amount = count;
    }

    public String toString(){
        if(amount > 0){
            return " Incn(" + amount + ")";
        }
        return " Decn(" + amount + ") ";
    }

    public void execute(InterpreterState state){
        state.incn(amount);
        state.incCommandCounter();
    }

    public void executeNTimes(InterpreterState state, int n){
        state.incn(amount * n);
    }
}
