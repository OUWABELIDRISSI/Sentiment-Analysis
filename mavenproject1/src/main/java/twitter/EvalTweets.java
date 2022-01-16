/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package twitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


/**
 *
 * @author omar
 */
public class EvalTweets {
    /*
    Polatity details :
    0 : negatif 
    2 : neutre
    4 : positif
    */
    
    /*public static void CountWord(Tweets t, String filePathNeg, String filePathPos) throws IOException{
        String[] array = t.getTweet().split(" ");
        System.out.println(Arrays.toString(array));
        int neg = 0;
        int neutral = 0;
        int pos = 0;
        for (String s : array) {
            if (compareCSVFile(filePathNeg, s)) {
                neg++;
            } else if (compareCSVFile(filePathPos, s)) {
                System.out.println();
                pos++;
                
            } else
                neutral++;
        
        }
        System.out.println("negative: " + neg +"positif: " + pos + "neutre : " + neutral);
        //System.out.println("Avant changement : " +t.getPolarity() + " positif: " + pos + " negatif :" +neg + " neutre"  +neutral); // verif
        int n = eval(pos, neg ,neutral);
        if (n == neg) t.setPolarity(0);
        else if (n == neutral) t.setPolarity(2);
        else if (n == pos) t.setPolarity(4);
        System.out.println(t.getTweet() + " Apres changement :" + t.getPolarity()); // verif
    }*/
        public static int eval(int pos, int neg , int neutre){
            if (pos > neg)
                return pos;
            else if (neg>pos)
                return neg;
            else
                return neutre;
                       
        }
        public static void main(String[] args) throws Exception{
            SearchForTweets s = new SearchForTweets();
            ArrayList<Tweets> res = new ArrayList<>();
            s.getTweet("mechant",10);
            res = s.getResultsOfTweets();
            System.out.println(res.get(0));
            //CountWord(res.get(0),"databases/negative.txt","databases/positive.txt");
        }
    
}
