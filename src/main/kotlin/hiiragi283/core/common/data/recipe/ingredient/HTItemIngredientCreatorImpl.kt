package hiiragi283.core.common.data.recipe.ingredient

import hiiragi283.core.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient

internal data object HTItemIngredientCreatorImpl : HTItemIngredientCreator {
    override fun from(type: Item, amount: Int): HTItemIngredient = from(Ingredient.of(type), amount)

    override fun from(types: Collection<Item>, amount: Int): HTItemIngredient = from(Ingredient.of(*types.toTypedArray()), amount)

    override fun fromTagKey(tagKey: TagKey<Item>, amount: Int): HTItemIngredient = fromTagKeys(listOf(tagKey), amount)

    override fun fromTagKeys(tagKeys: Collection<TagKey<Item>>, amount: Int): HTItemIngredient =
        from(tagKeys.map(Ingredient::TagValue).stream().let(Ingredient::fromValues), amount)
}
