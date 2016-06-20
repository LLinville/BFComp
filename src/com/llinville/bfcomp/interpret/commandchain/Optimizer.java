package com.llinville.bfcomp.interpret.commandchain;

import com.llinville.bfcomp.interpret.commandchain.CommandChain;
import com.llinville.bfcomp.interpret.commandchain.commands.*;
import com.llinville.bfcomp.interpret.commandchain.optimizers.BalancedLoopOptimizer;

public class Optimizer {
    public static CommandChain optimize(CommandChain commandChain){
        int commandChainLength = commandChain.length();
        commandChain = removeIncDec(commandChain);
        while(commandChainLength != commandChain.length()){
            commandChainLength = commandChain.length();
            commandChain = removeIncDec(commandChain);
            commandChain = removeZeroCell(commandChain);
            commandChain = removeLeftRight(commandChain);
        }
        return commandChain;
    }

    private static CommandChain removeIncDec(CommandChain commandChain){
        CommandChain toReturn = commandChain.copy();
        int currentIndex = 0;
        while(currentIndex < commandChain.length()){
            if(doesCommandMatchOperator(commandChain.getCommand(currentIndex), LiteralCommand.LiteralOperator.PLUS)) {
                //at the start of a potential chain of increments
                int runLength = getRunLength(commandChain, currentIndex);
                if (runLength > 1) {
                    System.out.println("Found run of +'s " + runLength + " long for removal");
                    //replace the run with a new IncDecValueCommand
                    return toReturn.replaceRange(currentIndex, currentIndex + runLength, new IncDecValueCommand(runLength));
                }
            } else if(doesCommandMatchOperator(commandChain.getCommand(currentIndex), LiteralCommand.LiteralOperator.MINUS)){
                //at the start of a potential chain of decrements
                int runLength = getRunLength(commandChain, currentIndex);
                if(runLength > 1){
                    System.out.println("Found run of -'s " + runLength + " long for removal");
                    //replace the run with a new IncDecValueCommand
                    return toReturn.replaceRange(currentIndex, currentIndex + runLength, new IncDecValueCommand(-1*runLength));
                }

            }
            currentIndex++;
        }
        return toReturn;
    }

    private static CommandChain removeLeftRight(CommandChain commandChain){
        CommandChain toReturn = commandChain.copy();
        int currentIndex = 0;
        while(currentIndex < commandChain.length()){
            if(doesCommandMatchOperator(commandChain.getCommand(currentIndex), LiteralCommand.LiteralOperator.LEFT)) {
                //at the start of a potential chain of increments
                int runLength = getRunLength(commandChain, currentIndex);
                if (runLength > 1) {
                    System.out.println("Found run of <'s " + runLength + " long for removal");
                    //replace the run with a new IncDecValueCommand
                    return toReturn.replaceRange(currentIndex, currentIndex + runLength, new LeftRightCommand(-1 * runLength));
                }
            } else if(doesCommandMatchOperator(commandChain.getCommand(currentIndex), LiteralCommand.LiteralOperator.RIGHT)){
                //at the start of a potential chain of decrements
                int runLength = getRunLength(commandChain, currentIndex);
                if(runLength > 1){
                    System.out.println("Found run of >'s " + runLength + " long for removal");
                    //replace the run with a new IncDecValueCommand
                    return toReturn.replaceRange(currentIndex, currentIndex + runLength, new LeftRightCommand(runLength));
                }

            }
            currentIndex++;
        }
        return toReturn;
    }

    public static CommandChain removeZeroCell(CommandChain commandChain){
        CommandChain toReturn = commandChain.copy();
        for(int currentIndex = 0; currentIndex < commandChain.length(); currentIndex++){
            if(doesCommandMatchOperator(commandChain.getCommand(currentIndex), LiteralCommand.LiteralOperator.OPEN)
                && doesCommandMatchOperator(commandChain.getCommand(currentIndex + 1), LiteralCommand.LiteralOperator.MINUS)
                && doesCommandMatchOperator(commandChain.getCommand(currentIndex + 2), LiteralCommand.LiteralOperator.CLOSE)){
                return toReturn.replaceRange(currentIndex, currentIndex + 3, new ZeroCellCommand());
            }
        }
        return toReturn;
    }

    public static CommandChain removeBalancedLoop(CommandChain commandChain){
        return BalancedLoopOptimizer.removeBalancedLoop(commandChain);
    }

    public static boolean doesCommandMatchOperator(Command command, LiteralCommand.LiteralOperator operator){
        if(!(command instanceof LiteralCommand)){
            return false;
        }
        return ((LiteralCommand) command).getOperator() == operator;
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
