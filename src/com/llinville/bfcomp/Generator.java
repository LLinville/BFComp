package com.llinville.bfcomp;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Generator {
    private int blockSize;
    private String program;
    private Map<String, Integer> variableLocations;
    private int openVariableLocation;

    private PointerLocation currentPointerLocation;
    private Stack<ControlFlowDescriptor> controlFlowStack;

    public Generator(){
        program = "";
        blockSize = 6;
        variableLocations = new HashMap<>();
        openVariableLocation = 0;
        currentPointerLocation = PointerLocation.ZERO;
        controlFlowStack = new Stack<>();
    }

    private enum PointerLocation{
        STACKEND,
        ZERO,
        UNKNOWN;
    }

    private enum ControlFlowOp{
        WHILE_VAR,
        IF_VAR,
        IF_STACK,
        IFNOT_STACK;
    }

    private class ControlFlowDescriptor{
        public ControlFlowOp operator;
        public String description;
        public ControlFlowDescriptor(ControlFlowOp operator, String description){
            this.operator = operator;
            this.description = description;
        }
    }

    private void controlFlowOp(ControlFlowOp op){
        controlFlowOp(op, null);
    }

    private void controlFlowOp(ControlFlowOp op, String description){
        controlFlowStack.push(new ControlFlowDescriptor(op, description));
    }

    public void end(){
        ControlFlowDescriptor descriptor = controlFlowStack.pop();
        switch(descriptor.operator){
            case WHILE_VAR:
                System.out.println("Ending while(" + descriptor.description + ")");
                endWhileVariable(descriptor.description);
                break;
            case IF_VAR:
                endIfVariable();
                break;
            case IF_STACK:
                endIf();
                break;
            case IFNOT_STACK:
                endIfNotStack();
                break;
        }
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
        System.out.println("Pre-Cleaning Program:");
        System.out.println(program);
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

    public void printCellValue(){
        literal(".");
    }

    public void printStackValue(){
        rightn(4);
        printCellValue();
        leftn(4);
    }

    public void printVariableValue(){
        rightn(5);
        printCellValue();
        leftn(5);
    }

    public void printStringFromVariable(String name){
        gotoVariable(name);
        rightn(4);
        open();
            printCellValue();
            rightblock();
        close();
        endPosition(PointerLocation.UNKNOWN);
    }

    public void debug(){
        literal("#");
    }

    public void initialize(int n){
        startPosition(PointerLocation.ZERO);
        rightblock();
        right();
        setValue(n-1);

            literal("[<+>-");
                literal("[-");
                rightblock();
                literal("+");
                leftblock();
                close();
            rightblock();
            close();
        left();
        leftblock();
        endPosition(PointerLocation.UNKNOWN);
    }

    public void gotoStart(){
        open();
        leftblock();
        close();
        endPosition(PointerLocation.ZERO);
    }

    public void gotoEndOfStack(){
        startPosition(PointerLocation.ZERO);
        rightn(3); //goto isEndOfStack
        open();
        rightblock();
        close();
        leftblock();
        leftn(3);
        endPosition(PointerLocation.STACKEND);
    }

    public void pushToStack(int n){
        startPosition(PointerLocation.STACKEND);
        rightblock();
        rightn(3); //goto isEndOfStack
        inc();
        right();
        setValue(n);
        leftn(4);
        endPosition(PointerLocation.STACKEND);
    }

    //go to the start of the zero-indexed block numbered n
    public void gotoBlock(int n){
        startPosition(PointerLocation.ZERO);
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
        endPosition(PointerLocation.UNKNOWN);
    }

    public void newVariable(String name){
        makeVariableTableEntry(name);
    }

    public void newVariable(String name, int value){
        gotoBlock(makeVariableTableEntry(name));
        rightn(5);
        setValue(value);
        leftn(5);
        endPosition(PointerLocation.UNKNOWN);
    }

    public void newArray(String name, int size){
        makeVariableTableEntry(name, size);
    }

    public void newString(String name, String value){
        makeVariableTableEntry(name, value.length() + 1);
    }

    public void gotoVariable(String name){
        int variableLocation = variableLocations.get(name);
        gotoBlock(variableLocation);
        endPosition(PointerLocation.UNKNOWN);
    }

    public void incnVariable(String name, int n){
        gotoVariable(name);
        rightn(5);
        incn(n);
        leftn(5);
        endPosition(PointerLocation.UNKNOWN);
    }

    public void decnVariable(String name, int n){
        gotoVariable(name);
        rightn(5);
        decn(n);
        leftn(5);
        endPosition(PointerLocation.UNKNOWN);
    }

    public void incVariable(String name){
        incnVariable(name, 1);
        endPosition(PointerLocation.UNKNOWN);
    }

    public void decVariable(String name){
        decnVariable(name, 1);
        endPosition(PointerLocation.UNKNOWN);
    }

    public void setVariable(String name, int value){
        gotoVariable(name);
        rightn(5);
        setValue(value);
        leftn(5);
        endPosition(PointerLocation.UNKNOWN);
    }

    public void initializeArray(String varName, int[] data){
        gotoVariable(varName);
        rightn(4);
        for(int i=0; i<data.length; i++){
            setValue(data[i]);
            rightblock();
        }
        leftn(4);
        endPosition(PointerLocation.UNKNOWN);
    }

    public void setStringValue(String name, String value){
        char[] charArray = value.toCharArray();
        int[] dataArray = new int[value.length() + 1];

        for(int i=0; i<value.length(); i++){
            dataArray[i] = (int) charArray[i];
        }
        dataArray[value.length()] = 0;

        initializeArray(name, dataArray);
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
        endPosition(PointerLocation.STACKEND);
    }

    public void popStackIntoVariable(String name){
        int variableLocation = variableLocations.get(name);
        startPosition(PointerLocation.STACKEND);
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
        endPosition(PointerLocation.UNKNOWN);
    }

    public void popStack(){
        startPosition(PointerLocation.STACKEND);
        rightn(3);
        zeroCell();
        right();
        zeroCell();
        leftn(4);
        leftblock();
        endPosition(PointerLocation.STACKEND);
    }

    //places another copy of the top of the stack onto the stack
    public void dupStackTop(){

    }

    public void add(){
        startPosition(PointerLocation.STACKEND);
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
        endPosition(PointerLocation.STACKEND);
    }

    public void sub(){
        startPosition(PointerLocation.STACKEND);
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
        endPosition(PointerLocation.STACKEND);
    }

    public void mult(){
        startPosition(PointerLocation.STACKEND);
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
        startPosition(PointerLocation.STACKEND);
        rightn(4);
        rightblock();
        zeroCell();
        rightblock();
        zeroCell();
        rightblock();
        zeroCell();
        rightblock();
        zeroCell();
        leftnblock(5);

        open();
            rightblock();
            open();
                dec();
                rightblock();
                inc();
                rightblock();
                inc();
                leftnblock(2);
            close();
            rightblock();
            open();
                dec();
                leftnblock(2);
                dec();
                open();
                    rightblock();
                close();
                rightnblock(3);
                open();
                    leftblock();
                    open();
                        dec();
                        leftblock();
                        dec();
                        rightblock();
                    close();
                    leftblock();
                    open();
                        rightblock();
                    close();
                    rightnblock(2);
                    open();
                        zeroCell();
                        rightnblock(2);
                        inc();
                        leftblock();
                    close();
                    rightblock();
                    dec();
                    leftblock();
                close();
                leftnblock(2);
            close();
            rightnblock(3);
            inc();
            leftnblock(2);
            open();
                dec();
                leftnblock(2);
                inc();
                rightnblock(2);
            close();
            leftnblock(3);
        close();
        rightnblock(5);
        open();
            dec();
            leftnblock(5);
            inc();
            rightnblock(5);
        close();
        leftnblock(4);
        leftn(4);
        endPosition(PointerLocation.STACKEND);
    }

    public void swapVariables(String var1, String var2){
        pushVariableOntoStack(var1);
        pushVariableOntoStack(var2);
        popStackIntoVariable(var1);
        popStackIntoVariable(var2);
        endPosition(PointerLocation.UNKNOWN);
    }

    public void whileVariable(String name){
        controlFlowOp(ControlFlowOp.WHILE_VAR, name);
        int variableLocation = variableLocations.get(name);
        gotoBlock(variableLocation);
        rightn(5);
        open();
        leftn(5);
        endPosition(PointerLocation.UNKNOWN);
    }

    private void endWhileVariable(String name){
        int variableLocation = variableLocations.get(name);
        gotoBlock(variableLocation);
        rightn(5);
        close();
        leftn(5);
        endPosition(PointerLocation.UNKNOWN);
    }

    public void ifVariable(String name){
        controlFlowOp(ControlFlowOp.IF_VAR, name);
        int variableLocation = variableLocations.get(name);
        gotoBlock(variableLocation);
        rightn(5);
        open();
        leftn(5);
        endPosition(PointerLocation.UNKNOWN);
    }

    private void endIfVariable(){
        startPosition(PointerLocation.ZERO);
        close();
        endPosition(PointerLocation.ZERO);
    }

    public void ifStack(){
        controlFlowOp(ControlFlowOp.IF_STACK);
        startPosition(PointerLocation.STACKEND);
        rightn(4);
        open();
        leftn(4);
        endPosition(PointerLocation.STACKEND);
    }

    private void endIf(){
        startPosition(PointerLocation.STACKEND);
        rightblock();
        rightn(4);
        zeroCell();
        close();
        leftn(4);
        endPosition(PointerLocation.UNKNOWN);
    }

    public void ifNotStack(){
        controlFlowOp(ControlFlowOp.IFNOT_STACK);
        startPosition(PointerLocation.STACKEND);
        rightn(4);
        rightblock();
        inc();
        leftblock();
        open();
            rightblock();
            dec();
        close();
        rightblock();
        open();
            rightblock();
        close();
        leftblock();
        open();
        leftblock();
        leftn(4);
        endPosition(PointerLocation.STACKEND);
    }

    private void endIfNotStack(){
        startPosition(PointerLocation.STACKEND);
        rightn(4);
        close();
        leftn(4);
        endPosition(PointerLocation.UNKNOWN);
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
}
