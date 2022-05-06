package com.adventofcode.day13;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KnightsOfTheDinnerTable {
	private static final Logger logger = LogManager.getLogger(KnightsOfTheDinnerTable.class);

	private List<String> inputData;
	private final Pattern NUMBER = Pattern.compile("\\d+");
	private final String COMMA = ",";

	public KnightsOfTheDinnerTable(File input) {
		inputData = Utils.fileToStringList(input);
	}

	public static void main(String[] args) {
		if (args.length == 0)
			logger.error("Input file missing in the program argument");
		else {
			String filePath = args[0];
			logger.info("Input file path - {}", filePath);
			KnightsOfTheDinnerTable tableArrangement = new KnightsOfTheDinnerTable(new File(filePath));
			System.out.println(
					"The total change in happiness with optimal seat arrangement is: " + tableArrangement.part1());
			System.out.println("The total change in happiness with optimal seat arrangement including myself is: "
					+ tableArrangement.part2());
		}
	}

	public int part1() {
		logger.info("Optimal seating arrangement calculation started");
		String[] people = inputData.stream().map(s -> s.split(" ", 2)[0]).distinct().toArray(String[]::new);
		Map<String, Integer> happinessTable = calculateHappinessScore();

		// Getting permutation of all the persons at the table
		List<String> permutations = Utils.getPermutation(people);

		return findMaxSeating(happinessTable, permutations);
	}

	public int part2() {
		logger.info("Optimal seating arrangement calculation by including myself started");
		String myself = "X";
		List<String> peopleList = inputData.stream().map(s -> s.split(" ", 2)[0]).distinct()
				.collect(Collectors.toList());
		peopleList.add(myself);
		String[] people = peopleList.toArray(String[]::new);
		Map<String, Integer> happinessTable = calculateHappinessScore();
		Map<String, Integer> tmpHappinessTable = new HashMap<>();
		happinessTable.entrySet().stream().forEach(e -> {
			tmpHappinessTable.putIfAbsent(myself + COMMA + e.getKey().split(COMMA)[0], 0);
			tmpHappinessTable.putIfAbsent(e.getKey().split(COMMA)[0] + COMMA + myself, 0);
			tmpHappinessTable.putIfAbsent(myself + COMMA + e.getKey().split(COMMA)[1], 0);
			tmpHappinessTable.putIfAbsent(e.getKey().split(COMMA)[1] + COMMA + myself, 0);
		});
		happinessTable.putAll(tmpHappinessTable);
		List<String> seatingPermutations = Utils.getPermutation(people);

		return findMaxSeating(happinessTable, seatingPermutations);
	}

	private Map<String, Integer> calculateHappinessScore() {
		Map<String, Integer> happinessScore = new HashMap<>();
		logger.info("Calculaing happiness score between each and every person");
		for (int i = 0; i < inputData.size(); i++) {
			String currentRelation = inputData.get(i).replace(".", "");
			String currentPerson = currentRelation.split(" ", 2)[0];
			String targetPerson = currentRelation.substring(currentRelation.lastIndexOf(" ") + 1);
			for (int j = i + 1; j < inputData.size(); j++) {
				String cmpRelationship = inputData.get(j).replace(".", "");
				String cmpPerson = cmpRelationship.split(" ", 2)[0];
				String cmpTarget = cmpRelationship.substring(cmpRelationship.lastIndexOf(" ") + 1);
				if (cmpPerson.equals(targetPerson) && currentPerson.equals(cmpTarget)) {
					Matcher matcherA = NUMBER.matcher(currentRelation);
					Matcher matcherB = NUMBER.matcher(cmpRelationship);
					matcherA.find();
					matcherB.find();
					int numA = Integer.parseInt(matcherA.group());
					numA = currentRelation.contains("gain") ? numA : -numA;
					int numB = Integer.parseInt(matcherB.group());
					numB = cmpRelationship.contains("gain") ? numB : -numB;
					happinessScore.putIfAbsent(currentPerson + COMMA + targetPerson, numA + numB);
					logger.debug("Relation: {} Happiness score: {}", currentPerson + COMMA + targetPerson, numA + numB);
					happinessScore.putIfAbsent(targetPerson + COMMA + currentPerson, numA + numB);
					logger.debug("Relation: {} Happiness score: {}", targetPerson + COMMA + currentPerson, numA + numB);
				}
			}
		}

		return happinessScore;
	}

	private int findMaxSeating(Map<String, Integer> happinessScore, List<String> permutations) {
		logger.info("Calculating total change in happiness with all the combinations");
		int max = 0;
		for (String seating : permutations) {
			String[] currentSeating = seating.split("\\s+");
			int sum = 0;
			for (int i = 0; i < currentSeating.length; i++) {
				if (i == currentSeating.length - 1) {
					sum += happinessScore.get(currentSeating[0] + COMMA + currentSeating[i]);
				} else {
					sum += happinessScore.get(String.join(COMMA, Arrays.copyOfRange(currentSeating, i, i + 2)));
				}
			}
			if (max < sum)
				max = sum;
		}
		return max;
	}
}
