package com.llinville.bfcomp.interpret.commandchain;

import com.llinville.bfcomp.interpret.Interpreter;
import com.llinville.bfcomp.interpret.commandchain.commands.Command;
import com.llinville.bfcomp.interpret.commandchain.commands.LiteralCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CommandChainInterpreter implements Interpreter {
    private static final int tapeSize = 30000;
    InterpreterState currentState;
    CommandChain commandChain;

    public CommandChainInterpreter(String program){
        program = program.replaceAll("[^\\+-<>\\[\\]\\.,#]","");
        System.out.println("Program after removing non-commands:");
        System.out.println(program);
        commandChain = stringToCommandChain(program);
        commandChain = Optimizer.optimize(commandChain);
        System.out.println("Program after optimization:");
        System.out.println(commandChain.toString());
        currentState = new InterpreterState(tapeSize, calculateBracketMap(commandChain));
    }

    public CommandChainInterpreter(CommandChain commandChain){
        this.commandChain = commandChain;
        currentState = new InterpreterState(tapeSize, calculateBracketMap(commandChain));
    }

    public void step(){
        Command command = commandChain.getCommand(currentState.getCommandCounter());
        command.execute(currentState);
    }

    public void run(){
        while(currentState.getCommandCounter() < commandChain.length()){
            step();
        }
    }

    public static CommandChain stringToCommandChain(String program){
        CommandChain commandChain = new CommandChain();
        for(char c : program.toCharArray()){
            Command commandToAdd = new LiteralCommand(c);
            if(commandToAdd != null) {
                commandChain.addCommand(commandToAdd);
            }
        }
        return commandChain;
    }

    public static Map<Integer, Integer> calculateBracketMap(CommandChain commandChain){
        Map<Integer, Integer> bracketMap = new HashMap<>();
        Stack<Integer> bracketLocationsToMatch = new Stack<>();
        for(int i=0; i<commandChain.length(); i++){
            //handle '[' by pushing the index onto the stack
            if(commandChain.getCommand(i).getCommandType() == Command.CommandType.LITERAL){
                LiteralCommand command = (LiteralCommand) commandChain.getCommand(i);
                if(command.getOperator() == LiteralCommand.LiteralOperator.OPEN){
                    bracketLocationsToMatch.add(i);
                }
            }

            if(commandChain.getCommand(i).getCommandType() == Command.CommandType.LITERAL){
                LiteralCommand command = (LiteralCommand) commandChain.getCommand(i);
                if(command.getOperator() == LiteralCommand.LiteralOperator.CLOSE){
                    int left = bracketLocationsToMatch.pop();
                    bracketMap.put(left, i);
                    bracketMap.put(i, left);
                }
            }
        }
        return bracketMap;
    }
}
