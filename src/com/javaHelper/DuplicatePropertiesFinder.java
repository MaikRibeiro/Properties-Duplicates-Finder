package com.javaHelper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

public class DuplicatePropertiesFinder {

	private final String ROOT_URI = "src/com/resources/";

	HashSet<String> hsUniqueKeys = new HashSet<String>();
	HashSet<String> hsUniqueValues = new HashSet<String>();
	HashSet<String> hsDuplicateKeys = new HashSet<String>();
	HashSet<String> hsDuplicateValues = new HashSet<String>();
	HashSet<String> alValuelessKey = new HashSet<String>();

	public void checkFile(String fileName) {

		if (fileName == null || fileName.isBlank()) {
			System.err.println("Error: Enter the file name");
			return;
		}

		BufferedReader br = null;
		try {

			br = new BufferedReader(new InputStreamReader(new FileInputStream(ROOT_URI + fileName), "UTF-8"));

			boolean hasDuplicities = false;

			String line;
			while ((line = br.readLine()) != null && !line.isBlank()) {

				/**
				 * Fields[0] returns key
				 * Fields[1] returns value
				 */
				String[] fields = line.split("=");
				String key = fields[0].trim();
				String value = (fields.length > 1) ? fields[1].trim() : null;
				
				// Check for duplicate keys
				if (!hsUniqueKeys.add(key)) {
					hsDuplicateKeys.add(key);
					hasDuplicities = true;
				}

				// Check for duplicate values
				if (value != null && !hsUniqueValues.add(value)) {
					hsDuplicateValues.add(value);
					hasDuplicities = true;
				}

				// Check for keys without values
				if (value == null) {
					alValuelessKey.add(key);
					hasDuplicities = true;
				}
			}

      if (hasDuplicities) {

      		// Map to group results
          HashMap<String, HashSet<String>> resultMap = new HashMap<String, HashSet<String>>();
          resultMap.put("Keys Without Values", alValuelessKey);
          resultMap.put("Duplicate Keys", new HashSet<String>(hsDuplicateKeys));
          resultMap.put("Duplicate Values", new HashSet<String>(hsDuplicateValues));

          StringBuffer result = new StringBuffer();

          // Display results all at once
          for (HashMap.Entry<String, HashSet<String>> entry : resultMap.entrySet()) {
              result.append("------------ ").append(entry.getKey()).append(" ------------\n");

              for (String item : entry.getValue())
                  result.append(item).append("\n");

          }
          System.out.println(result.toString());

			} else {
				System.out.println("There are no duplicate keys or values.");
			}

		} catch (FileNotFoundException e) {
			System.out.println("Error: " + e.getMessage());

		} catch (Exception e) {
			System.out.println("Error: " + e);

		} finally {
			if (br != null) 
				try { br.close(); } catch (IOException e) { System.out.println(e.getMessage()); }
		}
	}

	public void getKeys(String propertiesFile, boolean addOperatorEqual) {

		if (propertiesFile == null || propertiesFile.isBlank()) {
			System.err.println("Error: Enter a file name");
			return;
		}
	}

	public static void main(String[] args) {
		DuplicatePropertiesFinder duplicatePropertiesFinder = new DuplicatePropertiesFinder();
		duplicatePropertiesFinder.checkFile("test.properties");
	}
}