/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ovgu.de.classifier.shapelet_transforms.distance_functions;

import java.util.Arrays;
import weka.core.Instances;

/**
 *
 * @author raj09hxu
 */
public class MultiVariateSubSeqDistance extends SubSeqDistance {
    private int numDimensions;
    
    public MultiVariateSubSeqDistance(int dimensions){
        numDimensions = dimensions;
    }        
    //we take in a start pos, but we also start from 0.
    @Override
    public double calculate(double[] timeSeries, int timeSeriesId) 
    {
        //we assume we've been skipping every 3.
        double bestSum = Double.MAX_VALUE;
        double sum;
        double[] subseq;
        double temp;

        
        //we want to iterate over the dimensions.
        for (int i = 0; i < timeSeries.length - candidate.length; i+=numDimensions)
        {
            sum = 0;
            // get subsequence of two that is the same lengh as one
            subseq = new double[candidate.length];
            
            //lengths will be multiples of numDimenisons
            System.arraycopy(timeSeries, i, subseq, 0, candidate.length);

            //special normliase function for multidimension.
            subseq = zNormalise(subseq, false); // Z-NORM HERE


            for (int j = 0; j < candidate.length; j++)
            {
                //count ops
                incrementCount();
                temp = (candidate[j] - subseq[j]);
                sum += (temp * temp);
            }
            
            if (sum < bestSum)
            {
                bestSum = sum;
            }
        }

        return (bestSum == 0.0) ? 0.0 : (1.0 / candidate.length * bestSum);
    }
    
    public double[] zNormalise(double[] input, boolean classValOn)
    {
        double[] mean = new double[numDimensions];
        double[] stdv = new double[numDimensions];

        int classValPenalty = classValOn ? 1 : 0;
        int inputLength = input.length - classValPenalty;

        double[] output = new double[input.length];
        //we need series totals for each dimeisons.
        double[] seriesTotals = new double[numDimensions];

        for (int i = 0; i < inputLength; i++)
        {
            //alternate through the totals for ecah dimension.
            seriesTotals[i%numDimensions] += input[i];
        }

        for(int i=0; i< numDimensions; i++){
            mean[i] = seriesTotals[i]/((double)inputLength/(double)numDimensions);
        }

        double temp =0;
        for (int i = 0; i < inputLength; i++){
            temp = (input[i] - mean[i%numDimensions]);
            stdv[i%numDimensions] += temp * temp;
        }
        
        for(int i=0; i< numDimensions; i++){
            stdv[i] /= (double)inputLength/numDimensions;
            // if the variance is less than the error correction, just set it to 0, else calc stdv.
            stdv[i] = (stdv[i] < ROUNDING_ERROR_CORRECTION) ? 0.0 : Math.sqrt(stdv[i]);
        }
 
        for (int i = 0; i < inputLength; i++)
        {
            //if the stdv is 0 then set to 0, else normalise.
            output[i] = (stdv[i%numDimensions] == 0.0) ? 0.0 : ((input[i] - mean[i%numDimensions]) / stdv[i%numDimensions]);
        }

        if (classValOn)
        {
            output[output.length - 1] = input[input.length - 1];
        }

        return output;
    }

    
    public static void main(String[] args){
        
        int shapeletPos = 0;
        int shapeletLength = 6;
        
        Instances xyzInst = org.ovgu.de.classifier.utility.ClassifierTools.loadData("C:\\LocalData\\Dropbox\\TSC Problems\\AALTDChallenge\\XYZBySensorAaron\\AALTD0_XYZ_TRAIN.arff");
        
        MultiVariateSubSeqDistance mvs = new MultiVariateSubSeqDistance(3);
        double[] shapeletCandidate = new double[shapeletLength];
            
        //lengths will be multiples of numDimenisons
        System.arraycopy(xyzInst.get(0).toDoubleArray(), shapeletPos, shapeletCandidate, 0, shapeletLength);
        mvs.setCandidate(shapeletCandidate, shapeletPos);
        double[] series = new double[xyzInst.numAttributes() - 1];
        System.arraycopy(xyzInst.get(0).toDoubleArray(), 0, series, 0, series.length);
        System.out.println("xyz: "+ mvs.calculate(series, 0));
       
        System.out.println();
        Instances xInst =  org.ovgu.de.classifier.utility.ClassifierTools.loadData("C:\\LocalData\\Dropbox\\TSC Problems\\AALTDChallenge\\24SensorsAnnoyingAaronNames\\AALTD0_X_TRAIN.arff");
        Instances yInst =  org.ovgu.de.classifier.utility.ClassifierTools.loadData("C:\\LocalData\\Dropbox\\TSC Problems\\AALTDChallenge\\24SensorsAnnoyingAaronNames\\AALTD0_Y_TRAIN.arff");
        Instances zInst =  org.ovgu.de.classifier.utility.ClassifierTools.loadData("C:\\LocalData\\Dropbox\\TSC Problems\\AALTDChallenge\\24SensorsAnnoyingAaronNames\\AALTD0_Z_TRAIN.arff");
        
        SubSeqDistance ssqX = new SubSeqDistance();      
        double[] shapeletCandidateX = new double[shapeletLength/3];
        //lengths will be multiples of numDimenisons
        System.arraycopy(xInst.get(0).toDoubleArray(), shapeletPos, shapeletCandidateX, 0, shapeletLength/3);
        ssqX.setCandidate(shapeletCandidateX, shapeletPos);
        series = new double[xInst.numAttributes() - 1];
        System.arraycopy(xInst.get(0).toDoubleArray(), 0, series, 0, series.length);
        System.out.println("x: "+ ssqX.calculate(series, 0));
        
        System.out.println();
        
        SubSeqDistance ssqY = new SubSeqDistance();      
        double[] shapeletCandidateY = new double[shapeletLength/3];
        //lengths will be multiples of numDimenisons
        System.arraycopy(yInst.get(0).toDoubleArray(), shapeletPos, shapeletCandidateY, 0, shapeletLength/3);
        ssqY.setCandidate(shapeletCandidateY, shapeletPos);
        series = new double[yInst.numAttributes() - 1];
        System.arraycopy(yInst.get(0).toDoubleArray(), 0, series, 0, series.length);
        System.out.println("y: "+ ssqY.calculate(series, 0));
        
        
        System.out.println();
        
        SubSeqDistance ssqZ = new SubSeqDistance();      
        double[] shapeletCandidateZ = new double[shapeletLength/3];
        //lengths will be multiples of numDimenisons
        System.arraycopy(zInst.get(0).toDoubleArray(), shapeletPos, shapeletCandidateZ, 0, shapeletLength/3);
        ssqZ.setCandidate(shapeletCandidateZ, shapeletPos);
        series = new double[zInst.numAttributes() - 1];
        System.arraycopy(zInst.get(0).toDoubleArray(), 0, series, 0, series.length);
        System.out.println("z: "+ ssqZ.calculate(series, 0));
        
    }
}
