/**
 * Demonstrates correct cloning using super.clone().
 */
class Base implements Cloneable {
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    protected void doLogic() {
        System.out.println("Superclass doLogic");
    }
}

class Derived extends Base {
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    protected void doLogic() {
        System.out.println("Subclass's doLogic");
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        Derived original = new Derived();
        Base cloned = (Base) original.clone();
        cloned.doLogic();
    }
}
