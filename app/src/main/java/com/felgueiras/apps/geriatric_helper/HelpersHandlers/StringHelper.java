package com.felgueiras.apps.geriatric_helper.HelpersHandlers;

import java.text.Normalizer;

/**
 * Created by felgueiras on 22/03/2017.
 */

public class StringHelper {

    public static String removeAccents(String original) {
        String finalString = Normalizer.normalize(original, Normalizer.Form.NFD);
        finalString = finalString.replaceAll("[^\\p{ASCII}]", "");
        return finalString;
    }
}
