package com.xLumos.LumosBends.AirLift;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AirLift extends AirAbility implements AddonAbility {
    private int duration;
    @Attribute("Cooldown")
    private long cooldown;
    private double selectRange;
    private Entity entity;
    @Attribute("Damage")
    private double damage;

    public AirLift(Player player) {
        super(player);
        if (this.bPlayer.canBend(this) && !this.bPlayer.isOnCooldown(this)) {
            this.setFields();
            this.entity = GeneralMethods.getTargetedEntity(player, this.selectRange);
            if (this.entity != null) {
                this.start();
                this.bPlayer.addCooldown(this);
                DamageHandler.damageEntity(this.entity, this.damage, this);
            }
        }
    }

    public void setFields() {
        this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.AirLift.Cooldown");
        this.selectRange = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.AirLift.SelectRange");
        this.duration = ConfigManager.getConfig().getInt("ExtraAbilities.xLumos.AirLift.Duration");
        this.damage = (double)ConfigManager.getConfig().getInt("ExtraAbilities.xLumos.AirLift.Damage");
    }

    public void progress() {
        if (!this.player.isDead() && this.player.isOnline()) {
            if (this.entity.isDead()) {
                this.remove();
            } else if (this.entity instanceof Player && !((Player)this.entity).isOnline()) {
                this.remove();
            } else {
                playAirbendingParticles(this.entity.getLocation(), 2, (double)0.0F, (double)0.0F, (double)0.0F);
                playAirbendingParticles(this.entity.getLocation(), 1, (double)1.0F, (double)0.0F, (double)1.0F);
                ((LivingEntity)this.entity).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, this.duration / 500, 1));
                ((LivingEntity)this.entity).addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 10000, 1));
                if (this.getStartTime() + (long)this.duration <= System.currentTimeMillis()) {
                    ((LivingEntity)this.entity).removePotionEffect(PotionEffectType.NAUSEA);
                    this.remove();
                }

                if (!this.player.isSneaking()) {
                    ((LivingEntity)this.entity).removePotionEffect(PotionEffectType.NAUSEA);
                    this.remove();
                }
            }
        } else {
            this.remove();
        }
    }

    public void removeMove() {
        ((LivingEntity)this.entity).removePotionEffect(PotionEffectType. NAUSEA);
        this.remove();
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
        return "AirLift";
    }

    public Location getLocation() {
        return null;
    }

    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new AirLiftListener(), ProjectKorra.plugin);
        ProjectKorra.log.info(this.getName() + " " + this.getVersion() + " by " + this.getAuthor() + " loaded! ");
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.AirLift.Cooldown", 14000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.AirLift.SelectRange", (double)10.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.AirLift.Duration", 2500L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.AirLift.Damage", (double)1.0F);
        ConfigManager.defaultConfig.save();
    }

    public void stop() {
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getDescription() {
        return "An advanced airbending move, gather wind around your enemy's feet and send them into the sky!";
    }

    public String getInstructions() {
        return "To use AirLift, sneak (hold shift) while looking at your enemy! Make sure not to click or end your shifting - it will cancel the move early!";
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.AirLift.Enabled", true);
    }

    public String getVersion() {
        return "1.3";
    }
}
