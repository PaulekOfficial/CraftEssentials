package pro.paulek.CraftEssentials.objects;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import pro.paulek.CraftEssentials.ICraftEssentials;

import java.util.Objects;

public class Job<T extends Runnable> {

    private static final int MAX_TICK_TIME = 40;
    private static final int MAX_TASK_TIME = 60;

    private final T instance;
    private volatile boolean set;
    private long lastTaskTime;
    private final ICraftEssentials plugin;
    private final boolean async;

    public Job(T instance, boolean async, ICraftEssentials plugin) {
        this.instance = Objects.requireNonNull(instance);
        this.plugin = Objects.requireNonNull(plugin);
        this.async = async;
    }

    public int run() {
        if(async) {
            return runAsync();
        }
        return runSync();
    }

    private int runSync() {
        if(isSafeToRun(plugin)) {
            return -1;
        }
        BukkitTask task = Bukkit.getScheduler().runTask(plugin, instance);
        long startTimeMillis = System.currentTimeMillis();
        do {
            if((System.currentTimeMillis() - startTimeMillis) >= MAX_TASK_TIME) {
                task.cancel();
                return 1;
            }
        } while (Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId()));
        return 0;
    }

    private int runAsync() {
        if(isSafeToRun(plugin)) {
            return -1;
        }
        BukkitTask task = Bukkit.getScheduler().runTaskAsynchronously(plugin, instance);
        long startTimeMillis = System.currentTimeMillis();
        do {
            if((System.currentTimeMillis() - startTimeMillis) >= MAX_TASK_TIME) {
                task.cancel();
                return 1;
            }
        } while (Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId()));
        return 0;
    }

    private boolean isSafeToRun(ICraftEssentials plugin) {
        set = false;
        Bukkit.getScheduler().runTask(plugin, run -> {
            this.lastTaskTime = System.currentTimeMillis();
            set = true;
        });
        while (!set);
        return (System.currentTimeMillis() - lastTaskTime) > MAX_TICK_TIME;
    }

}
