package com.xLumos.LumosBends.BloodLet;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.BloodAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class BloodLet extends BloodAbility implements AddonAbility {
    @Attribute("Cooldown")
    private long cooldown;
    private double selectRange;
    private Entity entity;
    private double y;
    private int currPoint;
    private Location location;
    private Vector direction;

    public BloodLet(Player player) {
        super(player);
        if (this.bPlayer.canBend(this) && !this.bPlayer.isOnCooldown(this)) {
            this.location = player.getEyeLocation().add((double)0.0F, (double)0.0F, (double)0.0F);
            this.direction = this.location.getDirection();
            this.setFields();

            for(double i = (double)0.0F; i < this.selectRange; i += (double)0.5F) {
                this.location = this.location.add(this.direction.multiply((double)0.5F).normalize());

                for(Entity entity : GeneralMethods.getEntitiesAroundPoint(this.location, (double)1.5F)) {
                    if (entity instanceof LivingEntity && entity.getEntityId() != player.getEntityId() && !(entity instanceof ArmorStand) && entity instanceof LivingEntity) {
                        this.entity = (LivingEntity)entity;
                    }
                }
            }

            this.entity = GeneralMethods.getTargetedEntity(player, this.selectRange);
            if (this.entity != null) {
                this.y = (double)0.0F;
                this.bPlayer.addCooldown(this);
                this.start();
            }
        }
    }

    public void setFields() {
        this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.BloodLet.Cooldown");
        this.selectRange = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.BloodLet.SelectRange");
    }

    public void progress() {
        if (!this.player.isDead() && this.player.isOnline()) {
            if (this.entity.isDead()) {
                this.remove();
            } else if (this.entity instanceof Player && !((Player)this.entity).isOnline()) {
                this.remove();
            } else {
                ((LivingEntity)this.entity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 70, 1));
                this.animate(this.entity.getLocation());
            }
        } else {
            this.remove();
        }
    }

    private void animate(Location location) {
        this.y += 0.05;
        if (this.y >= (double)1.25F) {
            this.y = (double)0.0F;
            this.remove();
        } else {
            for(int i = 0; (double)i < (double)0.5F; ++i) {
                this.currPoint += 6;
                if (this.currPoint > 360) {
                    this.currPoint = 0;
                }

                Location baseLoc = location.clone();
                double angle = (double)this.currPoint * Math.PI / (double)180.0F;
                double x = (double)1.15F * Math.cos(angle);
                double z = (double)1.15F * Math.sin(angle);
                Location loc = baseLoc.add(x, this.y, z);
                Color red = Color.fromRGB(199, 46, 46);
                Particle.DustOptions redOptions = new Particle.DustTransition(red, red, 1.0F);
                this.player.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, loc, 3, (double)0.125F, (double)0.125F, (double)0.125F, (double)0.0F, redOptions);
            }

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
        return "BloodLet";
    }

    public Location getLocation() {
        return null;
    }

    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new BloodLetListener(), ProjectKorra.plugin);
        ProjectKorra.log.info(this.getName() + " " + this.getVersion() + " by " + this.getAuthor() + " loaded! ");
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.BloodLet.Cooldown", 8000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.BloodLet.SelectRange", (double)6.0F);
        ConfigManager.defaultConfig.save();
    }

    public void stop() {
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getDescription() {
        return "An advanced bloodbending move, draw out the blood of your enemies, causing them to reach a *withering* state of health!";
    }

    public String getInstructions() {
        return "To use BloodLet, sneak (hold shift) while looking at your enemy!";
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.BloodLet.Enabled", true);
    }

    public String getVersion() {
        return "1.3";
    }
}
