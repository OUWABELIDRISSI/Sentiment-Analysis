package twitter.classifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import twitter.SearchForTweets;
import twitter.TweetDataBase;
import twitter.Tweets;
import twitter.evaluation.CrossValidationNB;

public class NaiveBayes extends Classifier{
  
	 //private List<Tweets> tweets = new ArrayList<>();
     private int N = 0;
	 
	 public NaiveBayes(List<Tweets> tweets) {
		 super(tweets);
		 for(Tweets t : super.tweets) {
			 this.N += countWords(t);
		 }
		 
	 }
	 
	 //public List<Tweets> get_Tweets(){
		// return super.tweets;
	 //}
	 
	 /*
	 public List<Tweets> getTweetsOfClass(int c){
		 List<Tweets> tweets_of_c = new ArrayList<>();
		 for (Tweets t : super.tweets) {
			 if (t.getPolarity() == c)
				 tweets_of_c.add(t);
		 }
		 return tweets_of_c;
	 }
	 */
	 
	 public int countWords(Tweets t) {
		 String tweet = t.getTweet();
		 return tweet.split(" ").length;
	 }
	 
	 public int countBigrammes(Tweets t) {
		 return get_bigrammes(t).size();
	 }
	 
	 public int countOccu(Tweets t , String m) {
		 int nb_occu_m = 0;
		 String[] l_tweet = t.getTweet().split(" ");
		 for (String word : l_tweet) {
			 if (word.equals(m))
				 nb_occu_m += 1;
		 }
		 return nb_occu_m;
	 }
	 
	 public int countOccu_bigrammes(Tweets t , String m) {
		 int nb_occu_m = 0;
		 List<String> l_tweet = get_bigrammes(t);
		 for (String word : l_tweet) {
			 if (word.equals(m))
				 nb_occu_m += 1;
		 }
		 return nb_occu_m;
	 }
	 
	 public int get_nc(int c) {
		 
		 int nb_total = 0;
		 List<Tweets> tweets_of_c = super.getTweetsOfClass(c);
		 for (Tweets t: tweets_of_c) {
			 nb_total += this.countWords(t);
		 }
		 return nb_total;
	 }
	 public int get_nc_bigrammes(int c) {
		 
		 int nb_total = 0;
		 List<Tweets> tweets_of_c = super.getTweetsOfClass(c);
		 for (Tweets t: tweets_of_c) {
			 nb_total += countBigrammes(t);//this.countWords(t);
		 }
		 return nb_total;
	 }

	 public int get_n_mc(String m , int c) {
		 int nb_occu = 0;
		 List<Tweets> tweets_of_c = super.getTweetsOfClass(c);
		 for (Tweets t: tweets_of_c) {
			 nb_occu += this.countOccu(t,m);
		 }
		 return nb_occu;
	 }
	 
	 public int get_n_mc_bigrammes(String m , int c) {
		 int nb_occu = 0;
		 List<Tweets> tweets_of_c = super.getTweetsOfClass(c);
		 for (Tweets t: tweets_of_c) {
			 nb_occu += this.countOccu_bigrammes(t,m);
		 }
		 return nb_occu;
	 }
	 
	 public float get_Pmc_bigrammes(String m , int c) {
		 return ((float) (this.get_n_mc_bigrammes(m, c) + 1))/(this.get_nc_bigrammes(c) + N);
	 }
	 
	 public float get_Pmc(String m , int c) {
		 return ((float) (this.get_n_mc(m, c) + 1))/(this.get_nc(c) + N);
	 }
	 
	 public float get_P(int c) {
		 int nb_occu_c = super.getTweetsOfClass(c).size();
	     return (float) nb_occu_c/N;
	 }
	 
	 public double get_Pct_bigrammes(Tweets t, int c) {
		 List<String> words_of_t =  get_bigrammes(t);
		 double Pct = (double) 1.1;
		 for(String word : words_of_t) {
			 Pct *= this.get_Pmc_bigrammes(word, c)*this.get_P(c);
		 }
		 return Pct;
	 }
	 
	 public double get_Pct_uni_bi(Tweets t, int c) {
		 return get_Pct_bigrammes(t,c) * get_Pct(t,c);
	 }
	 public double get_Pct(Tweets t, int c) {
		 String[] words_of_t =  t.getTweet().split(" ");
		 double Pct = (double) 1.1;
		 for(String word : words_of_t) {
			 if (word.length() >= 3)
			    Pct *= this.get_Pmc(word, c)*this.get_P(c);
		 }
		 return Pct;
	 }
	 
