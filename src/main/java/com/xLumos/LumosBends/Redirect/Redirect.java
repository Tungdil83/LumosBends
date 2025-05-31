package com.xLumos.LumosBends.Redirect;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.LightningAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Redirect extends LightningAbility implements AddonAbility {
    @Attribute("Cooldown")
    private long cooldown;
    @Attribute("Damage")
    private int damage;
    private Location origin;
    private double strikes;
    private long charge;
    private double hitRadius;
    private int duration;

    public Redirect(Player player) {
        super(player);
        if (this.bPlayer.canBend(this) && !this.bPlayer.isOnCooldown(this)) {
            this.setFields();
            this.start();
            this.bPlayer.addCooldown(this);
        }
    }

    public void setFields() {
        this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.Redirect.Cooldown");
        this.charge = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.Redirect.Charge");
        this.damage = ConfigManager.getConfig().getInt("ExtraAbilities.xLumos.Redirect.Damage");
        this.strikes = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.Redirect.Strikes");
        this.hitRadius = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.Redirect.HitRadius");
        this.duration = ConfigManager.getConfig().getInt("ExtraAbilities.xLumos.Redirect.Duration");
    }

    public void progress() {
        if (!this.player.isDead() && this.player.isOnline()) {
            this.origin = this.player.getLocation();
            playLightningbendingParticle(this.origin, (double)1.0F, (double)0.5F, (double)1.0F);
            long chargeTime = System.currentTimeMillis() - this.getStartTime();
            if (chargeTime < this.charge) {
                if (Math.random() < 0.3) {
                    World world = this.player.getWorld();
                    world.spigot().strikeLightningEffect(this.origin, true);
                }

                if (!this.player.isSneaking()) {
                    DamageHandler.damageEntity(this.player, (double)this.damage, this);
                    this.remove();
                    return;
                }
            } else {
                if (Math.random() < this.strikes) {
                    for(int i = 0; i < 3; ++i) {
                        double angle = (double)i * Math.PI / (double)180.0F;
                        double x = 0.6 * (double)i * Math.cos((double)2.0F * angle);
                        double z = 0.6 * (double)i * Math.sin((double)2.0F * angle);
                        Location loc = this.player.getLocation();
                        loc.add(x, (double)0.0F, z);
                        playLightningbendingParticle(loc, (double)2.0F, (double)1.5F, (double)3.0F);
                        playLightningbendingParticle(loc, (double)1.0F, (double)1.5F, (double)1.5F);
                        playLightningbendingParticle(loc, (double)1.0F, (double)1.5F, (double)1.5F);
                    }

                    playLightningbendingParticle(this.origin, (double)6.0F, (double)6.0F, (double)6.0F);
                    playLightningbendingParticle(this.origin, (double)5.0F, (double)5.0F, (double)5.0F);
                    playLightningbendingParticle(this.origin, (double)4.0F, (double)4.0F, (double)4.0F);
                    playLightningbendingParticle(this.origin, (double)3.0F, (double)2.0F, (double)3.0F);
                    playLightningbendingParticle(this.origin, (double)5.0F, (double)5.0F, (double)5.0F);
                }

                for(Entity e : GeneralMethods.getEntitiesAroundPoint(this.origin, this.hitRadius)) {
                    if (e instanceof LivingEntity && e.getUniqueId() != this.player.getUniqueId()) {
                        DamageHandler.damageEntity(e, (double)this.damage, this);
                    }
                }

                if (chargeTime > (long)this.duration) {
                    this.remove();
                    return;
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
        return "Redirect";
    }

    public Location getLocation() {
        return null;
    }

    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new RedirectListener(), ProjectKorra.plugin);
        ProjectKorra.log.info(this.getName() + " " + this.getVersion() + " by " + this.getAuthor() + " loaded! ");
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.Redirect.Cooldown", 14000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.Redirect.Damage", (double)1.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.Redirect.Strikes", 0.6);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.Redirect.Charge", 3000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.Redirect.HitRadius", (double)4.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.Redirect.Duration", 8000L);
        ConfigManager.defaultConfig.save();
    }

    public void stop() {
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getDescription() {
        return "An advanced lightning move, charge lightning from the sky for a powerful burst of energy! Remember what Iroh said, channel it through your stomach!";
    }

    public String getInstructions() {
        return "To use Redirect, sneak (hold shift) don't unsneak until the lightning ends, or else you will get damaged!";
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.Redirect.Enabled", true);
    }

    public String getVersion() {
        return "1.2";
    }
}
