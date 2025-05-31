package com.xLumos.LumosBends.FireVortex;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.FireAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class FireVortex extends FireAbility implements AddonAbility {
    @Attribute("Cooldown")
    private long cooldown;
    private long duration;
    private double hitRadius;
    @Attribute("Damage")
    private double damage;
    private Location location;
    private double angle;
    private int height;

    public FireVortex(Player player) {
        super(player);
        if (this.bPlayer.canBend(this) && !this.bPlayer.isOnCooldown(this)) {
            this.location = this.getPlayer().getLocation();
            this.setFields();
            this.bPlayer.addCooldown(this);
            this.angle = (double)0.0F;
            this.start();
        }
    }

    public void setFields() {
        this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.FireVortex.Cooldown");
        this.duration = (long)ConfigManager.getConfig().getInt("ExtraAbilities.xLumos.FireVortex.Duration");
        this.hitRadius = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.FireVortex.HitRadius");
        this.damage = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.FireVortex.Damage");
        this.height = ConfigManager.getConfig().getInt("ExtraAbilities.xLumos.FireVortex.Height");
    }

    public void progress() {
        if (!this.player.isDead() && this.player.isOnline()) {
            int max_height = this.height;
            double max_radius = (double)8.0F;
            int lines = 7;
            double height_increasement = 0.4;

            for(int l = 0; l < lines; ++l) {
                for(double y = (double)0.0F; y < (double)max_height; y += height_increasement) {
                    double radius_increasement = max_radius / (double)max_height * 0.1 * y;
                    double radius = y * radius_increasement + 0.3;
                    double x = Math.cos(Math.toRadians((double)(360 / lines * l) + y * (double)10.0F - this.angle)) * radius;
                    double z = Math.sin(Math.toRadians((double)(360 / lines * l) + y * (double)10.0F - this.angle)) * radius;
                    Location loc = this.player.getLocation();
                    loc.add(x, y, z);
                    if (Math.random() < (double)0.5F) {
                        this.playFirebendingParticles(loc, 1, 0.05, 0.05, 0.05);
                        this.playFirebendingParticles(loc, 1, (double)0.0F, (double)0.0F, (double)0.0F);
                    }
                }
            }

            ++this.angle;

            for(Entity e : GeneralMethods.getEntitiesAroundPoint(this.location, this.hitRadius)) {
                if (e instanceof LivingEntity && e.getUniqueId() != this.player.getUniqueId()) {
                    DamageHandler.damageEntity(e, this.damage, this);
                    ((LivingEntity)e).setFireTicks(15);
                }
            }

            if (this.getStartTime() + this.duration <= System.currentTimeMillis()) {
                this.remove();
            }

            if (!this.player.isSneaking()) {
                this.remove();
            } else {
                if (this.getStartTime() + this.duration <= System.currentTimeMillis()) {
                    this.remove();
                }

            }
        } else {
            this.remove();
        }
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
        return "FireVortex";
    }

    public Location getLocation() {
        return null;
    }

    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new FireVortexListener(), ProjectKorra.plugin);
        ProjectKorra.log.info(this.getName() + " " + this.getVersion() + " by " + this.getAuthor() + " loaded! ");
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.FireVortex.Duration", 10500L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.FireVortex.Cooldown", 20000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.FireVortex.HitRadius", (double)6.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.FireVortex.Damage", (double)0.5F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.FireVortex.Height", 8);
        ConfigManager.defaultConfig.save();
    }

    public void stop() {
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getDescription() {
        return "Summon a fiery vortex, burning enemies who venture too close!";
    }

    public String getInstructions() {
        return "Hold down shift. If you unshift, the move stops!";
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.FireVortex.Enabled", true);
    }

    public String getVersion() {
        return "1.3";
    }
}