	 public double get_Pct_freq(Tweets t, int c) {
		 String[] words_of_t =  t.getTweet().split(" ");
		 double Pct = (double) 1.1;
		 for(String word : words_of_t) {
			 if (word.length() >= 3) {
				 int n_m = this.countOccu(t, word);
				 Pct *= (Math.pow(this.get_Pmc(word, c),n_m))*this.get_P(c);
			 }
		 }
		 return Pct;
	 }
     
	 public double get_Pct_freq_bigrammes(Tweets t, int c) {
		 List<String> words_of_t =  get_bigrammes(t);
		 double Pct = (double) 1.1;
		 for(String word : words_of_t) {
			 int n_m = this.countOccu_bigrammes(t, word);
			 Pct *= (Math.pow(this.get_Pmc_bigrammes(word, c),n_m))*this.get_P(c);
		 }
		 return Pct;
	 }
	 public double get_Pct_freq_uni_bi(Tweets t, int c) {
        return get_Pct_freq_bigrammes(t, c) * get_Pct_freq(t,c);
	 }
	 
	 public static List<String> get_bigrammes(Tweets t) {
		 List<String> bigrammes = new ArrayList<>();
		 String[] words_of_t =  t.getTweet().split(" ");
		 for (int i = 0; i < words_of_t.length-1;i++) {
			 String word = words_of_t[i] + " " + words_of_t[i+1];
			 bigrammes.add(word);
		 }
		 return bigrammes;
	 }
	 
	 public int classify(Tweets t,int pOrf,int uORb) {
		 
		 // uORb == 1 si on classifie selon les unigrammes , == 2 sinon on classifie selon les bigrammes ,
		 // == 0 si on classifie selon les deux
		 // frequnce 1 presence 0
		 int[] polarities = {0,2,4};
		 List<Double> probabilities = new ArrayList<>();
		 for (int i = 0;i<polarities.length;i++) {
			 if (pOrf == 1) { // Fréquence 
				 if (uORb == 1)
					 probabilities.add(this.get_Pct_freq(t,polarities[i]));
				 else if (uORb == 2) 
					 probabilities.add(this.get_Pct_freq_bigrammes(t,polarities[i]));
				 else
					 probabilities.add(this.get_Pct_freq_uni_bi(t,polarities[i])); 
			 } else { // Présence
				 if (uORb == 1)
					 probabilities.add(this.get_Pct(t,polarities[i]));
				 else if (uORb == 2) 
					 probabilities.add(this.get_Pct_bigrammes(t,polarities[i]));
				 else
					 probabilities.add(this.get_Pct_uni_bi(t,polarities[i]));
			 }
		 }
		 double max_probability = probabilities.get(0);
		 for (Double p : probabilities) {
			 if (p > max_probability)
				 max_probability = p;
		 }
		 int polarity = polarities[probabilities.indexOf(max_probability)];
		 //t.setPolarity(polarity);
		 return polarity;
	}
	
	

    public static void main(String[] args) throws Exception  {
         SearchForTweets s = new SearchForTweets();
         List<Tweets> res = new ArrayList<>();
         String theme = "covid";
         s.getTweet(theme,20);
         res = s.getResultsOfTweets();
         remove_less_3_letters(res);
         for (Tweets t: res) {
        	 t.cleanData();
         }
         TweetDataBase a = new TweetDataBase();
         List<Tweets> database = a.loadDBTweets();
         NaiveBayes nb = new NaiveBayes(database);
         for(int i=0;i<res.size();i++){
            	int pres_pol = nb.classify(res.get(i),0,1);
            	System.out.println("Uni: " + res.get(i).getTweet() + pres_pol);
            	int pres_pol_bi = nb.classify(res.get(i),0,2);
            	System.out.println("Bi: " + res.get(i).getTweet() + pres_pol_bi);
            	int pres_pol_uni_bi = nb.classify(res.get(i),0,0);
            	System.out.println("Uni & Bi: " + res.get(i).getTweet() + pres_pol_uni_bi);
            
        		int freq_pol = nb.classify(res.get(i),1,1);
        		System.out.println("Uni: " + res.get(i).getTweet() + freq_pol);
        		int freq_pol_bi = nb.classify(res.get(i),1,2);
        		System.out.println("Bi: " + res.get(i).getTweet() + freq_pol_bi);
        		int freq_pol_uni_bi = nb.classify(res.get(i),1,0);
        		System.out.println("Uni & Bi: " + res.get(i).getTweet() + freq_pol_uni_bi);
         }


     }
}