package com.llinville.bfcomp.interpret.commandchain.commands;

import com.llinville.bfcomp.interpret.commandchain.InterpreterState;

public interface Balanced {
    void executeNTimes(InterpreterState state, int n);
}
