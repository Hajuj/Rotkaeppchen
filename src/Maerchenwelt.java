import java.util.ArrayList;
import java.util.Random;

public class Maerchenwelt {

    // Attribute
    private int x;
    private int y;
    private VerwunschenerWald[][] karte;
    private Oma oma;
    private Rotkaeppchen rotkaeppchen;
    private Wolf wolf;

    // Konstruktor
    public Maerchenwelt(int x, int y, int gefahrenAnzahl, int baumAnzahl) {
        this.x = x;
        this.y = y;
        // Karte initialisieren
        karte = new VerwunschenerWald[x][y];
        // Rotkäppchen initialisieren
        rotkaeppchen = new Rotkaeppchen(new Position(0, 0));
        karte[0][0] = rotkaeppchen;
        // Oma initialisieren
        Random random = new Random();
        int k = random.nextInt((x - 1) - (x - 8)) + x - 8;      // x Gebiet limit
        int z = random.nextInt((y - 1) - (y - 8)) + y - 8;      // y Gebiet limit
        oma = new Oma(new Position(k, z));
        karte[k][z] = oma;
        // Wolf initialisieren
        int r = random.nextInt(x);
        int s = random.nextInt(y);
        while (karte[r][s] != null) {       // Falls karte[r][s] schon belegt, dann nochmal random.
            r = random.nextInt(x);
            s = random.nextInt(y);
        }
        wolf = new Wolf(new Position(r, s));
        karte[r][s] = wolf;
        // Bäume und Gefahren Anzahl check + Exception
        int felderAnzahl = (x * y) - 3;
        if (gefahrenAnzahl + baumAnzahl > felderAnzahl) {
            throw new IllegalArgumentException("Reduzieren Sie die Anzahl der Bäume und Gefahren.");
        }
        // Bäume initialisieren
        for (int i = 0; i < baumAnzahl; i++) {
            int w = random.nextInt(x);
            int h = random.nextInt(y);
            if (karte[w][h] == null) {      // Falls karte[w][h] ist nicht belegt dann Bäume auf der Karte setzen.
                karte[w][h] = new Baum(new Position(w, h));
            } else {                        // Falls schon belegt, dann zurück und nochmal random.
                i--;
            }
        }
        // Gefahren initialisieren
        for (int i = 0; i < gefahrenAnzahl; i++) {
            int q = random.nextInt(x);
            int t = random.nextInt(y);
            if (karte[q][t] == null) {      // Falls karte[q][t] ist nicht belegt dann Gefahren auf der Karte setzen.
                karte[q][t] = new Gefahr(new Position(q, t));
            } else {
                i--;                        // Falls schon belegt, dann zurück und nochmal random.
            }
        }
        // Wald Größe limit + Exception
        if (x < 10 || y < 10) {
            throw new IllegalArgumentException("Vergrößern Sie den verwunschenen Wald.");
        }
    }

    // Getter Methoden
    public VerwunschenerWald[][] getKarte() {
        return karte;
    }

    public Rotkaeppchen getRotkaeppchen() {
        return rotkaeppchen;
    }

    public Oma getOma() {
        return oma;
    }

