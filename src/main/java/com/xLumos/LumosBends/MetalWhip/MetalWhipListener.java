package com.xLumos.LumosBends.MetalWhip;

import com.projectkorra.projectkorra.BendingPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;

public class MetalWhipListener implements Listener {
    @EventHandler
    public void onLeftClick(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        BendingPlayer bendingPlayer = BendingPlayer.getBendingPlayer(player);
        if (!event.isCancelled() && bendingPlayer != null) {
            if (bendingPlayer.getBoundAbilityName().equalsIgnoreCase("metalwhip")) {
                new MetalWhip(player);
            }

        }
    }
}
