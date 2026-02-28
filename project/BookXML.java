import java.io.*;
import java.util.*;
import java.time.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



//Book id, Book name, Book author, Book category (fiction/non-fiction)

//Make a receipt on the current book

class BookXML{
    //This is an empty class. This class should hold no variables since all it does is read and 
    //write to XML files, and print receipts
    BookXML(){}

    //This is what makes the receipt. This takes a Book object and then takes out the
    void ReadFile(File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(file);

        NodeList booksList = document.getElementsByTagName("books");
        for(int i = 0; i < booksList.getLength(); i++){
            NodeList bookInfo = document.getElementsByTagName("book");
            ModifyBooks modifyBooks = new ModifyBooks();
            modifyBooks.addBook(new Book(bookInfo.item(0).getTextContent(), bookInfo.item(1).getTextContent(), bookInfo.item(2).getTextContent()));
        }
    }
    
    void MakeReceipt(Book currentBook, LocalDate nowDate) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("books");
        document.appendChild(root);

        Element book = document.createElement("book");
        Element title = document.createElement("title");
        title.appendChild(document.createTextNode(currentBook.getBookName()));
        Element author = document.createElement("author");
        author.appendChild(document.createTextNode(currentBook.getBookAuthor()));
        Element category = document.createElement("category");
        category.appendChild(document.createTextNode(currentBook.getBookCategory()));
        Element date = document.createElement("date");
        date.appendChild(document.createTextNode(nowDate.toString())); 
        
        book.appendChild(title);
        book.appendChild(author);
        book.appendChild(category);
        book.appendChild(date);

        root.appendChild(book);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult("./receipt.xml");
        transformer.transform(source, result);
        System.out.print("Receipt has been sent.\n");
    }
}