    // Die Bewegung
    public ArrayList<Position> wegFinden(Position ziel) {
        ArrayList<Position> path = new ArrayList<>();
        Random random = new Random();
        int rotX = getRotkaeppchen().getPosition().getX();
        int rotY = getRotkaeppchen().getPosition().getY();
        int i = 0;
        // Rotkäppchen erste Position (0,0) beim Pfad addieren.
        path.add(new Position(rotX, rotY));
        // Den Code beenden, wenn Rotkäppchen am Anfang (bei Position 0,0) von Bäumen umgeben ist. D.h. sie kann sich gar nicht bewegen.
        if (karte[0][0] == rotkaeppchen) {
            if (karte[rotX][rotY + 1] instanceof Baum) {            // Baum unten
                if (karte[rotX + 1][rotY] instanceof Baum) {        // Baum rechts
                    i = 500;                                        // i (Züge) = 500 damit den Code nicht mehr läuft.
                }
            }
        }
        // Rotkäppchen bewegen, wenn sie sich nicht beim Ziel befindet, und nicht mehr als 500 Züge genommen hat.
        while (!rotkaeppchen.getPosition().equals(ziel) && i < 500) {
            rotX = rotkaeppchen.getPosition().getX();
            rotY = rotkaeppchen.getPosition().getY();
            int w = random.nextInt(4);
            switch (w) {
                // Hoch
                case 0:
                    if (rotY > 0) {
                        if (karte[rotX][rotY - 1] == null) {
                            rotkaeppchen.geheHoch();
                            // um zu prüfen, dass die letzte Position nicht einen Wolf, eine Gefahr oder die Oma war.
                            if (!(karte[rotX][rotY] instanceof Wolf || karte[rotX][rotY] instanceof Gefahr || karte[rotX][rotY] instanceof Oma)) {
                                getKarte()[rotX][rotY - 1] = rotkaeppchen;      // neue Position als rotkäppchen speichern.
                                getKarte()[rotX][rotY] = null;                  // letzte Position auf null (leer) setzen.
                                path.add(new Position(rotX, rotY - 1));      // Position zum Pfad addieren
                                i++;                                            // Zug + 1
                            } else {                                            // falls einen Wolf, eine Gefahr oder die Oma, dann die letzte Position ist nicht leer.
                                getKarte()[rotX][rotY - 1] = rotkaeppchen;      // neue Position als rotkäppchen speichern.
                                path.add(new Position(rotX, rotY - 1));      // Position zum Pfad addieren
                                i++;                                            // Zug + 1
                            }
                        } else if (karte[rotX][rotY - 1] instanceof Oma) {
                            rotkaeppchen.geheHoch();
                            // um zu prüfen, dass die letzte Position nicht einen Wolf oder eine Gefahr war.
                            if (!(karte[rotX][rotY] instanceof Wolf || karte[rotX][rotY] instanceof Gefahr)) {
                                getKarte()[rotX][rotY] = null;                  // letzte Position auf null (leer) setzen.
                                path.add(new Position(rotX, rotY - 1));      // Position zum Pfad addieren
                                i++;                                            // Zug + 1
                            } else {                                            // falls einen Wolf oder eine Gefahr, dann die letzte Position ist nicht leer.
                                path.add(new Position(rotX, rotY - 1));      // Position zum Pfad addieren
                                i++;                                            // Zug + 1
                            }
                        } else if (karte[rotX][rotY - 1] instanceof Wolf) {
                            rotkaeppchen.geheHoch();
                            // um zu prüfen, dass die letzte Position nicht eine Gefahr oder die Oma war.
                            if (!(karte[rotX][rotY] instanceof Gefahr || karte[rotX][rotY] instanceof Oma)) {
                                getKarte()[rotX][rotY] = null;                  // letzte Position auf null (leer) setzen.
                            }
                            // Gesundheit verringern.
                            rotkaeppchen.gesundheitVerringern(karte[rotX][rotY - 1].getSchaden());
                            // überprüfen ob Rotkäppchen noch lebendig ist.
                            if (rotkaeppchen.istNochLebendig()) {
                                path.add(new Position(rotX, rotY - 1));     // Position zum Pfad addieren.
                                i++;                                           // Zug + 1
                            } else {                                           // Wenn Rotkäppchen nicht mehr lebendig ist.
                                path.add(new Position(rotX, rotY - 1));     // Position zum Pfad addieren
                                return path;                                   // Pfad zurückgeben
                            }
                        } else if (karte[rotX][rotY - 1] instanceof Gefahr) {
                            rotkaeppchen.geheHoch();
                            // um zu prüfen, dass die letzte Position nicht einen Wolf, eine Gefahr oder die Oma war.
                            if (!(karte[rotX][rotY] instanceof Wolf || karte[rotX][rotY] instanceof Gefahr || karte[rotX][rotY] instanceof Oma)) {
                                getKarte()[rotX][rotY] = null;                 // letzte Position auf null (leer) setzen
                            }
                            // Gesundheit verringern.
                            rotkaeppchen.gesundheitVerringern(karte[rotX][rotY - 1].getSchaden());
                            // überprüfen ob Rotkäppchen noch lebendig ist.
                            if (rotkaeppchen.istNochLebendig()) {
                                path.add(new Position(rotX, rotY - 1));     // Position zum Pfad addieren.
                                i++;                                           // Zug + 1
                            } else {                                           // Wenn Rotkäppchen nicht mehr lebendig ist.
                                path.add(new Position(rotX, rotY - 1));     // Position zum Pfad addieren
                                return path;                                   // Pfad zurückgeben
                            }
                        }
                    }
                    break;
                // Runter
                case 1:
                    if (rotY < y - 1) {
                        if (karte[rotX][rotY + 1] == null) {
                            rotkaeppchen.geheRunter();
                            // um zu prüfen, dass die letzte Position nicht einen Wolf, eine Gefahr oder die Oma war.
                            if (!(karte[rotX][rotY] instanceof Wolf || karte[rotX][rotY] instanceof Gefahr || karte[rotX][rotY] instanceof Oma)) {
                                getKarte()[rotX][rotY + 1] = rotkaeppchen;      // neue Position als rotkäppchen speichern.
                                getKarte()[rotX][rotY] = null;                  // letzte Position auf null (leer) setzen.
                                path.add(new Position(rotX, rotY + 1));      // Position zum Pfad addieren
                                i++;                                            // Zug + 1
                            } else {                                            // falls einen Wolf, eine Gefahr oder die Oma, dann die letzte Position ist nicht leer.
                                getKarte()[rotX][rotY + 1] = rotkaeppchen;      // neue Position als rotkäppchen speichern.
                                path.add(new Position(rotX, rotY + 1));      // Position zum Pfad addieren
                                i++;                                            // Zug + 1
                            }
                        } else if (karte[rotX][rotY + 1] instanceof Oma) {
                            rotkaeppchen.geheRunter();
                            // um zu prüfen, dass die letzte Position nicht einen Wolf oder eine Gefahr war.
                            if (!(karte[rotX][rotY] instanceof Wolf || karte[rotX][rotY] instanceof Gefahr)) {
                                getKarte()[rotX][rotY] = null;                  // letzte Position auf null (leer) setzen.
                                path.add(new Position(rotX, rotY + 1));      // Position zum Pfad addieren
                                i++;                                            // Zug + 1
                            } else {                                            // falls einen Wolf oder eine Gefahr, dann die letzte Position ist nicht leer.
                                path.add(new Position(rotX, rotY + 1));      // Position zum Pfad addieren
                                i++;                                            // Zug + 1
                            }
                        } else if (karte[rotX][rotY + 1] instanceof Wolf) {
                            rotkaeppchen.geheRunter();
                            // um zu prüfen, dass die letzte Position nicht eine Gefahr oder die Oma war.
                            if (!(karte[rotX][rotY] instanceof Gefahr || karte[rotX][rotY] instanceof Oma)) {
                                getKarte()[rotX][rotY] = null;                  // letzte Position auf null (leer) setzen.
                            }
                            // Gesundheit verringern.
                            rotkaeppchen.gesundheitVerringern(karte[rotX][rotY + 1].getSchaden());
                            // überprüfen ob Rotkäppchen noch lebendig ist.
                            if (rotkaeppchen.istNochLebendig()) {
                                path.add(new Position(rotX, rotY + 1));     // Position zum Pfad addieren
                                i++;                                           // Zug + 1
                            } else {                                           // Wenn Rotkäppchen nicht mehr lebendig ist.
                                path.add(new Position(rotX, rotY + 1));     // Position zum Pfad addieren
                                return path;                                   // Pfad zurückgeben
                            }
                        } else if (karte[rotX][rotY + 1] instanceof Gefahr) {
                            rotkaeppchen.geheRunter();
                            // um zu prüfen, dass die letzte Position nicht einen Wolf, eine Gefahr oder die Oma war.
                            if (!(karte[rotX][rotY] instanceof Wolf || karte[rotX][rotY] instanceof Gefahr || karte[rotX][rotY] instanceof Oma)) {
                                getKarte()[rotX][rotY] = null;                 // letzte Position auf null (leer) setzen.
                            }
                            // Gesundheit verringern.
                            rotkaeppchen.gesundheitVerringern(karte[rotX][rotY + 1].getSchaden());
                            // überprüfen ob Rotkäppchen noch lebendig ist.
                            if (rotkaeppchen.istNochLebendig()) {
                                path.add(new Position(rotX, rotY + 1));     // Position zum Pfad addieren
                                i++;                                           // Zug + 1
                            } else {                                           // Wenn Rotkäppchen nicht mehr lebendig ist.
                                path.add(new Position(rotX, rotY + 1));     // Position zum Pfad addieren
                                return path;                                   // Pfad zurückgeben
                            }
                        }
                    }
                    break;
                // Links
                case 2:
                    if (rotX > 0) {
                        if (karte[rotX - 1][rotY] == null) {
                            rotkaeppchen.geheLinks();
                            // um zu prüfen, dass die letzte Position nicht einen Wolf, eine Gefahr oder die Oma war.
                            if (!(karte[rotX][rotY] instanceof Wolf || karte[rotX][rotY] instanceof Gefahr || karte[rotX][rotY] instanceof Oma)) {
                                getKarte()[rotX - 1][rotY] = rotkaeppchen;      // neue Position als rotkäppchen speichern.
                                getKarte()[rotX][rotY] = null;                  // letzte Position auf null (leer) setzen.
                                path.add(new Position(rotX - 1, rotY));      // Position zum Pfad addieren
                                i++;                                            // Zug + 1
                            } else {                                            // falls einen Wolf, eine Gefahr oder die Oma, dann die letzte Position ist nicht leer.
                                getKarte()[rotX - 1][rotY] = rotkaeppchen;      // neue Position als rotkäppchen speichern.
                                path.add(new Position(rotX - 1, rotY));      // Position zum Pfad addieren
                                i++;                                            // Zug + 1
                            }
                        } else if (karte[rotX - 1][rotY] instanceof Oma) {
                            rotkaeppchen.geheLinks();
                            // um zu prüfen, dass die letzte Position nicht einen Wolf oder eine Gefahr war.
                            if (!(karte[rotX][rotY] instanceof Wolf || karte[rotX][rotY] instanceof Gefahr)) {
                                getKarte()[rotX][rotY] = null;                  // letzte Position auf null (leer) setzen.
                                path.add(new Position(rotX - 1, rotY));      // Position zum Pfad addieren
                                i++;                                            // Zug + 1
                            } else {                                            // falls einen Wolf oder eine Gefahr, dann die letzte Position ist nicht leer.
                                path.add(new Position(rotX - 1, rotY));      // Position zum Pfad addieren
                                i++;                                            // Zug + 1
                            }
                        } else if (karte[rotX - 1][rotY] instanceof Wolf) {
                            rotkaeppchen.geheLinks();
                            // um zu prüfen, dass die letzte Position nicht eine Gefahr oder die Oma war.
                            if (!(karte[rotX][rotY] instanceof Gefahr || karte[rotX][rotY] instanceof Oma)) {
                                getKarte()[rotX][rotY] = null;                  // letzte Position auf null (leer) setzen.
                            }
                            // Gesundheit verringern.
                            rotkaeppchen.gesundheitVerringern(karte[rotX - 1][rotY].getSchaden());
                            // überprüfen ob Rotkäppchen noch lebendig ist.
                            if (rotkaeppchen.istNochLebendig()) {
                                path.add(new Position(rotX - 1, rotY));     // Position zum Pfad addieren
                                i++;                                           // Zug + 1
                            } else {                                           // Wenn Rotkäppchen nicht mehr lebendig ist.
                                path.add(new Position(rotX - 1, rotY));     // Position zum Pfad addieren
                                return path;                                   // Pfad zurückgeben
                            }
                        } else if (karte[rotX - 1][rotY] instanceof Gefahr) {
                            rotkaeppchen.geheLinks();
                            // um zu prüfen, dass die letzte Position nicht einen Wolf, eine Gefahr  oder die Oma war.
                            if (!(karte[rotX][rotY] instanceof Wolf || karte[rotX][rotY] instanceof Gefahr || karte[rotX][rotY] instanceof Oma)) {
                                getKarte()[rotX][rotY] = null;                 // letzte Position auf null (leer) setzen.
                            }
                            // Gesundheit verringern.
                            rotkaeppchen.gesundheitVerringern(karte[rotX - 1][rotY].getSchaden());
                            // überprüfen ob Rotkäppchen noch lebendig ist.
                            if (rotkaeppchen.istNochLebendig()) {
                                path.add(new Position(rotX - 1, rotY));     // Position zum Pfad addieren
                                i++;                                           // Zug + 1
                            } else {                                           // Wenn Rotkäppchen nicht mehr lebendig ist.
                                path.add(new Position(rotX - 1, rotY));     // Position zum Pfad addieren
                                return path;                                   // Pfad zurückgeben
                            }
                        }
                    }
                    break;
                // Rechts
                case 3:
                    if (rotX < x - 1) {
                        if (karte[rotX + 1][rotY] == null) {
                            rotkaeppchen.geheRechts();
                            // um zu prüfen, dass die letzte Position nicht einen Wolf, eine Gefahr oder die Oma war.
                            if (!(karte[rotX][rotY] instanceof Wolf || karte[rotX][rotY] instanceof Gefahr || karte[rotX][rotY] instanceof Oma)) {
                                getKarte()[rotX + 1][rotY] = rotkaeppchen;      // neue Position als rotkäppchen speichern.
                                getKarte()[rotX][rotY] = null;                  // letzte Position auf null (leer) setzen.
                                path.add(new Position(rotX + 1, rotY));      // Position zum Pfad addieren
                                i++;                                            // Zug + 1
                            } else {                                            // falls einen Wolf, eine Gefahr oder die Oma, dann die letzte Position ist nicht leer.
                                getKarte()[rotX + 1][rotY] = rotkaeppchen;      // neue Position als rotkäppchen speichern.
                                path.add(new Position(rotX + 1, rotY));      // Position zum Pfad addieren
                                i++;                                            // Zug + 1
                            }
                        } else if (karte[rotX + 1][rotY] instanceof Oma) {
                            rotkaeppchen.geheRechts();
                            // um zu prüfen, dass die letzte Position nicht einen Wolf oder eine Gefahr war.
                            if (!(karte[rotX][rotY] instanceof Wolf || karte[rotX][rotY] instanceof Gefahr)) {
                                getKarte()[rotX][rotY] = null;                  // letzte Position auf null (leer) setzen.
                                path.add(new Position(rotX + 1, rotY));      // Position zum Pfad addieren
                                i++;                                            // Zug + 1
                            } else {                                            // falls einen Wolf oder eine Gefahr, dann die letzte Position ist nicht leer.
                                path.add(new Position(rotX + 1, rotY));      // Position zum Pfad addieren
                                i++;                                            // Zug + 1
                            }
                        } else if (karte[rotX + 1][rotY] instanceof Wolf) {
                            rotkaeppchen.geheRechts();
                            // um zu prüfen, dass die letzte Position nicht eine Gefahr oder die Oma war.
                            if (!(karte[rotX][rotY] instanceof Gefahr || karte[rotX][rotY] instanceof Oma)) {
                                getKarte()[rotX][rotY] = null;                 // letzte Position auf null (leer) setzen.
                            }
                            // Gesundheit verringern.
                            rotkaeppchen.gesundheitVerringern(karte[rotX + 1][rotY].getSchaden());
                            // überprüfen ob Rotkäppchen noch lebendig ist.
                            if (rotkaeppchen.istNochLebendig()) {
                                path.add(new Position(rotX + 1, rotY));     // Position zum Pfad addieren
                                i++;                                           // Zug + 1
                            } else {                                           // Wenn Rotkäppchen nicht mehr lebendig ist.
                                path.add(new Position(rotX + 1, rotY));     // Position zum Pfad addieren
                                return path;                                   // Pfad zurückgeben
                            }
                        } else if (karte[rotX + 1][rotY] instanceof Gefahr) {
                            rotkaeppchen.geheRechts();
                            // um zu prüfen, dass die letzte Position nicht einen Wolf, eine Gefahr oder die Oma  war.
                            if (!(karte[rotX][rotY] instanceof Wolf || karte[rotX][rotY] instanceof Gefahr || karte[rotX][rotY] instanceof Oma)) {
                                getKarte()[rotX][rotY] = null;                 // letzte Position auf null (leer) setzen.
                            }
                            // Gesundheit verringern.
                            rotkaeppchen.gesundheitVerringern(karte[rotX + 1][rotY].getSchaden());
                            // überprüfen ob Rotkäppchen noch lebendig ist.
                            if (rotkaeppchen.istNochLebendig()) {
                                path.add(new Position(rotX + 1, rotY));     // Position zum Pfad addieren
                                i++;                                           // Zug + 1
                            } else {                                           // Wenn Rotkäppchen nicht mehr lebendig ist.
                                path.add(new Position(rotX + 1, rotY));     // Position zum Pfad addieren
                                return path;                                   // Pfad zurückgeben
                            }
                        }
                    }
                    break;
            }
        }
        rotkaeppchen.setGesundheit(100);    // Gesundheit status auf 100 setzen.
        return path;                        // Pfad zurückgeben
    }

