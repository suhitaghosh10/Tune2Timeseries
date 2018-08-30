/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ovgu.de.classifier.shapelet_transforms.quality_measures;

import java.util.List;

import org.ovgu.de.classifier.shapelet_transforms.OrderLineObj;

import utilities.class_distributions.ClassDistribution;

/**
 *
 * @author raj09hxu
 */
    
    public interface ShapeletQualityMeasure 
    {
        public double calculateQuality(List<OrderLineObj> orderline, ClassDistribution classDistribution);

        public double calculateSeperationGap(List<OrderLineObj> orderline);
    }
