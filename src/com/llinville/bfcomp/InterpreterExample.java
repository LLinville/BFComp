package com.llinville.bfcomp;

import java.io.FileInputStream;

public class InterpreterExample {
    public static void main(String args[]){

        Generator g = new Generator();
        g.initialize(10);
        g.newVariable("a",10);
        g.newVariable("b");
        g.newVariable("c",11);

        Interpreter interpreter = new Interpreter(g.getProgram());
        interpreter.printState();
        interpreter.run();
        interpreter.printState();


    }
}
