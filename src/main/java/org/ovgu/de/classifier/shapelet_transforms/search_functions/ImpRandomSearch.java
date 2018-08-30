/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ovgu.de.classifier.shapelet_transforms.search_functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.ovgu.de.classifier.shapelet_transforms.Shapelet;

import org.ovgu.de.classifier.utility.generic_storage.Pair;
//import org.ovgu.de.classifier.utility.generic_storage.Triple;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author raj09hxu
 */
public class ImpRandomSearch extends RandomSearch{
    
    protected Map<Integer, ArrayList<Pair<Integer,Integer>>> shapeletsToFind = new HashMap<>();
    
    int currentSeries =0;
    public  Map<Integer, ArrayList<Pair<Integer,Integer>>> getShapeletsToFind(){
        return shapeletsToFind;
    }
        
    protected ImpRandomSearch(ShapeletSearchOptions ops) {
        super(ops);
    }

    
    @Override
    public void init(Instances input){
        inputData = input;
        int numLengths = maxShapeletLength - minShapeletLength; //want max value to be inclusive.
        
        
        //generate the random shapelets we're going to visit.
        for(int i=0; i<numShapelets; i++){
            //randomly generate values.
            int series = random.nextInt(input.numInstances());
            int length = random.nextInt(numLengths) + minShapeletLength; //offset the index by the min value.
            int position  = random.nextInt(input.numAttributes() - length); // can only have valid start positions based on the length. (numAtts-1)-l+1
            
            //find the shapelets for that series.
            ArrayList<Pair<Integer,Integer>> shapeletList = shapeletsToFind.get(series);
            if(shapeletList == null)
                shapeletList = new ArrayList<>();
            
            //add the random shapelet to the length
            shapeletList.add(new Pair(length, position));
            //put back the updated version.
            
            shapeletsToFind.put(series, shapeletList);
        }          
    }
    
    
    @Override
    public ArrayList<Shapelet> SearchForShapeletsInSeries(Instance timeSeries, ProcessCandidate checkCandidate){
        
        ArrayList<Shapelet> seriesShapelets = new ArrayList<>();
        
        double[] series = timeSeries.toDoubleArray();
        
        ArrayList<Pair<Integer,Integer>> shapeletList = shapeletsToFind.get(currentSeries);
        currentSeries++;
        
        //no shapelets to consider.
        if(shapeletList == null){
            return seriesShapelets;
        }
        
        //Only consider a fixed amount of shapelets.
        for(Pair<Integer,Integer> shapelet : shapeletList){
            //position is in var2, and length is in var1
            Shapelet shape = checkCandidate.process(series, shapelet.var1, shapelet.var2);
            if(shape != null)
                seriesShapelets.add(shape);           
        }

        return seriesShapelets;
    }
    
}
