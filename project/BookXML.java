import java.xml.*;
import java.io.*;
import javax.util.*;
import java.time.*;


//Book id, Book name, Book author, Book category (fiction/non-fiction)

//Make a receipt on the current book

class BookXML{
    //This is an empty class. This class should hold no variables since all it does is read and 
    //write to XML files, and print receipts
    BookXML(){}

    //This is what makes the receipt. This takes a Book object and then takes out the
    Document MakeReceipt(Book book){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("Receipt");
        document.appendChild(root);

        Element name = document.createElement("name");
        name.appendChild(document.createTextNode(book->name));
        Element author = document.createElement("author");
        author.appendChild(document.createTextNode(book->author));
        Element category = document.createElement("category");
        category.appendChild(document.createTextNode(book->category)); 
        LocalDate now = LocalDate.now();
        Element date = document.createElement("date");
        date.appendChild(document.createTextNode(now)); 
        
        root.appendChild(name);
        root.appendChild(author);
        root.appendChild(category);
        root.appendChild(date);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult("./receipt.xml");
        transformer.transform(source, result);
        System.out.print("Receipt has been sent.\n");
    }
}