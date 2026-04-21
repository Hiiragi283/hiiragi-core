package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.HTComparators
import hiiragi283.core.api.data.buildDataPredicate
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.core.component.DataComponentPredicate
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

/**
 * [Ingredient]を作成するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.15.2
 */
data object HTItemIngredientCreator {
    // Item
    fun create(item: ItemLike): Ingredient = Ingredient.of(item)

    @JvmName("createItems")
    fun create(items: Iterable<ItemLike>): Ingredient = items.map(::ItemStack).map(Ingredient::ItemValue).let(::create)

    // Tag
    fun create(prefix: HTTagPrefix, material: HTMaterialLike): Ingredient = create(prefix.itemTagKey(material))

    @JvmName("createItem")
    fun create(tagKey: TagKey<Item>): Ingredient = Ingredient.of(tagKey)

    @JvmName("createItem")
    fun create(tagKeys: Iterable<TagKey<Item>>): Ingredient =
        tagKeys.sortedWith(HTComparators.TAG_KEY).map(Ingredient::TagValue).let(::create)

    // Ingredient
    @JvmName("createValues")
    fun create(values: Iterable<Ingredient.Value>): Ingredient = Ingredient.fromValues(values.toSet().stream())

    inline fun create(strict: Boolean, vararg items: ItemLike, builderAction: DataComponentPredicate.Builder.() -> Unit): Ingredient =
        DataComponentIngredient.of(strict, buildDataPredicate(builderAction), *items)
}
