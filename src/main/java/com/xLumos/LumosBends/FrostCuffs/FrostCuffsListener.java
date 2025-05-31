package com.xLumos.LumosBends.FrostCuffs;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;

public class FrostCuffsListener implements Listener {
    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        BendingPlayer bendingPlayer = BendingPlayer.getBendingPlayer(player);
        if (bendingPlayer != null) {
            if (bendingPlayer.getBoundAbilityName().equalsIgnoreCase("FrostCuffs") && bendingPlayer.canBend(CoreAbility.getAbility(FrostCuffs.class))) {
                new FrostCuffs(player);
            }

        }
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.HAND) {
            Player player = event.getPlayer();
            BendingPlayer bendingplayer = BendingPlayer.getBendingPlayer(player);
            if (bendingplayer != null) {
                String fc = bendingplayer.getBoundAbilityName();
                if (fc.equalsIgnoreCase("FrostCuffs") && CoreAbility.hasAbility(player, FrostCuffs.class)) {
                    ((FrostCuffs)CoreAbility.getAbility(player, FrostCuffs.class)).cuffSourced();
                }

            }
        }
    }
}
