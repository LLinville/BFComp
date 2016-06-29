package com.llinville.bfcomp.generate.memory;

public class MemoryAllocation {
    private int location;
    private int length;

    public MemoryAllocation(String name, int location, int length){
        this.location = location;
        this.length = length;
    }

    public int getLocation() {
        return location;
    }

    public int getLength() {
        return length;
    }
}
