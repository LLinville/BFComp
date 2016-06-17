package com.llinville.bfcomp;

import java.io.FileInputStream;

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
//        g.endWhileVariable("a");

        g.initialize(10);
        g.pushToStack(0);
        g.pushToStack(1);

        g.checkEquality();
        g.ifStack();
            g.debug();
        g.endIf();

        Interpreter interpreter = new Interpreter(g.getProgram());
        interpreter.run();
        interpreter.printState();

    }
}
