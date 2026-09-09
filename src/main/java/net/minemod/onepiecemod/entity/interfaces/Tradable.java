package net.minemod.onepiecemod.entity.interfaces;

import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minemod.onepiecemod.entity.npcs.AbstractNPC;
import net.minemod.onepiecemod.entity.trade.Trade;

import java.util.List;

public interface Tradable {

    List<Trade> getTrades();

    default boolean isTradable(Player player) {
        return getTrades() != null && !getTrades().isEmpty();
    }

    default InteractionResult processTrade(AbstractNPC npc, Player player, InteractionHand hand) {
        if (isTradable(player)) {
            if (!npc.level().isClientSide()) {
                // 1. Build the offers list
                MerchantOffers offers = new MerchantOffers();
                for (Trade trade : getTrades()) {
                    offers.add(trade.toMerchantOffer());
                }

                // 2. Assign trading state to NPC
                npc.setTradingPlayer(player);
                npc.overrideOffers(offers);

                // 3. Open menu
                player.openMenu(new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return npc.getDisplayName();
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player pPlayer) {
                        return new MerchantMenu(containerId, playerInventory, npc);
                    }
                });

                // 4. Force packet sync to the client AFTER the menu is active
                if (player.containerMenu instanceof MerchantMenu merchantMenu) {
                    player.sendMerchantOffers(
                            merchantMenu.containerId,
                            npc.getOffers(),
                            1,      // Villager level icon
                            0,      // XP
                            false,  // Show progress bar
                            false   // Can restock
                    );
                }

                player.awardStat(Stats.TRADED_WITH_VILLAGER);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}