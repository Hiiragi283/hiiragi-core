package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import java.util.stream.Stream

/**
 * バニラの[Ingredient]を使用するレシピ向けのビルダー
 * @param BUILDER [HTIngredientRecipeBuilder]を継承したクラス
 */
interface HTIngredientRecipeBuilder<BUILDER : HTIngredientRecipeBuilder<BUILDER>> {
    // TagKey
    fun addIngredient(material: HTMaterialLike, vararg prefixes: HTPrefixLike): BUILDER =
        addIngredient(prefixes.map { it.itemTagKey(material) }.map(Ingredient::TagValue).stream())

    fun addIngredient(prefix: HTPrefixLike, material: HTMaterialLike): BUILDER = addIngredient(prefix.itemTagKey(material))

    fun addIngredient(tagKey: TagKey<Item>): BUILDER = addIngredient(Ingredient.of(tagKey))

    // Item
    fun addIngredient(vararg items: ItemLike): BUILDER = addIngredient(Ingredient.of(*items))

    fun addIngredient(items: Iterable<ItemLike>): BUILDER = addIngredient(items.map(::ItemStack).map(Ingredient::ItemValue).stream())

    // Ingredient
    fun addIngredient(values: Stream<out Ingredient.Value>): BUILDER = addIngredient(Ingredient.fromValues(values))

    fun addIngredient(ingredient: Ingredient): BUILDER
}
