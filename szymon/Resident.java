import java.io.Serializable;

public class Resident implements Serializable{
    private static final long serialVersionUID = 1L;
    String name;
    Address address;

    public Resident(String name, String address){
        this.name = name;
        this.address = new Address(address);
    }

    class Address implements Serializable{
        private static final long serialVersionUID = 1L;
        String address;

        public Address(String address){
            this.address = address;
        }

        public String toString(){
            return address;
        }
    }

    public String toString(){
        return "Resident name: " + name + "Address: " + address;
    }
}

