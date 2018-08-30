/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ovgu.de.classifier.shapelet_transforms.search_functions;

import java.util.ArrayList;

import org.ovgu.de.classifier.shapelet_transforms.Shapelet;

import weka.core.Instance;
import weka.core.Instances;
/**
 *
 * @author Aaron
 */
public class SkippingSearch extends ShapeletSearch{
    
    int[] positions;
    int[] lengths;
    
    protected SkippingSearch(ShapeletSearchOptions sops){
        super(sops);
        
        //TODO: if we're given a fixed amount of shapelets could calculate the skipping parameters.
    }
    
    @Override
    public void init(Instances input){
        super.init(input);
        
        //create array of classValues.
        positions = new int[input.numClasses()];
        lengths = new int[input.numClasses()];
    }
    
    
    @Override
    public ArrayList<Shapelet> SearchForShapeletsInSeries(Instance timeSeries, ProcessCandidate checkCandidate){
        //we want to store a startLength and startPos for each class and cycle them when we're skipping.
        int index = (int)timeSeries.classValue();
        
        //if it's been reset back to 0 we need to addon the minShapeletLength;
        if(lengths[index] == 0)
            lengths[index] += minShapeletLength;
        
        int start = positions[index];
        int length = lengths[index];
        
        ArrayList<Shapelet> seriesShapelets = new ArrayList<>();

        double[] series = timeSeries.toDoubleArray();

        
        for (; length <= maxShapeletLength; length+=lengthIncrement) {
            //for all possible starting positions of that length. -1 to remove classValue
            for (; start <= timeSeries.numAttributes() - length - 1; start+=positionIncrement) {
                Shapelet shapelet = checkCandidate.process(series, start, length);
                if (shapelet != null) {
                    seriesShapelets.add(shapelet);
                }
            }
        }
        
        
        //IE if we're skipping 2positions. we want to cycle between starting a series at 0,1
        positions[index] = ++positions[index] % positionIncrement;
        lengths[index] = ++lengths[index] % lengthIncrement;
        return seriesShapelets;
    }
    
}
