package com.xLumos.LumosBends.CrystalPrison;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class CrystalPrison extends EarthAbility implements AddonAbility {
    @Attribute("Cooldown")
    private long cooldown;
    private int duration;
    private Entity entity;
    private double selectRange;
    double max_height;
    static final Vector paralyze = new Vector(0, 0, 0);

    public CrystalPrison(Player player) {
        super(player);
        if (this.bPlayer.canBend(this) && !this.bPlayer.isOnCooldown(this)) {
            if (player.getInventory().contains(Material.EMERALD)) {
                Material m = Material.EMERALD;
                this.setFields();
                this.max_height = (double)0.0F;
                this.entity = GeneralMethods.getTargetedEntity(player, this.selectRange);
                if (this.entity != null) {
                    Location loc = this.entity.getLocation();
                    TempBlock base = new TempBlock(loc.subtract((double)0.0F, (double)1.0F, (double)0.0F).getBlock(), Material.EMERALD_BLOCK);
                    base.setRevertTime(4000L);
                    this.start();
                    this.bPlayer.addCooldown(this);
                }
            }
        }
    }

    public void setFields() {
        this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.CrystalPrison.Cooldown");
        this.duration = ConfigManager.getConfig().getInt("ExtraAbilities.xLumos.CrystalPrison.Duration");
        this.selectRange = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.CrystalPrison.selectRange");
    }

    public void progress() {
        if (!this.player.isDead() && this.player.isOnline()) {
            if (this.getStartTime() + (long)this.duration <= System.currentTimeMillis()) {
                this.remove();
            }

            if (this.max_height <= 1.2) {
                this.max_height += 0.02;
            }

            double height_increasement = 0.1;

            for(double y = (double)0.0F; y < this.max_height; y += height_increasement) {
                double radius = (double)1.5F;

                for(int i = 0; i < 360; i += 9) {
                    double angle = (double)i * Math.PI / (double)180.0F;
                    double x = 0.6 * radius * Math.cos(angle);
                    double z = 0.6 * radius * Math.sin(angle);
                    Location loc = this.entity.getLocation();
                    loc.add(x, y, z);
                    if (Math.random() < (double)0.5F) {
                        this.player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, loc, 3, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F);
                    }
                }
            }

            this.entity.setVelocity(paralyze);
            ((LivingEntity)this.entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, this.duration / 500, 9, false, false, false));
        } else {
            this.remove();
        }
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
        return "CrystalPrison";
    }

    public Location getLocation() {
        return null;
    }

    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new CrystalPrisonListener(), ProjectKorra.plugin);
        ProjectKorra.log.info(this.getName() + " " + this.getVersion() + " by " + this.getAuthor() + " loaded baked potatoes! ");
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.CrystalPrison.Cooldown", 14000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.CrystalPrison.Duration", 3000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.CrystalPrison.selectRange", (double)10.0F);
        ConfigManager.defaultConfig.save();
    }

    public void stop() {
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getDescription() {
        return "Use CrystalPrison to trap your enemy in a Jennamite cage!, thanks King Bumi!!";
    }

    public String getInstructions() {
        return "To CrystalPrison, tap shift while looking at an enemy! Note: you must have an emerald in your inventory!";
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.CrystalPrison.Enabled", true);
    }

    public String getVersion() {
        return "1.5";
    }
}
