package fr.pegasus.papermc.worlds.locations;

import org.bukkit.Location;

public class RelativeLocation {

    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    /**
     * Create a relative location
     * @param x X value
     * @param y Y value
     * @param z Z value
     */
    public RelativeLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0;
        this.pitch = 0;
    }

    /**
     * Create a relative location
     * @param x X value
     * @param y Y value
     * @param z Z value
     * @param yaw Yaw value
     * @param pitch Pitch value
     */
    public RelativeLocation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Get the relative location between two locations
     * @param loc1 First location
     * @param loc2 Second location
     * @return A new relative location
     */
    public static RelativeLocation getRelativeLocation(Location loc1, Location loc2){
        return new RelativeLocation(
                loc1.getX() - loc2.getX(),
                loc1.getY() - loc2.getY(),
                loc1.getZ() - loc2.getZ(),
                loc1.getYaw() - loc2.getYaw(),
                loc1.getPitch() - loc2.getPitch()
        );
    }

    /**
     * Convert the relative location to an absolute location
     * @param loc The base location
     * @return A new absolute location
     */
    public Location toAbsolute(Location loc){
        return new Location(
                loc.getWorld(),
                loc.getX() + x,
                loc.getY() + y,
                loc.getZ() + z,
                loc.getYaw() + yaw,
                loc.getPitch() + pitch
        );
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }
    public float getPitch() {
        return pitch;
    }
    public float getYaw() {
        return yaw;
    }
}
