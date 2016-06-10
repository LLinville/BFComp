package com.llinville.bfcomp;

import java.util.HashMap;
import java.util.Map;

public class Generator {
    private int blockSize;
    private String program;
    private Map<String, Integer> variableLocations;
    private int openVariableLocation;

    public Generator(){
        program = "";
        blockSize = 6;
        variableLocations = new HashMap<>();
        openVariableLocation = 0;
    }

    public int makeVariableTableEntry(String name){
        int originalOVL = openVariableLocation;
        variableLocations.put(name, openVariableLocation);
        openVariableLocation++;
        return originalOVL;
    }

    public String getProgram(){
        return program;
    }

    public void literal(String toAdd){
        program = program + toAdd;
    }

    public void inc(){
        literal("+");
    }

    public void dec(){
        literal("-");
    }

    public void incn(int n){
        for(int i=0; i<n; i++){
            inc();
        }
    }

    public void decn(int n){
        for(int i=0; i<n; i++){
            dec();
        }
    }

    public void left(){
        literal("<");
    }

    public void right(){
        literal(">");
    }

    public void leftn(int n){
        for(int i=0; i<n; i++){
            left();
        }
    }
    public void rightn(int n){
        for(int i=0; i<n; i++){
            right();
        }
    }

    public void setValue(int n){
        literal("[-]");
        incn(n);
    }

    public void leftblock(){
        leftn(blockSize);
    }

    public void rightblock(){
        rightn(blockSize);
    }

    public void initialize(int n){
        rightblock();
        right();
        setValue(n-1);

            literal("[<+>-");
                literal("[-");
                rightblock();
                literal("+");
                leftblock();
                literal("]");

            rightblock();
            literal("]");
        left();
        leftblock();
        gotoStart();

    }

    public void gotoStart(){
        literal("[");
        leftblock();
        literal("]");
    }

    public void gotoEndOfStack(){
        gotoStart();
        rightn(3); //goto isEndOfStack
        literal("[");
        rightblock();
        literal("]");
        leftn(3);
    }

    public void pushToStack(int n){
        gotoEndOfStack();
        rightn(3); //goto isEndOfStack
        inc();
        right();
        setValue(n);
        leftn(4);
    }

    //go to the start of the zero-indexed block numbered n
    public void gotoBlock(int n){
        gotoStart();
        right();
        setValue(n);
        literal("[-");
            literal("[-");
            rightblock();
            inc();
            leftblock();
            literal("]");
        rightblock();
        literal("]");
        left();
    }

    public void newVariable(String name){
        makeVariableTableEntry(name);
    }

    public void newVariable(String name, int value){
        gotoBlock(makeVariableTableEntry(name));
        rightn(5);
        setValue(value);
        leftn(5);
    }
}
