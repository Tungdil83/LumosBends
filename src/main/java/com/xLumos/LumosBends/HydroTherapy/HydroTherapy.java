package com.xLumos.LumosBends.HydroTherapy;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.Element.SubElement;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.WaterAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.BlockSource;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.waterbending.util.WaterReturn;
import java.util.Objects;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class HydroTherapy extends WaterAbility implements AddonAbility {
    private Location location;
    private Entity entity;
    @Attribute("Cooldown")
    private long cooldown;
    private long defaultCharge;
    private double selectRange;
    private int point;
    private Block sourceBlock;
    private boolean healed = false;
    private int healDuration;

    public HydroTherapy(Player player) {
        super(player);
        if (this.bPlayer.canBend(this) && !this.bPlayer.isOnCooldown(this)) {
            this.setFields();
            this.entity = GeneralMethods.getTargetedEntity(player, this.selectRange);
            if (this.entity == null) {
                this.entity = player;
            }

            if (player != null) {
                ;
            }
        }
    }

    public void play(Material mat) {
        if (mat == null || isAir(mat)) {
            ;
        }
    }

    public void setFields() {
        this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.HydroTherapy.Cooldown");
        this.defaultCharge = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.HydroTherapy.DefaultCharge");
        this.selectRange = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.HydroTherapy.SelectRange");
        this.healDuration = ConfigManager.getConfig().getInt("ExtraAbilities.xLumos.HydroTherapy.HealDuration");
        if (!WaterReturn.hasWaterBottle(this.player)) {
            this.sourceBlock = BlockSource.getWaterSourceBlock(this.player, (double)12.0F, ClickType.LEFT_CLICK, true, true, true, true, true);
        } else {
            this.sourceBlock = this.player.getEyeLocation().clone().getBlock();
        }

        if (this.sourceBlock != null) {
            this.location = this.sourceBlock.getLocation();
            this.start();
            if (!this.isRemoved()) {
                this.bPlayer.addCooldown(this);
            }

        }
    }

    public void progress() {
        if (!this.player.isDead() && this.player.isOnline()) {
            if (this.entity != null) {
                if (this.entity.isDead()) {
                    this.remove();
                } else if (this.entity instanceof Player && !((Player)this.entity).isOnline()) {
                    this.remove();
                } else {
                    Location currentLoc = this.location.clone();
                    Material mat = currentLoc.getBlock().getType();
                    if (mat != Material.AIR && !isWater(mat)) {
                        this.remove();
                    } else {
                        if (WaterReturn.hasWaterBottle(this.player)) {
                            WaterReturn.isBendableWaterTempBlock(currentLoc.getBlock());
                            this.play(Material.WATER);
                        }

                        if (!WaterReturn.hasWaterBottle(this.player)) {
                            if (isWater(this.sourceBlock)) {
                                this.play(Material.WATER);
                            }

                            if (isSnow(this.sourceBlock)) {
                                this.play(this.sourceBlock.getType());
                            }

                            if (this.bPlayer.canUseSubElement(SubElement.PLANT) && isPlant(this.sourceBlock) && (this.sourceBlock.getType() == Material.SHORT_GRASS || this.sourceBlock.getType() == Material.TALL_GRASS)) {
                                this.play(this.sourceBlock.getType());
                            }

                            if (this.bPlayer.canUseSubElement(SubElement.ICE) && isIce(this.sourceBlock)) {
                                this.play(this.sourceBlock.getType());
                            }
                        }

                        long chargeTime = System.currentTimeMillis() - this.getStartTime();
                        if (chargeTime < this.defaultCharge) {
                            ((World)Objects.requireNonNull(this.location.getWorld())).spawnParticle(Particle.DUST_COLOR_TRANSITION, this.getRightHipPos(), 2, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, new Particle.DustTransition(Color.fromRGB(92, 184, 230), Color.fromRGB(92, 184, 230), 1.0F));
                            if (!this.player.isSneaking()) {
                                this.remove();
                                return;
                            }
                        } else {
                            ((LivingEntity)this.entity).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
                            if (!this.healed) {
                                this.healAlly();
                            }

                            ++this.point;
                            Location circlement = this.entity.getLocation();
                            Location loc = circlement.clone().add((double)0.0F, (double)1.0F, (double)0.0F);

                            for(int i = -180; i < 180; i += 30) {
                                double angle = (double)i * Math.PI / (double)180.0F;
                                double xRotation = 2.0943951023931953;
                                Vector v = (new Vector(Math.cos(angle + (double)this.point), Math.sin(angle + (double)this.point), (double)0.0F)).multiply(2);
                                Vector v1 = v.clone();
                                this.rotateAroundAxisX(v, xRotation);
                                this.rotateAroundAxisY(v, -((double)loc.getYaw() * Math.PI / (double)180.0F - (double)1.5F));
                                this.rotateAroundAxisX(v1, -xRotation);
                                this.rotateAroundAxisY(v1, -((double)loc.getYaw() * Math.PI / (double)180.0F - (double)1.5F));
                                if (Math.random() < 0.7) {
                                    this.player.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, loc.clone().add(v), 2, (double)0.001F, (double)0.0F, (double)0.001F, (double)0.0F, new Particle.DustTransition(Color.fromRGB(92, 184, 230), Color.fromRGB(92, 184, 230), 1.0F));
                                    this.player.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, loc.clone().add(v1), 2, (double)0.001F, (double)0.0F, (double)0.001F, (double)0.0F, new Particle.DustTransition(Color.fromRGB(92, 184, 230), Color.fromRGB(92, 184, 230), 1.0F));
                                }
                            }
                        }

                        long goldTime = System.currentTimeMillis() - this.getStartTime();
                        if (goldTime > 11000L) {
                            double radius = (double)4.0F;

                            for(int i = 0; i < 360; i += 9) {
                                double angle = (double)i * Math.PI / (double)180.0F;
                                double x = 0.6 * radius * Math.cos(angle);
                                double z = 0.6 * radius * Math.sin(angle);
                                Location locsparkle = this.entity.getLocation();
                                Location locsparkle1 = this.entity.getLocation().add((double)0.0F, (double)2.5F, (double)0.0F);
                                locsparkle.add(x, (double)0.0F, z);
                                locsparkle1.add(x, (double)0.0F, z);
                                this.player.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, locsparkle, 1, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, new Particle.DustTransition(Color.fromRGB(232, 253, 255), Color.fromRGB(232, 253, 255), 1.0F));
                                this.player.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, locsparkle1, 1, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, new Particle.DustTransition(Color.fromRGB(232, 253, 255), Color.fromRGB(232, 253, 255), 1.0F));
                            }
                        }

                        Location locsparkle2 = this.entity.getLocation();
                        if (Math.random() < (double)0.5F) {
                            this.player.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, locsparkle2, 1, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, new Particle.DustTransition(Color.fromRGB(92, 184, 230), Color.fromRGB(92, 184, 230), 1.0F));
                        }

                        if (this.getStartTime() + 12000L <= System.currentTimeMillis()) {
                            this.remove();
                        }

                    }
                }
            }
        } else {
            this.remove();
        }
    }

    private Vector rotateAroundAxisX(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    private Vector rotateAroundAxisY(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
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
        return "HydroTherapy";
    }

    public Location getLocation() {
        return null;
    }

    private void healAlly() {
        ((LivingEntity)this.entity).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, this.healDuration, 0));
        this.healed = true;
    }

    public Location getRightHipPos() {
        return GeneralMethods.getRightSide(this.player.getLocation(), 0.37).add((double)0.0F, 1.1, (double)0.0F);
    }

    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new HydroTherapyListener(), ProjectKorra.plugin);
        ProjectKorra.log.info(this.getName() + " " + this.getVersion() + " by " + this.getAuthor() + " loaded! ");
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.HydroTherapy.Cooldown", 20000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.HydroTherapy.DefaultCharge", 3000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.HydroTherapy.SelectRange", (double)6.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.HydroTherapy.HealDuration", 100);
        ConfigManager.defaultConfig.save();
    }

    public void stop() {
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getDescription() {
        return "An advanced waterbending move, invigorate your ally with your water healing";
    }

    public String getInstructions() {
        return "To use HydroTherapy, click a water source (or water bottle), then sneak (hold shift) while looking at your ally! You must hold sneak until you see particles form rings around your ally! If there is no ally around, it will heal yourself!";
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.HydroTherapy.Enabled", true);
    }

    public String getVersion() {
        return "1.2";
    }
}
