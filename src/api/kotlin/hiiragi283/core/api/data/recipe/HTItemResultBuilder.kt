package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.HTBuilderMarker
import kotlin.properties.Delegates
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
    operator fun ItemStack.unaryPlus() {
        +HTItemResult.Simple(this)
    }

    operator fun ItemLike.unaryPlus() {
        +ItemStack(this.asItem())
    }

    // Tagged
    operator fun TagKey<Item>.unaryPlus() {
        +HTItemResult.Tagged(this, 1)
    }

    fun build(): HTItemResult = result

    fun buildWithChanced(): HTChancedItemResult = result withChance chance
}
