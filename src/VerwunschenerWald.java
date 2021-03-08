public abstract class VerwunschenerWald {

    // Attribute
    protected Position position;
    protected int schaden = 0;

    // Konstruktor
    public VerwunschenerWald(Position position) {
        this.position = position;
    }

    // Getter Methoden.
    public Position getPosition() {
        return position;
    }

    public int getSchaden() {
        return schaden;
    }

    public abstract String getName();
}

// Unterklasse
class Rotkaeppchen extends VerwunschenerWald implements Person {

    //Attribut
    private int gesundheit = 100;

    // Konstruktor
    public Rotkaeppchen(Position position) {
        super(position);
    }

    // Setter
    public void setGesundheit(int gesundheit) {
        this.gesundheit = gesundheit;
    }

    // Methoden zur Bewegung
    public void geheHoch() {
        position.setY(position.getY() - 1);
    }

    public void geheRunter() {
        position.setY(position.getY() + 1);
    }

    public void geheLinks() {
        position.setX(position.getX() - 1);
    }

    public void geheRechts() {
        position.setX(position.getX() + 1);
    }

    // Change health status
    public void gesundheitVerringern(int wert) {
        if (wert > 0) {
            if (gesundheit > wert) {
                gesundheit -= wert;
            } else {
                gesundheit = 0;
            }
        }
    }

    // Check if still alive
    public boolean istNochLebendig() {
        return gesundheit > 0;
    }

    // Methode um auf den Name zugreifen zu können.
    @Override
    public String getName() {
        return "R";
    }

    // sprechen Methode um das Gespräch auszulösen.
    @Override
    public void sprechen(Person konversationspartner, int zaehler) {
        switch (zaehler) {
            case 1:
                System.out.println("Hallo, Oma");
                zaehler++;
                break;
            case 3:
                System.out.println("Tschüss, Oma");
                break;
        }
    }
}

// Unterklasse
class Oma extends VerwunschenerWald implements Person {

    // Konstruktor
    public Oma(Position position) {
        super(position);
    }

    // Methode um auf den Name zugreifen zu können.
    @Override
    public String getName() {
        return "O";
    }

    // sprechen Methode um das Gespräch auszulösen.
    @Override
    public void sprechen(Person konversationspartner, int zaehler) {
        if (zaehler == 2) {
            System.out.println("Hallo, Rotkäppchen");
            zaehler++;

        }
    }
}

// Unterklasse
class Baum extends VerwunschenerWald {

    // Konstruktor
    public Baum(Position position) {
        super(position);
    }

    // Methode um auf den Name zugreifen zu können.
    @Override
    public String getName() {
        return "B";
    }
}

class Wolf extends VerwunschenerWald {

    // Konstruktor
    public Wolf(Position position) {
        super(position);
        schaden = 5;
    }

    // Methode um auf den Name zugreifen zu können.
    @Override
    public String getName() {
        return "W";
    }
}

class Gefahr extends VerwunschenerWald {

    // Konstruktor
    public Gefahr(Position position) {
        super(position);
        schaden = 2;
    }

    // Methode um auf den Name zugreifen zu können.
    @Override
    public String getName() {
        return "G";
    }
}
