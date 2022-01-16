/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package twitter.classifier;

import twitter.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import twitter.SearchForTweets;
import twitter.TweetDataBase;
import twitter.Tweets;

/**
 *
 * @author omar
 */
public class KNN extends Classifier{

	

	private final int neighbors;
	public KNN ( List<Tweets> tweetPool, int nbNeighbors ) {
		super(tweetPool);
		this.neighbors = nbNeighbors;
	}

	
	public int distance ( String tweet1, String tweet2 ) {
		List< String > msg1 = Arrays.asList( tweet1.split( " " ) );
		List< String > msg2 = Arrays.asList( tweet2.split( " " ) );
		int totalNbOfWords = msg1.size() + msg2.size();
		int commonNbOfWords = 0;

		for ( String word : msg1 ) {
			if ( msg2.contains( word ) ) {
				commonNbOfWords++;
			}
		}

		return totalNbOfWords - ( 2 * commonNbOfWords );
	}

	
	public int classify ( Tweets twet ) {
        String msg = twet.getTweet();
		int nb = this.neighbors;
		List< Tweets > tweets = new ArrayList<  >( super.tweets );
		Distance[] neighbors = new Distance[ nb ];
		int maxIndex = 0;

		for ( int i = 0; i < nb; i++ ) {
			Tweets tweet = tweets.get( i );
			neighbors[ i ] = new Distance( this.distance( msg, tweet.getTweet() ), tweet );
			if ( neighbors[ i ].getDistance() > neighbors[ maxIndex ].getDistance() ) {
				maxIndex = i;
			}
		}

		for ( int i = nb; i < tweets.size(); i++ ) {
			Tweets tweet = tweets.get( i );
			int distance = this.distance( msg, tweet.getTweet() );

			if ( distance < neighbors[ maxIndex ].getDistance() ) {
				neighbors[ maxIndex ] = new Distance( distance, tweet );

				// Search the new maxIndex
				for ( int k = 0; k < neighbors.length; k++ ) {
					if ( neighbors[ k ].getDistance() > neighbors[ maxIndex ].getDistance() ) {
						maxIndex = k;
					}
				}
			}
		}

		int cptPositive = 0;
		int cptNegative = 0;
		int cptNeutral = 0;

		for ( int i = 0; i < nb; i++ ) {
			int  feeling = neighbors[ i ].tweet.getPolarity();

            switch (feeling) {
                case 4:
                    cptPositive++;
                    break;
                case 0:
                    cptNegative++;
                    break;
                default:
                    cptNeutral++;
                    break;
            }
		}
        int polarity ;
		if ( ( cptNeutral >= cptPositive ) && ( cptNeutral >= cptNegative ) ) {
			
                        twet.setPolarity(2);
                        polarity = 2;
		} else if ( cptPositive > cptNegative ) {
			
                         twet.setPolarity(4);
                         polarity = 4;
		} else {
			
                         twet.setPolarity(0);
                         polarity = 0;
		}
		return polarity;
        }
	
	

	

	// Class representing a couple : ( Distance, Tweet )
	private class Distance {

		private int distance;

		private Tweets tweet;

		public Distance ( int distance, Tweets tweet ) {
			this.distance = distance;
			this.tweet = tweet;
		}

		public double getDistance () {
			return this.distance;
		}

		public Tweets getTweet () {
			return this.tweet;
		}

                @Override
		public String toString () {
			return "(" + this.distance + ", " + this.tweet + ")";
		}

	}
        
        /*public static void main(String[] args) throws Exception{
            SearchForTweets s = new SearchForTweets();
            ArrayList<Tweets> res = new ArrayList<>();
            s.getTweet("heureux",10);
            res = s.getResultsOfTweets();
            TweetDataBase a = new TweetDataBase();
            //a.saveTweets(res);
            ArrayList<Tweets> database = a.loadDBTweets();
            for(int i=0;i<res.size();i++){
            KNN knn = new KNN(database,5);
            System.out.println(res.get(i).getTweet() + res.get(i).getPolarity());
            //System.out.println(knn.classify(res.get(i)));
            }
            //System.out.println("KNN" + res.get(0).tweet + res.get(0).polarity);

        }*/
        
}