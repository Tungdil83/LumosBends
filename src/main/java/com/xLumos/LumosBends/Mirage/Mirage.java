package com.xLumos.LumosBends.Mirage;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Mirage extends AirAbility implements AddonAbility {
    private int duration;
    @Attribute("Cooldown")
    private long cooldown;
    private int rotation;
    private double y;
    private double originhealth;

    public Mirage(Player player) {
        super(player);
        if (this.bPlayer.canBendIgnoreCooldowns(this)) {
            this.setFields();
            this.originhealth = player.getHealth();
            this.y = (double)0.0F;
            this.bPlayer.addCooldown(this);
            this.start();
        }
    }

    public void setFields() {
        this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.Mirage.Cooldown");
        this.duration = ConfigManager.getConfig().getInt("ExtraAbilities.xLumos.Mirage.Duration");
    }

    public void progress() {
        playAirbendingParticles(this.player.getLocation(), 1, (double)0.0F, (double)0.0F, (double)0.0F);
        if (this.player.getHealth() < this.originhealth) {
            this.player.removePotionEffect(PotionEffectType.INVISIBILITY);
            this.player.removePotionEffect(PotionEffectType.SLOWNESS);
            this.remove();
        } else {
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, this.duration / 1000 * 8, 0));
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, this.duration / 1000 * 8, 0));
            ++this.rotation;
            this.y += (double)0.0F;
            if (this.getStartTime() + (long)this.duration <= System.currentTimeMillis()) {
                this.remove();
            }

            if (!(this.y >= (double)3.0F)) {
                for(int i = 0; i < 2; ++i) {
                    ++this.y;
                    double angle = (double)i * Math.PI / (double)180.0F;
                    double x = 0.6 * (double)i * Math.cos((double)2.0F * angle + (double)this.rotation);
                    double z = 0.6 * (double)i * Math.sin((double)2.0F * angle + (double)this.rotation);
                    Location loc = this.player.getLocation();
                    loc.add(x, this.y, z);
                    playAirbendingParticles(loc, 3, (double)0.0F, (double)0.0F, (double)0.0F);
                    playAirbendingParticles(loc, 1, (double)1.5F, (double)0.0F, (double)1.5F);
                    playAirbendingParticles(loc, 2, (double)1.5F, (double)3.0F, (double)1.5F);
                    this.player.getWorld().spawnParticle(Particle.CLOUD, loc, 3, (double)0.1F, (double)0.1F, (double)0.1F, (double)0.0F);
                    this.player.getWorld().spawnParticle(Particle.CLOUD, loc, 3, (double)0.5F, (double)0.5F, (double)0.5F, (double)0.0F);
                    this.player.getWorld().spawnParticle(Particle.CLOUD, loc, 3, (double)4.0F, (double)4.0F, (double)4.0F, (double)0.0F);
                    this.player.getWorld().spawnParticle(Particle.CLOUD, loc, 3, (double)4.0F, (double)4.0F, (double)4.0F, (double)0.0F);
                }

            }
        }
    }

    public void removeMove() {
        this.remove();
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
        return "Mirage";
    }

    public Location getLocation() {
        return null;
    }

    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new MirageListener(), ProjectKorra.plugin);
        ProjectKorra.log.info(this.getName() + " " + this.getVersion() + " by " + this.getAuthor() + " loaded! ");
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.Mirage.Duration", 4000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.Mirage.Cooldown", 20000L);
        ConfigManager.defaultConfig.save();
    }

    public void stop() {
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getDescription() {
        return "Wisp yourself away with light and cold air into a cloaking mirage";
    }

    public String getInstructions() {
        return "To use Mirage, sneak (click shift)!";
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.Mirage.Enabled", true);
    }

    public String getVersion() {
        return "1.3";
    }
}
