package com.xLumos.LumosBends.ChestPalm;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.Ability;
import com.projectkorra.projectkorra.ability.ChiAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class ChestPalmListener implements Listener {
    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent e) {
        Entity source = e.getDamager();
        Entity entity = e.getEntity();
        if (source instanceof Player) {
            Player sourcePlayer = (Player)source;
            BendingPlayer sourceBPlayer = BendingPlayer.getBendingPlayer(sourcePlayer);
            if (sourceBPlayer == null) {
                return;
            }

            Ability boundAbil = sourceBPlayer.getBoundAbility();
            if (sourceBPlayer.canBendPassive(sourceBPlayer.getBoundAbility()) && e.getCause() == DamageCause.ENTITY_ATTACK && sourceBPlayer.getBoundAbility() instanceof ChiAbility && sourceBPlayer.canCurrentlyBendWithWeapons() && sourceBPlayer.isElementToggled(Element.CHI) && boundAbil.equals(CoreAbility.getAbility(ChestPalm.class))) {
                new ChestPalm(sourcePlayer, entity);
            }
        }

    }
}
