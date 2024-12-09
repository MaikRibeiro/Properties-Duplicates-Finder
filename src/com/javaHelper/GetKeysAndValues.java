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
import java.util.Scanner;
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
			brWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(DuplicatePropertiesFinder.ROOT_URI + "keys.txt"), StandardCharsets.UTF_8));
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
							alKeys.add(key + " =");
						else
							alKeys.add(key);
					}
				}


				lineNumber++;
			}

			if (bError)
				System.out.println(sbError.toString());

			else {
				for (int i = 0; i < alKeys.size(); i++) {
					brWriter.write(alKeys.get(i));
					
					if (i < alKeys.size() - 1)
						brWriter.newLine();
				}
				System.out.println("Completed! Keys file created in src/com/resources/keys.txt");
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

			brReader = new BufferedReader(new InputStreamReader(new FileInputStream(DuplicatePropertiesFinder.ROOT_URI + propertiesFile), StandardCharsets.ISO_8859_1));
			bwWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(DuplicatePropertiesFinder.ROOT_URI + "values.txt"), StandardCharsets.ISO_8859_1));
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
			brPathKey = new BufferedReader(new InputStreamReader(new FileInputStream(DuplicatePropertiesFinder.ROOT_URI + keys), StandardCharsets.ISO_8859_1));
			brPathValues = new BufferedReader(new InputStreamReader(new FileInputStream(DuplicatePropertiesFinder.ROOT_URI + values), StandardCharsets.ISO_8859_1));
			bwWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(DuplicatePropertiesFinder.ROOT_URI + "result.txt"), StandardCharsets.ISO_8859_1));
			
			long keyCount = Files.lines(Paths.get(DuplicatePropertiesFinder.ROOT_URI + keys)).count();
			long valueCount = Files.lines(Paths.get(DuplicatePropertiesFinder.ROOT_URI + values), StandardCharsets.ISO_8859_1).count();

			if (keyCount != valueCount) {
				System.err.println("Error: Mismatched number of keys and values.");
		    return;
			}

			String lineKey = "";
			String lineValue = "";
//			int largestLineLength = 0;

			ArrayList<String> alKeysAndValues = new ArrayList<String>();

			while ((lineKey = brPathKey.readLine()) != null && (lineValue = brPathValues.readLine()) != null) {
//				if (largestLineLength < lineKey.length())
//					largestLineLength = lineKey.length() + 1;

				alKeysAndValues.add(lineKey.trim() + " = 								" + lineValue.trim());
				
			}

			for (int i = 0; i < alKeysAndValues.size(); i++) {
//				String[] fields = alKeysAndValues.get(i).split("=");
//				String key = fields[0];
//				String value = fields[1];
//
//				while (key.length() < largestLineLength)
//					key += " ";
//
//				bwWriter.write(key + " = " + value.trim());

				bwWriter.write(alKeysAndValues.get(i).trim());
//
				if (i < alKeysAndValues.size() - 1)
					bwWriter.newLine();
			}
			
			System.out.println("Completed! You can find the results in src/com/resources/result.txt");

		} catch (FileNotFoundException e) {
	    System.err.println("Error: File not found - " + e.getMessage());

		} catch (IOException e) {
		    System.err.println("Error: I/O exception occurred - " + e.getMessage());

		} catch (Exception e) {
			System.err.println("Error: exception occurred - " + e.getMessage());
		}
		finally {
			if (brPathKey != null)
				try { brPathKey.close(); } catch (Exception e2) { System.out.println(e2.getMessage()); }

			if (brPathValues != null)
				try { brPathValues.close(); } catch (Exception e3) { System.out.println(e3.getMessage()); }

			if (bwWriter != null)
				try { bwWriter.close(); } catch (Exception e4) { System.out.println(e4.getMessage()); }
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
    try {
        String options = "-------WHAT-YOU-WOULD-LIKE-TO-DO-------\n" +
                         "1. Find Duplicates\n" +
                         "2. Get Keys\n" +
                         "3. Get Values\n" +
                         "4. Get Both\n" +
                         "5. Match Keys and Values\n" +
                         "--------------------------------------";

        while (true) {
            System.out.println(options);
            String ans = scanner.nextLine().trim();

            switch (ans) {
                case "1": {
                    System.out.println("Enter .properties file name:");
                    String fileName = scanner.nextLine().trim();

                    if (fileName.isEmpty()) {
                        System.out.println("Error: File name cannot be empty.");
                    } else {
                        DuplicatePropertiesFinder duplicatePropertiesFinder = new DuplicatePropertiesFinder();
                        duplicatePropertiesFinder.checkFile(fileName);
                    }
                    break;
                }
                case "2": {
                    System.out.println("Enter .properties file name to get keys:");
                    String fileName = scanner.nextLine().trim();

                    if (fileName.isEmpty()) {
                        System.out.println("Error: File name cannot be empty.");
                    } else {
                        GetKeysAndValues getKeysAndValues = new GetKeysAndValues();
                        getKeysAndValues.getKeys(fileName, false); // Example without " ="
                    }
                    break;
                }
                case "3": {
                    System.out.println("Enter .properties file name to get values:");
                    String fileName = scanner.nextLine().trim();

                    if (fileName.isEmpty()) {
                        System.out.println("Error: File name cannot be empty.");
                    } else {
                        GetKeysAndValues getKeysAndValues = new GetKeysAndValues();
                        getKeysAndValues.getValues(fileName);
                    }
                    break;
                }
                case "4": {
                    System.out.println("Enter .properties file name to get both keys and values:");
                    String fileName = scanner.nextLine().trim();

                    if (fileName.isEmpty()) {
                        System.out.println("Error: File name cannot be empty.");
                    } else {
                        GetKeysAndValues getKeysAndValues = new GetKeysAndValues();
                        getKeysAndValues.getBoth(fileName, false); // Example without " ="
                    }
                    break;
                }
                case "5": {
                    System.out.println("Enter file names for keys and values:");
                    System.out.print("Keys file: ");
                    String keysFile = scanner.nextLine().trim();
                    System.out.print("Values file: ");
                    String valuesFile = scanner.nextLine().trim();

                    if (keysFile.isEmpty() || valuesFile.isEmpty()) {
                        System.out.println("Error: File names cannot be empty.");
                    } else {
                        GetKeysAndValues getKeysAndValues = new GetKeysAndValues();
                        getKeysAndValues.matchKeysAndValues(keysFile, valuesFile);
                    }
                    break;
                }
                default:
                    System.out.println("Invalid option, please choose a valid option.");
                    break;
            }

            System.out.println("Do you want to continue? (y/n):");
            String continueAnswer = scanner.nextLine().trim();
            if (!continueAnswer.equalsIgnoreCase("y")) {
                System.out.println("Exiting...");
                break;
            }
        }
    } catch (Exception e) {
        System.err.println("An error occurred: " + e.getMessage());
    } finally {
			scanner.close();
	}
	}
}