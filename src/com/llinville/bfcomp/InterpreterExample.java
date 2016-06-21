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

        //g.evaluatePolynomial(2,1,1,0,1,1);

        long startTime = new Date().getTime();
        CommandChainInterpreter interpreter = new CommandChainInterpreter(g.getProgram());
        interpreter.run();

        long middleTime = new Date().getTime();


//        g.initialize(10);
//        g.pushToStack(0);
//        g.pushToStack(1);
//
//        g.checkEquality();
//        g.ifStack();
//            g.debug();
//        g.endIf();

        StringInterpreter stringInterpreter = new StringInterpreter(g.getProgram());
        stringInterpreter.run();
        stringInterpreter.printState();

        long endTime = new Date().getTime();

        System.out.println("CommandChainInterpreter time: " + (middleTime - startTime));
        System.out.println("StringInterpreter time: " + (endTime - middleTime));

    }
}
