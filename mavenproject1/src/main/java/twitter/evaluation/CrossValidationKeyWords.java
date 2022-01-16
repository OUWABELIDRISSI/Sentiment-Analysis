package twitter.evaluation;

import java.util.ArrayList;
import java.util.List;

import twitter.TweetDataBase;
import twitter.Tweets;
import twitter.classifier.KeyWords;

public class CrossValidationKeyWords extends CrossValidation{
   
	int k ;
	KeyWords p ;
	public CrossValidationKeyWords(KeyWords p, int k) {
		super(p);
		this.k = k ;
		this.p = p;
	}
	public double get_Taux_Erreur(){
		    List<Tweets> original_db = new ArrayList<>();
		    for (Tweets t :this.p.get_Tweets()) original_db.add(t);
	    	List<List<Tweets>> E = super.getTraining_Testing_Sets(k);
			double taux_E = 0.0;
			double E_i = 0;
			double mis_class = 0.0;
			for (int i = 0 ; i<E.size();i++) {
				List<Tweets> test_set = E.get(i);
				List<List<Tweets>> train_temp_set = new ArrayList<>();
				train_temp_set.addAll(E);
				train_temp_set.remove(i);
				List<Tweets> train_set = super.merge(train_temp_set);
				this.p.set_db(train_set);
				for (Tweets t : test_set) {
					if (t.getPolarity() != this.p.classify(t)) {
						mis_class ++; 
					}
				}
				//System.out.println("mis_class: " + mis_class + " test_size : " + test_set.size());
				E_i +=   (mis_class /  test_set.size());
				mis_class = 0.0;
				//System.out.println("E_i : " + E_i);
			}
			taux_E = (double) E_i/k;
			this.p.set_db(original_db);
			return taux_E;
	    }
	 
	    public static void main(String[] args)   {
	        TweetDataBase a = new TweetDataBase();
	        List<Tweets> database = a.loadDBTweets();
	        KeyWords pol = new KeyWords(database,"databases/positive.txt","databases/negative.txt");
	        CrossValidationKeyWords cv = new CrossValidationKeyWords(pol,4);
	        System.out.println("Taux_erreur: " + cv.get_Taux_Erreur());
	    }
}