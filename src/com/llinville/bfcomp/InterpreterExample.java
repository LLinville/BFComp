package com.llinville.bfcomp;

import com.llinville.bfcomp.interpret.StringInterpreter;
import com.llinville.bfcomp.interpret.commandchain.CommandChainInterpreter;

import java.util.Date;

public class InterpreterExample {
    public static void main(String args[]){

        Generator g = new Generator();

//        //FIBONACCI
//        g.initialize(10);
//        g.newVariable("a",0);
//        g.newVariable("b",1);
//        g.newVariable("c",10);
//        g.whileVariable("a");
//            g.debug();
//            g.swapVariables("a", "b");
//            g.pushVariableOntoStack("a");
//            g.pushVariableOntoStack("b");
//            g.add();
//            g.printStackValue();
//            g.popStackIntoVariable("b");
//            g.decVariable("c");
//        g.endWhileVariable("c");

//        g.initialize(8);
//        g.pushToStack(5);
//        g.pushToStack(5);
//        g.checkEquality();
//        System.out.println(g.getProgram());

//        g.initialize(10);
//        g.evaluatePolynomial(10,3,1,4,1,5,9,2,6);

        g.initialize(10);
        g.pushToStack(30);
        g.pushToStack(5);
        g.divmod();
        g.ifNotStack();
            g.debug();
        g.end();


        CommandChainInterpreter interpreter = new CommandChainInterpreter(g.getProgram());
        long startTime = new Date().getTime();
        interpreter.run();
        long endTime = new Date().getTime();
        long commandChainTime = endTime - startTime;


//        g.initialize(10);
//        g.pushToStack(0);
//        g.pushToStack(1);
//
//        g.checkEquality();
//        g.ifStack();
//            g.debug();
//        g.endIf();

        StringInterpreter stringInterpreter = new StringInterpreter(g.getProgram());
        startTime = new Date().getTime();
        stringInterpreter.run();
        endTime = new Date().getTime();
        long stringTime = endTime - startTime;

        System.out.println("CommandChainInterpreter time: " + commandChainTime);
        System.out.println("StringInterpreter time: " + stringTime);

    }
}
