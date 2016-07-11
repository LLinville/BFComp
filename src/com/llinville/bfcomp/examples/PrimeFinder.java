package com.llinville.bfcomp.examples;

import com.llinville.bfcomp.generate.Generator;

public class PrimeFinder{
    public static String getProgram(){
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

        Generator g = new Generator();
        g.initialize(30);
        g.newVariable("currentNumber", 2);
        g.newVariable("primesLeft", 30);
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
        g.end();
        return g.getProgram();

        //version which runs forever printing primes:
//        g.initialize(15);
//        g.newVariable("currentNumber", 5);
//        g.newVariable("currentFactor");
//        g.newVariable("factorsLeft");
//        g.newVariable("isPrime");
//        g.whileVariable("currentNumber");
//            g.setVariable("isPrime", 1);
//            g.pushVariableOntoStack("currentNumber");
//            g.pushToStack(1);
//            g.sub();
//            g.popStackIntoVariable("currentFactor");
//            g.pushVariableOntoStack("currentNumber");
//            g.pushToStack(2);
//            g.sub();
//            g.popStackIntoVariable("factorsLeft");
//
//            g.whileVariable("factorsLeft");
//                g.pushVariableOntoStack("currentNumber");
//                g.pushVariableOntoStack("currentFactor");
//                g.divmod();
//                g.ifNotStack();
//                    g.setVariable("isPrime", 0);
//                g.end();
//                g.decVariable("factorsLeft");
//                g.decVariable("currentFactor");
//                g.popStack();
//                g.popStack();
//            g.end();
//
//            g.ifVariable("isPrime");
//                g.gotoVariable("currentNumber");
//                g.printVariableValue();
//            g.end();
//            g.incVariable("currentNumber");
//        g.end();
    }
}
