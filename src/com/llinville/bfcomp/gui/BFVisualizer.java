package com.llinville.bfcomp.gui;

import com.llinville.bfcomp.interpret.LoggingStringInterpreter;

import java.util.Random;

import javax.swing.*;
import java.awt.*;

public class BFVisualizer extends JPanel{
    private static final int CELL_WIDTH = 50;
    private static final int CELL_GRID_WIDTH = 6;
    private static final int CELL_GRID_HEIGHT = 10;

    String program = ">++++++++++>+>+[\n" +
            "    [+++++[>++++++++<-]>.<++++++[>--------<-]+<<<]>.>>[\n" +
            "        [-]<[>+<-]>>[<<+>+>-]<[>+<-[>+<-[>+<-[>+<-[>+<-[>+<-\n" +
            "            [>+<-[>+<-[>+<-[>[-]>+>+<<<-[>+<-]]]]]]]]]]]+>>>\n" +
            "    ]<<<\n" +
            "]";
    LoggingStringInterpreter interpreter = new LoggingStringInterpreter(program);

    private Random rand = new Random();

    public BFVisualizer() {
        setPreferredSize(new Dimension(500, 500));
    }

    public void stepInterpreter(){
        interpreter.step();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i = 0; i < CELL_GRID_WIDTH * CELL_GRID_HEIGHT; i++){
            drawMemoryCell(g, i);
        }
    }

    public void drawMemoryCell(Graphics g, int cellIndex){
        //luminance of the cell as a logarithm of its use frequency
        int cellLum = (int) (50 * Math.log(1 + interpreter.getCellUseFrequency(cellIndex)));
        if(cellLum > 255) cellLum = 255;
        g.setColor(new Color(cellLum, 255 - cellLum, 0));
        g.fillRect(getCellXPos(cellIndex), getCellYPos(cellIndex), CELL_WIDTH, CELL_WIDTH);

        cellLum = interpreter.getTapeValueAt(cellIndex) % 255;

        g.setColor(new Color(cellLum, cellLum, cellLum));
        g.fillRect(getCellXPos(cellIndex) + 10, getCellYPos(cellIndex) + 10, CELL_WIDTH - 20, CELL_WIDTH - 20);

        g.setColor(new Color(0,0,255));
        if(cellIndex == interpreter.getTapeLocation()){
            g.drawRect(getCellXPos(cellIndex) - 2, getCellYPos(cellIndex) - 2, CELL_WIDTH + 4, CELL_WIDTH + 4);
        }
    }

    public int getCellXPos(int cellIndex){
        return (cellIndex % CELL_GRID_WIDTH) * (CELL_WIDTH + 5);
    }

    public int getCellYPos(int cellIndex){
        return (cellIndex / CELL_GRID_WIDTH) * (CELL_WIDTH + 5);
    }
}
