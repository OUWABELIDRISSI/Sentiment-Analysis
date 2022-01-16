/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package twitter;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


/**
 *
 * @author omar
 */
public class TweetDataBase {
    // ../databases/
    private static final File filename = new File("databases/bdd.csv");
    //private ArrayList<Tweets> tweets;
    public ArrayList<Tweets> tweetsFromDB;
    
    public TweetDataBase(){
        //tweets = new ArrayList<Tweets>();
        this.tweetsFromDB = new ArrayList<>();
    }
    
    /* Save tweets to databases */
    public void saveTweets(ArrayList<Tweets> twets){
        int Idd= 0;
        if (twets.isEmpty()) {
            return;
        }
        try {
            try (CSVWriter writer = new CSVWriter(new FileWriter(filename.getPath(),true))) {
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                for (Tweets tweet : twets){
                    Idd++;
                    //String[] data = {Float.toString(tweet.getId()), tweet.getUser(), tweet.getTweet(), dateFormat.format(tweet.getDate()), String.valueOf(tweet.getPolarity()), tweet.getRequest()};
                    String[] data = {Integer.toString(tweet.getId()), tweet.getUser(), tweet.getTweet(), dateFormat.format(tweet.getDate()), String.valueOf(tweet.getPolarity()), tweet.getRequest()};
                    writer.writeNext(data);
                }
            }
        }
        catch (IOException e) {
        }
    }
        /*Get tweets from databases*/


        public ArrayList<Tweets> loadDBTweets() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        
        try {
            Reader reader = Files.newBufferedReader(Paths.get(filename.getPath()));
            CSVReader csvReader = new CSVReader(reader);
            String[] nextRecord;
            
            while ((nextRecord = csvReader.readNext()) != null) {
                //Idd++;
                nextRecord[0] = nextRecord[0].replace(" ", "");
                Date date = formatter.parse(nextRecord[3]);
                /*Tweets tweet = new Tweets(Float.parseFloat(nextRecord[0].substring(0, 4)), nextRecord[1], nextRecord[2], date,
                        Integer.parseInt(nextRecord[4]), nextRecord[5]); */
                Tweets tweet = new Tweets(Integer.parseInt(nextRecord[0]), nextRecord[1], nextRecord[2], date,
                        Integer.parseInt(nextRecord[4]), nextRecord[5]);
                this.tweetsFromDB.add(tweet);
            }
        } catch (IOException | ParseException e) {
        }
        Collections.shuffle(this.tweetsFromDB);
        return this.tweetsFromDB;
    }
        

        
        /*
        public static boolean compareCSVFile(String filename, String search) throws IOException {
        Path path = Paths.get(filename);
        String p = path.toString();
        File file = new File(p);
        String[] words;
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String str;
        while((str = br.readLine()) != null)
        {
            words = str.split(", ");  //les mots sont séparé par des virgules
            for (String word : words)
            {
                //Cherche le mot
                if (word.toLowerCase().equals(search.toLowerCase()))
                {
                    // On a trouvé le mot dans le fichier
                    fr.close();
                    br.close();
                    return true;
                }
            }
        }
        fr.close();
        return false;
    }
    */
        
        
    /*public static int max(int n1, int n2, int n3){
        if( n1 >= n2 && n1 >= n3) return n1;
        else if (n2 >= n1 && n2 >= n3) return n2;
        else return n3;
    }*/
            
        

        
    public static void main(String[] args) throws IOException {
        TweetDataBase a = new TweetDataBase();
        ArrayList<Tweets> loadDBTweets = a.loadDBTweets();
        //System.out.println(loadDBTweets);
        SearchForTweets s = new SearchForTweets();
        s.getTweet("macron", 2);
        System.out.println(s.getResultsOfTweets());
        //a.saveTweets(s.getResultsOfTweets());
         
    }    }
    
    

