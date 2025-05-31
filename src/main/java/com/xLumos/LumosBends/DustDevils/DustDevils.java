package com.xLumos.LumosBends.DustDevils;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.SandAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
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

public class DustDevils extends SandAbility implements AddonAbility {
    @Attribute("Cooldown")
    private long cooldown;
    private int duration;
    @Attribute("Damage")
    private int damage;
    @Attribute("Damage")
    private double range;
    private Location origin;
    private double cycloneAngle;
    private double max_height;
    private double hitRadius;
    private boolean playerLocated = false;
    Random r = new Random();
    int low = 10;
    int high = 100;
    private Vector direction;
    private Vector backDirection;
    private Vector rightDirection;
    private Vector leftDirection;
    List<Location> cyclones = new ArrayList();
    private Location cyclone1;
    private Location cyclone2;
    private Location cyclone3;
    private Location cyclone4;
    private boolean sourced = false;

    public DustDevils(Player player) {
        super(player);
        if (this.bPlayer.canBend(this) && !this.bPlayer.isOnCooldown(this)) {
            this.origin = player.getLocation();
            this.setFields();
            this.cycloneAngle = (double)0.0F;
            this.direction = player.getEyeLocation().getDirection();
            this.direction.normalize().multiply(0.2);
            this.backDirection = player.getEyeLocation().getDirection();
            this.backDirection.normalize().multiply(-0.2);
            this.leftDirection = GeneralMethods.getDirection(player.getLocation(), GeneralMethods.getLeftSide(this.origin, (double)3.0F)).multiply(0.3);
            this.rightDirection = GeneralMethods.getDirection(player.getLocation(), GeneralMethods.getRightSide(this.origin, (double)3.0F)).multiply(0.3);
            this.sourceBlocksExist();
            if (this.sourced) {
                this.start();
            }

        }
    }

    private void setFields() {
        this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.DustDevils.Cooldown");
        this.duration = ConfigManager.getConfig().getInt("ExtraAbilities.xLumos.DustDevils.Duration");
        this.damage = ConfigManager.getConfig().getInt("ExtraAbilities.xLumos.DustDevils.Damage");
        this.range = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.DustDevils.Range");
        this.hitRadius = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.DustDevils.HitRadius");
    }

    private void sourceBlocksExist() {
        for(int a = -2; a < 5; ++a) {
            for(int b = -5; b < 5; ++b) {
                for(int c = -5; c < 5; ++c) {
                    Block block = this.origin.clone().add((double)b, (double)a, (double)c).getBlock();
                    if (this.isEarthbendable(block) || block.getType().equals(Material.END_STONE)) {
                        this.sourced = true;
                        return;
                    }

                    this.sourced = false;
                }
            }
        }

    }

