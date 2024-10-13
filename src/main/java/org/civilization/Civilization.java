package org.civilization;

import java.util.Random;

public class Civilization {

    public final int T0 = Main.universe.Year;

    public Double techPoint = 0.0;
    public Double theoPoint = 0.0;

    boolean growing = true;

    byte sendCount = 0;
    int sendTime = -1;

    Random random = new Random();

    // 0-A 1-B 3-C;
    byte techType = (byte) random.nextInt(3);

    public final String name =
            switch (techType) {
                case 0 : yield "A";
                case 1 : yield "B";
                default : yield "C";
            }
            + random.nextDouble();

    public void next() {
        if ((techPoint < 1000.0) && growing) {
            switch (techType) {
                case 0 :
                    techPoint += ( random.nextDouble(-0.69, 0.7) * 0.0008 * (Math.exp((double) (Main.universe.Year - T0) / 100) - 1)) / 50;
                case 1 :
                    techPoint += ( random.nextDouble(-0.01, 1.05) * 0.5 * (Main.universe.Year - T0)) / 10;
                case 2 :
                    techPoint += ( random.nextDouble(0.95, 1.5) * 10 / (Main.universe.Year - T0)) / 10;
            }
        }
        if (techPoint > 1000.0) {
            techPoint = 1000.0;
            growing = false;
        }
        if (techPoint < 0.0) {
            techPoint = 0.0;
        }

        if ((theoPoint < 1000.0) && growing) {
            theoPoint += (random.nextDouble(0.5, 1) * 0.05 * (((1000 - techPoint) / 1000 * 60) - ((Math.abs(techPoint - 500)) / 1000 * 80) + 40));
            theoPoint += (random.nextDouble(-0.1, 0.3) * (techPoint / 4000) );
        }
        if (theoPoint > 1000) {
            theoPoint = 1000.0;
        }

        if ((techPoint > 650.0) && ((theoPoint == 1000.0) || ((sendCount >= 2) && techPoint != 1000))) {
            enterBlackHole();
        }

        // send
        if ((techPoint > 60.0) && (Universe.getBoolByPossibility((techPoint - theoPoint) / 3000) || Universe.getBoolByPossibility(0.002))) {
            send();
        }
        if (sendCount >= 2) {
            if (sendTime == -1) {
                sendTime = 5;
            } else if (sendTime > 0) {
                sendTime -= 1;
            } else {
                killMe();
            }
        }

        // new
        if ((techPoint >= 500.0) && (Universe.getBoolByPossibility(techPoint / (1000*50)))) {
            Main.universe.newCivilization(this);
        }
        if ((techPoint == 1000.0 && Universe.getBoolByPossibility(0.1))) {
            Main.universe.newCivilization(this);
        }

        // highTech suicide
        if ((techPoint >= 900.0) && Universe.getBoolByPossibility(0.05)) {
            kill(this);
            Main.universe.DarkForestPoint -= 1;
        }

    }

    private void send() {
        sendCount += 1;
        System.out.println("[SEND] | " + this);
    }

    private void killMe() {

        //Main.universe.civilizations.remove(this);
        boolean hasHighTech = false;
        //System.out.println(this.name + " killed.");

        //Main.universe.DarkForestPoint += 20;

        for (Civilization c : Main.universe.civilizations) {
            if (c.techPoint >= 700.0 && this.techPoint != 1000.0) {
                c.kill(this);
                hasHighTech = true;
            }
        }

        if (hasHighTech) {

            System.out.println("[KILLED] | " + this);

            Main.universe.DarkForestPoint += 30;
        }

    }

    public void kill(Civilization c) {

        //Main.universe.DarkForestPoint += 1;
        Main.universe.grave.add(c);

    }

    private void enterBlackHole() {
        System.out.println("[ENTER Black Hole] | " + this);
        Main.universe.blackHole.add(this);
        //Main.universe.DarkForestPoint -= 1;
    }

    public String toString() {
        return (
                this.name + " - sendCount : " + sendCount+" | " + this.T0 + "(" + (Main.universe.Year - this.T0) * 100 + ") [techPoint : " + this.techPoint.intValue() + "; theoPoint : " + this.theoPoint.intValue() + "]"
                );
    }

}
