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

/**
 * [VillagerTrade]向けのビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
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

    /**
     * 要求される一つ目のアイテム
     */
    var wants: TradeCost by HTDelegates.onceInitialize()

    /**
     * 要求される二つ目のアイテム
     */
    var additionalWants: TradeCost? = null

    /**
     * 提供されるアイテム
     */
    @PublishedApi internal var gives: ItemStackTemplate by HTDelegates.onceInitialize()

    /**
     * 取引可能な回数
     */
    var maxUses: Int = 4

    /**
     * 割引の倍率
     */
    var discount: Float = 0f

    /**
     * 取引でもらえる経験値量
     */
    var xp: Int = 1

    /**
     * 村人の条件
     */
    var merchantPredicate: LootItemCondition? = null

    /**
     * 提供品に適応される関数の一覧
     */
    val itemModifiers: MutableList<LootItemFunction> = mutableListOf()

    /**
     * 値段が二倍になる[Enchantment]の一覧
     */
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

    inline fun doubleTradePriceEnchantments(builderAction: HolderAcceptor.SetBuilder<Enchantment>.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        doubleTradePriceEnchantments = HolderAcceptor.SetBuilder<Enchantment>().apply(builderAction).build()
    }

    fun build(): VillagerTrade = VillagerTrade(wants, Optional.ofNullable(additionalWants), gives, maxUses, xp, discount, Optional.ofNullable(merchantPredicate), itemModifiers, Optional.ofNullable(doubleTradePriceEnchantments))
}
