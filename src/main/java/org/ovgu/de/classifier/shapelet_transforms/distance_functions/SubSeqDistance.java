/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ovgu.de.classifier.shapelet_transforms.distance_functions;

import java.io.Serializable;

import org.ovgu.de.classifier.shapelet_transforms.Shapelet;

import weka.core.Instances;

/**
 *
 * @author Aaron
 */
public class SubSeqDistance implements Serializable{
    
    public enum DistanceType{NORMAL, ONLINE, IMP_ONLINE, CACHED, ONLINE_CACHED};
    
    public static final double ROUNDING_ERROR_CORRECTION = 0.000000000000001;
    
    protected Shapelet shapelet;
    protected double[] candidate;
    protected int      seriesId;
    protected int      startPos;
    
    protected long count;
    
    public void init(Instances data)
    {
        count =0;
    }
    
    protected void incrementCount(){ count++;}
    
    public long getCount() {return count;}
    
    public void setShapelet(Shapelet shp) {
        shapelet = shp;
        candidate = shp.content;
        seriesId = shp.seriesId;
        startPos = shp.startPos;
    }
    
    public void setCandidate(double [] cnd, int strtPos) {
        candidate = cnd;
        startPos = strtPos;
    }
    
    public void setSeries(int srsId) {
        seriesId = srsId;
    }
    
           
    //we take in a start pos, but we also start from 0.
    public double calculate(double[] timeSeries, int timeSeriesId) 
    {
        double bestSum = Double.MAX_VALUE;
        double sum;
        double[] subseq;
        double temp;

        
        //System.out.println(startPos);
        
        for (int i = 0; i < timeSeries.length - candidate.length; i++)
        {
            sum = 0;
            // get subsequence of two that is the same lengh as one
            subseq = new double[candidate.length];
            System.arraycopy(timeSeries, i, subseq, 0, candidate.length);

            //System.out.println(i);
            subseq = zNormalise(subseq, false); // Z-NORM HERE
            //Euclidean distance calculation, first subtract then square on itself.
            for (int j = 0; j < candidate.length; j++)
            {
                //count ops
                incrementCount();
                temp = (candidate[j] - subseq[j]);
                sum = sum + (temp * temp);
            }
            
            if (sum < bestSum)
            {
                bestSum = sum;
            }
        }

        double dist = (bestSum == 0.0) ? 0.0 : (1.0 / candidate.length * bestSum);
        //System.out.println(dist);
        return dist;
    }

     /**
     * Z-Normalise a time series
     *
     * @param input the input time series to be z-normalised
     * @param classValOn specify whether the time series includes a class value
     * (e.g. an full instance might, a candidate shapelet wouldn't)
     * @return a z-normalised version of input
     */
    public double[] zNormalise(double[] input, boolean classValOn)
    {
        double mean;
        double stdv;

        int classValPenalty = classValOn ? 1 : 0;
        int inputLength = input.length - classValPenalty;

        double[] output = new double[input.length];
        double seriesTotal = 0;
        for (int i = 0; i < inputLength; i++)
        {
            seriesTotal += input[i];
        }

        mean = seriesTotal / (double) inputLength;
        stdv = 0;
        double temp;
        for (int i = 0; i < inputLength; i++)
        {
            temp = (input[i] - mean);
            stdv += temp * temp;
        }

        stdv /= (double) inputLength;

        // if the variance is less than the error correction, just set it to 0, else calc stdv.
        stdv = (stdv < ROUNDING_ERROR_CORRECTION) ? 0.0 : Math.sqrt(stdv);
        
        //System.out.println("mean "+ mean);
        //System.out.println("stdv "+stdv);
        
        for (int i = 0; i < inputLength; i++)
        {
            //if the stdv is 0 then set to 0, else normalise.
            output[i] = (stdv == 0.0) ? 0.0 : ((input[i] - mean) / stdv);
        }

        if (classValOn)
        {
            output[output.length - 1] = input[input.length - 1];
        }

        return output;
    }
}
