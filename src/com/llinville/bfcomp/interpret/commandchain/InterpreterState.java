package com.llinville.bfcomp.interpret.commandchain;

import com.llinville.bfcomp.interpret.Interpreter;

import java.util.Map;

public class InterpreterState {
    private int[] tape;
    private int tapeLocation;
    private Map<Integer, Integer> bracketMap;
    private int commandCounter; //what command in the commandChain we are on

    public InterpreterState(int tapeLength, Map<Integer, Integer> bracketMap){
        this.tape = new int[tapeLength];
        this.tapeLocation = 0;
        this.bracketMap = bracketMap;
        this.commandCounter = 0;
    }

    public InterpreterState(int[] tape, int tapeLocation, Map<Integer, Integer> bracketMap, int commandCounter){
        this.tape = tape;
        this.tapeLocation = tapeLocation;
        this.bracketMap = bracketMap;
        this.commandCounter = commandCounter;
    }

    public InterpreterState(int[] tape, int tapeLocation, Map<Integer, Integer> bracketMap){
        this.tape = tape;
        this.tapeLocation = tapeLocation;
        this.bracketMap = bracketMap;
        this.commandCounter = 0;
    }

    public int[] getTape() {
        return tape;
    }

    public int getTapeLocation() {
        return tapeLocation;
    }

    public Map<Integer,Integer> getBracketMap() {
        return bracketMap;
    }

    public int getCommandCounter(){
        return commandCounter;
    }

    public void setCommandCounter(int commandCounter){
        this.commandCounter = commandCounter;
    }

    public void incCommandCounter(){
        commandCounter++;
    }

    public void advanceTape(){
        tapeLocation++;
    }

    public void setTapeLocation(int location){
        tapeLocation = location;
    }

    public void incn(int n){
        tape[tapeLocation] += n;
    }

    public void decn(int n){
        incn(-1 * n);
    }

    public void inc(){
        incn(1);
    }

    public void dec(){
        incn(-1);
    }

    public void leftn(int n){
        tapeLocation -= n;
        if(tapeLocation < 0){
            tapeLocation = 0;
        }
    }

    public void rightn(int n){
        tapeLocation += n;
    }

    public void left(){
        leftn(1);
    }

    public void right(){
        rightn(1);
    }

    public void printState(){
        int printWidth = 6;
        int printLength = 20;
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

}
