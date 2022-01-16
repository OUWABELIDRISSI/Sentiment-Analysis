package twitter.evaluation;

import java.util.ArrayList;
import java.util.List;

import twitter.TweetDataBase;
import twitter.Tweets;
import twitter.classifier.NaiveBayes;

public class CrossValidationNB extends CrossValidation{
    
	NaiveBayes nb ; 
	int k ;
    public CrossValidationNB(NaiveBayes c , int k) {
    	super(c);
    	this.nb = c;
    	this.k = k ;
    }
	
	public double get_Taux_Erreur(int pOrf, int uOrb) {
		List<Tweets> original_db = new ArrayList<>();
		for (Tweets t :this.nb.get_Tweets()) original_db.add(t);
		List<List<Tweets>> E = super.getTraining_Testing_Sets(this.k);
		double taux_E = 0.0;
		double E_i = 0;
		double mis_class = 0.0;
		for (int i = 0 ; i<E.size();i++) {
			List<Tweets> test_set = E.get(i);
			List<List<Tweets>> train_temp_set = new ArrayList<>();
			train_temp_set.addAll(E);
			train_temp_set.remove(i);
			List<Tweets> train_set = super.merge(train_temp_set);
			//System.out.println("Neg Before: " + i + " " + nb.getTweetsOfClass(0).size());
			this.nb.set_db(train_set);
			//System.out.println("Neg After: " + i + " " + nb.getTweetsOfClass(0).size());
			for (Tweets t : test_set) {
				if (t.getPolarity() != this.nb.classify(t, pOrf, uOrb)) {
					mis_class ++; 
				}
			}
			//System.out.println("mis_class: " + mis_class + " test_size : " + test_set.size());
			E_i +=   (mis_class /  test_set.size());
			mis_class = 0.0;
			//System.out.println("E_i : " + E_i);
		}
		//System.out.println("Neg : " + nb.getTweetsOfClass(0).size());
		taux_E = (double) E_i/k;
		this.nb.set_db(original_db);
		//System.out.println("The end  : " + nb.getTweetsOfClass(0).size());
		return 1 - taux_E;
	}
	
	public static void main(String[] args) {
		 
        TweetDataBase a = new TweetDataBase();
         //a.saveTweets(res);
         List<Tweets> database = a.loadDBTweets();
         NaiveBayes nb = new NaiveBayes(database);
         CrossValidationNB cv = new CrossValidationNB(nb,3);
         System.out.println("Présence : ");
         System.out.println("Uni -> Taux_erreur: " + (cv.get_Taux_Erreur(0,1)*100));
         System.out.println("Uni -> Taux_GOOD: " + (100-(cv.get_Taux_Erreur(0,1)*100)));
         System.out.println("Bi -> Taux_erreur: " + cv.get_Taux_Erreur(0,2));
         System.out.println("Uni & Bi -> Taux_erreur: " + cv.get_Taux_Erreur(0,0));
         System.out.println("Fréquence : ");
         System.out.println("Uni -> Taux_erreur: " + cv.get_Taux_Erreur(1,1));
         System.out.println("Bi -> Taux_erreur: " + cv.get_Taux_Erreur(1,2));
         System.out.println("Uni & Bi -> Taux_erreur: " + cv.get_Taux_Erreur(1,0));
         System.out.println(nb.getTweetsOfClass(0).size());
	 }
	 
}