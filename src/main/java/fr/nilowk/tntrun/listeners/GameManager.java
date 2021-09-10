package fr.nilowk.tntrun.listeners;

import fr.nilowk.tntrun.Gstate;
import fr.nilowk.tntrun.Main;
import fr.nilowk.tntrun.task.BreakBlock;
import fr.nilowk.tntrun.task.FinishTask;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.CompletableFuture;

public class GameManager implements Listener {

    private Main instance;

    public GameManager(Main instance) {

        this.instance = instance;

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        if (instance.isState(Gstate.PLAYING)) {

            Player player = event.getPlayer();

            if (player.getGameMode() == GameMode.ADVENTURE) {

                Location pLoc = player.getLocation();

                Location loc = pLoc.clone().add(0, -1, 0);

                BreakBlock breakBlock = new BreakBlock(instance, loc);
                breakBlock.runTaskLater(instance, instance.getConfig().getInt("timer.breaktime"));

            }

        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if (instance.isState(Gstate.NOBREAK) || instance.isState(Gstate.PLAYING)) {

            Player player = event.getPlayer();

            player.setGameMode(GameMode.SPECTATOR);
            player.getInventory().clear();
            player.setLevel(0);
            player.teleport(instance.getSpawn());

            event.setJoinMessage("§f" + player.getName() + " " + instance.getConfig().getString("message.game.join"));

        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        if (instance.isState(Gstate.NOBREAK) || instance.isState(Gstate.PLAYING)) {

            Player player = event.getPlayer();

            if (instance.getPlayers().contains(player)) {

                instance.getPlayers().remove(player);

            }

            event.setQuitMessage("§f" + player.getName() + " " + instance.getConfig().getString("message.game.quit"));

            if (instance.getPlayers().size() == 2) {

                instance.getTop().put(3, player);

            } else if (instance.getPlayers().size() == 1) {

                instance.getTop().put(2, player);
                instance.getTop().put(1, instance.getPlayers().get(0));
                instance.getPlayers().get(0).teleport(instance.getSpawn());

                instance.setState(Gstate.FINISH);
                FinishTask finishTask = new FinishTask(instance);
                finishTask.runTaskTimer(instance, 0, 20);

            }

        }

    }

    @EventHandler
    public void onTakeDamage(EntityDamageEvent event) {

        if (instance.isState(Gstate.NOBREAK) || instance.isState(Gstate.PLAYING)) {

            if (event.getEntityType() == EntityType.PLAYER) {

                Player player = instance.getServer().getPlayer(event.getEntity().getName());

                if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {

                    instance.getPlayers().remove(player);
                    player.setGameMode(GameMode.SPECTATOR);
                    player.teleport(instance.getSpawn());

                    if (instance.getPlayers().size() == 2) {

                        instance.getTop().put(3, player);

                    } else if (instance.getPlayers().size() == 1) {

                        instance.getTop().put(2, player);
                        instance.getTop().put(1, instance.getPlayers().get(0));
                        instance.getPlayers().get(0).teleport(instance.getSpawn());

                        instance.setState(Gstate.FINISH);
                        FinishTask finishTask = new FinishTask(instance);
                        finishTask.runTaskTimer(instance, 0, 20);

                    }

                } else {

                    event.setCancelled(true);

                }

            }

        }

    }

}
