package com.llinville.bfcomp.interpret;

import java.util.ArrayList;
import java.util.List;

public class LoggingStringInterpreter extends StringInterpreter{
    private static final double scaleFactor = 0.999;
    private static final int tapeLength = 30000;

    double[] cellUseFrequencies;
    public LoggingStringInterpreter(String program){
        super(program);
        cellUseFrequencies = new double[tapeLength];
    }

    public void step() {
        super.step();
        cellUseFrequencies[getTapeLocation()] += 1;
        scaleAllUseFrequencies();
    }

    private void scaleAllUseFrequencies(){
        for(int i=0; i<tapeLength; i++){
            cellUseFrequencies[i] *= scaleFactor;
        }
    }

    public double getCellUseFrequency(int index){
        return cellUseFrequencies[index];
    }
}
