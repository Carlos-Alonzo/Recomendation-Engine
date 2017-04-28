/*
 * Recommendation Engine
 * Build a system to be able to recommend books to a user. Think NetFlix or Amazon. You need to be able to enter book information, ratings, users etc. Use one of two basic models to find a recommendation.
 * Model #1: Find another user most like the current user and recommend books they liked.
 * Model #2: Rate the book for various facets (author, genre etc.) and find out how it matches to the readers past recommendations.
 * We will discuss this bookinput class and there are several readings bookinput the Recommendation Engine Folder.
 * You should build a minimal working version first and then think about revisions.

 */
package myrecomendation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *
 * @author Carlos
 */
public class MyRecomendation 
{  
    private static String screenMenu = "\nMy Book Recomendation Engine, your place for all your reading needs!!\n" 
                            + "Welcome, please make a selection from following menu\n"
                            + "1. Register as a new reader\n"
                            + "2. See my read list\n"
                            + "3. Insert new book into booklist\n"
                            + "4. Read a Book from list(Suggestion to follow)\n"
                            + "5. Exit\n";
                            
    
    //private static String BOOKFILE="C:\\Users\\Carlos\\Dropbox\\CIS 252\\Projects\\MyRecomendation\\src\\Booklist.txt";
    private static String BOOKFILE="src\\Booklist.txt";
    //private static String READERFILE="C:\\Users\\Carlos\\Dropbox\\CIS 252\\Projects\\MyRecomendation\\src\\Readerslist.txt";
    private static String READERFILE="src\\Readerslist.txt";
    private static int LINEVAL=4;
    private Hashtable<String, Book> BookVault;
    private Hashtable <String, Reader> ReaderPool;
    private Hashtable <Integer, String> BookIDs;
    private Hashtable <Integer, String> ReaderIDs;
    private BufferedReader bookinput;
    private BufferedWriter bookoutput;
    private BufferedReader readerinput;
    private BufferedWriter readeroutput;
    private Scanner input;
    private Reader CurrReader;
    private Book CurrBook;
    
    public MyRecomendation() throws IOException 
    {
        BookVault = new Hashtable<>();
        BookIDs = new Hashtable<>();
        ReaderIDs = new Hashtable<>();
        this.loadBooks();
        ReaderPool = new Hashtable<>();
        this.loadReaders();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        bookoutput = new BufferedWriter(new FileWriter(BOOKFILE,true));//load both linkedlists        
        readeroutput = new BufferedWriter(new FileWriter(READERFILE,true));//load both linkedlists        
        
    }
    
    private void insertBook(String Author, String Title, String Genre, int pages) throws IOException
    {
       BookVault.put(Title,new Book(Author, Title, Genre, pages));
       BookIDs.put(BookVault.get(Title).getBID(), Title);
       
        //saveNewBook(Author, Title, Genre, pages); 
    }
    
    
    public void saveNewBook(String Author, String Title, String Genre, int pages) throws IOException
    {
        insertBook(Author,  Title,  Genre, pages);
        setCurrBook(Title);
        String line = "\r\n"+ Author + "," + Title + "," + Genre + "," + pages;
       
        try
        {  
            bookoutput.append(line);
            bookoutput.flush();
            System.out.println("Book:\" " + line + "\" has been added to the book vault file");
        }
        
        catch(NullPointerException e){ System.out.println("failed to insert new book: " + e.toString());}
        
    }
    public void saveNewReader(String name) throws IOException
    {
        addReader(name);
         try
        {  
            readeroutput.append("\r\n"+ name);
            readeroutput.flush();
            System.out.println("Reader:\" " + name + "\" has been added to the readers file");
        }
        
        catch(NullPointerException e){ System.out.println("failed to insert new reader: " + e.toString());}
        
    }
    
    private void addReader(String name) throws IOException
    {
        //CurrReader = new Reader(name);
        ReaderPool.put(name, new Reader(name));
        ReaderIDs.put(ReaderPool.get(name).getReaderID(), name);
           
    }
            
    
    private boolean loadBooks() throws IOException
    {
        String OneLine;
        String [] KeyandVal = new String[LINEVAL];
        boolean success;
        
        try{bookinput = new BufferedReader(new FileReader(BOOKFILE));}
        catch(FileNotFoundException e)
        {            
            System.out.println("No bookfile found, please check location/filename");
            success=false;
        }    
        while(null != (OneLine = bookinput.readLine()))
            {
                KeyandVal= OneLine.split(",");
                //Book(String Author, String Title, String Genre, int pages)
                this.insertBook(KeyandVal[0],KeyandVal[1],KeyandVal[2],Integer.parseInt(KeyandVal[3]));
            }
       success = true;
       bookinput.close();
       return success;    
        
    
    }
    private boolean loadReaders() throws IOException
    {
        String OneLine;
        //String [] KeyandVal = new String[LINEVAL];
        boolean success;
        
        try{readerinput = new BufferedReader(new FileReader(READERFILE));}
        catch(FileNotFoundException e)
        {            
            System.out.println("No reader found, please check location/filename");
            success=false;
        }    
        while(null != (OneLine = readerinput.readLine()))
            {
                //KeyandVal= OneLine.split(",");
                //Book(String Author, String Title, String Genre, int pages)
                this.addReader(OneLine);
            }
       success = true;
       readerinput.close();
       return success;    
        
    
    }
    
