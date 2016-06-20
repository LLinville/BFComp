package com.llinville.bfcomp.interpret.commandchain.commands;

import com.llinville.bfcomp.interpret.commandchain.InterpreterState;
import com.llinville.bfcomp.interpret.commandchain.optimizers.BalancedLoopOptimizer;

import java.util.List;
import java.util.Map;

public class BalancedLoopCommand extends Command implements Balanced{
    Map<Integer, Integer> offsetIncrements;
    List<Command> subCommands;

    public BalancedLoopCommand(Map<Integer, Integer> offsetIncrements, List<Command> subCommands){
        super(Command.CommandType.BALLANCEDLOOP);
        this.offsetIncrements = offsetIncrements;
        this.subCommands = subCommands;
    }

    public void execute(InterpreterState state){
        for(int key : offsetIncrements.keySet()){

        }
    }

    public void executeNTimes(InterpreterState state, int n){

    }
}
