package com.llinville.bfcomp.interpret.commandchain;

import com.llinville.bfcomp.interpret.commandchain.commands.Command;
import com.llinville.bfcomp.interpret.commandchain.commands.IncDecValueCommand;
import com.llinville.bfcomp.interpret.commandchain.commands.LiteralCommand;
import com.llinville.bfcomp.interpret.commandchain.commands.SetValueCommand;

public class Optimizer {
    public static CommandChain optimize(CommandChain commandChain){
        int commandChainLength = commandChain.length();
        commandChain = removeIncDec(commandChain);
        while(commandChainLength != commandChain.length()){
            commandChainLength = commandChain.length();
            removeIncDec(commandChain);
        }
        return commandChain;
    }

    private static CommandChain removeIncDec(CommandChain commandChain){
        CommandChain toReturn = commandChain.copy();
        int currentIndex = 0;
        while(currentIndex < commandChain.length()){
            if(commandChain.getCommand(currentIndex).getCommandType() == Command.CommandType.LITERAL){
                Command com = commandChain.getCommand(currentIndex);
                System.out.println("Current command type: " + com.getClass());
                LiteralCommand command = (LiteralCommand) com;
                int runLength = getRunLength(commandChain, currentIndex);
                if(command.getOperator() == LiteralCommand.LiteralOperator.PLUS){
                    //at the start of a potential chain of increments
                    System.out.println("Found run of +'s " + runLength + " long for removal");
                    if(runLength > 1){
                        //replace the run with a new IncDecValueCommand
                        return toReturn.replaceRange(currentIndex, currentIndex + runLength, new IncDecValueCommand(runLength));
                    }
                } else if(command.getOperator() == LiteralCommand.LiteralOperator.MINUS){
                    //at the start of a potential chain of decrements
                    System.out.println("Found run of -'s " + runLength + " long for removal");
                    if(runLength > 1){
                        //replace the run with a new IncDecValueCommand
                        return toReturn.replaceRange(currentIndex, currentIndex + runLength, new IncDecValueCommand(-1*runLength));
                    }
                }
            }
            currentIndex++;
        }
        return toReturn;
    }

    public static int getRunLength(CommandChain commandChain, int startPosition){
        if(!(commandChain.getCommand(startPosition) instanceof LiteralCommand)){
            System.err.println("Tried to get run length of non-literal command");
            return 0;
        }

        LiteralCommand.LiteralOperator originalOperator = ((LiteralCommand) commandChain.getCommand(startPosition)).getOperator();
        int currentPosition = startPosition;
        while(currentPosition < commandChain.length()
                && commandChain.getCommand(currentPosition) instanceof LiteralCommand
                && ((LiteralCommand) commandChain.getCommand(currentPosition)).getOperator() == originalOperator){
            currentPosition++;
        }

        return currentPosition - startPosition;
    }
}
