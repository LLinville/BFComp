package com.llinville.bfcomp.examples;

import com.llinville.bfcomp.generate.Generator;
import com.llinville.bfcomp.interpret.StringInterpreter;
import com.llinville.bfcomp.interpret.commandchain.CommandChainInterpreter;

import java.util.Date;

public class InterpreterExample {
    public static void main(String args[]){

        Generator g = new Generator(30000);

        //PRINT MULTIPLES
//        g.initialize(10);
//        g.newVariable("currentNumber", 0);
//        g.newVariable("numbersLeft", 100);
//        g.whileVariable("numbersLeft");
//            g.pushVariableOntoStack("currentNumber");
//            g.pushToStack(4);
//            g.divmod();
//            g.ifNotStack();
//                g.gotoVariable("currentNumber");
//                g.printVariableValue();
//            g.end();
//            g.popStack();
//            g.popStack();
//            g.decVariable("numbersLeft");
//            g.incVariable("currentNumber");
//        g.end();


        CommandChainInterpreter interpreter = new CommandChainInterpreter(g.getProgram());
        long startTime = new Date().getTime();
        interpreter.run();
        long endTime = new Date().getTime();
        long commandChainTime = endTime - startTime;

        StringInterpreter stringInterpreter = new StringInterpreter(g.getProgram());
        startTime = new Date().getTime();
        stringInterpreter.run();
        endTime = new Date().getTime();
        long stringTime = endTime - startTime;

        System.out.println("CommandChainInterpreter time: " + commandChainTime);
        System.out.println("StringInterpreter time: " + stringTime);

    }
}
