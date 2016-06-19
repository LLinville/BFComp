package com.llinville.bfcomp.interpret.commandchain;

import com.llinville.bfcomp.interpret.commandchain.commands.LiteralCommand;

public class Optimizer {
    public static CommandChain optimize(CommandChain commandChain){
        return commandChain;
    }



    public static int getRunLength(CommandChain commandChain, int startPosition){
        if(!(commandChain.getCommand(startPosition) instanceof LiteralCommand)){
            System.err.println("Tried to get run length of non-literal command");
            return 0;
        }

        LiteralCommand.LiteralOperator originalOperator = ((LiteralCommand) commandChain.getCommand(startPosition)).getOperator();
        int currentPosition = startPosition;
        while(((LiteralCommand) commandChain.getCommand(currentPosition)).getOperator() == originalOperator){
            currentPosition++;
        }

        return currentPosition - startPosition;
    }
}
