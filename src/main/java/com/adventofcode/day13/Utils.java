package com.adventofcode.day13;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	public static List<String> getPermutation(String... input) {
		List<String> permutations = new ArrayList<>();
		getPermutation(input, input.length, "", permutations);
	    return permutations;
	}

	private static void getPermutation(String[] input, int length, String currentWord, List<String> permutations) {
	    if (length == 0)
	    	permutations.add(currentWord.trim());
	    for (int i = 0; i < input.length; i++) {
	        if (!currentWord.contains(input[i]))
	        	getPermutation(input, length - 1, currentWord +" "+ input[i], permutations);
	    }
	}
	
	public static List<String> fileToStringList(File file) {
		List<String> content = new ArrayList<>();
		try {
		    BufferedReader br = new BufferedReader(new FileReader(file));
		    String line = "";
		    while ((line = br.readLine()) != null) {
			content.add(line);
		    }
		    br.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return content;
	}
}
