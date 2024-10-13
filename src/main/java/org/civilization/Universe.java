package org.civilization;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.HashSet;

public class Universe {

    public int DarkForestPoint = 0;

    public HashSet<Civilization> civilizations = new HashSet<>();
    public HashSet<Civilization> blackHole = new HashSet<>();
    public HashSet<Civilization> grave = new HashSet<>();
    public HashSet<Civilization> newCiv = new HashSet<>();

    public int Year = 0;    // a hundred years

    public boolean next(int checkPoint) {
        System.out.println("Year - " + Year * 100);

        for (Civilization c : civilizations) {
            c.next();
        }

        for (Civilization c : grave) {
            civilizations.removeIf(ci -> ci == c);
        }
        for (Civilization c : blackHole) {
            civilizations.removeIf(ci -> ci == c);
        }
        civilizations.addAll(newCiv);
        newCiv.clear();

        if ((Universe.getBoolByPossibility((double) (10000 - civilizations.size()) / 50000)) || Universe.getBoolByPossibility(0.00001)) {
            newCivilization();
        }

        if ((DarkForestPoint < checkPoint) && !(civilizations.size() < Main.originalCivNum / 10)) {
            Year += 1;
            System.out.println("----------");
            System.out.println("Civilization number : " + civilizations.size());
            System.out.println("BlackHole number : " + blackHole.size());
            System.out.println("Dead number : " + grave.size());
            System.out.println("Dark Forest Point : " + DarkForestPoint);
            System.out.println("----------");
            return true;
        } else {

            System.out.println("-----END-----");
            System.out.println("Civilizations : " + civilizations.size());
            for (Civilization c : civilizations) {
                System.out.println(c.toString());
                log(c);
            }
            System.out.println("----------");
            System.out.println("Black Hole : " + blackHole.size());
            for (Civilization c : blackHole) {
                System.out.println(c.toString());
                log(c);
            }
            System.out.println("----------");
            System.out.println("Grave : " + grave.size());
            for (Civilization c : grave) {
                System.out.println(c.toString());
                log(c);
            }

            System.out.println("----------");
            System.out.println("Total Number : " + civilizations.size() + blackHole.size() + grave.size());
            return false;
        }

    }

    public void newCivilization(Civilization cp) {

        Civilization c = new Civilization();
        c.techType = cp.techType;
        c.techPoint = cp.techPoint / 5;
        c.theoPoint = cp.theoPoint / 3;

        //System.out.println("new copied Civilization");
        System.out.println("New colony set up.");

        DarkForestPoint += 1;

        newCiv.add(c);

    }
    public void newCivilization() {
        Civilization c = new Civilization();

        System.out.println("new born Civilization");

        newCiv.add(c);
    }

    // 0 < p < 1
    public static boolean getBoolByPossibility(double p) {
        if (0 <= p) {
            Random random = new Random();
            double point = random.nextDouble();
            //System.out.println(p);
            return point <= p;
        } else { return false; }
    }

    private void log(Civilization c) {

        File file = new File("log.txt");
        if (!(file.exists())) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        byte[] bytes;
        bytes = c.toString().getBytes();

        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(bytes, 0, bytes.length);
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
