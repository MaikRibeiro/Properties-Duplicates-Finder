package com.javaHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
			List<String> alValuelessKey = new ArrayList<String>();

			boolean hasDuplicities = false;

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				
				/**
				 * Fields[0] returns key
				 * Fields[1] returns value
				 */
				String[] fields = line.split("=");
				
				if (!hsUniqueKeys.add(fields[0])) {
					hsDuplicateKeys.add(fields[0].trim());
					hasDuplicities = true;
				}

				if (fields.length > 1) {

					if (!hsUniqueValues.add(fields[1])) {
						hsDuplicateValues.add(fields[1].trim());
						hasDuplicities = true;
					}

				} else {
					alValuelessKey.add(fields[0]);
				}
			}

			if (hasDuplicities) {
				System.out.println("------------Valueless-Key-------------");
				for (String key : alValuelessKey)
					System.out.println(key);

				System.out.println("\n------------Duplicate-Keys-------------");
				for (String key : hsDuplicateKeys)
					System.out.println(key);

				System.out.println("\n------------Duplicate-Values------------");
				for (String value : hsDuplicateValues)
					System.out.println(value);
				
			} else {
				System.out.println("There are no duplicate keys or values");
			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());

		} finally {
			if (scanner != null)
				scanner.close();
		}
	}
	
	public static void main(String[] args) {
		DuplicatePropertiesFinder duplicatePropertiesFinder = new DuplicatePropertiesFinder();
		duplicatePropertiesFinder.checkFile("teste.properties");
	}
}