package twitter.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import twitter.TweetDataBase;
import twitter.Tweets;
import twitter.classifier.KNN;

public class CrossValidationKNN extends CrossValidation {
   
	
	KNN c ; 
	int k ; 
    public CrossValidationKNN(KNN c , int k) {
    	super(c);
    	this.c = c;
    	this.k = k ;
    }
    
    public double get_Taux_Erreur(){
		List<Tweets> original_db = new ArrayList<>();
		for (Tweets t :this.c.get_Tweets()) original_db.add(t);
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
			this.c.set_db(train_set);
			for (Tweets t : test_set) {
				if (t.getPolarity() != this.c.classify(t)) {
					mis_class ++; 
				}
			}
			//System.out.println("mis_class: " + mis_class + " test_size : " + test_set.size());
			E_i +=   (mis_class /  test_set.size());
			mis_class = 0.0;
			//System.out.println("E_i : " + E_i);
		}
		taux_E = (double) E_i/k;
		this.c.set_db(original_db);
		return taux_E;
    }
    
    public static void main(String[] args)   {
        TweetDataBase a = new TweetDataBase();
        List<Tweets> database = a.loadDBTweets();
        Collections.shuffle(database);
        KNN knn = new KNN(database,3);
        CrossValidationKNN cv = new CrossValidationKNN(knn,5);
        System.out.println("Taux_erreur: " + (1-cv.get_Taux_Erreur())*100);
    }
}