package fr.pegasus.papermc.utilities.commons;

import java.util.Objects;

public class ObjectOperation {
    public static boolean equals (Object... objects) {
        if (objects.length < 2) throw new IllegalArgumentException("At least 2 objects to compare");

        for (int i = 1; i < objects.length; ++i)
            if (!Objects.equals(objects[i - 1], objects[i]))
                return false;

        return true;
    }
}
