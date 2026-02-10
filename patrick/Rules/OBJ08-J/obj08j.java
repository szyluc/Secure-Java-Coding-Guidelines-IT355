/**
 * RULE: OBJ08-J
 */

class Coordinate {
    private int x;
    private int y;

    private class Point {
        private void getPoint() {
            System.out.println("(" + x + ", " + y + ")");
        }
    }
}

class AnotherClass {
    public static void main(String[] args) {
        Coordinate c = new Coordinate();
        Coordinate.Point p = new Point(); // The fact this does not work shows the rule is successful
    }
}