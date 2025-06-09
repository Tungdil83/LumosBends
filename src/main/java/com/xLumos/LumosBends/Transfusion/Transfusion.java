package com.xLumos.LumosBends.Transfusion;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.BloodAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ClickType;
import java.util.ArrayList;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Transfusion extends BloodAbility implements AddonAbility, ComboAbility {
    @Attribute("Cooldown")
    private long cooldown;
    private double selectRange;
    private long duration;
    private Entity entity;
    private Location destination;
    private Location lineLoc;
    double lineLength = (double)0.0F;

    public Transfusion(Player player) {
        super(player);
        if (this.bPlayer.canBendIgnoreBindsCooldowns(this) && !this.bPlayer.isOnCooldown(this)) {
            this.setFields();
            this.entity = GeneralMethods.getTargetedEntity(player, this.selectRange);
            if (this.entity != null) {
                this.lineLoc = player.getLocation().add((double)0.0F, (double)1.0F, (double)0.0F);
                this.bPlayer.addCooldown(this);
                ((LivingEntity)this.entity).addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE, 10, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 70, 2));
                this.start();
            }
        }
    }

    public void setFields() {
        this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.Transfusion.Cooldown");
        this.selectRange = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.Transfusion.SelectRange");
        this.duration = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.Transfusion.Duration");
    }

    public void progress() {
        if (!this.player.isDead() && this.player.isOnline()) {
            if (this.entity.isDead()) {
                this.remove();
            } else if (this.entity instanceof Player && !((Player)this.entity).isOnline()) {
                this.remove();
            } else {
                this.destination = this.player.getLocation().add((double)0.0F, (double)1.0F, (double)0.0F);
                this.lineLoc = this.entity.getLocation().add((double)0.0F, (double)1.0F, (double)0.0F);
                Vector lineDir = GeneralMethods.getDirection(this.lineLoc, this.destination);
                double distance = this.entity.getLocation().distance(this.destination);
                if (this.lineLength <= distance) {
                    this.lineLength += 0.2;
                } else {
                    this.lineLength = distance;
                }

                for(double i = (double)0.0F; i < this.lineLength; i += 0.3) {
                    this.lineLoc = this.lineLoc.add(lineDir.normalize().multiply(0.3));
                    if (Math.random() < 0.7) {
                        Color color = Color.fromRGB(199, 46, 46);
                        this.player.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, this.lineLoc, 2, (double)0.0F, (double)0.0F, (double)0.0F, 0.04, new Particle.DustTransition(color, color, 1.0F));
                    }
                }

                if (this.lineLength > this.selectRange * (double)3.0F) {
                    this.remove();
                }

                if (this.player.getWorld() != this.entity.getWorld()) {
                    this.remove();
                }

                if (System.currentTimeMillis() > this.getStartTime() + this.duration) {
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
        return "Transfusion";
    }

    public Location getLocation() {
        return null;
    }

    public void load() {
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.Transfusion.Cooldown", 12000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.Transfusion.SelectRange", (double)12.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.Transfusion.Duration", 4000);
        ConfigManager.defaultConfig.save();
    }

    public void stop() {
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getInstructions() {
        return "BloodLet (Left Click) > HealingWaters (Left click)";
    }

    public String getDescription() {
        return "Using your bloodbending skills, as well as your skills of a healer, regenerate your health while damaging your enemies with Transfusion";
    }

    public Object createNewComboInstance(Player player) {
        return new Transfusion(player);
    }

    public ArrayList<ComboManager.AbilityInformation> getCombination() {
        ArrayList<ComboManager.AbilityInformation> combo = new ArrayList<>();
        combo.add(new ComboManager.AbilityInformation("BloodLet", ClickType.LEFT_CLICK_ENTITY));
        combo.add(new ComboManager.AbilityInformation("HealingWaters", ClickType.LEFT_CLICK_ENTITY));
        return combo;
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.Transfusion.Enabled", true);
    }

    public String getVersion() {
        return "1.4";
    }
}
