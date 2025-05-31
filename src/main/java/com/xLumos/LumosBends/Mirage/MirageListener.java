package com.xLumos.LumosBends.Mirage;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class MirageListener implements Listener {
    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        if (bPlayer != null) {
            CoreAbility coreAbil = bPlayer.getBoundAbility();
            String abil = bPlayer.getBoundAbilityName();
            if (coreAbil != null) {
                if (abil.equalsIgnoreCase("Mirage") && bPlayer.canBend(CoreAbility.getAbility(Mirage.class)) && !CoreAbility.hasAbility(player, Mirage.class)) {
                    new Mirage(player);
                }

            }
        }
    }

    @EventHandler
    public void damageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
            if (bPlayer == null) {
                return;
            }

            if (CoreAbility.getAbility(player, Mirage.class) != null) {
                ((Mirage)CoreAbility.getAbility(player, Mirage.class)).removeMove();
                return;
            }
        }

    }
}
