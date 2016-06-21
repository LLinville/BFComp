package com.llinville.bfcomp;

import java.util.HashMap;
import java.util.Map;

public class Generator {
    private int blockSize;
    private String program;
    private Map<String, Integer> variableLocations;
    private int openVariableLocation;

    private PointerLocation currentPointerLocation;

    public Generator(){
        program = "";
        blockSize = 6;
        variableLocations = new HashMap<>();
        openVariableLocation = 0;
        currentPointerLocation = PointerLocation.ZERO;
    }

    private enum PointerLocation{
        STACKEND,
        ZERO,
        UNKNOWN;
    }

    //prepares for a function by putting the cursor in the right location
    private void startPosition(PointerLocation target){
        if(target == PointerLocation.UNKNOWN){
            //no action
        } else if(target == PointerLocation.ZERO){
            switch(currentPointerLocation){
                case UNKNOWN:
                    gotoStart();
                    break;
                case ZERO:
                    break;
                case STACKEND:
                    gotoStart();
                    break;
            }
        } else if(target == PointerLocation.STACKEND){
            switch(currentPointerLocation){
                case UNKNOWN:
                    gotoEndOfStack();
                    break;
                case ZERO:
                    gotoEndOfStack();
                    break;
                case STACKEND:
                    break;
            }
        }
        currentPointerLocation = target;
    }

    private void endPosition(PointerLocation result){
        currentPointerLocation = result;
    }

    public int makeVariableTableEntry(String name){
        int originalOVL = openVariableLocation;
        variableLocations.put(name, openVariableLocation);
        openVariableLocation++;
        return originalOVL;
    }

    public int makeVariableTableEntry(String name, int size){
        int originalOVL = openVariableLocation;
        variableLocations.put(name, openVariableLocation);
        openVariableLocation += size;
        return originalOVL;
    }

    private void removeUselessLoops(){
        int startIndex = program.indexOf("][") + 1;
        int endIndex = startIndex;
        int currentDepth = 1;
        if(startIndex == -1 + 1) return;

        for(int currentIndex = startIndex+1; currentIndex < program.length(); currentIndex++){
            if(currentDepth == 0){
                endIndex = currentIndex;
                break;
            }

            if(program.charAt(currentIndex) == '['){
                currentDepth++;
            } else if(program.charAt(currentIndex) == ']'){
                currentDepth--;
            }
        }

        System.out.println("Removing characters " + startIndex + " - " + endIndex);
        program = program.substring(0, startIndex) + program.substring(endIndex, program.length());

    }

    private void cleanOnce(){
        program = program.replaceAll("<>","");
        program = program.replaceAll("><","");
        program = program.replaceAll("\\+-","");
        program = program.replaceAll("-\\+","");
        program = program.replaceAll("\\[\\]","");

        removeUselessLoops();
        //program = program.replaceAll("\\]\\[.*\\]","]");
    }

    public void cleanProgram(){
        int oldlen = program.length();
        System.out.println("Before cleaning length: " + oldlen);
        cleanOnce();
        while(oldlen != program.length()){
            oldlen = program.length();
            cleanOnce();
        }
        System.out.println("After cleaning length: " + oldlen);
        System.out.println("Index of \"][\" : " + program.indexOf("]["));
    }

    public String getProgram(){
        cleanProgram();
        return program;
    }

    public void literal(String toAdd){
        program = program + toAdd;
    }

    public void newLine(){
        literal("\n");
    }

