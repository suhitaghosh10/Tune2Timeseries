/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ovgu.de.classifier.shapelet_transforms.search_functions;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.ovgu.de.classifier.shapelet_transforms.Shapelet;

import weka.core.Instance;
import weka.core.Instances;
/**
 *
 * @author raj09hxu
 */
public class ShapeletSearch implements Serializable{
    
    public enum SearchType {FULL, FS, GENETIC, RANDOM, LOCAL, MAGNIFY, TIMED_RANDOM, SKIPPING, TABU, REFINED_RANDOM, IMP_RANDOM, SUBSAMPLE_RANDOM, SKEWED};
    
    public interface ProcessCandidate{
        public Shapelet process(double[] candidate, int start, int length);
    }
    
    ArrayList<String> shapeletsVisited = new ArrayList<>();
    int seriesCount;
    
    public ArrayList<String> getShapeletsVisited() {
        return shapeletsVisited;
    }
    
    protected Comparator<Shapelet> comparator;
    
    public void setComparator(Comparator<Shapelet> comp){
        comparator = comp;
    }
    
    
    protected int minShapeletLength;
    protected int maxShapeletLength;
    
    protected int lengthIncrement = 1;
    protected int positionIncrement = 1;
    
    protected Instances inputData;
    
    protected ShapeletSearchOptions options;
    
    protected ShapeletSearch(ShapeletSearchOptions ops){
        options = ops;
        
        minShapeletLength = ops.getMin();
        maxShapeletLength = ops.getMax();
        lengthIncrement = ops.getLengthInc();
        positionIncrement = ops.getPosInc();
    }
    
    public void setMinAndMax(int min, int max){
        minShapeletLength = min;
        maxShapeletLength = max;
    }
    
    public int getMin(){
        return minShapeletLength;
    }
    
    public int getMax(){
        return maxShapeletLength;
    }
    
    public void init(Instances input){
        inputData = input;
    }
    
    
    //given a series and a function to find a shapelet 
    /**
     * @param timeSeries
     * @param checkCandidate
     * @return
     */
    public ArrayList<Shapelet> SearchForShapeletsInSeries(Instance timeSeries, ProcessCandidate checkCandidate){
        ArrayList<Shapelet> seriesShapelets = new ArrayList<>();
        
        double[]  series = timeSeries.toDoubleArray(); // check this double array so that it has no trailing NAN's
        int j=0;
        //Code to remove "?" from the series.
        for( int i=0; i<series.length; i++ ){
            if (!Double.isNaN(series[i]))
            	series[j++] = series[i];
        }
        double[] questionMarkRemovedSeries = new double[j];
        System.arraycopy( series, 0, questionMarkRemovedSeries, 0, j );
        for (int length = minShapeletLength; length <= maxShapeletLength; length+=lengthIncrement) {	
            for (int start = 0; start <= questionMarkRemovedSeries.length - length - 1; start+=positionIncrement) {
            	Shapelet shapelet = checkCandidate.process(questionMarkRemovedSeries, start, length);
                	if (shapelet != null) {
                		seriesShapelets.add(shapelet);
                		shapeletsVisited.add(seriesCount+","+length+","+start+","+shapelet.qualityValue);
                	}
            }
        }
        
        seriesCount++;
        return seriesShapelets;
    }
}
