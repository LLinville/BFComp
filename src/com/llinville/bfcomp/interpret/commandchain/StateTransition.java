package com.llinville.bfcomp.interpret.commandchain;

public interface StateTransition {
    void execute(InterpreterState state);
}
