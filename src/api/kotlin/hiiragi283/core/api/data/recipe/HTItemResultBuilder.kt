package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTSimpleDeferredItem
import hiiragi283.core.api.util.HTBuilderMarker
import hiiragi283.core.api.util.HTDelegates
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import org.apache.commons.lang3.math.Fraction

/**
 * [HTItemResult]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
@HTBuilderMarker
class HTItemResultBuilder {
    @PublishedApi internal var entry: HTItemResult.Entry by HTDelegates.onceInitialize()
    var count: Int by HTDelegates.onceInitialize { 1 }
    var chance: Fraction = Fraction.ONE

    operator fun HTItemResult.Entry.unaryPlus() {
        entry = this
    }

    // Simple
    operator fun ResourceLocation.unaryPlus() {
        +HTItemResult.SimpleEntry(HTSimpleDeferredItem(this))
    }

    operator fun ResourceKey<Item>.unaryPlus() {
        +HTItemResult.SimpleEntry(HTSimpleDeferredItem(this))
    }

    operator fun ItemLike.unaryPlus() {
        +ItemStack(this.asItem())
    }

    operator fun ItemStack.unaryPlus() {
        +HTItemResult.SimpleEntry(this)
    }

    // Tag
    operator fun TagKey<Item>.unaryPlus() {
        +HTItemResult.TagEntry(this)
    }

    fun build(): HTItemResult = HTItemResult(entry, count)

    fun buildWithChanced(): HTChancedItemResult = build() withChance chance
}
