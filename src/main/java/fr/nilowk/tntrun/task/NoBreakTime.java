package fr.nilowk.tntrun.task;

import fr.nilowk.tntrun.Gstate;
import fr.nilowk.tntrun.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NoBreakTime extends BukkitRunnable {

    private Main instance;
    private int timer;

    public NoBreakTime(Main instance) {

        this.instance = instance;
        this.timer = instance.getConfig().getInt("timer.nobreak");

    }

    @Override
    public void run() {

        for (Player player : instance.getPlayers()) {

            player.setLevel(timer);

        }

        if (timer == 0) {

            Bukkit.broadcastMessage(instance.getConfig().getString("message.break"));

            GameCycle gameCycle = new GameCycle(instance);
            instance.setState(Gstate.PLAYING);
            gameCycle.runTaskTimer(instance, 0, 20);

            cancel();

        }

        if (instance.isState(Gstate.FINISH)) {

            for (Player player : instance.getPlayers()){

                player.setLevel(0);
                player.setGameMode(GameMode.SPECTATOR);

            }

            cancel();

        }

        timer--;

    }

}
