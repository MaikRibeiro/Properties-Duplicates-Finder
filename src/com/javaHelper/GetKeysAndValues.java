package com.javaHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GetKeysAndValues {
	public void getKeys(String propertiesFile, boolean addOperatorEqual) {

		if (propertiesFile == null || propertiesFile.isBlank()) {
			System.err.println("Error: Enter a file name");
			return;
		}

		BufferedReader brReader = null;
		BufferedWriter brWriter = null;

		try {

			brReader = new BufferedReader(new InputStreamReader(new FileInputStream("src/com/resources/" + propertiesFile)));
			brWriter = Files.newBufferedWriter(Paths.get(DuplicatePropertiesFinder.ROOT_URI + "keys.txt"), StandardCharsets.UTF_8);
			boolean bError = false;

			String line = "";
			StringBuilder sbError = new StringBuilder();

			Set<String> hsDuplicateKey = new HashSet<String>();
			List<String> alKeys = new ArrayList<String>();
			
			int lineNumber = 1;
			while ((line = brReader.readLine()) != null && !line.isBlank()) {

				String[] fields = line.split("=");
				String key = fields[0].trim();
				String value = (fields.length > 1) ? fields[1].trim() : null;

				if (!hsDuplicateKey.add(key)) {
					sbError.append("LINE ").append(lineNumber).append(": " + key).append(" -> ALREDY EXISTS");
					bError = true;
				}

				if (value == null) {
					sbError.append("LINE ").append(lineNumber).append(": Invalid key format: ").append(key).append("\n");
					bError = true;
				} else {

					if (!bError) {
						if (addOperatorEqual)
							brWriter.write(key + " =");
						else
							brWriter.write(key);
	
						brWriter.newLine();
						alKeys.add(key);
					}
				}
				
				lineNumber++;
			}

			if (bError)
				System.out.println(sbError.toString());
			else {
				for (String s : alKeys) {
					if (addOperatorEqual)
						System.out.println(s + " =");
					else
						System.out.println(s);
				}
			}

		} catch (FileNotFoundException e) {
	    System.err.println("Error: File not found - " + e.getMessage());

		} catch (IOException e) {
		    System.err.println("Error: I/O exception occurred - " + e.getMessage());

		} finally {
			if (brReader != null)
				try { brReader.close(); } catch (IOException e2) { System.out.println(e2.getMessage()); }

			if (brWriter != null)
				try { brWriter.close(); } catch (IOException e2) { System.out.println(e2.getMessage()); }
		}
	}

	public void getValues(String propertiesFile) {

		if (propertiesFile == null || propertiesFile.isBlank()) {
			System.err.println("Error: Enter a file name");
			return;
		}

		BufferedReader brReader = null;
		BufferedWriter bwWriter = null;

		try {

			brReader = new BufferedReader(new InputStreamReader(new FileInputStream(DuplicatePropertiesFinder.ROOT_URI + propertiesFile), "UTF-8"));
			bwWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(DuplicatePropertiesFinder.ROOT_URI + "values.txt"), "UTF-8"));
			boolean bError = false;

			String line = "";
			StringBuilder sbError = new StringBuilder();

			Set<String> hsDuplicateValues = new HashSet<String>();
			List<String> alValues = new ArrayList<String>();

			int lineNumber = 1;
			while ((line = brReader.readLine()) != null && !line.isBlank()) {

				String[] fields = line.split("=");
				String key = fields[0].trim();
				String value = (fields.length > 1) ? fields[1].trim() : null;

				if (value != null && !hsDuplicateValues.add(value)) {
					sbError.append("LINE ").append(lineNumber).append(": " + key).append(" -> ALREDY EXISTS");
					bError = true;
				}

				if (value == null || value.isBlank()) {
					sbError.append("LINE ").append(lineNumber).append(": Invalid key format: ").append(key).append("\n");
					bError = true;
				}

				alValues.add(value);
				lineNumber++;
			}

			if (bError)
				System.out.println(sbError.toString());

			else {
				for (int i = 0; i < alValues.size(); i++) {
					bwWriter.write(alValues.get(i));

					if (i < alValues.size() - 1)
						bwWriter.newLine();
				}

				System.out.println("Completed! Values file created in src/com/resources/values.txt");
			}

		} catch (FileNotFoundException e) {
	    System.err.println("Error: File not found - " + e.getMessage());

		} catch (IOException e) {
		    System.err.println("Error: I/O exception occurred - " + e.getMessage());

		} finally {
			if (brReader != null)
				try { brReader.close(); } catch (IOException e2) { System.out.println(e2.getMessage()); }

			if (bwWriter != null)
				try { bwWriter.close(); } catch (IOException e2) { System.out.println(e2.getMessage()); }
		}
	}
	
	public void getBoth(String fileName, boolean addOperatorEqual) {
		getKeys(fileName, addOperatorEqual);
		getValues(fileName);
	}
	
	public void matchKeysAndValues(String keys, String values) {

		if ((keys == null || keys.isBlank()) && (values == null || values.isBlank())) {
			System.err.println("Error: Enter a keys or values file");
			return;
		}

		BufferedReader brPathKey = null;
		BufferedReader brPathValues = null;
		BufferedWriter bwWriter = null;

		try {
			brPathKey = new BufferedReader(new InputStreamReader(new FileInputStream(DuplicatePropertiesFinder.ROOT_URI + keys), "UTF-8"));
			brPathValues = new BufferedReader(new InputStreamReader(new FileInputStream(DuplicatePropertiesFinder.ROOT_URI + values), "UTF-8"));
			bwWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(DuplicatePropertiesFinder.ROOT_URI + "result.txt"), "UTF-8"));
			
			long keyCount = Files.lines(Paths.get(DuplicatePropertiesFinder.ROOT_URI + keys)).count();
			long valueCount = Files.lines(Paths.get(DuplicatePropertiesFinder.ROOT_URI + values)).count();

			if (keyCount != valueCount) {
				System.err.println("Error: Mismatched number of keys and values.");
		    return;
			}

			String lineKey = "";
			String lineValue = "";
			int largestLineLength = 0;

			ArrayList<String> alKeysAndValues = new ArrayList<String>();

			while ((lineKey = brPathKey.readLine()) != null && (lineValue = brPathValues.readLine()) != null) {
				if (largestLineLength < lineKey.length())
					largestLineLength = lineKey.length() + 1;

				alKeysAndValues.add(lineKey + " = " + lineValue);
			}

			for (int i = 0; i < alKeysAndValues.size(); i++) {
				String[] fields = alKeysAndValues.get(i).split("=");
				String key = fields[0];
				String value = fields[1];

				while (key.length() < largestLineLength)
					key += " ";

				bwWriter.write(key + " = " + value);

				if (i < alKeysAndValues.size() - 1)
					bwWriter.newLine();
			}
			
			System.out.println("Completed! You can find the results in src/com/resources/result.txt");

		} catch (FileNotFoundException e) {
	    System.err.println("Error: File not found - " + e.getMessage());

		} catch (IOException e) {
		    System.err.println("Error: I/O exception occurred - " + e.getMessage());

		} finally {
			if (brPathKey != null)
				try { brPathKey.close(); } catch (Exception e2) { System.out.println(e2.getMessage()); }

			if (brPathValues != null)
				try { brPathValues.close(); } catch (Exception e3) { System.out.println(e3.getMessage()); }

			if (bwWriter != null)
				try { bwWriter.close(); } catch (Exception e4) { System.out.println(e4.getMessage()); }
		}
	}

	public static void main(String[] args) {
		GetKeysAndValues getKeysAndValues = new GetKeysAndValues();
		getKeysAndValues.getValues("test.properties");
	}
}