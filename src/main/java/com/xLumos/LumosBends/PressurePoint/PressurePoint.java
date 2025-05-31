package com.xLumos.LumosBends.PressurePoint;

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

public class PressurePoint extends ChiAbility implements AddonAbility {
    private Entity target;
    @Attribute("Cooldown")
    private long cooldown;
    @Attribute("Damage")
    private double damage;
    private int duration;
    private double y;

    public PressurePoint(Player player, Entity entity) {
        super(player);
        if (this.bPlayer.canBend(this)) {
            this.target = this.getTargetLivingEntity(player, 3.4);
            this.setFields();
            if (this.getTargetLivingEntity(player, 3.4) != null) {
                this.start();
                this.bPlayer.addCooldown(this);
            }

        }
    }

    public void setFields() {
        this.damage = getConfig().getDouble("ExtraAbilities.xLumos.PressurePoint.Damage");
        this.cooldown = getConfig().getLong("ExtraAbilities.xLumos.PressurePoint.Cooldown");
        this.duration = getConfig().getInt("ExtraAbilities.xLumos.PressurePoint.Duration");
    }

    public LivingEntity getTargetLivingEntity(Player player, double range) {
        Vector direction = player.getLocation().getDirection().clone().multiply(0.1);
        Location loc = player.getEyeLocation().clone();
        Location startLoc = loc.clone();

        do {
            loc.add(direction);

            for(Entity target : GeneralMethods.getEntitiesAroundPoint(loc, (double)0.5F)) {
                if (target.getEntityId() != player.getEntityId() && target instanceof LivingEntity) {
                    this.y = loc.getY();
                    return (LivingEntity)target;
                }
            }
        } while(startLoc.distance(loc) < range && !GeneralMethods.isSolid(loc.getBlock()));

        return null;
    }

    public void progress() {
        if (this.target != null) {
            if (this.target instanceof Player && ChiPassive.willChiBlock(this.player, (Player)this.target)) {
                ChiPassive.blockChi((Player)this.target);
            }

            if (this.y > this.target.getLocation().add((double)0.0F, (double)1.5F, (double)0.0F).getY()) {
                this.hitHead();
                this.remove();
            } else if (this.y < this.target.getLocation().add((double)0.0F, (double)1.5F, (double)0.0F).getY() && this.y > this.target.getLocation().add((double)0.0F, (double)1.0F, (double)0.0F).getY()) {
                this.hitChest();
                this.remove();
            } else if (this.y < this.target.getLocation().add((double)0.0F, (double)1.0F, (double)0.0F).getY()) {
                this.hitLeg();
                this.remove();
            }
        }

    }

    private void hitHead() {
        DamageHandler.damageEntity(this.target, this.damage, this);
        ((LivingEntity)this.target).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, this.duration / 50, 1));
    }

    private void hitChest() {
        DamageHandler.damageEntity(this.target, this.damage, this);
        ((LivingEntity)this.target).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, this.duration / 50, 1));
    }

    private void hitLeg() {
        DamageHandler.damageEntity(this.target, this.damage, this);
        ((LivingEntity)this.target).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, this.duration / 50, 1));
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
        return "PressurePoint";
    }

    public Location getLocation() {
        return null;
    }

    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new PressurePointListener(), ProjectKorra.plugin);
        ProjectKorra.log.info(this.getName() + " " + this.getVersion() + " by " + this.getAuthor() + " loaded! ");
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.PressurePoint.Cooldown", 6000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.PressurePoint.Damage", (double)2.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.PressurePoint.Duration", 6000L);
        ConfigManager.defaultConfig.save();
    }

    public void stop() {
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getDescription() {
        return "Like Ty Lee, the best chiblockers know exactly where to hit their enemy. Based on where you hit your target, different pressure points will be activated, giving different effects!! Slow for legs, weakness for chest/arms, and blindness for head!";
    }

    public String getInstructions() {
        return "To use PressurePoint, simply hit your enemy at the desired location (legs, chest/arms, or head) while the move slot is selected!";
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.PressurePoint.Enabled", true);
    }

    public String getVersion() {
        return "1.2";
    }
}
