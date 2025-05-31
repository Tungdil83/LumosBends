package com.xLumos.LumosBends.MetalShots;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.ElementalAbility;
import com.projectkorra.projectkorra.ability.MetalAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MetalShots extends MetalAbility implements AddonAbility {
    private int startAmount;
    private int amount;
    @Attribute("Cooldown")
    private long cooldown;
    @Attribute("Range")
    private int range;
    @Attribute("Damage")
    private double damage;
    private double hitRadius;
    private final List<MetalShot> shots = new ArrayList();

    public MetalShots(Player player) {
        super(player);
        if (this.bPlayer.canBend(this) && !this.bPlayer.isOnCooldown(this)) {
            Material m;
            if (player.getInventory().contains(Material.IRON_NUGGET)) {
                m = Material.IRON_NUGGET;
            } else {
                if (!player.getInventory().contains(Material.GOLD_NUGGET)) {
                    return;
                }

                m = Material.GOLD_NUGGET;
            }

            int slot = player.getInventory().first(m);
            ItemStack is = player.getInventory().getItem(slot);
            if (is != null) {
                is.setAmount(is.getAmount() - 1);
                player.getInventory().setItem(slot, is);
                this.setFields();
                this.amount = this.startAmount;
                this.start();
            }
        }
    }

    public void setFields() {
        this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.MetalShots.Cooldown");
        this.startAmount = ConfigManager.getConfig().getInt("ExtraAbilities.xLumos.MetalShots.MetalCords");
        this.range = ConfigManager.getConfig().getInt("ExtraAbilities.xLumos.MetalShots.Range");
        this.damage = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.MetalShots.Damage");
        this.hitRadius = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.MetalShots.hitRadius");
    }

    public void progress() {
        if (!this.player.isDead() && this.player.isOnline()) {
            if (!this.bPlayer.canBendIgnoreCooldowns(this)) {
                this.amount = 0;
                if (!this.bPlayer.isOnCooldown(this)) {
                    this.bPlayer.addCooldown(this);
                }
            }

            Iterator<MetalShot> iterator = this.shots.iterator();

            while(iterator.hasNext()) {
                MetalShot shot = (MetalShot)iterator.next();
                if (!shot.progress()) {
                    iterator.remove();
                }
            }

            if (this.amount <= 0 && this.shots.isEmpty()) {
                this.remove();
            } else {
                if (this.amount > 0) {
                    this.displayMetalCords();
                }

            }
        } else {
            this.remove();
        }
    }

    public static void metalShot(Player player) {
        MetalShots ms = (MetalShots)getAbility(player, MetalShots.class);
        if (ms != null) {
            ms.metalShot();
        }

    }

    public void metalShot() {
        if (this.amount >= 1) {
            if (--this.amount <= 0) {
                this.bPlayer.addCooldown(this);
            }

            this.shots.add(new MetalShot(this.player, this.getRightHipPos(), this.range, this.damage));
        }

    }

    public Location getRightHipPos() {
        return GeneralMethods.getRightSide(this.player.getLocation(), 0.55).add((double)0.0F, (double)1.0F, (double)0.0F);
    }

    private void displayMetalCords() {
        ParticleEffect.REDSTONE.display(this.getRightHipPos().toVector().add(this.player.getEyeLocation().getDirection().clone().multiply(0.8)).toLocation(this.player.getWorld()), 3, (double)0.0F, (double)0.0F, (double)0.0F, 0.01, new Particle.DustOptions(Color.fromRGB(0, 0, 0), 1.0F));
    }

    public boolean isSneakAbility() {
        return true;
    }

    public boolean isHarmlessAbility() {
        return false;
    }

    public long getCooldown() {
        return this.cooldown;
    }

    public String getName() {
        return "MetalShots";
    }

    public Location getLocation() {
        return null;
    }

    public List<Location> getLocations() {
        return (List)this.shots.stream().map((shot) -> shot.location).collect(Collectors.toList());
    }

    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new MetalShotsListener(), ProjectKorra.plugin);
        ProjectKorra.log.info(this.getName() + " " + this.getVersion() + " by " + this.getAuthor() + " loaded! ");
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.MetalShots.Cooldown", 4000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.MetalShots.hitRadius", (double)1.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.MetalShots.Damage", (double)2.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.MetalShots.MetalCords", 2);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.MetalShots.Range", (double)15.0F);
        ConfigManager.defaultConfig.save();
    }

    public void stop() {
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getDescription() {
        return "An advanced metalbending move, use your *Republic City Police Issued* Metal Cables, and send short strips of cable at your enemy! Boy, that hurts!!";
    }

    public String getInstructions() {
        return "To MetalShots, tap shift and then click for each shot! Note: you need either iron or gold nuggets in your inventory to use!";
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.MetalShots.Enabled", true);
    }

    public String getVersion() {
        return "1.3";
    }

    public class MetalShot {
        private Player player;
        private Location location;
        private int range;
        private double distanceTravelled;
        private double damage;
        private Vector direction = null;

        public MetalShot(Player player, Location location, int range, double damage) {
            this.player = player;
            this.location = location;
            this.range = range;
            this.damage = damage;
        }

        public boolean progress() {
            if (!this.player.isDead() && this.player.isOnline()) {
                if (this.distanceTravelled >= (double)this.range) {
                    return false;
                } else {
                    for(int i = 0; i < 2; ++i) {
                        ++this.distanceTravelled;
                        if (this.distanceTravelled >= (double)this.range) {
                            return false;
                        }

                        Vector dir = this.direction;
                        if (dir == null) {
                            dir = this.player.getLocation().getDirection().clone();
                        }

                        this.location = this.location.add(dir);
                        if (GeneralMethods.isSolid(this.location.getBlock()) || ElementalAbility.isWater(this.location.getBlock())) {
                            return false;
                        }

                        Color color = Color.fromRGB(0, 0, 0);
                        this.player.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, this.location, 2, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, new Particle.DustTransition(color, color, 1.0F));

                        for(Entity e : GeneralMethods.getEntitiesAroundPoint(this.location, MetalShots.this.hitRadius)) {
                            if (e instanceof LivingEntity && e.getUniqueId() != this.player.getUniqueId()) {
                                DamageHandler.damageEntity(e, this.damage, CoreAbility.getAbility(this.player, MetalShots.class));
                                return false;
                            }
                        }
                    }

                    return true;
                }
            } else {
                return false;
            }
        }
    }
}
