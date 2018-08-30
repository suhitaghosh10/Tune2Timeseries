/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ovgu.de.classifier.shapelet_transforms.search_functions;

import java.util.ArrayList;
import org.ovgu.de.classifier.utility.generic_storage.Pair;
import weka.core.Instances;

/**
 *
 * @author Aaron
 */
public class SubsampleRandomSearch extends ImpRandomSearch{

    float shapeletToSeriesRatio;
    
    protected SubsampleRandomSearch(ShapeletSearchOptions ops) {
        super(ops);
        
        shapeletToSeriesRatio = ops.getProportion();
    }
       
    @Override
    public void init(Instances input){
        int numInstances = (int) (input.numInstances() * shapeletToSeriesRatio) ;
        int numAttributes = input.numAttributes() - 1;
        
        System.out.println(input.numInstances());
        System.out.println(numInstances);
        
        inputData = input;
        int numLengths = maxShapeletLength - minShapeletLength; //want max value to be inclusive.
        
        
        //generate the random shapelets we're going to visit.
        for(int i=0; i<numShapelets; i++){
            //randomly generate values.
            int series = random.nextInt(numInstances);
            int length = random.nextInt(numLengths) + minShapeletLength; //offset the index by the min value.
            int position  = random.nextInt(numAttributes - length + 1); // can only have valid start positions based on the length. the upper bound is exclusive. 
            //so for the m-m+1 case it always resolves to 0.
            
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
}
