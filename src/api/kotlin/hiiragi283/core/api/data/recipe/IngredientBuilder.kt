package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.HTComparators
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.util.HTBuilderMarker
import hiiragi283.core.api.util.HTDelegates
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.CompoundIngredient
import net.neoforged.neoforge.common.crafting.ICustomIngredient

/**
 * [Ingredient]および[HTItemIngredient]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
@HTBuilderMarker
class IngredientBuilder {
    private var ingredient: Ingredient by HTDelegates.onceInitialize()
    var count: Int = 1

    // Ingredient
    operator fun Ingredient.unaryPlus() {
        ingredient = this
    }

    @JvmName("unaryPlusCompound")
    operator fun Iterable<Ingredient>.unaryPlus() {
        +CompoundIngredient(this.toList())
    }

    operator fun ICustomIngredient.unaryPlus() {
        +this.toVanilla()
    }

    // Item
    operator fun ItemLike.unaryPlus() {
        +Ingredient.of(this)
    }

    // Tag
    operator fun TagKey<Item>.unaryPlus() {
        +Ingredient.of(this)
    }

    @JvmName("unaryPlusTag")
    operator fun Iterable<TagKey<Item>>.unaryPlus() {
        +Ingredient.fromValues(this.sortedWith(HTComparators.TAG_KEY).map(Ingredient::TagValue).stream())
    }

    fun build(): Ingredient = ingredient

    fun buildSized(): HTItemIngredient = HTItemIngredient(build(), count)
}
