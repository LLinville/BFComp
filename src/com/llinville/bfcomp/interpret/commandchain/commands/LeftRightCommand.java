package com.llinville.bfcomp.interpret.commandchain.commands;

import com.llinville.bfcomp.interpret.commandchain.InterpreterState;

public class LeftRightCommand extends Command{
    private int amount;

    public LeftRightCommand(int count){
        super(Command.CommandType.LEFTRIGHT);
        amount = count;
    }

    public String toString(){
        if(amount > 0){
            return " Right(" + amount + ")";
        }
        return " Left(" + -1 * amount + ") ";
    }

    public void execute(InterpreterState state){
        if(amount > 0) {
            state.rightn(amount);
        } else {
            state.leftn(-1 * amount);
        }
        state.incCommandCounter();
    }
}