    private String bookTitle(int bookid)
    {
        return BookIDs.get(bookid);
    }
    public void printBooks()
    {
        String printout = "\nNo.| Title         |    Author     | Genre     |  Rating\n";
        Set<Integer> keySet;
        keySet = BookIDs.keySet();
        
        for (Integer bid : keySet)
            printout+= bid +". "+ BookVault.get(bookTitle(bid)).toString()+ "\n";
       
        System.out.println(printout);    
    }

    public String getCurrReaderName() {
        return CurrReader.getName();
    }
    public Reader getCurrReader() {
        return CurrReader;
    }

    public void setCurrReader(int readerID) {
        
        CurrReader=ReaderPool.get(ReaderIDs.get(readerID));
        //setCurrReader(ReaderPool.get(name));
        System.out.println("Welcome, " + CurrReader.getName() + ". You are the current reader."); 
    }
    
    public void setCurrReader(String name) {
        
        CurrReader=ReaderPool.get(name);
        //setCurrReader(ReaderPool.get(name));
        System.out.println("Welcome, " + CurrReader.getName() + ". You are the current reader."); 
    }
    
    public String getCurrBookName() {
        return CurrBook.getTitle();
    }
    public Book getCurrBook() {
        return CurrBook;
    }

    public void setCurrBook(int bookID) {
        
        CurrBook=BookVault.get(BookIDs.get(bookID));
        //setCurrReader(ReaderPool.get(name));
        System.out.println("Book titled, " + getCurrBookName() + ". is the current book."); 
    }
    
    public void setCurrBook(String name) {
        
        CurrBook=BookVault.get(name);
        //setCurrReader(ReaderPool.get(name));
        System.out.println("Book titled, " + getCurrBookName() + ". is the current book."); 
    }
    
    public void printReaders()
    {
        Set<String> keySet;
        keySet = ReaderPool.keySet();        
        String printout="\nNo.| Name    |\n";
        for (String reader : keySet)
            printout+=ReaderPool.get(reader).getReaderID() + "  | " + reader + " |\n";
                            
        System.out.println(printout); 
    }
    
    public void printReaderHist(String name)
     {
         System.out.println(name  + "'s " + 
                                "Rating History \n" +
                                "|    Title       , Rating |");            
            ReaderPool.get(name).printRatingHist();
     }
    public void rateBookForCurrReader(String bookName, int rating) throws IOException
    {
        getCurrReader().rateBook(bookName, rating);    
    }
    private void registerReader() throws IOException
    {
        System.out.println("ADD NEW USER");
        System.out.println("Please enter your name:");
        String readername = input.next();
        saveNewReader(readername);
        //addReader(readername);
        setCurrReader(readername);
    
    }
    
    public void printSuggestion(String suggestion, HashMap<Integer,String> HighestRatedBooks)
    {
        String printout= suggestion+"\n";
        Set<Integer> ratedkeySet;
        ratedkeySet = HighestRatedBooks.keySet();
        
        for (Integer bid : ratedkeySet)
              printout+=bid+". "+ bookTitle(bid) + ", rated: "+ BookVault.get(bookTitle(bid)).getRating()+"\n";
            
        System.out.println(printout);
        
    }
    
