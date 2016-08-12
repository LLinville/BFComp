package com.llinville.bfcomp.interpret;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class StringInterpreter implements Interpreter{
    private static final int tapeLength = 30000;
    private static final int printWidth = 6;
    private static final int printLength = 15;
    private int[] tape;
    private int tapeLocation;
    private Map<Integer, Integer> bracketMap;
    private char [] program;
    private int programCounter;
    private int instructionsExecuted;

    public StringInterpreter(String program){
        tape = new int[tapeLength];
        bracketMap = new HashMap<>();
        programCounter = 0;
        tapeLocation = 0;
        instructionsExecuted = 0;
        program = program.replaceAll("[^\\+-<>\\[\\]\\.,#]","");
        this.program = program.toCharArray();
        System.out.println("Program after removing non-commands:");
        System.out.println(program);
        calculateBracketMap();
    }

    private void calculateBracketMap(){
        Stack<Integer> bracketLocationsToMatch = new Stack<>();
        for(int i=0; i<program.length; i++){
            if(program[i] == '['){
                bracketLocationsToMatch.add(i);
            }
            if(program[i] == ']'){
                int left = bracketLocationsToMatch.pop();
                bracketMap.put(left, i);
                bracketMap.put(i, left);
            }
        }
    }

    public void printState(){
        System.out.println("After " + instructionsExecuted + " steps:");
        System.out.print("\n\n");
        for(int i=0; i<printWidth; i++){
            System.out.print("\t " + i + " \t");
        }
        System.out.print("\n\n");
        for(int row=0; row<printLength; row++){
            for(int col=0; col<printWidth; col++){
                if(row * printWidth + col == tapeLocation){
                    System.out.print("\t_" + tape[row * printWidth + col] + "_\t");
                } else {
                    System.out.print("\t " + tape[row * printWidth + col] + "\t ");
                }
            }
            System.out.print("\n");
        }
    }

    public void step(){
        switch(program[programCounter]) {
            case '+':
                tape[tapeLocation] += 1;
                programCounter++;
                break;
            case '-':
                tape[tapeLocation] -= 1;
                programCounter++;
                break;
            case '<':
                if (tapeLocation != 0) {
                    tapeLocation--;
                }
                programCounter++;
                break;
            case '>':
                if (tapeLocation != tapeLength - 1) {
                    tapeLocation++;
                }
                programCounter++;
                break;
            case '[':
                if (tape[tapeLocation] == 0) {
                    //go one past the end of the loop
                    programCounter = bracketMap.get(programCounter) + 1;
                } else {
                    //enter the loop
                    programCounter++;
                }
                break;
            case ']':
                if (tape[tapeLocation] == 0) {
                    //leave the loop
                    programCounter++;
                } else {
                    //start the loop over
                    programCounter = bracketMap.get(programCounter) + 1;
                }
                break;
            case '.':
                System.out.println("Output: " + tape[tapeLocation] + " = " + (char) tape[tapeLocation]);
                //System.out.print((char) tape[tapeLocation]);
                programCounter++;
                break;
            case '#':
                printState();
                programCounter++;
                break;
            default:
                System.out.println("Unknown character: " + program[programCounter]);
                programCounter++;
        }
        instructionsExecuted++;
    }

    public void run(){
        while(programCounter < program.length){
            step();
        }
    }

    public int getTapeLocation(){
        return tapeLocation;
    }

    public int getTapeValueAt(int index){
        return tape[index];
    }
}
