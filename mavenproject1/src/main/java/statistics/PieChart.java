/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package statistics;

import java.awt.Color;
import java.awt.EventQueue;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import twitter.Tweets;
import org.jfree.data.general.DefaultPieDataset;
import twitter.TweetDataBase;


/**
 *
 * @author omar
 */
public class PieChart {
    
    public void createGraphics(ArrayList<Tweets> tweets) {
		Double positive = new Double(0), neutral = new Double(0), negative = new Double(0), nonAnnotated = new Double(0);
		for(Tweets tweet : tweets){
			switch(tweet.getPolarity()){
				case 0:
					negative++;
					break;
				case 2:
					neutral++;
					break;
				case 4:
					positive++;
					break;
                                case -1:
                                        nonAnnotated++;
                                        break;
				default:
					
                                       break;
			}
		}
                System.out.println(nonAnnotated);
		negative/=tweets.size();
		neutral/=tweets.size();
		positive/=tweets.size();
		nonAnnotated/=tweets.size();

		Double finalPositive = positive;
		Double finalNeutral = neutral;
		Double finalNegative = negative;
		Double finalNonAnnotated = nonAnnotated;
		EventQueue.invokeLater(() -> {
                    JFrame frame = new JFrame("pie chart");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    try
                    {
                        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.nimbuslookandfeel");
                    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
                    }
                    
                    DefaultPieDataset dataset = new DefaultPieDataset();
                    dataset.setValue("Pos", finalPositive);
                    dataset.setValue("Neutre", finalNeutral);
                    dataset.setValue("Negative", finalNegative);
                    dataset.setValue("Non annoté", finalNonAnnotated);
                    
                    JFreeChart chart = ChartFactory.createPieChart3D(
                            "statistiques des Tweets" ,  // chart title
                            dataset ,         // data
                            true ,            // include legend
                            false,
                            false);
                    
                    final PiePlot3D plot = (PiePlot3D) chart.getPlot();
                    chart.setBackgroundPaint(Color.WHITE);
                    
                    plot.setSectionPaint("Pos", Color.GREEN);
                    plot.setSectionPaint("Neutre", Color.BLUE);
                    plot.setSectionPaint("Negative", Color.RED );
                    plot.setSectionPaint("Non annoté",Color.WHITE);
                    
                    plot.setStartAngle( 270 );
                    plot.setForegroundAlpha( 0.60f );
                    plot.setInteriorGap( 0.02 );
                    plot.setLabelBackgroundPaint(Color.WHITE);
                    plot.setBackgroundPaint(Color.WHITE);
                    
                    final ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
                    frame.setContentPane(chartPanel);
                    frame.pack();
                    frame.setLocationByPlatform(true);
                    frame.setVisible(true);
                    frame.setResizable(false);
                });
	}
    public static void main(String[] args) throws Exception{
            TweetDataBase a = new TweetDataBase();
            ArrayList<Tweets> loadDBTweets = a.loadDBTweets();
            System.out.println(loadDBTweets);
            PieChart pie = new PieChart();
            
            pie.createGraphics(loadDBTweets);
        }
}
