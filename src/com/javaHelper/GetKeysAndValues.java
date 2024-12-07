package com.javaHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
			
			while ((line = brReader.readLine()) != null && !line.isBlank()) {

				String[] fields = line.split("=");
				String key = fields[0].trim();
				String value = (fields.length > 1) ? fields[1].trim() : null;

				if (!hsDuplicateKey.add(key)) {
					sbError.append(key).append(" -> ALREDY EXISTS");
					bError = true;
				}

				if (value == null) {
					sbError.append(key).append(" -> CONTENS NO VALUE").append("\n");
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

		} catch (Exception e) {
			System.out.println(e.getMessage());

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
		BufferedWriter brWriter = null;

		try {
			
			brReader = new BufferedReader(new InputStreamReader(new FileInputStream("src/com/resources/" + propertiesFile)));
			brWriter = Files.newBufferedWriter(Paths.get(DuplicatePropertiesFinder.ROOT_URI + "values.txt"), StandardCharsets.UTF_8);
			boolean bError = false;
			
			String line = "";
			StringBuilder sbError = new StringBuilder();
			
			Set<String> hsDuplicateValues = new HashSet<String>();
			List<String> alValues = new ArrayList<String>();
			
			while ((line = brReader.readLine()) != null && !line.isBlank()) {

				String[] fields = line.split("=");
				String key = fields[0].trim();
				String value = (fields.length > 1) ? fields[1].trim() : null;

				if (value != null && !hsDuplicateValues.add(value)) {
					sbError.append(key).append(" -> ALREDY EXISTS");
					bError = true;
				}

				if (value == null) {
					sbError.append(key).append(" -> CONTENS NO VALUE").append("\n");
					bError = true;
				} else {
					if (!bError) {
						brWriter.write(value);
						brWriter.newLine();
						alValues.add(value);
					}
				}
			}

			if (bError)
				System.out.println(sbError.toString());
			else {
				for (String s : alValues)
						System.out.println(s);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());

		} finally {
			if (brReader != null)
				try { brReader.close(); } catch (IOException e2) { System.out.println(e2.getMessage()); }

			if (brWriter != null)
				try { brWriter.close(); } catch (IOException e2) { System.out.println(e2.getMessage()); }
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
	}
	
	public static void main(String[] args) {
		GetKeysAndValues getKeysAndValues = new GetKeysAndValues();
		getKeysAndValues.matchKeysAndValues("keys.txt", "values.txt");
	}
}