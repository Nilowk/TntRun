package fr.nilowk.tntrun.listeners;

import fr.nilowk.tntrun.Gstate;
import fr.nilowk.tntrun.Main;
import fr.nilowk.tntrun.task.AutoStart;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private Main instance;

    public PlayerListener(Main instance) {

        this.instance = instance;

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if (instance.isState(Gstate.WAITING) || instance.isState(Gstate.STARTING)) {

            instance.getPlayers().add(player);
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            player.setLevel(0);
            player.teleport(instance.getSpawn());

            event.setJoinMessage("§f" + player.getName() + " " + instance.getConfig().getString("message.nogame.join") + " (" + instance.getPlayers().size() + "/"+ instance.getConfig().getInt("start.maxplayer") +")");

            if (instance.getPlayers().size() == instance.getConfig().getInt("start.minplayer")) {

                AutoStart start = new AutoStart(instance);
                instance.setState(Gstate.STARTING);
                start.runTaskTimer(instance, 0, 20);

            }

        } else if (instance.isState(Gstate.FINISH)) {

            event.setJoinMessage("§f" + player.getName() + " " + instance.getConfig().getString("message.game.join"));

        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        if (instance.isState(Gstate.WAITING) || instance.isState(Gstate.STARTING)) {

            if (instance.getPlayers().contains(player)) {

                instance.getPlayers().remove(player);

            }

            event.setQuitMessage("§f" + player.getName() + " " + instance.getConfig().getString("message.nogame.quit") + " (" + instance.getPlayers().size() + "/" + instance.getConfig().getInt("start.maxplayer") + ")");

        } else if (instance.isState(Gstate.FINISH)) {

            event.setQuitMessage("§f" + player.getName() + " " + instance.getConfig().getString("message.game.quit"));

        }

    }

    @EventHandler
    public void onTakeDamage(EntityDamageEvent event) {

        if (instance.isState(Gstate.STARTING) || instance.isState(Gstate.WAITING)) {

            if (event.getEntityType() == EntityType.PLAYER) {

                Player player = instance.getServer().getPlayer(event.getEntity().getName());
                event.setCancelled(true);

            }

        }

    }

}

