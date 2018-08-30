/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ovgu.de.classifier.shapelet_transforms;

/**
 *
 * @author raj09hxu
 */
/*
 * A legacy shapelet class to recreate the results from the following paper:
 * Classification of Time Series by Shapelet Transformation,
 * Hills, J., Lines, J., Baranauskas, E., Mapp, J., and Bagnall, A.
 * Data Mining and Knowledge Discovery (2013)
 * 
 */

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import utilities.class_distributions.ClassDistribution;
import org.ovgu.de.classifier.shapelet_transforms.quality_measures.ShapeletQualityMeasure;

/**
 *
 * @author Aaron Bostrom, modified from Jon Hills
 */
public class Shapelet implements Comparable<Shapelet>, Serializable
{

    public double separationGap;
    /*It is optional whether to store the whole shapelet or not. It is much more 
     memory efficient not to, but it does make life a little easier. 
     */
    public double[] content;
    public int length;
    public int seriesId;
    public int startPos;
    public ShapeletQualityMeasure qualityType;
    public double qualityValue;
    public boolean hasContent = true;
    boolean useSeparationGap = false;
    public double classValue;

    public void setUseSeparationGap(boolean b)
    {
        useSeparationGap = true;
    }

    public double[] getContent()
    {
        return content;
    }

    public double getQualityValue()
    {
        return qualityValue;
    }

    public int getSeriesId()
    {
        return seriesId;
    }

    public int getStartPos()
    {
        return startPos;
    }

    public void setSeriesID(int a)
    {
        seriesId = a;
    }

    public Shapelet(double[] content)
    {
        this.content = content;
        length = content.length;
    }

    public Shapelet(int seriesId, int startPos, ShapeletQualityMeasure qualityChoice)
    {
        this.seriesId = seriesId;
        this.startPos = startPos;
        this.qualityType = qualityChoice;
        this.content = null;
        length = 0;
        this.hasContent = false;
    }

    public Shapelet(double[] content, double qualValue, int seriesId, int startPos)
    {
        this.content = content;
        length = content.length;
        this.seriesId = seriesId;
        this.startPos = startPos;
        this.qualityValue = qualValue;
    }

    public Shapelet(double[] content, double qualValue, int seriesId, int startPos, double sepGap)
    {
        this.content = content;
        length = content.length;
        this.seriesId = seriesId;
        this.startPos = startPos;
        this.qualityValue = qualValue;
        this.separationGap = sepGap;
    }

    public Shapelet(double[] content, int seriesId, int startPos, ShapeletQualityMeasure qualityChoice)
    {
        this.content = content;
        length = content.length;
        this.seriesId = seriesId;
        this.startPos = startPos;
        this.qualityType = qualityChoice;
    }

    public void clearContent()
    {
        this.length = content.length;
        this.content = null;
        this.hasContent = false;
    }

    public void calculateQuality(List<OrderLineObj> orderline, ClassDistribution classDistribution)
    {
        qualityValue = qualityType.calculateQuality(orderline, classDistribution);
        this.qualityValue = this.qualityType.calculateQuality(orderline, classDistribution);
    }
    
    public void calculateSeperationGap(List<OrderLineObj> orderline ){
        this.separationGap = this.qualityType.calculateSeperationGap(orderline);
    }

    @Override
    public int compareTo(Shapelet shapelet) {
        //compare by quality, if there quality is equal we sort by the shorter shapelets.
        int compare1 = Double.compare(qualityValue, shapelet.qualityValue);
        int compare2 = Double.compare(content.length, shapelet.content.length);
        return compare1 != 0 ? compare1 : compare2;
    }

    @Override
    public String toString()
    {
        String str = seriesId + "," + startPos + "," + length +"," + qualityValue;

        return str;
    }
    
    public static class ShortOrder implements Comparator<Shapelet>, Serializable{
        
        @Override
        public int compare(Shapelet s1, Shapelet s2)
        {
            int compare1 = Double.compare(s2.qualityValue, s1.qualityValue); // prefer higher info gain
            int compare2 = (s2.content.length - s1.content.length); //prefer the short ones.
            return compare1 != 0 ? compare1 : compare2;
        }
    }
    

    public static class LongOrder implements Comparator<Shapelet>, Serializable
    {

        @Override
        public int compare(Shapelet s1, Shapelet s2)
        {
            int compare1 = Double.compare(s2.qualityValue, s1.qualityValue); //prefer higher info gain
            int compare2 = (s1.content.length - s2.content.length); //prefer the long ones.
            return compare1 != 0 ? compare1 : compare2;
        }
    }

    public static class ReverseSeparationGap implements Comparator<Shapelet>, Serializable
    {

        @Override
        public int compare(Shapelet s1, Shapelet s2)
        {
            return -(new SeparationGap().compare(s1, s2));
        }
    }

    public static class SeparationGap implements Comparator<Shapelet>, Serializable
    {
        @Override
        public int compare(Shapelet s1, Shapelet s2)
        {
            int compare1 = Double.compare(s1.qualityValue, s2.qualityValue);
            if(compare1 != 0) return compare1;
            
            int compare2 = Double.compare(s1.separationGap, s2.separationGap);
            if(compare2 != 0) return compare2;
            
            int compare3 = Double.compare(s1.length, s2.length);
            return compare3;
        }

    }

}
