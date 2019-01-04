package pl.bkobeszko.b2bcalculator.utils;

/**
 * Copyright (c) 2019, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class EnumUtils {
    private EnumUtils() {}
    
    /**
     * <p>Gets the enum for the class, returning {@code null} if not found.</p>
     *
     * <p>This method differs from {@link Enum#valueOf} in that it does not throw an exception
     * for an invalid enum name and performs case insensitive matching of the name.</p>
     *
     * @source Apache Commons
     * @param <E>         the type of the enumeration
     * @param enumClass   the class of the enum to query, not null
     * @param enumName    the enum name, null returns null
     * @return the enum, null if not found
     */
    public static <E extends Enum<E>> E getEnumIgnoreCase(final Class<E> enumClass, final String enumName) {
        if (enumName == null || !enumClass.isEnum()) {
            return null;
        }
        for (final E each : enumClass.getEnumConstants()) {
            if (each.name().equalsIgnoreCase(enumName)) {
                return each;
            }
        }
        return null;
    }
}
