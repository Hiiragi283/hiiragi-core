package hiiragi283.core.api.data.recipe.ingredient

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.CompoundIngredient
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
import net.neoforged.neoforge.common.crafting.DifferenceIngredient
import net.neoforged.neoforge.common.crafting.IntersectionIngredient

interface HTItemIngredientCreator : HTIngredientCreator.Registered<Item, Ingredient> {
    // ItemLike
    fun fromItem(item: ItemLike): Ingredient = from(item.asItem())

    fun fromItems(items: Collection<ItemLike>): Ingredient = from(items.map(ItemLike::asItem))

    // Material
    fun fromTagPrefix(prefix: HTTagPrefix): Ingredient = fromTagKey(prefix.createCommonTagKey(Registries.ITEM))

    fun fromMaterial(prefix: HTTagPrefix, material: HTMaterialLike): Ingredient = fromTagKeys(prefix.itemTagKeys(material))

    // HolderSet
    override fun fromHolderSet(holderSet: HolderSet<Item>): Ingredient = Ingredient.of(holderSet)

    // Custom
    override fun allOf(ingredients: List<Ingredient>): Ingredient = IntersectionIngredient(ingredients).toVanilla()

    override fun anyOf(ingredients: List<Ingredient>): Ingredient = CompoundIngredient(ingredients).toVanilla()

    override fun difference(base: Ingredient, subtracted: Ingredient): Ingredient = DifferenceIngredient(base, subtracted).toVanilla()

    fun dataComponents(holderSet: HolderSet<Item>, components: DataComponentPatch, exhaustive: Boolean): Ingredient =
        DataComponentIngredient(holderSet, components, exhaustive).toVanilla()
}
