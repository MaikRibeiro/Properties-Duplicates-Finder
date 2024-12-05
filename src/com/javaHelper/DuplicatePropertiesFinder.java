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