package org.ovgu.de.unisens;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ovgu.de.utils.Constants;
import org.unisens.DataType;
import org.unisens.Entry;
import org.unisens.SignalEntry;
import org.unisens.Unisens;
import org.unisens.UnisensFactory;
import org.unisens.UnisensFactoryBuilder;
import org.unisens.UnisensParseException;

public class UnisensCSVGenerator {

	public static void main(String[] args) throws IOException {
		String binLoc = "E:\\user-study\\2018-05-31 14.46.01";
		String csvLoc = "E:\\user-study\\5Orig.csv";
		UnisensCSVGenerator csv = new UnisensCSVGenerator();
		csv.generateDataFromBin(binLoc, csvLoc, true);
	}

	public List<Double> generateDataFromBin(String binLoc, String csvLoc, boolean writeToCSV) throws IOException {
		//System.out.println("This example reads the first samples of a Unisens dataset.");
		///.out.println("Unisens File: " + binLoc);

		UnisensFactory uf = UnisensFactoryBuilder.createFactory();
		Unisens u = null;

		try {
			u = uf.createUnisens(binLoc);
		} catch (UnisensParseException e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}

		List<Double> edaList = new ArrayList<>();
		ArrayList<Entry> list = (ArrayList<Entry>) u.getEntries();
		//System.out.println("Timestamp start: " + u.getTimestampStart().toString());
		//System.out.println("Number of entries: " + list.size());
		BufferedWriter writer = null;
		for (int i = 0; i < list.size(); i++) {
			SignalEntry se = list.get(i) instanceof SignalEntry ? (SignalEntry) list.get(i) : null;
			if (se != null && se.getId().equals(Constants.EDA_BIN)) {
				//System.out.println(se.getId());
				//System.out.println("\nReading entry  " + list.get(i).getId() + "...");
				System.out.println("Sampling rate: " + se.getSampleRate());
				//System.out.println("Number of samples: " + se.getCount());
				//System.out.println("Number of Channels: " + se.getChannelCount());

				Long count = se.getCount();

				if (writeToCSV) {
					writer = new BufferedWriter(new FileWriter(csvLoc, true));
				}
				// read data
				if (se.getDataType() == DataType.INT16) {
					try {
						double[][] data = (double[][]) se.readScaled(count.intValue());

						System.out.println("\nsample# \tchannel# \tdata"+data.length);

						for (long s = 0; s < se.getCount(); s++) // chop 1 min, so consider from 1920
						{
							if (writeToCSV) {
								writer.append((s) + "," + data[Math.toIntExact(s)][0]);
								writer.append("\n");
							}
							edaList.add(data[Math.toIntExact(s)][0]);
						}
						//System.out.println("Done!");
					} catch (IOException e) {
						System.out.println("Exception: Can't read data.");
						e.printStackTrace();
					} finally {
						if (writer != null)
							writer.close();
					}
				} else {
					System.out.println("Data type is not INT16");
				}
			}
		}

		// close data set
		u.closeAll();
		return edaList;
	}
}