    public void tab(){
        literal("\t");
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
    
    public void open(){
        literal("[");
    }
    
    public void close(){
        literal("]");
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
    
    public void leftnblock(int n){
        leftn(blockSize * n);
    }
    
    public void rightnblock(int n){
        rightn(blockSize * n);
    }

    public void printStackValue(){
        rightn(4);
        literal(".");
        leftn(4);
    }

    public void printVariableValue(){
        rightn(5);
        literal(".");
        leftn(5);
    }

    public void debug(){
        literal("#");
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
                close();
                debug();
            rightblock();
            close();
        left();
        leftblock();
        gotoStart();

    }

    public void gotoStart(){
        open();
        leftblock();
        close();
    }

    public void gotoEndOfStack(){
        gotoStart();
        rightn(3); //goto isEndOfStack
        open();
        rightblock();
        close();
        leftblock();
        leftn(3);
    }

    public void pushToStack(int n){
        startPosition(PointerLocation.STACKEND);
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
            close();
        rightblock();
        close();
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

    public void gotoVariable(String name){
        int variableLocation = variableLocations.get(name);
        gotoBlock(variableLocation);
    }

    public void incnVariable(String name, int n){
        gotoVariable(name);
        rightn(5);
        incn(n);
        leftn(5);
    }

    public void decnVariable(String name, int n){
        gotoVariable(name);
        rightn(5);
        decn(n);
        leftn(5);
    }

    public void incVariable(String name){
        incnVariable(name, 1);
    }

    public void decVariable(String name){
        decnVariable(name, 1);
    }

    public void pushVariableOntoStack(String name){
        int variableLocation = variableLocations.get(name);
        gotoBlock(variableLocation);
        rightn(5); //goto variableValue
        //copy to the scratch variable move along
        literal("[-<<<+<+>>>>]<<<<[->>>>+<<<<]");
        left();

        //move variable to scratch of first block
        open();//until we are at the first block
            leftblock();
            rightn(2);
            zeroCell();

            //move variable one block left
            rightblock();
            open();
                leftblock();
                inc();
                rightblock();
                dec();
            close();
            leftn(2);
            leftblock();
        close();

        //move variable to end of stack
        rightn(3);
        open();
            left();
            open();
                dec();
                rightblock();
                inc();
                leftblock();
            close();
            right();
            rightblock();
        close();
        literal("+<[->>+<<]<<");

    }

    public void popStackIntoVariable(String name){
        int variableLocation = variableLocations.get(name);
        literal(">>[-]>>[-<<+>>]<-<<<"); //copy from stack value into scratch space

        //go to the first block
        open();
            rightn(2);
            open();
                dec();
                leftblock();
                inc();
                rightblock();
            close();
            leftn(2);
            leftblock();
        close();

        //goto the variable slot, carrying the value with us
        right();
        setValue(variableLocation);
        open();
            right();
            open();
                dec();
                rightblock();
                inc();
                leftblock();
            close();
            left();
            open();
                dec();
                rightblock();
                inc();
                leftblock();
            close();
            rightblock();
            dec();
        close();

        //move the value to the variable slot
        literal(">>>>[-]<<<[->>>+<<<]<<");
    }

    public void popStack(){

    }

    //places another copy of the top of the stack onto the stack
    public void dupStackTop(){

    }

    public void add(){
        rightn(3);
        dec();
        right();
        open();
            dec();
            leftblock();
            inc();
            rightblock();
        close();
        leftn(4);
        leftblock();
    }

    public void sub(){
        rightn(3);
        dec();
        right();
        open();
        dec();
        leftblock();
        dec();
        rightblock();
        close();
        leftn(4);
        leftblock();
    }

    public void mult(){
        //clean temporary cells
        rightn(3);
        dec();
        right();
        rightblock();
        zeroCell();
        rightblock();
        zeroCell();
        leftnblock(2);

        leftblock();
        open();
            rightnblock(3);
            inc();
            leftnblock(3);
            dec();
        close();

        rightnblock(3);
        open();
            leftnblock(2);
            open();
                leftblock();
                inc();
                rightnblock(2);
                inc();
                leftblock();
                dec();
            close();
            rightblock();
            open();
                leftblock();
                inc();
                rightblock();
                dec();
            close();
            rightblock();
            dec();
        close();
        leftn(4);
        leftnblock(3);
        endPosition(PointerLocation.STACKEND);
    }

    //replace a,b on the stack with a/b, a%b
    //[>[->+>+<<]>[-<<-[>]>>>[<[-<->]<[>]>>[[-]>>+<]>-<]<<]>>>+<<[-<<+>>]<<<]>>>>>[-<<<<<+>>>>>]<<<<<
    public void divmod(){

    }

    public void swapVariables(String var1, String var2){
        pushVariableOntoStack(var1);
        pushVariableOntoStack(var2);
        popStackIntoVariable(var1);
        popStackIntoVariable(var2);
    }

    public void whileVariable(String name){
        int variableLocation = variableLocations.get(name);
        gotoBlock(variableLocation);
        rightn(5);
        open();
        leftn(5);
    }

    public void endWhileVariable(String name){
        int variableLocation = variableLocations.get(name);
        gotoBlock(variableLocation);
        rightn(5);
        close();
        leftn(5);
    }

    public void ifVariable(String name){
        int variableLocation = variableLocations.get(name);
        gotoBlock(variableLocation);
        rightn(5);
        open();
        leftn(5);
    }

    public void endIfVariable(){
        gotoStart();
        close();
    }

    public void ifStack(){

    }

    public void evaluatePolynomial(int base, int ... coefficients){
        startPosition(PointerLocation.STACKEND);
        int order = coefficients.length;
        pushToStack(0);
        for(int i = 0; i < order - 1; i++){
            pushToStack(coefficients[i]);
            add();
            pushToStack(base);
            mult();
        }
        pushToStack(coefficients[coefficients.length-1]);
        add();
        endPosition(PointerLocation.STACKEND);
    }

    //eat two from the stack and put back 1 if they were equal and zero otherwise
    public void checkEquality(){
        startPosition(PointerLocation.STACKEND);
//        sub();
//        rightn(4);
//        rightblock();
//        inc();
//        leftblock();
//        open();
//            rightblock();
//            dec();
//            leftblock();
//            zeroCell();
//        close();
//        rightblock();
//        open();
//            dec();
//            leftblock();
//            inc();
//            rightblock();
//        close();
//        leftblock();




        endPosition(PointerLocation.STACKEND);
    }
}
