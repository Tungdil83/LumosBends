package com.xLumos.LumosBends.FrostCuffs;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.IceAbility;
import com.projectkorra.projectkorra.ability.WaterAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.BlockSource;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.DamageHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class FrostCuffs extends IceAbility implements AddonAbility {
    @Attribute("Cooldown")
    private long cooldown;
    private int duration;
    @Attribute("Damage")
    private double damage;
    @Attribute("Range")
    private double range;
    private Location origin;
    private Location location;
    private Entity target;
    private TempArmorStand cuff1;
    private TempArmorStand cuff2;
    private Block sourceBlock;
    private double sourceRange;
    private boolean sourced;
    private boolean cuffed = false;

    public FrostCuffs(Player player) {
        super(player);
        if (this.bPlayer.canBend(this) && !hasAbility(player, FrostCuffs.class)) {
            this.setFields();
            this.origin = player.getLocation().subtract((double)0.0F, 0.45, (double)0.0F);
            this.location = this.origin.clone();
            this.sourceBlock = BlockSource.getWaterSourceBlock(player, this.sourceRange, ClickType.SHIFT_DOWN, true, this.bPlayer.canIcebend(), false, true, false);
            if (this.sourceBlock != null) {
                this.start();
            }
        }
    }

    public void setFields() {
        this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.FrostCuffs.Cooldown");
        this.duration = ConfigManager.getConfig().getInt("ExtraAbilities.xLumos.FrostCuffs.Duration");
        this.damage = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.FrostCuffs.Damage");
        this.range = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.FrostCuffs.Range");
        this.sourceRange = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.FrostCuffs.SourceRange");
    }

    public void cuffSourced() {
        this.sourced = true;
        this.bPlayer.addCooldown(this);
    }

    public void progress() {
        if (!this.bPlayer.canBendIgnoreCooldowns(this)) {
            this.remove();
        } else {
            if (!this.sourced) {
                WaterAbility.playFocusWaterEffect(this.sourceBlock);
            } else {
                if (this.cuffed) {
                    if (this.getStartTime() + (long)this.duration <= System.currentTimeMillis()) {
                        this.removeCuffs();
                        this.remove();
                        return;
                    }

                    if (this.target == null || !this.target.isValid() || this.target instanceof Player && !((Player)this.target).isOnline() || !this.player.getWorld().equals(this.target.getWorld())) {
                        this.remove();
                        return;
                    }

                    this.teleportCuffs();
                } else {
                    this.advanceCuffs();
                }

                if (!this.bPlayer.canBendIgnoreCooldowns(this)) {
                    this.remove();
                    return;
                }
            }

        }
    }

    private void removeCuffs() {
        if (this.cuff1 != null) {
            this.cuff1.remove();
        }

        if (this.cuff2 != null) {
            this.cuff2.remove();
        }

        ((LivingEntity)this.target).removePotionEffect(PotionEffectType.WEAKNESS);
    }

    public boolean isCuffed() {
        return this.cuffed;
    }

    private void advanceCuffs() {
        if (!this.cuffed) {
            Vector direction = this.player.getEyeLocation().getDirection();
            direction.normalize().multiply((double)0.5F);
            this.location.add(direction);
            if (this.location.distanceSquared(this.origin) > this.range * this.range) {
                this.remove();
                return;
            }

            new TempArmorStand(this, this.location.clone(), Material.PACKED_ICE, 80L, true);

            for(Entity e : GeneralMethods.getEntitiesAroundPoint(this.location, (double)1.0F)) {
                if (e instanceof LivingEntity && e.getUniqueId() != this.player.getUniqueId() && !(e instanceof ArmorStand)) {
                    DamageHandler.damageEntity(e, this.damage, this);
                    this.target = (LivingEntity)e;
                    this.cuffed = true;
                    this.cuffEnemy();
                    return;
                }
            }
        }

    }

    private void cuffEnemy() {
        this.cuff1 = new TempArmorStand(this, this.getRightHipPos(), Material.PACKED_ICE, 4000L, true);
        this.cuff2 = new TempArmorStand(this, this.getLeftHipPos(), Material.PACKED_ICE, 4000L, true);
    }

    private void teleportCuffs() {
        this.cuff1.getArmorStand().teleport(this.getRightHipPos());
        this.cuff2.getArmorStand().teleport(this.getLeftHipPos());
        ((LivingEntity)this.target).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 10000, 1, false, false));
    }

    public Location getRightHipPos() {
        return GeneralMethods.getRightSide(this.target.getLocation(), 0.55).add((double)0.0F, 0.1, (double)0.0F);
    }

    public Location getLeftHipPos() {
        return GeneralMethods.getLeftSide(this.target.getLocation(), 0.55).add((double)0.0F, 0.1, (double)0.0F);
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
        return "FrostCuffs";
    }

    public Location getLocation() {
        return null;
    }

    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new FrostCuffsListener(), ProjectKorra.plugin);
        ProjectKorra.log.info(this.getName() + " " + this.getVersion() + " by " + this.getAuthor() + " loaded! ");
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.FrostCuffs.Cooldown", 8000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.FrostCuffs.Duration", 7000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.FrostCuffs.Damage", (double)2.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.FrostCuffs.Range", (double)15.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.FrostCuffs.SourceRange", (double)10.0F);
        ProjectKorra.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(ProjectKorra.plugin, TempArmorStand::manage, 0L, 1L);
        ConfigManager.defaultConfig.save();
    }

    public void stop() {
        TempArmorStand.removeAll();
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getDescription() {
        return "Shoot a pair of ice handcuffs at your enemy, giving them weakness!!";
    }

    public String getInstructions() {
        return "To activate, first shift at a water source, then click at your enemy!";
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.FrostCuffs.Enabled", true);
    }

    public String getVersion() {
        return "1.1";
    }
}
