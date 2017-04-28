/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myrecomendation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Carlos
 */
public class Reader 
{
    
    private static int readerCounter=0;
    private String name;
    private String filepath;
    private HashMap<String,Integer> BookRatingHistory;
    private int readerID;
    private BufferedReader in;
    private BufferedWriter out;
    
    public Reader(String name) throws IOException 
    {
        this.name = name;        
        this.readerID = ++readerCounter;
        BookRatingHistory = new HashMap();
        filepath= "Readers\\"+name + "_readlist.txt";
        out = new BufferedWriter(new FileWriter(filepath,true)); 
        this.loadHistory();
    }

    public String getName() {
        return name;
    }
    
    public int getReaderID() {
        return readerID;
    }
    
    private boolean loadHistory() throws IOException
    {
        String OneLine;
        String [] KeyandVal = new String[2];
        boolean success;
        
        try{in = new BufferedReader(new FileReader(filepath));}
        catch(FileNotFoundException e)
        {            
            System.out.println("Not rating history found");
            success=false;
        }    
        while(null != (OneLine = in.readLine()))
            {
                KeyandVal= OneLine.split(",");
                BookRatingHistory.put(KeyandVal[0],Integer.parseInt(KeyandVal[1]));
            }
       success = true;
       return success;                  
    }   

    public void printRatingHist() 
    {
        Set<String> keySet;
        keySet = BookRatingHistory.keySet();
        for (String key : keySet)
            System.out.println(key + ", " + BookRatingHistory.get(key));
        
    }

    public HashMap<String, Integer> getBookRatingHistory() {
        return BookRatingHistory;
    }
    
    public void rateBook(String bookName, int rating) throws IOException
    {
        BookRatingHistory.put(bookName, rating);  
        appendRating(bookName+","+String.valueOf(rating));
        
    }
    public void appendRating(String line) throws IOException
    {
        try
        {
            out.append(line);
            out.newLine();
            out.flush();
        }
        //out.write(line);}
        catch(IOException | NullPointerException e){ System.out.println("failed to add rating to book rating file" + e.toString());}
    
    }
}
