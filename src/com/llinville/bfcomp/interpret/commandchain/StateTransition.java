package com.llinville.bfcomp.interpret.commandchain;

public interface StateTransition {
    public void execute(InterpreterState state);
}
