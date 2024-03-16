package fr.pegasus.papermc.utilities.locations;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class Locations {
    public static @NotNull String encode (final @NotNull Location location, final boolean direction, final boolean round, final char separator) {
        final World world = location.getWorld();
        double x    = location.getX();
        double y    = location.getY();
        double z    = location.getZ();
        final float yaw   = location.getYaw();
        final float pitch = location.getPitch();

        if (round) {
            x = Math.floor(x * 10) / 10;
            y = Math.floor(y * 10) / 10;
            z = Math.floor(z * 10) / 10;
        }

        return world.getName() + separator + x + separator + y + separator + z + (direction ? separator + yaw + separator + pitch : "");
    }
    public static @NotNull Location decode (final @NotNull String string, final char separator) {
        final String[] part = string.split(separator + "");

        final World world = Bukkit.getWorld(part[0]);
        final double x    = Double.parseDouble(part[1]);
        final double y    = Double.parseDouble(part[2]);
        final double z    = Double.parseDouble(part[3]);

        if (part.length > 4) {
            final float yaw   = Float.parseFloat(part[4]);
            final float pitch = Float.parseFloat(part[5]);

            return new Location(world, x, y, z, yaw, pitch);
        }

        return new Location(world, x, y, z);
    }
    public static @NotNull Block blockFromCoordinates (final @NotNull World world, final int x, final int y, final int z) {
        return world.getBlockAt(x, y, z);
    }
    public static @NotNull Location fromCoordinates (final @NotNull World world, final double x, final double y, final double z) {
        return new Location(world, x, y, z);
    }
    public static @NotNull Location fromCoordinates (final @NotNull World world, final double x, final double y, final double z, final float yaw, final float pitch) {
        return new Location(world, x, y, z, yaw, pitch);
    }
}