    public  HashMap<Integer,String> suggestHighestBook()
    {
        //Model #1: Find another user most like the current user and recommend books they liked.
        //Model #2: Rate the book for various facets (author, genre etc.) and find out how it matches to the readers past recommendations.
        
        
        HashMap<Integer,Integer> RatedBooks = new HashMap();
        HashMap<Integer,String> HighestRatedBooks = new HashMap();
        //Suggestions
        //  1. suggest highest rated book
        //     iterate through list finding 5's and if not 5's 4's so on an so forth
             //String printout = "\nNo.| Title     |    Author  | Genre    |  Pages\n";
        Set<Integer> keySet;
        keySet = BookIDs.keySet();
        
        
        //get all rated books that are not equal to current book
        for (Integer bid : keySet)
        {
            int rating = BookVault.get(bookTitle(bid)).getRating();
            if(rating >=1 || rating <=5 && !bookTitle(bid).equals(CurrBook.getTitle()))
                RatedBooks.put(bid, rating);
        }
        
        Set<Integer> ratedkeySet;
        ratedkeySet = RatedBooks.keySet();
        
        if(!RatedBooks.isEmpty())
        //get the highest rated boooks        
        {
            if(RatedBooks.containsValue(5))
            {
                for (Integer bid : ratedkeySet)
                {   
                    if(RatedBooks.get(bid)==5)
                            HighestRatedBooks.put(bid, bookTitle(bid));
                }
                
            }   
            else if(RatedBooks.containsValue(4))
            {
                for (Integer bid : ratedkeySet)
                {   
                    if(RatedBooks.get(bid)==4)
                            HighestRatedBooks.put(bid, bookTitle(bid));

                }
                //printSuggestion(HighestRatedBooks);
            }   
            else if(RatedBooks.containsValue(3))
            {
                for (Integer bid : ratedkeySet)
                {   
                    if(RatedBooks.get(bid)==3)
                            HighestRatedBooks.put(bid, bookTitle(bid));

                }
               // printSuggestion(HighestRatedBooks);
            }   
            else if(RatedBooks.containsValue(2))
            {
                for (Integer bid : ratedkeySet)
                {   
                    if(RatedBooks.get(bid)==2)
                            HighestRatedBooks.put(bid, bookTitle(bid));

                }
                //printSuggestion(HighestRatedBooks);
            }   
            else if(RatedBooks.containsValue(1))
            {
                for (Integer bid : ratedkeySet)
                {   
                    if(RatedBooks.get(bid)==1)
                            HighestRatedBooks.put(bid, bookTitle(bid));

                }
                //printSuggestion(HighestRatedBooks);
            }   
        
            printSuggestion("Highest Rated Books",HighestRatedBooks);
            return HighestRatedBooks;
        }
        else
        {
            System.out.println("There are no rated books to suggest to reader");
            return null;
        }
          
    }
    public boolean suggestHighestSameGenre(HashMap<Integer,String> HighestRatedBooks)
    {
        boolean success=false;
       if(HighestRatedBooks!=null)
       {
           HashMap<Integer,String> HighestSameGenreBooks = new HashMap();
       
        Set<Integer> ratedkeySet;
        ratedkeySet = HighestRatedBooks.keySet();
        String currbookgenre = getCurrBook().getGenre();
           System.out.println("The current Book Genre is: " + currbookgenre);
           
        for (Integer bid : ratedkeySet)
        {
            if(currbookgenre.equals(BookVault.get(bookTitle(bid)).getGenre())&& !bookTitle(bid).equals(CurrBook.getTitle()))
                HighestSameGenreBooks.put(bid, bookTitle(bid));
        }
    
        if(HighestSameGenreBooks.isEmpty())
        {
            success=false;
            System.out.println("There are no rated books of the same genre");
        }
        else
        {
            printSuggestion("Highest Rated Books of same Genre: "+ currbookgenre, HighestSameGenreBooks);
            success=true;
            
        }
       }
       else
           success=false;
       return success;
    }
    
    public int runMenu() throws IOException
    {
                            /*"My Book Recomendation Engine, your place for all your reading needs!!\n" 
                            + "Welcome, please make a selection from following menu\n"
                            + "1. Register as a new reader\n"
                            + "2. See my read list\n"
                            + "3. Insert new book into booklist\n"
                            + "4. Read a Book from list(Suggestion to follow)\n"
                            + "5. Exit*/
        int success=5;
        if(CurrReader==null)
        {     
            printReaders();
            System.out.println("Hi, are you anyone of above readers? (Y or N)");

            String yesorno=input.next();
            if (yesorno.equals("Y") || yesorno.equals("y"))
            {
                System.out.println("Please enter number:");
                int readernum = input.nextInt();
                setCurrReader(readernum);

            }
            else
                registerReader();
        } 
        
        System.out.println(screenMenu);        
        System.out.println("Enter the number for the option desired:");
        int choice = input.nextInt();
        System.out.println("You entered: "+ choice);
        success=choice;
        
        switch(choice)
        {  
            case 1://"1. Register as a new reader\n"
                registerReader();
                break;
                
            case 2://+ "2. See my read list\n"
                System.out.println("YOUR READ LIST");
                printReaderHist(getCurrReaderName());                
                break;
                
            case 3://+ "3. Insert new book into booklist\n"
                
                System.out.println("INSERT NEW BOOK");
                System.out.println("Please enter Author:");
                String author = input.next();
                System.out.println("Please enter Book Title:");
                String title = input.next();
                System.out.println("Please enter genre:");
                String genre = input.next();
                System.out.println("Please enter no. of pages:");
                int pages = input.nextInt();
                this.saveNewBook(author, title, genre, pages);
                break;
                
            case 4://+ "4. Read a Book from list(Suggestion to follow)\n"
                this.printBooks();
                System.out.println("Please enter the number for the book you want to read:");
                int bookid = input.nextInt();
                System.out.println("How would you rate this book (1-5): " + bookTitle(bookid));
                int rating = input.nextInt();
                BookVault.get(bookTitle(bookid)).rateBook(rating);
                rateBookForCurrReader(BookIDs.get(bookid), rating);
                setCurrBook(bookid);
                suggestHighestSameGenre(suggestHighestBook());
                break;
                
            case 5://exit
                break;
              
                
            default:
                success=5;
                System.out.println("Not a valid choice");
                break;
       }
        
        return success;
        
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException 
    {
      MyRecomendation RecoEng = new MyRecomendation(); 
      int choice=0;
      
      while(choice!=5)
          choice=RecoEng.runMenu();
         
      
    }
    
}
