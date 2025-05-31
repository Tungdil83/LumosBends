package com.xLumos.LumosBends.Flatulate;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import java.util.Objects;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Flatulate extends AirAbility implements AddonAbility {
    @Attribute("Cooldown")
    private long cooldown;
    private Location loc;
    private Location origin;
    private boolean launched = false;

    public Flatulate(Player player) {
        super(player);
        if (this.bPlayer.canBend(this) && !this.bPlayer.isOnCooldown(this)) {
            this.loc = player.getLocation().subtract(player.getLocation().getDirection().normalize().multiply(0.3));
            this.origin = this.loc.add((double)0.0F, 0.6, (double)0.0F);
            this.setFields();
            this.start();
            this.bPlayer.addCooldown(this);
        }
    }

    private void setFields() {
        this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.Flatulate.Cooldown");
    }

    public void progress() {
        if (!this.player.isDead() && this.player.isOnline()) {
            if (Math.random() < (double)0.5F) {
                Color color = Color.fromRGB(126, 214, 144);
                ((World)Objects.requireNonNull(this.player.getWorld())).spawnParticle(Particle.DUST_COLOR_TRANSITION, this.origin, 1, (double)0.2F, (double)0.2F, (double)0.2F, (double)0.0F, new Particle.DustTransition(color, color, 1.0F));
                ((World)Objects.requireNonNull(this.player.getWorld())).spawnParticle(Particle.DUST_COLOR_TRANSITION, this.origin, 1, (double)0.3F, (double)0.3F, (double)0.3F, (double)0.0F, new Particle.DustTransition(color, color, 1.0F));
            }

            playAirbendingParticles(this.origin, 1, (double)0.1F, (double)0.1F, (double)0.1F);
            playAirbendingParticles(this.origin, 1, (double)0.3F, (double)0.3F, (double)0.3F);
            if (!this.launched) {
                this.launchPlayer();
            }

            long chargeTime = System.currentTimeMillis() - this.getStartTime();
            if (chargeTime > 3000L) {
                this.remove();
            }

        } else {
            this.remove();
        }
    }

    private void launchPlayer() {
        this.player.setVelocity(GeneralMethods.getDirection(this.player.getLocation(), this.player.getLocation().add(this.player.getLocation().getDirection().normalize().multiply(1.2)).add((double)0.0F, (double)1.5F, (double)0.0F)).multiply((double)1.0F));
        this.launched = true;
    }

    public boolean isSneakAbility() {
        return true;
    }

    public boolean isHarmlessAbility() {
        return true;
    }

    public long getCooldown() {
        return this.cooldown;
    }

    public String getName() {
        return "Flatulate";
    }

    public Location getLocation() {
        return null;
    }

    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new FlatulateListener(), ProjectKorra.plugin);
        ProjectKorra.log.info(this.getName() + " " + this.getVersion() + " by " + this.getAuthor() + " loaded! ");
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.Flatulate.Cooldown", 6000L);
        ConfigManager.defaultConfig.save();
    }

    public void stop() {
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getDescription() {
        return "A technique developed by Meelo, blast away! This move is a meme... ";
    }

    public String getInstructions() {
        return "To use Flatulate, sneak (click shift)!";
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.Flatulate.Enabled", true);
    }

    public String getVersion() {
        return "1.1";
    }
}
