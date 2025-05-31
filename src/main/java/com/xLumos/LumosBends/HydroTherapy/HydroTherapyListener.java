package com.xLumos.LumosBends.HydroTherapy;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class HydroTherapyListener implements Listener {
    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        if (bPlayer != null) {
            CoreAbility coreAbil = bPlayer.getBoundAbility();
            String abil = bPlayer.getBoundAbilityName();
            if (coreAbil != null) {
                if (abil.equalsIgnoreCase("HydroTherapy") && bPlayer.canBend(CoreAbility.getAbility(HydroTherapy.class)) && !CoreAbility.hasAbility(player, HydroTherapy.class)) {
                    new HydroTherapy(player);
                }

            }
        }
    }
}