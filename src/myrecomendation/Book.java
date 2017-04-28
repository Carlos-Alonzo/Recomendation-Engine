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
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author Carlos
 */
public class Book {
    private static int bookCounter=0;
    
    private String Author;
    private String Title;
    private String Genre;
    private String filepath;
    //private String Subject;
    private int rating;
    private int pages;
    private int timesRead;
    private int bookID;
    private BufferedReader in;
    private BufferedWriter out;
    private Vector<Integer> ratingHist;
    
    public Book(String Author, String Title, String Genre, int pages) throws IOException {
        this.Author = Author;
        this.Title = Title;
        this.Genre = Genre;
        //this.Subject = Subject;
        this.pages = pages;
        bookID = ++bookCounter;
        ratingHist = new Vector();
        filepath = "BookRatings\\"+ Title + "_ratings.txt"; 
        out = new BufferedWriter(new FileWriter(filepath,true));
        this.loadHistory();
    }
    
    private boolean loadHistory() throws IOException
    {
        String OneLine;
        boolean success;
        
        try{in = new BufferedReader(new FileReader(filepath));}
        catch(FileNotFoundException e)
        {            
            System.out.println("Not rating history found");
            success=false;
        }    
        while(null != (OneLine = in.readLine()))
            {
                ratingHist.add(Integer.parseInt(OneLine));
                read();
            }
       success = true;
       in.close();
       return success;                  
    }   

    public void printRatingHist() 
    {
        for (Integer rating : ratingHist)
            System.out.println(this.Title + ", " + rating);
    }
    
    public void rateBook(int rating) throws IOException
    {
        if(rating > 0 && rating <=5)
        {
            ratingHist.add(rating);  
            appendRating(String.valueOf(rating));
            read();
        }
        
    }
    private void appendRating(String line) throws IOException
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
    
    public void read() {
        this.timesRead++;
    }   
    
    public String toString()
    {
        return getTitle()+", "+ getAuthor() + ", " + getGenre() + ", " + getRating();
    }
    public String getAuthor() {
        return Author;
    }

    public String getGenre() {
        return Genre;
    }

    public int getPages() {
        return pages;
    }

    public String getTitle() {
        return Title;
    }

    public int getRating() {
        
        
        int sum=0;
        if(timesRead>0)
        {
            for(Integer rating : ratingHist)
            sum+=rating;
            return(sum/timesRead);
        }
        else
            return sum;
        
    }

    public int getBID() {
        return bookID;
    }
    
    

}
    
    