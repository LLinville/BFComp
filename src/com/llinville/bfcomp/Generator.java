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

    private void cleanOnce(){
        program = program.replaceAll("<>","");
        program = program.replaceAll("><","");
        program = program.replaceAll("\\+-","");
        program = program.replaceAll("-\\+","");
        program = program.replaceAll("\\[\\]","");
    }

    public void cleanProgram(){
        int oldlen = program.length();
        cleanOnce();
        while(oldlen != program.length()){
            oldlen = program.length();
            cleanOnce();
        }
    }

    public String getProgram(){
        cleanProgram();
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

    public void zeroCell(){
        literal("[-]");
    }

    public void setValue(int n){
        zeroCell();
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
        leftblock();
        leftn(3);
    }

    public void pushToStack(int n){
        gotoEndOfStack();
        rightblock();
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

    public void pushVariableOntoStack(String name){
        int variableLocation = variableLocations.get(name);
        gotoBlock(variableLocation);
        rightn(5); //goto variableValue
        //copy to the scratch variable move along
        literal("[-<<<+<+>>>>]<<<<[->>>>+<<<<]");
        left();

        //move variable to scratch of first block
        literal("[");//until we are at the first block
            leftblock();
            rightn(2);
            zeroCell();

            //move variable one block left
            rightblock();
            literal("[");
                leftblock();
                inc();
                rightblock();
                dec();
            literal("]");
            leftn(2);
            leftblock();
        literal("]");

        //move variable to end of stack
        rightn(3);
        literal("[");
            left();
            literal("[");
                dec();
                rightblock();
                inc();
                leftblock();
            literal("]");
            right();
            rightblock();
        literal("]");
        literal("+<[->>+<<]<<");

    }

    public void popStackIntoVariable(String name){
        int variableLocation = variableLocations.get(name);
        gotoEndOfStack();
        literal(">>[-]>>[-<<+>>]<-<<<"); //copy from stack value into scratch space

        //go to the first block
        literal("[");
            rightn(2);
            literal("[");
                dec();
                leftblock();
                inc();
                rightblock();
            literal("]");
            leftn(2);
            leftblock();
        literal("]");

        //goto the variable slot, carrying the value with us
        right();
        setValue(variableLocation);
        literal("#[");
            right();
            literal("[");
                dec();
                rightblock();
                inc();
                leftblock();
            literal("]");
            left();
            literal("[");
                dec();
                rightblock();
                inc();
                leftblock();
            literal("]");
            rightblock();
            dec();
        literal("]");

        //move the value to the variable slot
        literal("#>>>>[-]<<<[->>>+<<<]<<");
    }

    public void add(){
        gotoEndOfStack();
        rightn(3);
        dec();
        right();
        literal("[");
            dec();
            leftblock();
            inc();
            rightblock();
        literal("]");
        leftn(4);
        leftblock();
    }

    public void sub(){
        gotoEndOfStack();
        rightn(3);
        dec();
        right();
        literal("[");
        dec();
        leftblock();
        dec();
        rightblock();
        literal("]");
        leftn(4);
        leftblock();
    }


}
