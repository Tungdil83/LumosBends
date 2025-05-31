package com.xLumos.LumosBends.FrostTrap;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.Element.SubElement;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.IceAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.BlockSource;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.TempBlock;
import com.projectkorra.projectkorra.waterbending.util.WaterReturn;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FrostTrap extends IceAbility implements AddonAbility {
    private List<Block> ice;
    private List<Block> packed;
    private List<Block> blue;
    @Attribute("Cooldown")
    private long cooldown;
    private long revertTime;
    private int duration;
    private double hitRadius;
    @Attribute("Damage")
    private double damage;
    private Location origin;
    private Location location;
    private Block sourceBlock;

    public FrostTrap(Player player) {
        super(player);
        if (this.bPlayer.canBend(this) && !this.bPlayer.isOnCooldown(this)) {
            this.origin = player.getLocation();
            this.setFields();
        }
    }

    public void play(Material mat) {
        if (mat == null || isAir(mat)) {
            ;
        }
    }

    public void setFields() {
        this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.FrostTrap.Cooldown");
        this.duration = ConfigManager.getConfig().getInt("ExtraAbilities.xLumos.FrostTrap.Duration");
        this.hitRadius = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.FrostTrap.HitRadius");
        this.damage = ConfigManager.getConfig().getDouble("ExtraAbilities.xLumos.FrostTrap.Damage");
        this.revertTime = ConfigManager.getConfig().getLong("ExtraAbilities.xLumos.FrostTrap.RevertTime");
        this.ice = new ArrayList();
        this.packed = new ArrayList();
        this.blue = new ArrayList();
        if (!WaterReturn.hasWaterBottle(this.player)) {
            this.sourceBlock = BlockSource.getWaterSourceBlock(this.player, (double)12.0F, ClickType.LEFT_CLICK, true, true, true, true, true);
        } else {
            this.sourceBlock = this.player.getEyeLocation().clone().getBlock();
        }

        if (this.sourceBlock != null) {
            this.location = this.sourceBlock.getLocation();
            this.start();
            this.illustrate();
        }
    }

    private void illustrate() {
        Block base = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).getBlock();
        Block base1 = this.origin.clone().subtract((double)1.0F, (double)1.0F, (double)0.0F).getBlock();
        Block base2 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)1.0F, (double)0.0F, (double)0.0F).getBlock();
        Block base3 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)1.0F).getBlock();
        Block base4 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)0.0F, (double)0.0F, (double)1.0F).getBlock();
        Block base5 = this.origin.clone().subtract((double)2.0F, (double)1.0F, (double)0.0F).getBlock();
        Block base6 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)2.0F, (double)0.0F, (double)0.0F).getBlock();
        Block base7 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)2.0F).getBlock();
        Block base8 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)0.0F, (double)0.0F, (double)2.0F).getBlock();
        Block base37 = this.origin.clone().subtract((double)2.0F, (double)1.0F, (double)2.0F).getBlock();
        Block base38 = this.origin.clone().subtract((double)2.0F, (double)1.0F, (double)0.0F).add((double)0.0F, (double)0.0F, (double)2.0F).getBlock();
        Block base39 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)2.0F).add((double)2.0F, (double)0.0F, (double)0.0F).getBlock();
        Block base40 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)2.0F, (double)0.0F, (double)2.0F).getBlock();
        Block base41 = this.origin.clone().subtract((double)2.0F, (double)1.0F, (double)2.0F).getBlock();
        Block base42 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)2.0F).add((double)2.0F, (double)0.0F, (double)0.0F).getBlock();
        Block base43 = this.origin.clone().subtract((double)2.0F, (double)1.0F, (double)0.0F).add((double)0.0F, (double)0.0F, (double)2.0F).getBlock();
        Block base44 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)2.0F, (double)0.0F, (double)2.0F).getBlock();
        Block base9 = this.origin.clone().subtract((double)1.0F, (double)1.0F, (double)1.0F).getBlock();
        Block base10 = this.origin.clone().subtract((double)1.0F, (double)1.0F, (double)0.0F).add((double)0.0F, (double)0.0F, (double)1.0F).getBlock();
        Block base11 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)1.0F, (double)0.0F, (double)1.0F).getBlock();
        Block base12 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)1.0F).add((double)1.0F, (double)0.0F, (double)0.0F).getBlock();
        Block base13 = this.origin.clone().subtract((double)3.0F, (double)1.0F, (double)0.0F).getBlock();
        Block base14 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)3.0F, (double)0.0F, (double)0.0F).getBlock();
        Block base15 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)3.0F).getBlock();
        Block base16 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)0.0F, (double)0.0F, (double)3.0F).getBlock();
        Block base17 = this.origin.clone().subtract((double)3.0F, (double)1.0F, (double)1.0F).getBlock();
        Block base18 = this.origin.clone().subtract((double)3.0F, (double)1.0F, (double)0.0F).add((double)0.0F, (double)0.0F, (double)1.0F).getBlock();
        Block base19 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)1.0F).add((double)3.0F, (double)0.0F, (double)0.0F).getBlock();
        Block base20 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)3.0F, (double)0.0F, (double)1.0F).getBlock();
        Block base21 = this.origin.clone().subtract((double)1.0F, (double)1.0F, (double)3.0F).getBlock();
        Block base22 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)3.0F).add((double)1.0F, (double)0.0F, (double)0.0F).getBlock();
        Block base23 = this.origin.clone().subtract((double)1.0F, (double)1.0F, (double)0.0F).add((double)0.0F, (double)0.0F, (double)3.0F).getBlock();
        Block base24 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)1.0F, (double)0.0F, (double)3.0F).getBlock();
        Block base45 = this.origin.clone().subtract((double)3.0F, (double)1.0F, (double)3.0F).getBlock();
        Block base46 = this.origin.clone().subtract((double)3.0F, (double)1.0F, (double)0.0F).add((double)0.0F, (double)0.0F, (double)3.0F).getBlock();
        Block base47 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)3.0F).add((double)3.0F, (double)0.0F, (double)0.0F).getBlock();
        Block base48 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)3.0F, (double)0.0F, (double)3.0F).getBlock();
        Block base49 = this.origin.clone().subtract((double)3.0F, (double)1.0F, (double)3.0F).getBlock();
        Block base50 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)3.0F).add((double)3.0F, (double)0.0F, (double)0.0F).getBlock();
        Block base51 = this.origin.clone().subtract((double)3.0F, (double)1.0F, (double)0.0F).add((double)0.0F, (double)0.0F, (double)3.0F).getBlock();
        Block base52 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)3.0F, (double)0.0F, (double)3.0F).getBlock();
        Block base25 = this.origin.clone().subtract((double)4.0F, (double)1.0F, (double)0.0F).getBlock();
        Block base26 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)4.0F, (double)0.0F, (double)0.0F).getBlock();
        Block base27 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)4.0F).getBlock();
        Block base28 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)0.0F, (double)0.0F, (double)4.0F).getBlock();
        Block base61 = this.origin.clone().subtract((double)4.0F, (double)1.0F, (double)4.0F).getBlock();
        Block base62 = this.origin.clone().subtract((double)4.0F, (double)1.0F, (double)0.0F).add((double)0.0F, (double)0.0F, (double)4.0F).getBlock();
        Block base63 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)4.0F).add((double)4.0F, (double)0.0F, (double)0.0F).getBlock();
        Block base64 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)4.0F, (double)0.0F, (double)4.0F).getBlock();
        Block base65 = this.origin.clone().subtract((double)4.0F, (double)1.0F, (double)4.0F).getBlock();
        Block base66 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)4.0F).add((double)4.0F, (double)0.0F, (double)0.0F).getBlock();
        Block base67 = this.origin.clone().subtract((double)4.0F, (double)1.0F, (double)0.0F).add((double)0.0F, (double)0.0F, (double)4.0F).getBlock();
        Block base68 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)4.0F, (double)0.0F, (double)4.0F).getBlock();
        Block base53 = this.origin.clone().subtract((double)5.0F, (double)1.0F, (double)0.0F).getBlock();
        Block base54 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)5.0F, (double)0.0F, (double)0.0F).getBlock();
        Block base55 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)5.0F).getBlock();
        Block base56 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)0.0F, (double)0.0F, (double)5.0F).getBlock();
        Block base29 = this.origin.clone().subtract((double)5.0F, (double)1.0F, (double)1.0F).getBlock();
        Block base30 = this.origin.clone().subtract((double)5.0F, (double)1.0F, (double)0.0F).add((double)0.0F, (double)0.0F, (double)1.0F).getBlock();
        Block base31 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)1.0F).add((double)5.0F, (double)0.0F, (double)0.0F).getBlock();
        Block base32 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)5.0F, (double)0.0F, (double)1.0F).getBlock();
        Block base33 = this.origin.clone().subtract((double)1.0F, (double)1.0F, (double)5.0F).getBlock();
        Block base34 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)5.0F).add((double)1.0F, (double)0.0F, (double)0.0F).getBlock();
        Block base35 = this.origin.clone().subtract((double)1.0F, (double)1.0F, (double)0.0F).add((double)0.0F, (double)0.0F, (double)5.0F).getBlock();
        Block base36 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)1.0F, (double)0.0F, (double)5.0F).getBlock();
        Block base57 = this.origin.clone().subtract((double)6.0F, (double)1.0F, (double)0.0F).getBlock();
        Block base58 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)6.0F, (double)0.0F, (double)0.0F).getBlock();
        Block base59 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)6.0F).getBlock();
        Block base60 = this.origin.clone().subtract((double)0.0F, (double)1.0F, (double)0.0F).add((double)0.0F, (double)0.0F, (double)6.0F).getBlock();
        Block[] iceBlocks = new Block[]{base37, base38, base39, base40, base41, base42, base43, base44, base13, base14, base15, base16, base17, base18, base19, base20, base21, base22, base23, base24};
        Block[] packedIce = new Block[]{base1, base2, base3, base4, base5, base6, base7, base8, base25, base26, base27, base28, base53, base54, base55, base56, base29, base30, base31, base32, base33, base34, base35, base36, base57, base58, base59, base60};
        Block[] blueIce = new Block[]{base, base9, base10, base11, base12, base45, base46, base47, base48, base49, base50, base51, base52, base61, base62, base63, base64, base65, base66, base67, base68, base55};

        for(Block iceB : iceBlocks) {
            this.ice.add(iceB);
        }

        for(Block packedB : packedIce) {
            this.packed.add(packedB);
        }

        for(Block blueB : blueIce) {
            this.blue.add(blueB);
        }

        this.iterateBlocks(this.ice, Material.ICE.createBlockData());
        this.iterateBlocks(this.packed, Material.PACKED_ICE.createBlockData());
        this.iterateBlocks(this.blue, Material.BLUE_ICE.createBlockData());
    }

    private void iterateBlocks(List<Block> tempList, BlockData data) {
        ListIterator<Block> itr = tempList.listIterator();

        while(itr.hasNext()) {
            Block block = (Block)itr.next();
            Block top = GeneralMethods.getTopBlock(block.getLocation(), 10);
            if (this.isTransparent(top)) {
                new TempBlock(top.getLocation().add((double)0.0F, (double)-1.0F, (double)0.0F).getBlock(), data, 10500L);
            } else {
                new TempBlock(top, data, this.revertTime);
            }
        }

    }

    public void progress() {
        if (!this.player.isDead() && this.player.isOnline()) {
            double range = (double)15.0F;
            if (this.sourceBlock.getLocation().distance(this.origin) > range) {
                this.remove();
            } else {
                this.bPlayer.addCooldown(this);
                Location feet = this.player.getLocation();
                this.player.getWorld().spawnParticle(Particle.SNOWFLAKE, feet, 2, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F);
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

                    for(Entity e : GeneralMethods.getEntitiesAroundPoint(this.origin, this.hitRadius)) {
                        if (e instanceof LivingEntity && e.getUniqueId() != this.player.getUniqueId()) {
                            DamageHandler.damageEntity(e, this.damage, this);
                            this.player.getWorld().spawnParticle(Particle.SNOWFLAKE, ((LivingEntity)e).getLocation(), 3, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F);
                            ((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, this.duration / 500, 1, false, false));
                        }
                    }

                    if (this.getStartTime() + (long)this.duration <= System.currentTimeMillis()) {
                        this.remove();
                    }

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
        return "FrostTrap";
    }

    public Location getLocation() {
        return null;
    }

    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new FrostTrapListener(), ProjectKorra.plugin);
        ProjectKorra.log.info(this.getName() + " " + this.getVersion() + " by " + this.getAuthor() + " loaded! ");
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.FrostTrap.Duration", 9000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.FrostTrap.RevertTime", 9000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.FrostTrap.Cooldown", 15000L);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.FrostTrap.HitRadius", (double)4.0F);
        ConfigManager.getConfig().addDefault("ExtraAbilities.xLumos.FrostTrap.Damage", (double)1.0F);
        ConfigManager.defaultConfig.save();
    }

    public void stop() {
    }

    public String getAuthor() {
        return "xLumos (updated by Tungdil83)";
    }

    public String getDescription() {
        return "Summon an icy trap around you, slowing and hurting players who dare venture inside!";
    }

    public String getInstructions() {
        return "With a water bottle or water source, click, then shift to activate!";
    }

    public boolean isEnabled() {
        return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.FrostTrap.Enabled", true);
    }

    public String getVersion() {
        return "1.3";
    }
}
