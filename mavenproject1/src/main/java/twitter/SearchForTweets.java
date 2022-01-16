/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package twitter;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
//import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


/**
 *
 * @author omar
 */
public class SearchForTweets {
    
    public ArrayList<Tweets> results;
    
    public SearchForTweets(){
        this.results = new ArrayList<>();
    }
   
        public boolean tweetNotDuplicated(Tweets tweet) {
        return this.results.stream().noneMatch(tmp -> (tmp.getTweet().equals(tweet.getTweet())));
    }
    

        public void getTweet1(String item , int numberOfTweets){
            
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                .setOAuthConsumerKey(Constant.OAuthConsumerKey)
                .setOAuthConsumerSecret(Constant.OAuthConsumerSecret)
		.setOAuthAccessToken(Constant.OAuthAccessToken)
		.setOAuthAccessTokenSecret(Constant.OAuthAccessTokenSecret)
		.setIncludeEntitiesEnabled(false); 
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter;
            twitter = tf.getInstance();
            Query query = new Query( item );
            QueryResult result ;
            query.setLang( "fr" );
            query.setCount( numberOfTweets );
            try {
		result = twitter.search( query );
		// Try to have extactly nbTweets tweets in the res list
		while ( result.hasNext() && ( this.results.size() < numberOfTweets ) ) {
                    List< Status > list = result.getTweets();
			int i = 0;
			while ( ( i < list.size() ) && ( this.results.size() < numberOfTweets ) ) {
                            
                            Status status = list.get( i );
                            if(status.isRetweet()){
                                
                            Tweets tt = new Tweets(status.getRetweetedStatus(),item);
                            tt.cleanData();
                            if (this.tweetNotDuplicated(tt)) {
                                this.results.add(tt);                           
                            }
                            }
                            else {
                            Tweets tt = new Tweets(status,item);
                            tt.cleanData();
                            if (this.tweetNotDuplicated(tt)) {
                                this.results.add(tt);                           
                            }
                            }
                            i++;
                                              
				
			}

                    query = result.nextQuery();
                    result = twitter.search( query );
                    }
                }
            catch ( TwitterException e ) {
                System.out.println(e.getErrorMessage());
		
		}
            
        }
        
        public void getTweet(String item, int nb) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(Constant.OAuthConsumerKey)
                .setOAuthConsumerSecret(Constant.OAuthConsumerSecret)
		.setOAuthAccessToken(Constant.OAuthAccessToken)
		.setOAuthAccessTokenSecret(Constant.OAuthAccessTokenSecret)
		.setIncludeEntitiesEnabled(false); 
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

       
        Query query = new Query(item);
        query.setCount(nb);
        query.setLang("fr");
        QueryResult tweets = null;
        try {
            tweets = twitter.search(query);
        } catch (TwitterException ex) {
            Logger.getLogger(SearchForTweets.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        for (Status status : tweets.getTweets()) {
            
            Tweets tt = new Tweets(status,"covid");
            tt.cleanData();
            if (this.tweetNotDuplicated(tt)) {
                this.results.add(tt);                           
                }
           
           

        }}
        
        
        public void SaveOnDB(){
            TweetDataBase db = new TweetDataBase();
            ArrayList<Tweets> tweets ;
            
            tweets = this.getResultsOfTweets();
            
            db.saveTweets(tweets);
        }
        
        public ArrayList<Tweets> getResultsOfTweets(){
        return this.results;
    }
        
        
        public static void main(String[] args) throws Exception{
            SearchForTweets s = new SearchForTweets();
            s.getTweet("covid", 5);
            System.out.println(s.getResultsOfTweets());
           
        }
    
    
}
