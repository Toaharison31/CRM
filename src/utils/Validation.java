package utils;

import java.util.regex.Pattern;

public class Validation {

    /**
     * Manamarina raha mahafeno ny fepetra notadiavina ilay teny miafina.
     * Fepetra: +8 caractères, 1 Majuscule, 1 Chiffre, 1 Caractère spécial (# ! ? .)
     * @return hafatra maneho ny error, na andian-tsoratra foana (empty) raha "valid"
     */
    public static String validatePasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            return "Le mot de passe doit contenir au moins 8 caractères.";
        }
        
        // Hijerena raha misy Majuscule
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            return "Le mot de passe doit contenir au moins une lettre majuscule.";
        }
        
        // Hijerena raha misy Chiffre (0-9)
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            return "Le mot de passe doit contenir au moins un chiffre.";
        }
        
        // Hijerena raha misy caractère spécifique (# ! ? .)
        if (!Pattern.compile("[#!?\\.]").matcher(password).find()) {
            return "Le mot de passe doit contenir au moins un caractère spécial parmi (# ! ? .).";
        }

        return ""; // Foana = Valid soa aman-tsara
    }
}