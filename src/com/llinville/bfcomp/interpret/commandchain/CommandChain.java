package com.llinville.bfcomp.interpret.commandchain;

import com.llinville.bfcomp.interpret.commandchain.commands.Command;

import java.util.ArrayList;
import java.util.List;

public class CommandChain {
    List<Command> commands;
    public CommandChain(){
        commands = new ArrayList<>();
    }

    public CommandChain(List<Command> commands){
        this.commands = commands;
    }

    public void addCommand(Command command){
        commands.add(command);
    }

    public void addAll(List<Command> commands){
        this.commands.addAll(commands);
    }

    public CommandChain withAll(List<Command> commands){
        this.commands.addAll(commands);
        return this;
    }

    public Command getCommand(int index){
        return commands.get(index);
    }

    public int length() {
        return commands.size();
    }

    public CommandChain replaceRange(int start, int end, Command command){
        List<Command> newCommands = new ArrayList<>();
        newCommands.addAll(commands.subList(0,start));
        newCommands.add(command);
        newCommands.addAll(commands.subList(end, commands.size()));
        return new CommandChain(newCommands);
    }

    public CommandChain copy(){
        return new CommandChain().withAll(commands);
    }

    public String toString(){
        String toReturn = "";
        for(Command command : commands){
            toReturn = toReturn + command.toString();
        }
        return toReturn;
    }

}
