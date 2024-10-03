package org.civilization;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {

    public static int checkPoint = 10000;
    public static int originalCivNum = 0;
    public static boolean doFileRecord = true;


    public static Universe universe = new Universe();

    public static void main(String[] args) throws FileNotFoundException {

        double startTime = (double) (System.currentTimeMillis() / 1000);

        if (doFileRecord) {
            PrintStream fileOutput = new PrintStream(new FileOutputStream("output.txt"));
            System.setOut(fileOutput);
        }

        System.out.println("Starting...");
        System.out.println(" ");
        System.out.println("checkPoint : " + checkPoint);
        System.out.println("originalCivNum : " + originalCivNum);
        System.out.println("----------");

        for (int i = 0; i <= originalCivNum - 1; i++) {
            universe.newCivilization();
        }

        System.out.println("----------");

        while (universe.next(checkPoint)) {
            System.out.println(" ");
        }

        double stopTime = (double) (System.currentTimeMillis() / 1000);

        System.out.println(" ");
        System.out.println("Start : " + startTime + "s");
        System.out.println("Stop : " + stopTime + "s");
        System.out.println("Running : " + (startTime - stopTime) + "s");

    }
}