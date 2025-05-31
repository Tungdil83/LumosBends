package com.xLumos.LumosBends.FrostCuffs;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class TempArmorStand {
    private static final Map<ArmorStand, TempArmorStand> instances = new ConcurrentHashMap();
    private static final Queue<TempArmorStand> tasQueue = new PriorityQueue(Comparator.comparingLong(TempArmorStand::getExpirationTime));
    private final ArmorStand armorStand;
    private final CoreAbility ability;
    private final Material headMaterial;
    private final long expirationTime;
    private final boolean particles;

    public TempArmorStand(CoreAbility abilityInstance, Location location, Material material, long delay) {
        this(abilityInstance, location, material, delay, false);
    }

    public TempArmorStand(CoreAbility abilityInstance, Location location, Material material, long delay, boolean showRemoveParticles) {
        this.headMaterial = material;
        this.armorStand = (ArmorStand)location.getWorld().spawn(location, ArmorStand.class, (entity) -> {
            entity.setInvulnerable(true);
            entity.setVisible(false);
            entity.setGravity(false);
            entity.setSmall(true);
            entity.getEquipment().setHelmet(new ItemStack(this.headMaterial));
            entity.setMetadata("No_Interaction", new FixedMetadataValue(ProjectKorra.plugin, ""));
        });
        this.expirationTime = System.currentTimeMillis() + delay;
        this.ability = abilityInstance;
        instances.put(this.armorStand, this);
        tasQueue.add(this);
        this.particles = showRemoveParticles;
        this.showParticles(true);
    }

    public void showParticles(boolean show) {
        if (show) {
            ParticleEffect.BLOCK_CRACK.display(this.armorStand.getEyeLocation().add((double)0.0F, 0.2, (double)0.0F), 4, (double)0.25F, (double)0.125F, (double)0.25F, (double)0.0F, this.headMaterial.createBlockData());
            ParticleEffect.BLOCK_DUST.display(this.armorStand.getEyeLocation().add((double)0.0F, 0.2, (double)0.0F), 6, (double)0.25F, (double)0.125F, (double)0.25F, (double)0.0F, this.headMaterial.createBlockData());
        }

    }

    public CoreAbility getAbility() {
        return this.ability;
    }

    public ArmorStand getArmorStand() {
        return this.armorStand;
    }

    public long getExpirationTime() {
        return this.expirationTime;
    }

    public static boolean isTempArmorStand(ArmorStand as) {
        return instances.containsKey(as);
    }

    public static TempArmorStand get(ArmorStand as) {
        return (TempArmorStand)instances.get(as);
    }

    public static Set<TempArmorStand> getFromAbility(CoreAbility ability) {
        return (Set)instances.values().stream().filter((tas) -> tas.getAbility().equals(ability)).collect(Collectors.toSet());
    }

    public static void manage() {
        long currentTime = System.currentTimeMillis();

        while(!tasQueue.isEmpty()) {
            TempArmorStand tas = (TempArmorStand)tasQueue.peek();
            if (currentTime <= tas.getExpirationTime()) {
                return;
            }

            tasQueue.poll();
            tas.remove();
        }

    }

    public void remove() {
        instances.remove(this.armorStand);
        this.showParticles(this.particles);
        this.armorStand.remove();
    }

    public static void removeAll() {
        tasQueue.clear();
        instances.keySet().forEach(Entity::remove);
        instances.clear();
    }
}
