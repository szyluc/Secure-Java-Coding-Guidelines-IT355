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
    void ReadFile(File file){
        File xmlFile = newFile(file);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(xmlFile);

        NodeList booksList = document.getElementsByTagName("books");
        for(int i = 0; i < booksList.getLength(); i++){
            NodeList bookInfo = document.getElementsByTageName("book");
            ModifyBooks.addBook(new Book(bookInfo.item(0), bookInfo.item(1), bookInfo.item(2)));
        }
    }
    
    void MakeReceipt(Book book){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("books");
        document.appendChild(root);

        Element book = document.createElement("book");
        Element title = document.createElement("title");
        title.appendChild(document.createTextNode(book->title));
        Element author = document.createElement("author");
        author.appendChild(document.createTextNode(book->author));
        Element category = document.createElement("category");
        category.appendChild(document.createTextNode(book->category)); 
        LocalDate now = LocalDate.now();
        Element date = document.createElement("date");
        date.appendChild(document.createTextNode(now)); 
        
        book.appendChild(title);
        book.appendChild(author);
        book.appendChild(category);
        book.appendChild(date);

        books.appendChild(book);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult("./receipt.xml");
        transformer.transform(source, result);
        System.out.print("Receipt has been sent.\n");
    }
}
