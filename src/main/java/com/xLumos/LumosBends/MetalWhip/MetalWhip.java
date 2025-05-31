package com.xLumos.LumosBends.MetalWhip;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.MetalAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;
import java.util.Objects;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MetalWhip extends MetalAbility implements AddonAbility {
    @Attribute("Cooldown")
    private long cooldown;
    private double hitRadius;
    @Attribute("Damage")
    private double damage;
    @Attribute("Range")
    private double range;
    private Location location;
    private Location origin;

    public MetalWhip(Player player) {
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

            assert is != null;

            is.setAmount(is.getAmount() - 1);
            player.getInventory().setItem(slot, is);
            this.origin = player.getEyeLocation();
            this.location = this.origin.clone();
            this.setFields();
            this.start();
            this.bPlayer.addCooldown(this);
        }
    }

    public void setFields() {
        this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.MetalWhip.Cooldown");
        this.hitRadius = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.MetalWhip.HitRadius");
        this.damage = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.MetalWhip.Damage");
        this.range = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.MetalWhip.Range");
    }

    public void progress() {
        if (!this.bPlayer.canBendIgnoreCooldowns(this)) {
            this.remove();
        } else if (GeneralMethods.isRegionProtectedFromBuild(this, this.location)) {
            this.remove();
        } else if (this.location.distanceSquared(this.origin) > this.range * this.range) {
            this.remove();
        } else {
            Vector direction = this.player.getEyeLocation().getDirection();
            direction.normalize().multiply(1);
            this.location.add(direction);
            Color color = Color.fromRGB(0, 0, 0);
            ((World)Objects.requireNonNull(this.location.getWorld())).spawnParticle(Particle.DUST_COLOR_TRANSITION, this.getLocation(), 3, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, new Particle.DustTransition(color, color, 1.0F));

            for(Entity e : GeneralMethods.getEntitiesAroundPoint(this.location, this.hitRadius)) {
                if (e instanceof LivingEntity && e.getUniqueId() != this.player.getUniqueId()) {
                    DamageHandler.damageEntity(e, this.damage, this);
                    this.remove();
                    return;
                }
            }

        }
    }

    public boolean isSneakAbility() {
        return false;
    }

    public boolean isHarmlessAbility() {
        return false;
    }

    public long getCooldown() {
        return this.cooldown;
    }

    public String getName() {
        return "MetalWhip";
    }

    public Location getLocation() {
        return this.location;
    }

    public void load() {
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.MetalWhip.Cooldown", 4500L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.MetalWhip.HitRadius", (double)1.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.MetalWhip.Damage", (double)2.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.MetalWhip.Range", (double)28.0F);
        ConfigManager.defaultConfig.save();
        ProjectKorra.plugin.getServer().getLogger().info("MetalWhip has been successfully enabled!");
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new MetalWhipListener(), ProjectKorra.plugin);
    }

    public void stop() {
        ProjectKorra.plugin.getServer().getLogger().info("MetalWhip has been disabled!");
        super.remove();
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getDescription() {
        return "An advanced metal bending move, send out a cable to whip your enemies, and control its path!";
    }

    public String getInstructions() {
        return "Left click to send cable, control it by moving your mouse. Player must have iron nuggets in their inventory.";
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.MetalWhip.Enabled", true);
    }

    public String getVersion() {
        return "1.2";
    }
}
