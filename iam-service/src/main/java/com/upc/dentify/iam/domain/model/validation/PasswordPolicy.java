package com.upc.dentify.iam.domain.model.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validador simple y reutilizable de contraseñas fuertes.
 * Reglas por defecto:
 *  - longitud mínima (MIN_LENGTH)
 *  - al menos una letra mayúscula
 *  - al menos una letra minúscula
 *  - al menos un dígito
 *  - al menos un carácter especial (no alfanumérico)
 *
 * Devuelve la lista de violaciones.
 */
public final class PasswordPolicy {

    public static final int MIN_LENGTH = 12;

    private static final Pattern UPPERCASE = Pattern.compile(".*[A-Z].*");
    private static final Pattern LOWERCASE = Pattern.compile(".*[a-z].*");
    private static final Pattern DIGIT = Pattern.compile(".*\\d.*");
    private static final Pattern SPECIAL = Pattern.compile(".*[^A-Za-z0-9].*");

    private PasswordPolicy() { /* util class */ }

    /**
     * Valida la contraseña y devuelve una lista con las violaciones encontradas.
     * @param password contraseña (no nula)
     * @return lista de mensajes de error (vacía si válida)
     */
    public static List<String> validate(String password) {
        List<String> violations = new ArrayList<>();

        if (password == null) {
            violations.add("Password is required.");
            return violations;
        }

        if (password.length() < MIN_LENGTH) {
            violations.add(String.format("Password must be at least %d characters long.", MIN_LENGTH));
        }
        if (!UPPERCASE.matcher(password).matches()) {
            violations.add("Password must contain at least one uppercase letter (A-Z).");
        }
        if (!LOWERCASE.matcher(password).matches()) {
            violations.add("Password must contain at least one lowercase letter (a-z).");
        }
        if (!DIGIT.matcher(password).matches()) {
            violations.add("Password must contain at least one digit (0-9).");
        }
        if (!SPECIAL.matcher(password).matches()) {
            violations.add("Password must contain at least one special character (e.g. !@#$%^&*()).");
        }

        return violations;
    }
}
