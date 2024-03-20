package fr.pegasus.papermc.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class Countdown {

    private int duration;
    private final Consumer<Integer> announcer;
    private final Runnable action;

    /**
     * Create a countdown with a duration
     * @param duration The duration of the countdown (in seconds)
     */
    public Countdown(int duration, Consumer<Integer> announcer, Runnable action){
        this.duration = duration;
        this.announcer = announcer;
        this.action = action;
    }

    public void start(JavaPlugin plugin){
        new BukkitRunnable() {
            @Override
            public void run() {
                if(duration == 0){
                    action.run();
                    cancel();
                    return;
                }
                announcer.accept(duration);
                duration--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}
