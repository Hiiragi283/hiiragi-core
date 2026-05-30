package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.CompoundIngredient
import net.neoforged.neoforge.common.crafting.SizedIngredient

class HTItemIngredientCreator(getter: HolderGetter<Item>) : HTSizedIngredientCreatorImpl<Item, HTItemIngredient>(getter) {
    fun item(item: ItemLike, size: Int = getDefaultSize()): HTItemIngredient = create(item.asItem(), size)

    fun items(items: Collection<ItemLike>, size: Int = getDefaultSize()): HTItemIngredient = create(items.map(ItemLike::asItem), size)

    fun create(ingredient: Ingredient, size: Int = getDefaultSize()): HTItemIngredient = HTItemIngredient(ingredient, size)

    fun create(ingredient: SizedIngredient): HTItemIngredient = HTItemIngredient(ingredient)

    //    HTSizedIngredientCreator    //

    override fun getDefaultSize(): Int = 1

    override fun holderSet(holderSet: HolderSet<Item>, size: Int): HTItemIngredient = create(Ingredient.of(holderSet), size)

    override fun holderSets(holderSets: Collection<HolderSet<Item>>, size: Int): HTItemIngredient = when (holderSets.size) {
        1 -> holderSet(holderSets.first(), size)
        else -> create(CompoundIngredient(holderSets.map(Ingredient::of)).toVanilla(), size)
    }

    @Suppress("DEPRECATION")
    override fun getHolder(type: Item): Holder<Item> = type.builtInRegistryHolder()
}
