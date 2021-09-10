package fr.nilowk.tntrun.task;

import fr.nilowk.tntrun.Gstate;
import fr.nilowk.tntrun.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoStart extends BukkitRunnable {

    private Main instance;
    private int timer;

    public AutoStart(Main instance) {

        this.instance = instance;
        this.timer = instance.getConfig().getInt("timer.start");

    }

    @Override
    public void run() {

        for (Player player : instance.getPlayers()) {

            player.setLevel(timer);

        }

        if (timer == 10) {

            Bukkit.broadcastMessage(instance.getConfig().getString("message.start.before") + " 10 " + instance.getConfig().getString("message.start.after"));

        }

        if (timer <= 5 && timer > 0) {

            Bukkit.broadcastMessage(instance.getConfig().getString("message.start.before") + " " + timer + " " + instance.getConfig().getString("message.start.after"));

        }

        if (instance.getPlayers().size() < instance.getConfig().getInt("start.minplayer")) {

            for (Player player : instance.getPlayers()) {

                player.sendTitle(instance.getConfig().getString("title.noplayer.title"), instance.getConfig().getString("title.noplayer.subtitle"), 1, 40, 1);
                player.setLevel(0);

            }

            instance.setState(Gstate.WAITING);

            cancel();

        }

        if (instance.getPlayers().size() == instance.getConfig().getInt("start.maxplayer") || timer == 0) {

            for (Player player : instance.getPlayers()) {

                player.sendTitle(instance.getConfig().getString("title.start"), "", 1, 40, 1);
                player.teleport(instance.getSpawn());

            }

            NoBreakTime nbt = new NoBreakTime(instance);
            instance.setState(Gstate.NOBREAK);
            nbt.runTaskTimer(instance, 0, 20);

            cancel();

        }

        timer--;

    }

}
