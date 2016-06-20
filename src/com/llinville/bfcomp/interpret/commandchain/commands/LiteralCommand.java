package com.llinville.bfcomp.interpret.commandchain.commands;

import com.llinville.bfcomp.interpret.commandchain.InterpreterState;

import java.util.Map;

public class LiteralCommand extends Command{
    private LiteralOperator operator;

    public enum LiteralOperator{
        PLUS('+'),
        MINUS('-'),
        LEFT('<'),
        RIGHT('>'),
        OPEN('['),
        CLOSE(']'),
        READ(','),
        WRITE('.'),
        DEBUG('#');

        private final char operator;
        private LiteralOperator(char operator){
            this.operator = operator;
        }

        public char getSymbol(){
            return operator;
        }

        public static LiteralOperator getValue(char text){
            for (LiteralOperator b : LiteralOperator.values()) {
                if (text == b.operator) {
                    return b;
                }
            }
            return null;
        }
    }

    public LiteralCommand(LiteralOperator operator){
        super(CommandType.LITERAL);
        this.operator = operator;
    }

    public LiteralCommand(char text){
        super(CommandType.LITERAL);
        this.operator = LiteralOperator.getValue(text);
    }

    public LiteralOperator getOperator(){
        return operator;
    }

    public boolean equals(LiteralCommand command){
        return this.operator == command.getOperator();
    }

    public String toString(){
        return Character.toString(operator.getSymbol());
    }

    public void execute(InterpreterState state){
        int[] tape = state.getTape();
        int tapeLocation = state.getTapeLocation();
        Map<Integer,Integer> bracketMap = state.getBracketMap();
        int commandCounter = state.getCommandCounter();

        switch(operator) {
            case PLUS:
                state.inc();
                state.incCommandCounter();
                break;
            case MINUS:
                state.dec();
                state.incCommandCounter();
                break;
            case LEFT:
                state.left();
                state.incCommandCounter();
                break;
            case RIGHT:
                state.right();
                state.incCommandCounter();
                break;
            case OPEN:
                if (tape[tapeLocation] == 0) {
                    //go one past the end of the loop
                    state.setCommandCounter(bracketMap.get(commandCounter) + 1);
                } else {
                    //enter the loop
                    state.incCommandCounter();
                }
                break;
            case CLOSE:
                if (tape[tapeLocation] == 0) {
                    //leave the loop
                    state.incCommandCounter();
                } else {
                    //start the loop over
                    state.setCommandCounter(bracketMap.get(commandCounter));
                }
                break;
            case WRITE:
                System.out.println("Output: " + tape[tapeLocation] + " = " + (char) tape[tapeLocation]);
                state.incCommandCounter();
                break;
            case DEBUG:
                state.printState();
                state.incCommandCounter();
                break;
        }
    }
}
