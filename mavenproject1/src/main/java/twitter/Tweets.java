/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package twitter;

import java.text.Normalizer;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import java.util.*;
import java.util.regex.*;

public class Tweets  {
     int id;
     String user;
     String tweet;
     Date date;
     /**
      * Polarité d'un Tweet (Note)
      * -1 pas annoté
      * 0 négatif
      * 2 neutre
      * 4 positif
      */
     int polarity;
     String request;


     public Tweets(Status status, String request){
         this.id = 1; //(float) status.getId();
         this.user = status.getUser().getName();
         if (status.getRetweetedStatus() != null) {
             this.tweet = status.getRetweetedStatus().getText().toLowerCase();
         } else if (status.getRetweetedStatus() == null) {
             this.tweet = status.getText().toLowerCase();
         }
         this.date = status.getCreatedAt();
         //this.polarity = Polarity.NONANNOTATE;
         this.polarity = -1;
         this.request = request;
     }

     public Tweets(int id, String user, String tweet, Date date, int polarity, String request){
         this.id = id;
         this.user = user;
         this.tweet = tweet.toLowerCase();
         this.date = date;
         this.polarity = polarity;
         this.request = request;
     }


     public void cleanData(){    //nettoie le tweet de ses userstag hashtag et link
        this.tweet = Pattern.compile("@([a-zA-Z])+").matcher(this.tweet)
                 .replaceAll(""); // tout d'abord on enleve les @

         this.tweet = Pattern.compile("\\bRT\\b").matcher(this.tweet)
                 .replaceAll(""); // puis on enleve les RT

         this.tweet = Pattern.compile("#").matcher(this.tweet)
                 .replaceAll(""); // puis on enleve les #

         String urlPattern = "((https?|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
         this.tweet = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE).matcher(this.tweet)
                 .replaceAll(""); // puis on enleve les liens

        /* Je le mets en commentaire car je pense que les smileys aident quand meme a dechiffrer des sentiments
        this.tweet = Pattern.compile(":-.?( \( | \) )").matcher(this.tweet)
                .replaceAll(""); //on enleve les smileys
         */

         //On ne garde que les lettres, les ponctuations et les espaces
         String regex = "[^\\p{L}\\p{P}\\p{Z}]";
         this.tweet = this.tweet.replaceAll(regex, "");
     }
     
     public int getId() {
         return id;
     }

     public String getUser() {
         return user;
     }
     
     public void setTweet(String t) {
    	 this.tweet = t;
     }
     public String getTweet() {
         return tweet;
     }

     public Date getDate() {
         return date;
     }

     public int getPolarity() {
         return polarity;
     }

     public String getRequest() {
         return request;
     }

     public void setPolarity(int polarity) {
         this.polarity = polarity;
     }
     
     public String getUserName(){
         return this.user;
     }
     @Override
     public String toString(){
         String emoticones ="nul";
         switch (this.polarity) {
             case 4:
                 emoticones = "😀";
                 break;
             case 0:
                 emoticones = "😡";
                 break;
             case 2:
                 emoticones = "😶";
                 break;
             default:
                 emoticones = "NON ANOTATED";
                 break;
         }
         return emoticones + "  @"+ this.user + "  "+ this.tweet + "\n" + "\n";
     }


     
}