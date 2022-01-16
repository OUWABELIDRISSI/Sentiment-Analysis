/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package twitter.classifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import twitter.SearchForTweets;
import twitter.TweetDataBase;
import twitter.Tweets;

/**
 * Class reprsenting objects that classify the message of a tweet using the Polarity
 * method.
 * 
 * @author 
 */
public class KeyWords  extends Classifier{

	
	/**
	 * Path to the positive dictionary of the DictionaryAssigner.
	 */
	private final String positivePath;

	/**
	 * Path to the negative dictionary of the DictionaryAssigner.
	 */
	private final String negativePath;

	/**
	 * Contructor of DictionaryAssigner.
	 * 
         * @param tweets
	 * @param positivePath
	 *            path to the positive file
	 * @param negativePath
	 *            path to the negative file
	 */
	public KeyWords ( List<Tweets> tweets , String positivePath, String negativePath ) {
		super(tweets);
		this.positivePath = positivePath;
		this.negativePath = negativePath;
	}

	// Load a file into a string
	public String fileToString ( String path ) {
		StringBuilder res = new StringBuilder();
		File file = new File( path );

		if ( file.exists() && !file.isDirectory() ) {
			try {
                            try (BufferedReader br = new BufferedReader( new FileReader( path ) )) {
                                String line ;
                                
                                while ( ( line = br.readLine() ) != null ) {
                                    res.append( line );
                                }
                            }
			} catch ( FileNotFoundException e ) {
				System.out.println( "Error: file " + path + " not found" );
			} catch ( IOException e ) {
			}
		}

		return res.toString();
	}

	// Add back slash before metachars of regex
	private String addBackSlash ( String s ) {
		String metachars = "()[]";
		StringBuilder buf = new StringBuilder();

		for ( int i = 0; i < s.length(); i++ ) {
			String charRead = ( ( Character ) s.charAt( i ) ).toString();

			if ( metachars.contains( charRead ) ) {
				buf.append("\\").append(charRead);
			} else {
				buf.append( charRead );
			}
		}

		return buf.toString();
	}

	// Gives a feeling to a tweet using dictonaries
	private int msgDictionaryPolarize ( Tweets msg, List< String > positiveWords, List< String > negativeWords) {
		int cpt = 0;

		for ( String positiveWord : positiveWords ) {
			if ( msg.getTweet().matches( ".*\\b" + this.addBackSlash( positiveWord ) + "\\b.*" ) ) {
				cpt++;
			}
		}

		for ( String negativeWord : negativeWords ) {
			if ( msg.getTweet().matches( ".*\\b" + this.addBackSlash( negativeWord ) + "\\b.*" ) ) {
				cpt--;
			}
		}
        int polarity = 0;
		if ( cpt < 0 ) {
                        msg.setPolarity(0); 
			//return "NEGATIVE";
		} else if ( cpt > 0 ) {
                        msg.setPolarity(4); polarity = 4;
			//return "POSITIVE";
		} else {
                        msg.setPolarity(2); polarity = 2;
			//return "NEUTRAL"; 
		}
		return polarity;
	}

	// Removes empty strings from a list of string
	private List< String > removeEmptyString ( List< String > ls ) {
		List< String > res = new ArrayList<  >();

		for ( String s : ls ) {
			String st = s.trim();

			if ( !st.isEmpty() ) {
				res.add( st );
			}
		}

		return res;
	}

	
	public int classify ( Tweets msg ) {
		// Lists with clean string
		List< String > positiveWords;
            positiveWords = this.removeEmptyString( Arrays.asList( this.fileToString( this.positivePath )
                    .split( "," ) ) );
		List< String > negativeWords;
            negativeWords = this.removeEmptyString( Arrays.asList( this.fileToString( this.negativePath )
                    .split( "," ) ) );

		return msgDictionaryPolarize( msg, positiveWords, negativeWords );
	}
	
        @Override
	public String toString () {
		return "KeyWords classification";
	}
        
        
}