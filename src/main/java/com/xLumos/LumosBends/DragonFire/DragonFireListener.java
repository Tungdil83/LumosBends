package com.xLumos.LumosBends.DragonFire;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;

public class DragonFireListener implements Listener {
    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        BendingPlayer bendingPlayer = BendingPlayer.getBendingPlayer(player);
        if (bendingPlayer != null) {
            if (bendingPlayer.getBoundAbilityName().equalsIgnoreCase("DragonFire") && bendingPlayer.canBend(CoreAbility.getAbility(DragonFire.class))) {
                new DragonFire(player);
            }

        }
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.HAND) {
            Player player = event.getPlayer();
            BendingPlayer bendingplayer = BendingPlayer.getBendingPlayer(player);
            if (bendingplayer != null) {
                String df = bendingplayer.getBoundAbilityName();
                if (df.equalsIgnoreCase("DragonFire") && CoreAbility.hasAbility(player, DragonFire.class)) {
                    ((DragonFire)CoreAbility.getAbility(player, DragonFire.class)).dragonBlastedReady();
                }

            }
        }
    }
}
