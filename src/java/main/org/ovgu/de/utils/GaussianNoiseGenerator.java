package org.ovgu.de.utils;
/**
 * 
 */

/**
 * @author Suhita Ghosh
 *
 */
import java.util.Random;

/**
 * The class generates pseudo-random numbers which follows a Gaussian
 * distribution. We assume the noise follows normal distribution - safest thing
 * to do!
 */
public final class GaussianNoiseGenerator {

	private static final double MEAN = 0.0f;
	private static final double VARIANCE = 1.5f;

	public static double getGaussian() {
		Random random = new Random();
		return MEAN + random.nextGaussian() * VARIANCE;
	}

	private static void log(Object aMsg) {
		System.out.println(String.valueOf(aMsg));
	}
}