package com.llinville.bfcomp.interpret.commandchain.optimizers;

import com.llinville.bfcomp.interpret.commandchain.CommandChain;
import com.llinville.bfcomp.interpret.commandchain.commands.*;

public class BalancedLoopOptimizer {

    public static CommandChain removeBalancedLoop(CommandChain commandChain){
        CommandChain toReturn = commandChain.copy();

        return toReturn;
    }

    //returns if the given commands make up the *body* of a balanced loop
    public static boolean isLoopBalanced(CommandChain commandChain, int startIndex, int endIndex){
        int relativePosition = 0;

        for(int index = startIndex; index < endIndex){
            Command command = commandChain.getCommand(index);
            if(command instanceof LiteralCommand) {
                if()
            } else {
                if(!(command instanceof Balanced)){
                    return false;
                }
            }
        }

        return relativePosition == 0;
    }
}
