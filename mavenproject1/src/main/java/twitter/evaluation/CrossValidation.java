package twitter.evaluation;

import java.util.ArrayList;
import java.util.List;

import twitter.Tweets;
import twitter.classifier.Classifier;

public class CrossValidation {
    protected Classifier c ;
	
	public CrossValidation(Classifier c ) {
		this.c = c;
	}
	 
	/*
	public double get_Taux_Erreur() {
		List<List<Tweets>> E = this.getTraining_Testing_Sets(k);
		double taux_E = 0.0;
		double E_i = 0;
		double mis_class = 0.0;
		for (int i = 0 ; i<E.size();i++) {
			List<Tweets> test_set = E.get(i);
			List<List<Tweets>> train_temp_set = new ArrayList<>();
			train_temp_set.addAll(E);
			train_temp_set.remove(i);
			List<Tweets> train_set = this.merge(train_temp_set);
			this.c.set_db(train_set);
			for (Tweets t : test_set) {
				if (t.getPolarity() != c.classify(t)) {
					mis_class ++; 
				}
			}
			//System.out.println("mis_class: " + mis_class + " test_size : " + test_set.size());
			E_i +=   (mis_class /  test_set.size());
			mis_class = 0.0;
			//System.out.println("E_i : " + E_i);
		}
		taux_E = (double) E_i/k;
		return taux_E;
	}
	*/
	public List<Tweets> merge(List<List<Tweets>> t){
		List<Tweets> merged_t = new ArrayList<>();
		for (List<Tweets> l_tweet : t) {
			for (Tweets tweet : l_tweet) {
				merged_t.add(tweet);
			}
		}
		return merged_t;
	}
	public List<List<Tweets>>  getTraining_Testing_Sets(int k) {
		List<Tweets> negative_tweets = new ArrayList<>(c.getTweetsOfClass(0));
		List<Tweets> neutral_tweets = new ArrayList<>(c.getTweetsOfClass(2));
		List<Tweets> positive_tweets = new ArrayList<>(c.getTweetsOfClass(4));
		//System.out.println("Neg : " + negative_tweets.size() + " Neutral : " + neutral_tweets.size() + " Positive : " + positive_tweets.size());
		List<List<Tweets>> A = new ArrayList<>();
		for (int i=0;i<k;i++) {
			A.add(new ArrayList<>());
		}
		int i = 0;
		int j = 0;
		while (i < negative_tweets.size()) {
			A.get(j).add(negative_tweets.get(i));
			j = (j + 1)%k;
			i++;
		}
		i=0 ;
		j=0 ;
		while (i < positive_tweets.size()) {
			A.get(j).add(positive_tweets.get(i));
			j = (j + 1)%k;
			i++;
		}
		i=0;
		j=0;
		while (i < neutral_tweets.size()) {
			A.get(j).add(neutral_tweets.get(i));
			j = (j + 1)%k;
			i++;
		}

     return A;
	}

}