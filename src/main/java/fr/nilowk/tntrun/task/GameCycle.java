package fr.nilowk.tntrun.task;

import fr.nilowk.tntrun.Gstate;
import fr.nilowk.tntrun.Main;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameCycle extends BukkitRunnable {

    private Main instance;
    private int timer = 0;

    public GameCycle(Main instance) {

        this.instance = instance;

    }

    @Override
    public void run() {

        for (Player player : instance.getPlayers()){

            player.setLevel(timer);

        }

        if (instance.isState(Gstate.FINISH)) {

            for (Player player : instance.getPlayers()){

                player.setLevel(0);
                player.setGameMode(GameMode.SPECTATOR);

            }

            cancel();

        }

        timer++;

    }

}