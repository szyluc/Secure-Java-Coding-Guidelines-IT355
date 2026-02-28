import java.sql.SQLException;

public class Driver {
    public static void main(String [] args) {
        System.out.println("Driver is running");

        InputController inputController = new InputController();

        try {
            inputController.startMenu();
			inputController.loginMenu();
		} catch (SQLException e) {
			e.printStackTrace();
		}

        
    }
}
