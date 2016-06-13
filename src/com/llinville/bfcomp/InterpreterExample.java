package com.llinville.bfcomp;

import java.io.FileInputStream;

public class InterpreterExample {
    public static void main(String args[]){

        Generator g = new Generator();
        g.initialize(10);
        g.newVariable("a", 100);
        g.whileVariable("a");
        g.pushVariableOntoStack("a");
        g.pushToStack(1);
        g.sub();
        g.popStackIntoVariable("a");
        g.gotoVariable("a");
        g.printVariableValue();
        g.endWhileVariable("a");



        Interpreter interpreter = new Interpreter(g.getProgram());
        interpreter.run();
        interpreter.printState();


    }
}
