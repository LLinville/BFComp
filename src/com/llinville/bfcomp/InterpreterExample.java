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

//            g.initialize(10);
//            g.pushToStack(30);
//            g.pushToStack(5);
//            g.divmod();
//            g.ifNotStack();
//            g.debug();
//            g.end();

        //PRIMES
//        currentNumber = 2
//        primesLeft = 10
//        currentFactor
//        factorsLeft
//        isPrime
//        while(primesLeft){
//            isPrime = 1
//            currentFactor = currentNumber - 1
//            factorsLeft = currentNumber - 2
//            while(factorsLeft){
//                if(! currentNumber % currentFactor){
//                    isPrime = 0
//                }
//                factorsLeft--;
//                currentFactor--;
//            }
//
//            if(isPrime){
//                print(currentFactor)
//                primesLeft--;
//            }
//
//            currentNumber++;
//        }

        g.initialize(15);
        g.newVariable("currentNumber", 2);
        g.newVariable("primesLeft", 10);
        g.newVariable("currentFactor");
        g.newVariable("factorsLeft");
        g.newVariable("isPrime");
        g.whileVariable("primesLeft");
            g.setVariable("isPrime", 1);
            g.pushVariableOntoStack("currentNumber");
            g.pushToStack(1);
            g.sub();
            g.popStackIntoVariable("currentFactor");
            g.pushVariableOntoStack("currentNumber");
            g.pushToStack(2);
            g.sub();
            g.popStackIntoVariable("factorsLeft");
            g.whileVariable("factorsLeft");
                g.pushVariableOntoStack("currentNumber");
                g.pushVariableOntoStack("currentFactor");
                g.divmod();
                g.ifNotStack();
                    g.setVariable("isPrime", 0);
                g.end();
                g.decVariable("factorsLeft");
                g.decVariable("currentFactor");
                g.popStack();
                g.popStack();
            g.end();
            g.ifVariable("isPrime");
                g.gotoVariable("currentNumber");
                g.printVariableValue();
                g.decVariable("primesLeft");
            g.end();
            g.incVariable("currentNumber");
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
