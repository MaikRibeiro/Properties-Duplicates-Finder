package com.javaHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class DuplicatePropertiesFinder {
	public void checkFile(String fileName) {

		Scanner scanner = null;

		try {

			if (fileName == null || fileName.isBlank()) {
				System.err.println("Enter the file name");
				return;
			}

			File file = new File("src/com/resources/" + fileName);
			scanner = new Scanner(file);

			HashSet<String> hsUniqueKeys = new HashSet<String>();
			HashSet<String> hsUniqueValues = new HashSet<String>();
			HashSet<String> hsDuplicateKeys = new HashSet<String>();
			HashSet<String> hsDuplicateValues = new HashSet<String>();
			HashSet<String> alValuelessKey = new HashSet<String>();

			boolean hasDuplicities = false;

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				if (line.isEmpty())
					continue;
				
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
				if (value == null)
					alValuelessKey.add(key);
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

		} finally {
			if (scanner != null)
				scanner.close();
		}
	}
	
	public static void main(String[] args) {
		DuplicatePropertiesFinder duplicatePropertiesFinder = new DuplicatePropertiesFinder();
		duplicatePropertiesFinder.checkFile("test.properties");
	}
}