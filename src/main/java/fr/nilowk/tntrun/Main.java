package fr.nilowk.tntrun;

import fr.nilowk.tntrun.listeners.GameManager;
import fr.nilowk.tntrun.listeners.PlayerListener;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {

    private Location spawn = new Location(getServer().getWorld("world"), this.getConfig().getDouble("spawn.x"), this.getConfig().getDouble("spawn.y"), this.getConfig().getDouble("spawn.z"), 180f, 0f);
    private List<Player> players = new ArrayList<>();
    private List<Block> blockToRegen = new ArrayList<>();
    private HashMap<Integer, Player> top = new HashMap<>();
    private Gstate state;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        setState(Gstate.WAITING);

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new GameManager(this), this);

    }

    public Location getSpawn() {

        return this.spawn;

    }

    public void setState(Gstate state) {

        this.state = state;

    }

    public boolean isState(Gstate state) {

        return this.state == state;

    }

    public List<Player> getPlayers() {

        return this.players;

    }

    public List<Block> getBlockToRegen() {

        return this.blockToRegen;

    }

    public HashMap<Integer, Player> getTop() {

        return this.top;

    }

}
