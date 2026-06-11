@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data

import hiiragi283.lib.item.ItemInstanceBuilder
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.HolderSet
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.trading.TradeCost
import net.minecraft.world.item.trading.VillagerTrade
import net.minecraft.world.level.storage.loot.functions.LootItemFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition

@HTBuilderMarker
class HTVillagerTradeBuilder {
    companion object {
        @JvmStatic
        inline fun build(builderAction: HTVillagerTradeBuilder.() -> Unit): VillagerTrade {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTVillagerTradeBuilder().apply(builderAction).build()
        }
    }

    var wants: TradeCost by HTDelegates.onceInitialize()
    var additionalWants: TradeCost? = null
    @PublishedApi internal var gives: ItemStackTemplate by HTDelegates.onceInitialize()
    var maxUses: Int = 4
    var discount: Float = 0f
    var xp: Int = 1
    var merchantPredicate: LootItemCondition? = null
    val itemModifiers: MutableList<LootItemFunction> = mutableListOf()
    @PublishedApi internal var doubleTradePriceEnchantments: HolderSet<Enchantment>? = null

    operator fun ItemStackTemplate.unaryPlus() {
        gives = this
    }

    operator fun HolderSet<Enchantment>.unaryPlus() {
        doubleTradePriceEnchantments = this
    }

    inline fun gives(builderAction: ItemInstanceBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        gives = ItemInstanceBuilder.buildTemplate(builderAction)
    }

    inline fun doubleTradePriceEnchantments(builderAction: HolderAccepter.SetBuilder<Enchantment>.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        doubleTradePriceEnchantments = HolderAccepter.SetBuilder<Enchantment>().apply(builderAction).build()
    }

    fun build(): VillagerTrade = VillagerTrade(wants, Optional.ofNullable(additionalWants), gives, maxUses, xp, discount, Optional.ofNullable(merchantPredicate), itemModifiers, Optional.ofNullable(doubleTradePriceEnchantments))
}
