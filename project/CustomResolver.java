import java.io.IOException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Custom implementation of the EntityResolver interface used to control how
 * eternal XML entities are resolved during parsing
 * 
 * This resolver restricts entity resolution to a predefined set of trusted xml files (admin.xml, books.xml)
 * Any unknown or untrusted references are blocked by returning empty
 * which protects XML External entity attacks and unauthorized access
 */
class CustomResolver implements EntityResolver {
  /**
   * Resolves external XML entities by allowing only trusted/pre-defined local file paths
   * Unknown are rejected
   * 
   * @param publicId the public identifier of the external entity
   * @param systemId the system ID fo the external entity
   * @return an link InputSoruce for trusted entities 
   *        or an empty link to InputSorce for trusted
   *        or an empty link if entity is not recognized
   * @throws SAXEception if parsing related error occurs
   * @throws IOEception if an input/output error occurs
   */
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
