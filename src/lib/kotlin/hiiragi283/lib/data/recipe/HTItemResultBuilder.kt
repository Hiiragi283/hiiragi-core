package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.result.HTChancedItemResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike
import org.apache.commons.lang3.math.Fraction

class HTItemResultBuilder {
    @PublishedApi internal var result: HTItemResult by HTDelegates.onceInitialize()
    var chance: Fraction = Fraction.ONE

    operator fun HTItemResult.unaryPlus() {
        result = this
    }

    // Simple
    operator fun ItemStackTemplate.unaryPlus() {
        +HTItemResult.Simple(this)
    }

    operator fun ItemLike.unaryPlus() {
        +ItemStackTemplate(this.asItem())
    }

    @JvmName("unaryPlusSimple")
    operator fun Pair<ItemLike, Int>.unaryPlus() {
        val (item: ItemLike, count: Int) = this
        +ItemStackTemplate(item.asItem(), count)
    }

    // Tagged
    operator fun TagKey<Item>.unaryPlus() {
        +HTItemResult.Tagged(this, 1)
    }

    @JvmName("unaryPlusTagged")
    operator fun Pair<TagKey<Item>, Int>.unaryPlus() {
        val (tagKey: TagKey<Item>, count: Int) = this
        +HTItemResult.Tagged(tagKey, count)
    }

    fun build(): HTItemResult = result

    fun buildWithChanced(): HTChancedItemResult = result withChance chance
}
