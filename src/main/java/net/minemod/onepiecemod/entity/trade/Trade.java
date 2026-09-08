package net.minemod.onepiecemod.entity.trade;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.Optional;

public class Trade {
    private final ItemCost primaryCost;
    private final Optional<ItemCost> secondaryCost;
    private final ItemStack result;
    private final int maxUses;
    private final int xp;
    private final float priceMultiplier;

    public Trade(ItemCost primaryCost, Optional<ItemCost> secondaryCost, ItemStack result, int maxUses, int xp, float priceMultiplier) {
        this.primaryCost = primaryCost;
        this.secondaryCost = secondaryCost;
        this.result = result;
        this.maxUses = maxUses;
        this.xp = xp;
        this.priceMultiplier = priceMultiplier;
    }

    public MerchantOffer toMerchantOffer() {
        return new MerchantOffer(
                this.primaryCost,
                this.secondaryCost,
                this.result,
                0,             // current uses
                this.maxUses,  // max uses
                this.xp,       // reward xp
                this.priceMultiplier
        );
    }
}