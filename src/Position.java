public class Position {

    // Attribute
    private int x;
    private int y;

    // Konstruktor
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getter und Setter Methoden.
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // equals Methode um Objekte zu vergleichen.
    @Override
    public boolean equals(Object o) {
        // falls das Objekt mit sich selbst verglichen wird.
        if (this == o) {
            return true;
        }
        // Überprüft ob o eine Instanz von Position ist.
        if (!(o instanceof Position)) {
            return false;
        }
        // o passend casten um auf die Position zugreifen zu können.
        Position q = (Position) o;
        return this.x == (q.getX()) && this.y == (q.getY());
    }
}
