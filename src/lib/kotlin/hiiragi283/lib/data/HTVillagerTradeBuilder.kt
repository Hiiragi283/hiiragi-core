package hiiragi283.lib.data

import hiiragi283.lib.util.HTDelegates
import java.util.Optional
import net.minecraft.core.HolderSet
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.trading.TradeCost
import net.minecraft.world.item.trading.VillagerTrade
import net.minecraft.world.level.storage.loot.functions.LootItemFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition

class HTVillagerTradeBuilder {
    companion object {
        @JvmStatic
        inline fun build(builderAction: HTVillagerTradeBuilder.() -> Unit): VillagerTrade = HTVillagerTradeBuilder().apply(builderAction).build()
    }

    var wants: TradeCost by HTDelegates.onceInitialize()
    var additionalWants: TradeCost? = null
    var gives: ItemStackTemplate by HTDelegates.onceInitialize()
    var maxUses: Int = 4
    var discount: Float = 0f
    var xp: Int = 1
    var merchantPredicate: LootItemCondition? = null
    val itemModifiers: MutableList<LootItemFunction> = mutableListOf()
    var doubleTradePriceEnchantments: HolderSet<Enchantment>? = null

    fun build(): VillagerTrade = VillagerTrade(wants, Optional.ofNullable(additionalWants), gives, maxUses, xp, discount, Optional.ofNullable(merchantPredicate), itemModifiers, Optional.ofNullable(doubleTradePriceEnchantments))
}
