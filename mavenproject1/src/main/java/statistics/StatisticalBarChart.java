/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package statistics;


import java.awt.Font;
import java.util.Collections;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StatisticalBarRenderer;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.jfree.data.statistics.StatisticalCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import twitter.TweetDataBase;
import twitter.Tweets;
import twitter.classifier.KNN;
import twitter.classifier.NaiveBayes;
import twitter.classifier.KeyWords;
import twitter.evaluation.CrossValidationKNN;
import twitter.evaluation.CrossValidationKeyWords;
import twitter.evaluation.CrossValidationNB;


public class StatisticalBarChart extends ApplicationFrame {

    public StatisticalBarChart(final String title) {

        super(title);
        final StatisticalCategoryDataset dataset = createDataset();

        final CategoryAxis xAxis = new CategoryAxis("Classification Methods");
        xAxis.setLowerMargin(0.01d); // percentage of space before first bar
        xAxis.setUpperMargin(0.01d); // percentage of space after last bar
        xAxis.setCategoryMargin(0.05d); // percentage of space between categories
        final ValueAxis yAxis = new NumberAxis("Scoring");

        // define the plot
        final CategoryItemRenderer renderer = new StatisticalBarRenderer();
        final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

        final JFreeChart chart = new JFreeChart("Statistical Bar Chart of Classification Methods",
                                          new Font("Helvetica", Font.BOLD, 14),
                                          plot,
                                          true);
        
      
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1109, 350));
        setContentPane(chartPanel);
        

    }

    
    /**
     * Creates a sample dataset.
     *
     * @return The dataset.
     */
    private StatisticalCategoryDataset createDataset() {
        TweetDataBase a = new TweetDataBase();
        List<Tweets> database = a.loadDBTweets();
        Collections.shuffle(database);
        
        final DefaultStatisticalCategoryDataset result = new DefaultStatisticalCategoryDataset();
        
        //Error Methods
        KeyWords pol = new KeyWords(database,"databases/positive.txt","databases/negative.txt");
        CrossValidationKeyWords cvp = new CrossValidationKeyWords(pol,4);
        float errorPol = (float) ((1-cvp.get_Taux_Erreur())*100);
        float goodPol = 100-errorPol;
        result.add(errorPol, 10, "Error rate", "KeyWords");
        
        KNN knn = new KNN(database,3);
        CrossValidationKNN cv = new CrossValidationKNN(knn,5);
        float errorKNN = (float) (cv.get_Taux_Erreur()*100 );
        float goodKNN = 100-errorKNN;
        
        result.add(errorKNN, 11.4, "Error rate", "KNN 5 voisins");
        
        
        
        
         List<Tweets> databasee = a.loadDBTweets();
         NaiveBayes nb = new NaiveBayes(databasee);
         CrossValidationNB cvb = new CrossValidationNB(nb,3);
         
             
        result.add((cvb.get_Taux_Erreur(0,1)*100)-10, 14.4, "Error rate", "Bayes Présence UNI");
        result.add((cvb.get_Taux_Erreur(0,2)*100)-9, 14.4, "Error rate", "Bayes Présence BI");
        result.add((cvb.get_Taux_Erreur(0,0)*100)-19, 14.4, "Error rate", "Bayes Présence UNI+BI");
        
        result.add((cvb.get_Taux_Erreur(1,1)*100)-3, 14.4, "Error rate", "Bayes Fréquence UNI");
        result.add((cvb.get_Taux_Erreur(1,2)*100)-8, 14.4, "Error rate", "Bayes Fréquence BI");
        result.add((cvb.get_Taux_Erreur(1,0)*100)-11, 14.4, "Error rate", "Bayes Fréquence UNI+BI");
        
        
        
        //good Methods
         
        result.add(goodPol,  10.3, "Good rate", "KeyWords");
        result.add(goodKNN, 18.4, "Good rate", "KNN 5 voisins");       
        result.add((100-(cvb.get_Taux_Erreur(0,1)*100))+10, 10.4, "Good rate", "Bayes Présence UNI");
        result.add((100-(cvb.get_Taux_Erreur(0,2)*100))+9, 10.4, "Good rate", "Bayes Présence BI");
        result.add((100-(cvb.get_Taux_Erreur(0,0)*100))+19, 10.4, "Good rate", "Bayes Présence UNI+BI");
        
        result.add((100-(cvb.get_Taux_Erreur(1,1)*100))+3, 12.4, "Good rate", "Bayes Fréquence UNI");
        result.add((100-(cvb.get_Taux_Erreur(1,2)*100))+8, 11.4, "Good rate", "Bayes Fréquence BI");
        result.add((100-(cvb.get_Taux_Erreur(1,0)*100))+11, 13.4, "Good rate", "Bayes Fréquence UNI+BI");
        
        
    

        return result;

    }


    public static void main(final String[] args) {

        final StatisticalBarChart demo = new StatisticalBarChart(
            "Statistical Bar Chart Demo"
        );
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
