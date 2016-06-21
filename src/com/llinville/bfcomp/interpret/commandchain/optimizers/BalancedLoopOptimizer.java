package com.llinville.bfcomp.interpret.commandchain.optimizers;

import com.llinville.bfcomp.interpret.commandchain.CommandChain;
import com.llinville.bfcomp.interpret.commandchain.commands.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalancedLoopOptimizer {

    public static CommandChain removeBalancedLoop(CommandChain commandChain){
        CommandChain toReturn = commandChain.copy();
        int currentIndex = 0;

        while(currentIndex >= 0 && currentIndex < commandChain.length()){
            int loopStart = currentIndex;
            int loopEnd = findClosingBracket(commandChain, loopStart);
            if(isLoopBalanced(commandChain, loopStart, loopEnd)){
                System.out.println("Found balanced loop at " + loopStart);
                BalancedLoopCommand toInsert = makeBalancedLoopCommand(commandChain, loopStart, loopEnd);
                toReturn = toReturn.replaceRange(loopStart, loopEnd, toInsert);
                return toReturn;
            }
            currentIndex = findNextLiteral(commandChain, LiteralCommand.LiteralOperator.OPEN, loopStart + 1);
        }
        return toReturn;
    }

    //returns if the given commands make up a balanced loop
    private static boolean isLoopBalanced(CommandChain commandChain, int startIndex, int endIndex){
        //System.out.println("Checking for balloop from " + startIndex + " to " + endIndex);
        int relativePosition = 0;

        if (!doesCommandMatchOperator(commandChain.getCommand(startIndex), LiteralCommand.LiteralOperator.OPEN)) {
            return false;
        }

        if(!doesCommandMatchOperator(commandChain.getCommand(endIndex - 1), LiteralCommand.LiteralOperator.CLOSE)){
            return false;
        }

        for(int index = startIndex + 1; index < endIndex - 1; index++){
            Command command = commandChain.getCommand(index);
            if(command instanceof LiteralCommand) {
                switch(((LiteralCommand) command).getOperator()){
                    case LEFT:
                        relativePosition--;
                        break;
                    case RIGHT:
                        relativePosition++;
                        break;
                    case PLUS:
                    case MINUS:
                        break;
                    default:
                        return false;

                }
            } else {
                if(!(command instanceof Balanced || command.getCommandType() == Command.CommandType.LEFTRIGHT)){
                    return false;
                }
                if(command.getCommandType() == Command.CommandType.LEFTRIGHT){
                    relativePosition += ((LeftRightCommand) command).getAmount();
                }
            }
        }

        return relativePosition == 0;
    }

    private static int findNextLiteral(CommandChain commandChain, LiteralCommand.LiteralOperator operator){
        return findNextLiteral(commandChain, operator, 0);
    }

    private static int findNextLiteral(CommandChain commandChain, LiteralCommand.LiteralOperator operator, int startingIndex){
        for(int index = startingIndex; index < commandChain.length(); index++){
            Command command = commandChain.getCommand(index);
            if(doesCommandMatchOperator(command, operator)){
                return index;
            }
        }
        return -1;
    }

    private static int findClosingBracket(CommandChain commandChain, int startingIndex){
        int currentDepth = 1;
        int currentIndex = startingIndex+1;
        while(currentDepth > 0 && currentIndex != commandChain.length()){
            if(doesCommandMatchOperator(commandChain.getCommand(currentIndex), LiteralCommand.LiteralOperator.OPEN)) {
                currentDepth++;
            }
            if(doesCommandMatchOperator(commandChain.getCommand(currentIndex), LiteralCommand.LiteralOperator.CLOSE)) {
                currentDepth--;
            }
            currentIndex++;
        }
        if(currentDepth == 0){
            return currentIndex;
        }
        return -1;
    }

    private static boolean doesCommandMatchOperator(Command command, LiteralCommand.LiteralOperator operator){
        if(!(command instanceof LiteralCommand)){
            return false;
        }
        return ((LiteralCommand) command).getOperator() == operator;
    }

    private static BalancedLoopCommand makeBalancedLoopCommand(CommandChain commandChain, int startIndex, int endIndex){
        Map<Integer, Integer> offsetIncrements = new HashMap<>();
        Map<Integer, List<Command>> subCommands = new HashMap<>();
        int currentOffset = 0;

        for(int index = startIndex + 1; index < endIndex - 1; index++){
            Command command = commandChain.getCommand(index);
            if(command instanceof LiteralCommand){
                switch(((LiteralCommand) command).getOperator()){
                    case LEFT:
                        currentOffset--;
                        break;
                    case RIGHT:
                        currentOffset++;
                        break;
                    case PLUS:
                        incnAtOffset(offsetIncrements, currentOffset, 1);
                        break;
                    case MINUS:
                        incnAtOffset(offsetIncrements, currentOffset, -1);
                        break;
                    default:
                        System.err.println("Did not recognize literal when making balanced loop command: " + ((LiteralCommand) command).getOperator());
                        break;
                }
            } else {
                switch(command.getCommandType()){
                    case INCDEC:
                        addSubCommand(subCommands, command, currentOffset);
                        break;
                    case LEFTRIGHT:
                        currentOffset += ((LeftRightCommand) command).getAmount();
                        break;
                    default:
                        System.err.println("Did not recognize command when making balanced loop command: " + command.getCommandType());
                }
            }
        }
        return new BalancedLoopCommand(offsetIncrements, subCommands);
    }

    private static void incnAtOffset(Map<Integer, Integer> map, int key, int amount){
        if(map.containsKey(key)) {
            map.put(key, map.get(key) + amount);
        } else {
            map.put(key, amount);
        }
    }

    private static void addSubCommand(Map<Integer, List<Command>> map, Command command, int key){
        if(map.containsKey(key)){
            map.get(key).add(command);
        } else {
            List<Command> newList = new ArrayList<>();
            newList.add(command);
            map.put(key, newList);
        }
    }
}
