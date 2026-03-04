import java.io.IOException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

class CustomResolver implements EntityResolver {
  public InputSource resolveEntity(String publicId, String systemId)
      throws SAXException, IOException {

    // Check for known good entities
    String adminEntityPath = "./admins.xml";
    String bookEntityPath = "./books.xml";
    if (systemId.equals(adminEntityPath)) {
      System.out.println("Resolving entity: " + publicId + " " + systemId);
      return new InputSource(adminEntityPath);
    } else if (systemId.equals(bookEntityPath)) {
      System.out.println("Resolving entity: " + publicId + " " + systemId);
      return new InputSource(bookEntityPath);
    } else {
      // Disallow unknown entities by returning a blank path
      return new InputSource(); 
    }
  }
}
