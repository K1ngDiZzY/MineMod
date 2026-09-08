package net.minemod.onepiecemod.entity.trade;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TradeBuilder {
    private final List<Trade> trades = new ArrayList<>();

    public static TradeBuilder create() {
        return new TradeBuilder();
    }

    /** Basic 1-to-1 Trade (e.g., 5 Berrys -> 1 Bread) */
    public TradeBuilder addTrade(ItemLike costItem, int costCount, ItemLike resultItem, int resultCount) {
        return addTrade(costItem, costCount, resultItem, resultCount, 12, 2, 0.05f);
    }

    /** 1-to-1 Trade with customization */
    public TradeBuilder addTrade(ItemLike costItem, int costCount, ItemLike resultItem, int resultCount, int maxUses, int xp, float priceMultiplier) {
        this.trades.add(new Trade(
                new ItemCost(costItem.asItem(), costCount),
                Optional.empty(),
                new ItemStack(resultItem.asItem(), resultCount),
                maxUses,
                xp,
                priceMultiplier
        ));
        return this;
    }

    /** 2-to-1 Trade (e.g., 5 Berrys + 1 Iron Ingot -> 1 Sword) */
    public TradeBuilder addTrade(ItemLike primaryCost, int primaryCount, ItemLike secondaryCost, int secondaryCount, ItemLike resultItem, int resultCount) {
        this.trades.add(new Trade(
                new ItemCost(primaryCost.asItem(), primaryCount),
                Optional.of(new ItemCost(secondaryCost.asItem(), secondaryCount)),
                new ItemStack(resultItem.asItem(), resultCount),
                12,
                2,
                0.05f
        ));
        return this;
    }

    /** Allows directly supplying an explicit ItemStack result (useful for NBT/Damaged items) */
    public TradeBuilder addTrade(ItemLike costItem, int costCount, ItemStack resultStack) {
        this.trades.add(new Trade(
                new ItemCost(costItem.asItem(), costCount),
                Optional.empty(),
                resultStack,
                12,
                2,
                0.05f
        ));
        return this;
    }

    public List<Trade> build() {
        return this.trades;
    }
}