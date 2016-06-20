package com.llinville.bfcomp;

import com.llinville.bfcomp.interpret.StringInterpreter;
import com.llinville.bfcomp.interpret.commandchain.CommandChainInterpreter;

import java.util.Date;

public class InterpreterExample {
    public static void main(String args[]){

        Generator g = new Generator();

        //FIBONACCI
        g.initialize(10);
        g.debug();
        g.newVariable("a",0);
        g.newVariable("b",1);
        g.newVariable("c",30);
        g.whileVariable("c");
            g.swapVariables("a", "b");
            g.pushVariableOntoStack("a");
            g.pushVariableOntoStack("b");
            g.add();
            g.printStackValue();
            g.popStackIntoVariable("b");
            g.decVariable("c");
        g.endWhileVariable("c");

//        g.initialize(8);
//        g.pushToStack(5);
//        g.pushToStack(5);
//        g.checkEquality();
//        System.out.println(g.getProgram());


        CommandChainInterpreter interpreter = new CommandChainInterpreter(g.getProgram());
        long startTime = new Date().getTime();
        interpreter.run();
        long endTime = new Date().getTime();
        long commandChainTime = endTime - startTime;

        StringInterpreter stringInterpreter = new StringInterpreter(g.getProgram());
        startTime = new Date().getTime();
        stringInterpreter.run();
        endTime = new Date().getTime();
        stringInterpreter.printState();
        long stringTime = endTime - startTime;

        System.out.println("CommandChainInterpreter time: " + commandChainTime);
        System.out.println("StringInterpreter time: " + stringTime);

    }
}
