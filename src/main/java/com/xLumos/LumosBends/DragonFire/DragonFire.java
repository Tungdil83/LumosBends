package com.xLumos.LumosBends.DragonFire;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.FireAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class DragonFire extends FireAbility implements AddonAbility {
    public boolean startDragonBlast = false;
    public boolean playerLocated = false;
    @Attribute("Cooldown")
    private long cooldown;
    private long defaultCharge;
    private Location loc;
    private Location origin;
    @Attribute("Damage")
    private double damage;
    @Attribute("Range")
    private double range;
    private static final List<Color> DUSTCOLORS = Arrays.asList(Color.fromRGB(227, 77, 32), Color.fromRGB(252, 223, 106), Color.fromRGB(149, 107, 255), Color.fromRGB(146, 247, 129), Color.fromRGB(245, 193, 64));
    Random random = new Random();

    public DragonFire(Player player) {
        super(player);
        if (this.bPlayer.canBend(this)) {
            this.setFields();
            this.origin = player.getEyeLocation();
            this.start();
        }
    }

    public void setFields() {
        this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.DragonFire.Cooldown");
        this.defaultCharge = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.DragonFire.DefaultCharge");
        this.damage = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.DragonFire.Damage");
        this.range = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.DragonFire.Range");
    }

    public void progress() {
        if (!this.bPlayer.canBendIgnoreCooldowns(this)) {
            this.remove();
        } else {
            if (!this.player.equals((Object)null)) {
                if (!this.startDragonBlast) {
                    if (!this.player.isSneaking()) {
                        this.remove();
                        return;
                    }

                    long chargeTime = System.currentTimeMillis() - this.getStartTime();
                    if (chargeTime < this.defaultCharge) {
                        this.playFirebendingParticles(this.getRightHipPos(), 0, (double)0.07F, (double)0.6F, (double)0.07F);
                        this.playFirebendingParticles(this.getLeftHipPos(), 0, (double)0.07F, (double)0.6F, (double)0.07F);
                    } else {
                        this.createParticles(this.player.getWorld(), 1, 0.02F, 0.2F, 0.02F, this.getRightHipPos(), (Color)DUSTCOLORS.get(this.random.nextInt(DUSTCOLORS.size())));
                        this.createParticles(this.player.getWorld(), 1, 0.02F, 0.2F, 0.02F, this.getLeftHipPos(), (Color)DUSTCOLORS.get(this.random.nextInt(DUSTCOLORS.size())));
                    }
                } else if (!this.playerLocated) {
                    this.getPlayerLocation();
                } else {
                    Vector direction = this.player.getEyeLocation().getDirection();
                    direction.normalize().multiply(1);
                    this.loc.add(direction);
                    this.createParticles(this.loc.getWorld(), 3, (double)0.5F, 0.4, (double)0.5F, this.loc, (Color)DUSTCOLORS.get(this.random.nextInt(DUSTCOLORS.size())));

                    for(Entity e : GeneralMethods.getEntitiesAroundPoint(this.loc, (double)1.0F)) {
                        if (e instanceof LivingEntity && e.getUniqueId() != this.player.getUniqueId() && !(e instanceof ArmorStand)) {
                            DamageHandler.damageEntity(e, this.damage, this);
                            this.remove();
                            return;
                        }
                    }

                    if (this.loc.distanceSquared(this.origin) > this.range * this.range) {
                        this.remove();
                        return;
                    }
                }
            }

        }
    }

    private void createParticles(World world, int amount, float x, float y, float z, Location location, Color color) {
        ((World)Objects.requireNonNull(world)).spawnParticle(Particle.DUST_COLOR_TRANSITION, location, amount, (double)x, (double)y, (double)z, (double)0.0F, new Particle.DustTransition(color, color,1.0F));
    }

    private void createParticles(World world, int amount, double x, double y, double z, Location location, Color color) {
        ((World)Objects.requireNonNull(world)).spawnParticle(Particle.DUST_COLOR_TRANSITION, location, amount, x, y, z, (double)0.0F, new Particle.DustTransition(color, color, 1.0F));
    }

    public void getPlayerLocation() {
        this.loc = this.player.getLocation().add((double)0.0F, 1.3, (double)0.0F);
        this.playerLocated = true;
    }

    public void dragonBlastedReady() {
        long chargeTime = System.currentTimeMillis() - this.getStartTime();
        if (chargeTime > this.defaultCharge) {
            this.startDragonBlast = true;
            this.bPlayer.addCooldown(this);
        }

    }

    public Location getRightHipPos() {
        return GeneralMethods.getRightSide(this.player.getLocation().add(this.player.getLocation().getDirection().normalize().multiply(0.2)), 0.4).add((double)0.0F, 1.05, (double)0.0F);
    }

    public Location getLeftHipPos() {
        return GeneralMethods.getLeftSide(this.player.getLocation().add(this.player.getLocation().getDirection().normalize().multiply(0.2)), 0.4).add((double)0.0F, 1.05, (double)0.0F);
    }

    public boolean isSneakAbility() {
        return false;
    }

    public boolean isHarmlessAbility() {
        return false;
    }

    public long getCooldown() {
        return this.cooldown;
    }

    public String getName() {
        return "DragonFire";
    }

    public Location getLocation() {
        return null;
    }

    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new DragonFireListener(), ProjectKorra.plugin);
        ProjectKorra.log.info(this.getName() + " " + this.getVersion() + " by " + this.getAuthor() + " loaded! ");
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.DragonFire.Cooldown", 8000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.DragonFire.DefaultCharge", 3000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.DragonFire.Damage", (double)4.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.DragonFire.Range", (double)20.0F);
        ConfigManager.defaultConfig.save();
    }

    public void stop() {
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getDescription() {
        return "Fuel the inner dragon, charging first to release a powerful blast of dragon fire!";
    }

    public String getInstructions() {
        return "To use DragonFire, first hold shift (sneak) to charge the ability. Once you see the fire in your hand turn into DragonFire (it is very colorful), click where you want it to go!";
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.DragonFire.Enabled", true);
    }

    public String getVersion() {
        return "1.2";
    }
}
