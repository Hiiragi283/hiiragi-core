package hiiragi283.core.api.data.recipe.builder

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import java.util.stream.Stream

/**
 * バニラの[Ingredient]を使用するレシピ向けのインターフェースです。
 * @param BUILDER [HTIngredientRecipeBuilder]を継承したクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTIngredientRecipeBuilder<BUILDER : HTIngredientRecipeBuilder<BUILDER>> {
    // TagKey
    /**
     * 指定した[プレフィックス][prefixes]と[素材][material]から材料を追加します。
     */
    fun addIngredient(material: HTMaterialLike, vararg prefixes: HTPrefixLike): BUILDER =
        addIngredient(prefixes.map { it.itemTagKey(material) }.map(Ingredient::TagValue).stream())

    /**
     * 指定した[プレフィックス][prefix]と[素材][materials]から材料を追加します。
     */
    fun addIngredient(prefix: HTPrefixLike, vararg materials: HTMaterialLike): BUILDER =
        addIngredient(materials.map(prefix::itemTagKey).map(Ingredient::TagValue).stream())

    /**
     * 指定した[プレフィックス][prefix]と[素材][material]から材料を追加します。
     */
    fun addIngredient(prefix: HTPrefixLike, material: HTMaterialLike): BUILDER = addIngredient(prefix.itemTagKey(material))

    /**
     * 指定した[タグ][tagKey]から材料を追加します。
     */
    fun addIngredient(tagKey: TagKey<Item>): BUILDER = addIngredient(Ingredient.of(tagKey))

    // Item

    /**
     * 指定した[アイテム][items]から材料を追加します。
     */
    fun addIngredient(vararg items: ItemLike): BUILDER = addIngredient(Ingredient.of(*items))

    /**
     * 指定した[アイテム][items]から材料を追加します。
     */
    fun addIngredient(items: Iterable<ItemLike>): BUILDER = addIngredient(items.map(::ItemStack).map(Ingredient::ItemValue).stream())

    // Ingredient

    /**
     * 指定した[values]から材料を追加します。
     */
    fun addIngredient(values: Stream<out Ingredient.Value>): BUILDER = addIngredient(Ingredient.fromValues(values))

    /**
     * 指定した[材料][ingredient]を追加します。
     */
    fun addIngredient(ingredient: Ingredient): BUILDER
}
