package pro.paulek.CraftEssentials.data;

import org.bukkit.Bukkit;
import pro.paulek.CraftEssentials.ICraftEssentials;
import pro.paulek.CraftEssentials.objects.Job;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class JobQueue implements Runnable {

    private final ICraftEssentials plugin;
    Deque<Job<Runnable>> jobs = new ArrayDeque<>();

    public JobQueue(ICraftEssentials plugin) {
        this.plugin = Objects.requireNonNull(plugin);
    }

    public void init() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, 0, 0);
    }

    public void add(Job<Runnable> job) {
        jobs.add(job);
    }

    public void delete(Job<Runnable> job) {
        jobs.remove(job);
    }

    @Override
    public void run() {
        if(jobs.isEmpty()) {
            return;
        }
        if(jobs.getFirst().run() == -1) {
            return;
        }
        jobs.removeFirst();
    }

}
