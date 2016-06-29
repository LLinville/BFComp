package com.llinville.bfcomp.generate.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryManager {
    private Map<String, MemoryAllocation> variableLocations;
    private boolean[] openLocationTable;

    public MemoryManager(int size){
        variableLocations = new HashMap<>();
        openLocationTable = new boolean[size];
    }

    public int getFreeBlock(String name){
        int openVariableLocation = findOpenLocation(1);
        if(openVariableLocation == -1){
            System.err.println("Out of memory allocation slots");
        }
        variableLocations.put(name, new MemoryAllocation(name, openVariableLocation, 1));
        openLocationTable[openVariableLocation] = true;
        return openVariableLocation;
    }

    public int getFreeBlock(String name, int size){
        int openVariableLocation = findOpenLocation(size);
        if(openVariableLocation == -1){
            System.err.println("Out of memory allocation slots");
        }

        variableLocations.put(name, new MemoryAllocation(name, openVariableLocation, size));
        for(int i = openVariableLocation; i < openVariableLocation + size; i++){
            openLocationTable[i] = false;
        }

        return openVariableLocation;
    }

    public int getLocation(String name){
        return getAllocation(name).getLocation();
    }

    public MemoryAllocation getAllocation(String name){
        if(!variableLocations.keySet().contains(name)){
            System.err.println("Unknown variable: " + name);
            return null;
        }
        return variableLocations.get(name);
    }

    public void free(String name){
        MemoryAllocation toFree = getAllocation(name);
        variableLocations.remove(name);
        for(int i = toFree.getLocation(); i < toFree.getLocation() + toFree.getLength(); i++){
            openLocationTable[i] = true;
        }
    }

    private int findOpenLocation(int size){
        int currentStart = 0;
        int currentRunSize = 0;
        for(int i = 0; i < openLocationTable.length; i++){
            if(currentRunSize == size){
                return currentStart;
            }

            if(openLocationTable[i]){
                currentRunSize++;
            } else {
                currentRunSize = 0;
                currentStart = i + 1;
            }
        }
        return -1;
    }
}
