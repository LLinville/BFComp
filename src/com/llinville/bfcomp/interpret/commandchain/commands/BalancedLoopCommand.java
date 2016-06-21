package com.llinville.bfcomp.interpret.commandchain.commands;

import com.llinville.bfcomp.interpret.commandchain.InterpreterState;
import com.llinville.bfcomp.interpret.commandchain.optimizers.BalancedLoopOptimizer;

import java.util.List;
import java.util.Map;

public class BalancedLoopCommand extends Command{
    Map<Integer, Integer> offsetIncrements;
    Map<Integer, List<Command>> subCommands;

    public BalancedLoopCommand(Map<Integer, Integer> offsetIncrements, Map<Integer, List<Command>> subCommands){
        super(Command.CommandType.BALLANCEDLOOP);
        this.offsetIncrements = offsetIncrements;
        this.subCommands = subCommands;
    }

    public void execute(InterpreterState state){
        int startingLocation = state.getTapeLocation();
        int numIterations = state.getTape()[state.getTapeLocation()];

        for(int key : offsetIncrements.keySet()){
            if(startingLocation + key < 0){
                System.err.println("Tried to execute a balanced loop which modified before the start of the tape");
                return;
            }
            state.incValueAtLocation(startingLocation + key, offsetIncrements.get(key) * numIterations);
        }

        for(int key : subCommands.keySet()){
            if(startingLocation + key < 0){
                System.err.println("Tried to execute a balanced loop which modified before the start of the tape");
                return;
            }

            state.setTapeLocation(startingLocation + key);
            for(Command command : subCommands.get(key)){
                if(command instanceof Balanced){
                    ((Balanced) command).executeNTimes(state, numIterations);
                } else {
                    System.err.println("Tried to execute a non-balanced command as balanced");
                    return;
                }
            }
        }

        state.setTapeLocation(startingLocation);
        state.incCommandCounter();
    }
}
