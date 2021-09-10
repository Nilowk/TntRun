package fr.nilowk.tntrun.task;

import fr.nilowk.tntrun.Gstate;
import fr.nilowk.tntrun.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FinishTask extends BukkitRunnable {

    private Main instance;
    private int timer;

    public FinishTask(Main instance) {

        this.instance = instance;
        this.timer = instance.getConfig().getInt("timer.finish_time");

    }

    @Override
    public void run() {

        for (Player player : instance.getServer().getOnlinePlayers()) {

            if (instance.getTop().containsKey(3)) {

                player.sendTitle("§61: " + instance.getTop().get(1).getName(), "§62: " + instance.getTop().get(2).getName() + " || 3: " + instance.getTop().get(3).getName(), 0, 20 ,0);

            } else {

                player.sendTitle("§61: " + instance.getTop().get(1).getName(), "§62: " + instance.getTop().get(2).getName(), 0, 20, 0);

            }

        }

        if (timer <= 5 && timer != 0) {

            Bukkit.broadcastMessage("§6" + instance.getConfig().getString("message.restart.before") + " " + timer + " " + instance.getConfig().getString("message.restart.after"));

        }

        if (timer == 0) {

            Bukkit.broadcastMessage(instance.getConfig().getString("message.restart.now"));

            instance.getPlayers().clear();
            instance.getTop().clear();

            for (Player player : instance.getServer().getOnlinePlayers()) {

                instance.getPlayers().add(player);
                player.setGameMode(GameMode.ADVENTURE);
                player.teleport(instance.getSpawn());

            }

            for (Block block : instance.getBlockToRegen()) {

                block.setType(Material.STONE);

            }

            if (instance.getPlayers().size() >= instance.getConfig().getInt("start.minplayer")) {

                AutoStart start = new AutoStart(instance);
                instance.setState(Gstate.STARTING);
                start.runTaskTimer(instance, 0, 20);

            } else {

                instance.setState(Gstate.WAITING);

            }

            cancel();

        }

        timer--;

    }

}
