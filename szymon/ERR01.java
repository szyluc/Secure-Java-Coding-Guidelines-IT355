public class ERR01 {
    
	public static void main(String[] args) {
		try {
			recursion();
		} catch (StackOverflowError e) {
			System.out.println("Error occurred.");
			// e.printStackTrace(); 
			// Printing stack trace would show the recursive method causes a StackOverflowError, which brings in the threat of a DoS attack.
		}
	}
	
	public static void recursion() {
		recursion();
	}

}
