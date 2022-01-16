package twitter.classifier;

import java.util.ArrayList;
import java.util.List;

import twitter.Tweets;

public abstract class Classifier {
    
	
	protected  List<Tweets> tweets;
    
	public Classifier() {};
	public Classifier(List<Tweets> tweets) {
		remove_less_3_letters(tweets);
		this.tweets = tweets;
	}
	

	 static void remove_less_3_letters(List<Tweets> tweets) {
		 for (Tweets t : tweets) {
			 String[] words_of_t = t.getTweet().split(" ");
             String new_tweet = "";	
			 for (String word : words_of_t) {
				 if (word.length() >= 3) {
				   new_tweet += word + " ";
				 }	 
			 }
			 t.setTweet(new_tweet);
		 }
	 }
	 public  List<Tweets> getTweetsOfClass(int c){
		 List<Tweets> tweets_of_c = new ArrayList<>();
		 //System.out.println("size:" + this.tweets.size());
		 for (Tweets t : this.tweets) {
			 if (t.getPolarity() == c)
				 tweets_of_c.add(t);
		 }
		 return tweets_of_c;
	 }
	 public void set_db(List<Tweets> t ) {
		 this.tweets = t;
	 }
	 
	 public List<Tweets> get_Tweets(){
		 return this.tweets;
	 }
	 
	
} 