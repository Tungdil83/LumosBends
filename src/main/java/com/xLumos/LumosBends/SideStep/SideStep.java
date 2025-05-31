package com.xLumos.LumosBends.SideStep;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ChiAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import java.util.Objects;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SideStep extends ChiAbility implements AddonAbility {
    @Attribute("Cooldown")
    private long cooldown;
    private long duration;
    private Location loc;
    private double dodgeSpeed;
    private double dodgeDistance;
    private boolean started;

    public SideStep(Player player) {
        super(player);
        if (this.bPlayer.canBend(this) && !this.bPlayer.isOnCooldown(this)) {
            this.setFields();
            this.start();
            this.bPlayer.addCooldown(this);
        }
    }

    public void setFields() {
        this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.SideStep.Cooldown");
        this.duration = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.SideStep.Duration");
        this.dodgeDistance = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.SideStep.DodgeDistance");
        this.dodgeSpeed = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.SideStep.DodgeSpeed");
    }

    public void progress() {
        if (!this.player.isDead() && this.player.isOnline()) {
            this.started = true;
            Location origin = this.player.getLocation();
            Color color1 = Color.fromRGB(130, 129, 126);
            ((World)Objects.requireNonNull(this.player.getWorld())).spawnParticle(Particle.DUST_COLOR_TRANSITION, origin, 2, (double)0.001F, (double)0.001F, (double)0.001F, (double)0.0F, new Particle.DustTransition(color1, color1, 1.0F));
            if (this.getStartTime() + this.duration <= System.currentTimeMillis()) {
                this.remove();
            }

        } else {
            this.remove();
        }
    }

    public void dodgeAttack() {
        this.loc = this.player.getLocation();
        Color color2 = Color.fromRGB(120, 113, 102);
        if (!isLava(GeneralMethods.getRightSide(this.loc, (double)4.0F).getBlock())) {
            this.player.setVelocity(GeneralMethods.getDirection(this.player.getLocation(), GeneralMethods.getRightSide(this.loc, this.dodgeDistance)).multiply(this.dodgeSpeed));
            ((World)Objects.requireNonNull(this.player.getWorld())).spawnParticle(Particle.DUST_COLOR_TRANSITION, this.loc, 2, (double)0.01F, (double)0.01F, (double)0.01F, (double)0.0F, new Particle.DustTransition(color2, color2, 1.0F));
            this.remove();
        } else {
            this.player.setVelocity(GeneralMethods.getDirection(this.player.getLocation(), GeneralMethods.getLeftSide(this.loc, this.dodgeDistance)).multiply(this.dodgeSpeed));
            ((World)Objects.requireNonNull(this.player.getWorld())).spawnParticle(Particle.DUST_COLOR_TRANSITION, this.loc, 2, (double)0.01F, (double)0.01F, (double)0.01F, (double)0.0F, new Particle.DustTransition(color2, color2, 1.0F));
            this.remove();
        }

    }

    public boolean isStarted() {
        return this.started;
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
        return "SideStep";
    }

    public Location getLocation() {
        return null;
    }

    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new SideStepListener(), ProjectKorra.plugin);
        ProjectKorra.log.info(this.getName() + " " + this.getVersion() + " by " + this.getAuthor() + " loaded! ");
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.SideStep.Cooldown", 25000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.SideStep.Duration", 20000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.SideStep.DodgeDistance", (double)1.5F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.SideStep.DodgeSpeed", (double)1.5F);
        ConfigManager.defaultConfig.save();
    }

    public void stop() {
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getDescription() {
        return "A wily ChiBlocking move, be able to dodge any move and sidestep to safety!";
    }

    public String getInstructions() {
        return "To activate SideStep, sneak (shift). If you take damage throughout the activation, you will dodge a move!!";
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.SideStep.Enabled", true);
    }

    public String getVersion() {
        return "1.2";
    }
}
