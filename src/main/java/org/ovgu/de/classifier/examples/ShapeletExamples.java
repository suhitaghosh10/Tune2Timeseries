/*
This class is a helper class to describe the structure of our shapelet code and 
* demonstrate how to use it.
 *copyright Anthony Bagnall
 * @author Anthony Bagnall, Jason Lines, Jon Hills and Edgaras Baranauskas
 */
package org.ovgu.de.classifier.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
/* Package   weka.core.shapelet.* contains the classes 
 *          Shapelet that stores the actual shapelet, its location
 * in the data set, the quality assessment and a reference to the quality 
 * measure used
 *          BinaryShapelet that extends Shapelet to store the threshold used to 
 *  measure quality
 *          OrderLineObj: A simple class to store <distance,classValue> pairs 
 * for calculating the quality of a shapelet
 *          QualityMeasures: A class to store shapelet quality measure 
 * implementations. This includes an abstract quality measure class,
 * and implementations of each of the four shapelet quality measures
 *          QualityBound: A class to store shapelet quality measure bounding 
 * implementations. This is used to determine whether an early abandonment is 
 * permissible for the four quality measures.
 */
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ovgu.de.classifier.shapelet_transforms.*;

/* package weka.filters.timeseries.shapelet_transforms.* contains
 *      FullShapeletTransform: Enumerative search to find the best k shapelets.
 *        ShapeletTransformDistCaching: subclass of FullShapeletTransform that 
 * uses the distance caching algorithm described in Mueen11. This is the fastest
 * exact approach, but is memory intensive. 
 *        ShapeletTransform: subclass of FullShapeletTransform that uses  
 distance online normalisation and early abandon described in ??. Not as fast,
 * but does not require the extra memory.
 *      ClusteredShapeletTransform: contains a FullShapeletTransform, and does post 
 * transformation clustering. 
*       
* */

/* package weka.classifiers.trees.shapelet_trees.* contains
 *  ShapeletTreeClassifier: implementation of a shapelet tree to match the 
 * description on the original paper.
 * 4x tree classifiers based on the alternative distance measures in class 
 * QualityMeasures.
 */
import weka.core.*;
import org.ovgu.de.classifier.shapelet_transforms.distance_functions.*;
import org.ovgu.de.classifier.shapelet_transforms.quality_measures.ShapeletQuality.ShapeletQualityChoice;

public class ShapeletExamples {

	public static ShapeletTransform st;

	public static Instances basicTransformExample(Instances train) {
		/*
		 * Class to demonstrate the usage of the ShapeletTransform. Returns the
		 * transformed set of instances
		 */
		st = new ShapeletTransform();
		st.setSubSeqDistance(new OnlineSubSeqDistance());
		/*
		 * The number of shapelets defaults to 100. we recommend setting it to a large
		 * value, since there will be many duplicates and there is little overhead in
		 * keeping a lot (although the shapelet early abandon becomes less efficient).
		 * 
		 */
		// Let m=train.numAttributes()-1 (series length)
		// Let n= train.numInstances() (number of series)
		int nosShapelets = (train.numAttributes() - 1) * train.numInstances() / 5;
		if (nosShapelets < ShapeletTransform.DEFAULT_NUMSHAPELETS)
			nosShapelets = ShapeletTransform.DEFAULT_NUMSHAPELETS;
		st.setNumberOfShapelets(nosShapelets);
		/*
		 * Two other key parameters are minShapeletLength and maxShapeletLength. For
		 * each value between these two, a full search is performed, which is order
		 * (m^2n^2), so clearly there is a time/accuracy trade off. Defaults to min of 3
		 * max of 30.
		 */
		int minLength = 5;
		int maxLength = (train.numAttributes() - 1) / 10;
		if (maxLength < ShapeletTransform.DEFAULT_MINSHAPELETLENGTH)
			maxLength = ShapeletTransform.DEFAULT_MINSHAPELETLENGTH;
		st.setShapeletMinAndMax(minLength, maxLength);

		/*
		 * Next you need to set the quality measure. This defaults to IG, but we
		 * recommend using the F stat. It is faster and (debatably) more accurate.
		 */
		st.setQualityMeasure(ShapeletQualityChoice.F_STAT);
		// You can set the filter to output details of the shapelets or not
		st.setLogOutputFile("ShapeletExampleLog.csv");
		// Alternatively, you can turn the logging off
		// st.turnOffLog();

		/*
		 * Thats the basic options. Now you need to perform the transform.
		 * FullShapeletTransform extends the weka SimpleBatchFilter, but we have made
		 * the method process public to make usage easier.
		 */
		Instances shapeletT = null;
		try {
			shapeletT = st.process(train);
		} catch (Exception ex) {
			System.out.println("Error performing the shapelet transform" + ex);
			ex.printStackTrace();
			System.exit(0);
		}
		return shapeletT;
	}

	public static Instances clusteredShapeletTransformExample(Instances train) {
		/*
		 * The class ClusteredShapeletTransform contains a FullShapeletTransform and
		 * post transform clusters it. You can either perform the transform outside of
		 * the ClusteredShapeletTransform or leave it to do it internally.
		 * 
		 */

		Instances shapeletT = null;
		// Cluster down to 10% of the number.
		int nosShapelets = (train.numAttributes() - 1) * train.numInstances() / 50;
		ClusteredShapeletTransform cst = new ClusteredShapeletTransform(st, nosShapelets);
		System.out.println(" Clustering down to " + nosShapelets + " Shapelets");
		System.out.println(" From " + st.getNumberOfShapelets() + " Shapelets");

		try {
			shapeletT = cst.process(train);
		} catch (Exception ex) {
			System.out.println("Error performing the shapelet clustering" + ex);

			ex.printStackTrace();
			System.exit(0);
		}
		return shapeletT;

	}

	public static void main(String[] args) {
		Instances train = null, test = null;
		FileReader r;
		/*
		 * Read from ARFF file and check if the attributes match with the ones declared.
		 * 
		 */
		
		String path = "C:\\Masters\\DKE\\KMDProject\\Data\\";
        String dataset = "BeetleFly";
        try{		
            r= new FileReader(path+dataset+"\\"+dataset+"_TRAIN1.arff"); 
//            r= new FileReader(train_path); 
            train = new Instances(r); 
		/*String path = "/home/sghosh/Data/";
		String dataset = "arff";
		try {
			r = new FileReader(path + dataset + "/" + "darshan.arff");
			// r= new FileReader(train_path);
			train = new Instances(r);*/
			System.out.println(train);
			// Indicates the position of the class
			train.setClassIndex(train.numAttributes() - 1);

		} catch (Exception e) {
			System.out.println("Unable to load data. Exception thrown =" + e);
			System.exit(0);
		}
		// System.out.println("****************** PERFORMING BASIC TRANSFORM *******");
		Instances shapeletT = basicTransformExample(train);
		System.out.println("\n **************** CLUSTERING *******");
		shapeletT = clusteredShapeletTransformExample(train);
		/*
		 * Write output of a variable to a file.
		 */
		try (PrintWriter out = new PrintWriter("ST_ShapeletDarshan.arff")) {
			out.println(shapeletT);
		} catch (FileNotFoundException f) {
			System.out.println(f);
		}
		System.out.println(" Clustered Transformed data set =" + shapeletT);
	}
}
