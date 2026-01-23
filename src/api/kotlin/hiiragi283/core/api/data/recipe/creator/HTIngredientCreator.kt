package hiiragi283.core.api.data.recipe.creator

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.buildDataPredicate
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.core.component.DataComponentPredicate
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.CompoundFluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

/**
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
data object HTIngredientCreator {
    //    Item    //

    // Item
    fun create(item: ItemLike, amount: Int = 1): HTItemIngredient = create(Ingredient.of(item), amount)

    @JvmName("createItems")
    fun create(items: Iterable<ItemLike>, amount: Int = 1): HTItemIngredient =
        create(items.map(::ItemStack).map(Ingredient::ItemValue), amount)

    // Tag
    fun create(prefix: HTTagPrefix, material: HTMaterialLike, amount: Int = 1): HTItemIngredient =
        create(prefix.itemTagKey(material), amount)

    @JvmName("createItem")
    fun create(tagKey: TagKey<Item>, amount: Int = 1): HTItemIngredient = create(Ingredient.of(tagKey), amount)

    fun create(prefixes: Iterable<HTTagPrefix>, materials: Iterable<HTMaterialLike>, amount: Int = 1): HTItemIngredient =
        create(prefixes.flatMap { prefix: HTTagPrefix -> materials.map(prefix::itemTagKey) }, amount)

    @JvmName("createItem")
    fun create(tagKeys: Iterable<TagKey<Item>>, amount: Int = 1): HTItemIngredient = create(tagKeys.map(Ingredient::TagValue), amount)

    // Ingredient
    @JvmName("createValues")
    fun create(values: Iterable<Ingredient.Value>, amount: Int = 1): HTItemIngredient =
        create(Ingredient.fromValues(values.toList().stream()), amount)

    inline fun create(
        strict: Boolean,
        vararg items: ItemLike,
        amount: Int = 1,
        builderAction: DataComponentPredicate.Builder.() -> Unit,
    ): HTItemIngredient = create(DataComponentIngredient.of(strict, buildDataPredicate(builderAction), *items), amount)

    fun create(ingredient: Ingredient, amount: Int = 1): HTItemIngredient = create(SizedIngredient(ingredient, amount))

    fun create(ingredient: SizedIngredient): HTItemIngredient = HTItemIngredient(ingredient)

    //    Fluid    //

    // Fluid
    fun create(fluid: Fluid, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidIngredient = create(FluidIngredient.of(fluid), amount)

    // Tag
    @JvmName("createFluid")
    fun create(tagKey: TagKey<Fluid>, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidIngredient =
        create(FluidIngredient.tag(tagKey), amount)

    @JvmName("createFluid")
    fun create(tagKeys: Iterable<TagKey<Fluid>>, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidIngredient =
        create(CompoundFluidIngredient.of(tagKeys.map(FluidIngredient::tag)), amount)

    fun create(content: HTFluidContent<*, *, *>, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidIngredient =
        create(content.fluidTag, amount)

    fun water(amount: Int): HTFluidIngredient = create(VanillaFluidContents.WATER, amount)

    fun lava(amount: Int): HTFluidIngredient = create(VanillaFluidContents.LAVA, amount)

    fun milk(amount: Int): HTFluidIngredient = create(VanillaFluidContents.MILK, amount)

    // Ingredient
    fun create(ingredient: FluidIngredient, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidIngredient =
        create(SizedFluidIngredient(ingredient, amount))

    fun create(ingredient: SizedFluidIngredient): HTFluidIngredient = HTFluidIngredient(ingredient)
}
