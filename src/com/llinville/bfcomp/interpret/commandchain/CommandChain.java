package com.llinville.bfcomp.interpret.commandchain;

import com.llinville.bfcomp.interpret.commandchain.commands.Command;

import java.util.ArrayList;
import java.util.List;

public class CommandChain {
    List<Command> commands;
    public CommandChain(){
        commands = new ArrayList<>();
    }

    public void addCommand(Command command){
        commands.add(command);
    }

    public Command getCommand(int index){
        return commands.get(index);
    }

    public int length(){
        return commands.size();
    }
}
