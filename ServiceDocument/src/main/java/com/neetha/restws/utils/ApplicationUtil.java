package com.neetha.restws.utils;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

public class ApplicationUtil {

	public static String getRandomId() {
		// TODO: Generate random alph numeric 20 digit string

		RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder().withinRange('0', 'z')
				.filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS).build();

		return randomStringGenerator.generate(20).toUpperCase();
	}

}
