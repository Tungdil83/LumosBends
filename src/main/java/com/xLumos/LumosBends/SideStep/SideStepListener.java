package com.xLumos.LumosBends.SideStep;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class SideStepListener implements Listener {
    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        if (bPlayer != null) {
            CoreAbility coreAbil = bPlayer.getBoundAbility();
            String abil = bPlayer.getBoundAbilityName();
            if (coreAbil != null) {
                if (abil.equalsIgnoreCase("SideStep") && bPlayer.canBend(CoreAbility.getAbility(SideStep.class)) && !CoreAbility.hasAbility(player, SideStep.class)) {
                    new SideStep(player);
                }

            }
        }
    }

    @EventHandler
    public void damageEvent(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
            if (bPlayer == null) {
                return;
            }

            SideStep ss = (SideStep)CoreAbility.getAbility(player, SideStep.class);
            if (ss == null) {
                return;
            }

            if (ss.isStarted()) {
                ((SideStep)CoreAbility.getAbility(player, SideStep.class)).dodgeAttack();
                event.setCancelled(true);
                return;
            }
        }

    }
}
