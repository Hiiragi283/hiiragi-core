package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.result.HTChancedItemResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTBuilderMarker
import kotlin.properties.Delegates
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike
import org.apache.commons.lang3.math.Fraction

/**
 * [HTItemResult]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.1
 */
@HTBuilderMarker
class HTItemResultBuilder {
    @PublishedApi internal var result: HTItemResult by Delegates.notNull()
    var chance: Fraction = Fraction.ONE

    var count: Int
        get() = result.count
        set(value) {
            result = result.copyWithCount(value)
        }

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

    // Tagged
    operator fun TagKey<Item>.unaryPlus() {
        +HTItemResult.Tagged(this, 1)
    }

    fun build(): HTItemResult = result

    fun buildWithChanced(): HTChancedItemResult = result withChance chance
}
