package fr.nilowk.tntrun.task;

import fr.nilowk.tntrun.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;

public class BreakBlock extends BukkitRunnable {

    private Main instance;
    private Location loc;

    public BreakBlock(Main instance, Location loc) {

        this.instance = instance;
        this.loc = loc;

    }

    private final int SCAN_DEPTH = 1;

    @Override
    public void run() {

        int y = loc.getBlockY() + 1;
        Block block = null;

        for (int i = 0; i <= SCAN_DEPTH; i++) {

            block = getBlockUnderPlayer(y, loc);
            y--;

            if (block != null) {

                break;

            }

        }

        if (block != null) {

            final Block fblock = block;

            removeAndSync(fblock);

        }

    }

    private void removeAndSync(Block block) {

        instance.getBlockToRegen().add(block);
        block.setType(Material.AIR);

    }

    private static double PLAYER_BOUNDINGBOX_ADD = 0.3;

    private Block getBlockUnderPlayer(int y, Location location) {

        PlayerPosition loc = new PlayerPosition(location.getX(), y, location.getZ());

        Block b11 = loc.getBlock(location.getWorld(), +PLAYER_BOUNDINGBOX_ADD, -PLAYER_BOUNDINGBOX_ADD);

        if (b11.getType() != Material.AIR) {

            return b11;

        }

        Block b12 = loc.getBlock(location.getWorld(), -PLAYER_BOUNDINGBOX_ADD, +PLAYER_BOUNDINGBOX_ADD);

        if (b12.getType() != Material.AIR) {

            return b12;

        }

        Block b21 = loc.getBlock(location.getWorld(), +PLAYER_BOUNDINGBOX_ADD, +PLAYER_BOUNDINGBOX_ADD);

        if (b21.getType() != Material.AIR) {

            return b21;

        }

        Block b22 = loc.getBlock(location.getWorld(), -PLAYER_BOUNDINGBOX_ADD, -PLAYER_BOUNDINGBOX_ADD);

        if (b22.getType() != Material.AIR) {

            return b22;

        }

        return null;
    }

    private static class PlayerPosition {

        private double x;
        private int y;
        private double z;

        public PlayerPosition(double x, int y, double z) {

            this.x = x;
            this.y = y;
            this.z = z;

        }

        public Block getBlock(World world, double addx, double addz) {

            return world.getBlockAt(NumberConversions.floor(x + addx), y, NumberConversions.floor(z + addz));

        }

    }

}