    public void progress() {
        if (!this.player.isDead() && this.player.isOnline()) {
            long chargeTime = System.currentTimeMillis() - this.getStartTime();
            if (chargeTime < 3500L) {
                if (this.max_height <= 1.2) {
                    this.max_height += 0.02;
                }

                double max_radius = (double)2.5F;
                int lines = 6;
                double height_increasement = 0.2;

                for(int l = 0; l < lines; ++l) {
                    for(double y = (double)0.0F; y < this.max_height; y += height_increasement) {
                        double radius_increasement = max_radius / this.max_height * 0.1 * y;
                        double radius = y * radius_increasement + 0.3;
                        double x = Math.cos(Math.toRadians((double)(360 / lines * l) + y * (double)10.0F - this.cycloneAngle)) * radius;
                        double z = Math.sin(Math.toRadians((double)(360 / lines * l) + y * (double)10.0F - this.cycloneAngle)) * radius;
                        Location loc = this.player.getLocation();
                        loc.add(x, y, z);
                        if (Math.random() < (double)0.5F) {
                            ((World)Objects.requireNonNull(loc.getWorld())).spawnParticle(Particle.DUST_COLOR_TRANSITION, loc, 3, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, new Particle.DustTransition(Color.fromRGB(201, 197, 135), Color.fromRGB(201, 197, 135), 1.0F));
                        }
                    }
                }
            } else {
                double max_radius = (double)2.5F;
                int lines = 6;
                double height_increasement = 0.2;

                for(int l = 0; l < lines; ++l) {
                    for(double y = (double)0.0F; y < (double)2.5F; y += height_increasement) {
                        double radius_increasement = max_radius / this.max_height * 0.1 * y;
                        double radius = y * radius_increasement + 0.3;
                        double x = Math.cos(Math.toRadians((double)(360 / lines * l) + y * (double)10.0F - this.cycloneAngle)) * radius;
                        double z = Math.sin(Math.toRadians((double)(360 / lines * l) + y * (double)10.0F - this.cycloneAngle)) * radius;
                        if (!this.playerLocated) {
                            this.getPlayerLocation();
                        } else {
                            for(Location cyclone : this.cyclones) {
                                if (Math.random() < (double)0.5F) {
                                    ((World)Objects.requireNonNull(this.cyclone1.getWorld())).spawnParticle(Particle.DUST_COLOR_TRANSITION, cyclone.getX() + x, cyclone.getY() + y, cyclone.getZ() + z, 1, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, new Particle.DustTransition(Color.fromRGB(191, 188, 142), Color.fromRGB(191, 188, 142), 1.0F));
                                }
                            }
                        }
                    }

                    ++this.cycloneAngle;
                    Double directionX = this.direction.normalize().multiply(0.2).getX();
                    Double directionZ = this.direction.normalize().multiply(0.2).getZ();
                    Double directionX1 = this.backDirection.normalize().multiply(0.2).getX();
                    Double directionZ1 = this.backDirection.normalize().multiply(0.2).getZ();
                    Double directionX2 = this.leftDirection.normalize().multiply(0.2).getX();
                    Double directionZ2 = this.leftDirection.normalize().multiply(0.2).getZ();
                    Double directionX3 = this.rightDirection.normalize().multiply(0.2).getX();
                    Double directionZ3 = this.rightDirection.normalize().multiply(0.2).getZ();
                    this.cyclone1.add(directionX, (double)0.0F, directionZ);
                    this.cyclone2.add(directionX1, (double)0.0F, directionZ1);
                    this.cyclone3.add(directionX2, (double)0.0F, directionZ2);
                    this.cyclone4.add(directionX3, (double)0.0F, directionZ3);
                    int result = this.r.nextInt(this.high - this.low) + 10;

                    for(Location cycloneLoc : Arrays.asList(this.cyclone1, this.cyclone2, this.cyclone3, this.cyclone4)) {
                        for(Entity e : GeneralMethods.getEntitiesAroundPoint(cycloneLoc, this.hitRadius)) {
                            if (e instanceof LivingEntity && e.getUniqueId() != this.player.getUniqueId()) {
                                DamageHandler.damageEntity(e, (double)this.damage, this);
                                ((World)Objects.requireNonNull(this.cyclone1.getWorld())).spawnParticle(Particle.DUST_COLOR_TRANSITION, e.getLocation(), 1, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, new Particle.DustTransition(Color.fromRGB(191, 188, 142), Color.fromRGB(191, 188, 142), 1.0F));
                                if (!((LivingEntity)e).hasPotionEffect(PotionEffectType.BLINDNESS)) {
                                    ((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, this.duration / 200, 1, false, false));
                                }
                            }
                        }

                        if (!this.isTransparent(cycloneLoc.getBlock())) {
                            this.rotateAroundAxisY(this.direction, (double)180.0F);
                            this.rotateAroundAxisY(this.backDirection, (double)180.0F);
                            this.rotateAroundAxisY(this.leftDirection, (double)180.0F);
                            this.rotateAroundAxisY(this.rightDirection, (double)180.0F);
                        } else if (Math.random() < (double)0.5F && Math.random() < 0.2) {
                            this.rotateAroundAxisY(this.direction, (double)result);
                            this.rotateAroundAxisY(this.backDirection, (double)result);
                            this.rotateAroundAxisY(this.leftDirection, (double)result);
                            this.rotateAroundAxisY(this.rightDirection, (double)result);
                        }

                        if (cycloneLoc != null && cycloneLoc.distanceSquared(this.origin) > this.range * this.range) {
                            this.bPlayer.addCooldown(this);
                            this.remove();
                            return;
                        }
                    }

                    if (this.getStartTime() + (long)this.duration <= System.currentTimeMillis()) {
                        this.bPlayer.addCooldown(this);
                        this.remove();
                    }
                }
            }

        } else {
            this.remove();
        }
    }

    public void getPlayerLocation() {
        this.cyclone1 = this.player.getLocation();
        this.cyclone2 = this.player.getLocation();
        this.cyclone3 = this.player.getLocation();
        this.cyclone4 = this.player.getLocation();
        this.cyclones.add(this.cyclone1);
        this.cyclones.add(this.cyclone2);
        this.cyclones.add(this.cyclone3);
        this.cyclones.add(this.cyclone4);
        this.playerLocated = true;
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

    private Vector rotateAroundAxisY(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    public String getName() {
        return "DustDevils";
    }

    public Location getLocation() {
        return null;
    }

    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new DustDevilsListener(), ProjectKorra.plugin);
        ProjectKorra.log.info(this.getName() + " " + this.getVersion() + " by " + this.getAuthor() + " loaded! ");
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.DustDevils.Cooldown", 18000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.DustDevils.Duration", 10000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.DustDevils.Damage", (double)1.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.DustDevils.Range", (double)15.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.DustDevils.HitRadius", (double)1.5F);
        ConfigManager.defaultConfig.save();
    }

    public void stop() {
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getDescription() {
        return "An advanced sandbending move, gather dust, sand, rocks, and pebbles with your airbending, sending off dustdevils!!";
    }

    public String getInstructions() {
        return "To use DustDevils, shift (sneak), after a short charge time, the DustDevils will begin to form, and move. Note: there must be some sort of sand, dirt, stone, or grass around you!!!";
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.DustDevils.Enabled", true);
    }

    public String getVersion() {
        return "1.2";
    }
}
