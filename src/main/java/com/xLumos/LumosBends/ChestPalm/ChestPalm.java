package com.xLumos.LumosBends.ChestPalm;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ChiAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.chiblocking.passive.ChiPassive;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class ChestPalm extends ChiAbility implements AddonAbility {
    private Entity target;
    @Attribute("Damage")
    private double damage;
    @Attribute("Cooldown")
    private long cooldown;
    private int duration;
    private double knockbackDistance;

    public ChestPalm(Player sourceplayer, Entity targetentity) {
        super(sourceplayer);
        if (this.bPlayer.canBend(this)) {
            this.setFields();
            this.target = targetentity;
            double activationAngle = Math.toRadians((double)90.0F);
            Vector targetDirection = this.target.getLocation().getDirection().setY(0).normalize();
            Vector toTarget = this.target.getLocation().toVector().subtract(this.player.getLocation().toVector()).setY(0).normalize();
            double angle = (double)toTarget.angle(targetDirection);
            if (angle >= activationAngle && this.target.getLocation().distanceSquared(this.player.getLocation()) <= (double)25.0F) {
                this.start();
                this.bPlayer.addCooldown(this);
            }
        }
    }

    public void setFields() {
        this.damage = getConfig().getDouble("ExtraAbilities.xLumos.ChestPalm.Damage");
        this.knockbackDistance = getConfig().getDouble("ExtraAbilities.xLumos.ChestPalm.KnockbackDistance");
        this.cooldown = getConfig().getLong("ExtraAbilities.xLumos.ChestPalm.Cooldown");
        this.duration = getConfig().getInt("ExtraAbilities.xLumos.ChestPalm.Duration");
    }

    public void progress() {
        if (this.target instanceof Player && ChiPassive.willChiBlock(this.player, (Player)this.target)) {
            ChiPassive.blockChi((Player)this.target);
        }

        DamageHandler.damageEntity(this.target, this.damage, this);
        this.knockback();
        ((LivingEntity)this.target).addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, this.duration / 50, 1));
        ((LivingEntity)this.target).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, this.duration / 50, 1));
        this.remove();
    }

    private void knockback() {
        if (this.target != null) {
            Location loc = this.target.getLocation().subtract(this.target.getLocation().getDirection().normalize().multiply(1));
            this.target.setVelocity(GeneralMethods.getDirection(this.target.getLocation(), loc).multiply(this.knockbackDistance));
        }

    }

    public boolean isSneakAbility() {
        return false;
    }

    public boolean isHarmlessAbility() {
        return false;
    }

    public double getDamage() {
        return this.damage;
    }

    public long getCooldown() {
        return this.cooldown;
    }

    public String getName() {
        return "ChestPalm";
    }

    public Location getLocation() {
        return null;
    }

    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new ChestPalmListener(), ProjectKorra.plugin);
        ProjectKorra.log.info(this.getName() + " " + this.getVersion() + " by " + this.getAuthor() + " loaded! ");
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.ChestPalm.Cooldown", 6000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.ChestPalm.Damage", (double)2.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.ChestPalm.Duration", 4000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.ChestPalm.KnockbackDistance", (double)1.5F);
        ConfigManager.defaultConfig.save();
    }

    public void stop() {
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getDescription() {
        return "A powerful palm to your enemy's chest, send them hurtling backwards and gasping for breath!";
    }

    public String getInstructions() {
        return "To use ChestPalm, simply hit your enemy (on the front of their body) while the move slot is selected!";
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.ChestPalm.Enabled", true);
    }

    public String getVersion() {
        return "1.2";
    }
}