    public void printWald() {
        // Rahmen: linke obere Ecke
        System.out.print("+");
        // Rahmen: erste Zeile
        for (int i = 0; i < x; i++) {
            System.out.print("-");
        }
        // Rahmen: rechte obere Ecke
        System.out.println("+");
        for (int j = 0; j < y; j++) {
            // Rahmen: linker Rand
            System.out.print("|");

            // Die eigentliche Karte
            for (int i = 0; i < x; i++) {
                if (karte[i][j] != null) {
                    System.out.print(karte[i][j].getName());
                } else {
                    System.out.print(" ");
                }
            }
            // Rahmen: rechter Rand
            System.out.println("|");
        }
        // Rahmen: linke untere Ecke
        System.out.print("+");
        // Rahmen: letzte Zeile
        for (int i = 0; i < x; i++) {
            System.out.print("-");
        }
        // Rahmen: rechte untere Ecke
        System.out.println("+");
    }

    public void start() {
        printWald();
        // Ziel auf Oma setzen
        wegFinden(getOma().getPosition());
        // Wenn Rotkäppchen Position mit der Oma stimmt, dann Gespräch auslösen
        if (rotkaeppchen.getPosition().equals(getOma().getPosition())) {
            System.out.println("Rotkäppchen ist bei Oma angekommen.");
            rotkaeppchen.sprechen(oma, 1);
            oma.sprechen(rotkaeppchen, 2);
            rotkaeppchen.sprechen(oma, 3);
            // Ziel auf Rotkäppchen zu Hause setzen
            wegFinden(new Position(0, 0));
            // Wenn Rotkäppchen zu Hause ist
            if (rotkaeppchen == karte[0][0]) {
                System.out.println("Rotkäppchen ist wieder zu Hause angekommen.");
                // Wenn sie auf dem Weg nach Hause aber nicht mehr lebendig ist
            } else if (!rotkaeppchen.istNochLebendig()) {
                System.out.println("Rotkäppchen ist nicht wieder zu Hause angekommen.");
                // Wenn sie mehr als 500 Züge genommen hat
            } else {
                System.out.println("Rotkäppchen hat sich auf dem Heimweg verlaufen.");
            }
            // Wenn sie auf dem Weg zur Oma aber nicht mehr lebendig ist
        } else if (!rotkaeppchen.istNochLebendig()) {
            System.out.println("Rotkäppchen ist nicht bei der Oma angekommen.");
            // Wenn sie mehr als 500 Züge genommen hat
        } else {
            System.out.println("Rotkäppchen hat sich auf dem Weg zur Oma verlaufen.");
        }
    }
}
