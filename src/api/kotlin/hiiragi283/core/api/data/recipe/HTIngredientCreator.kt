package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.buildDataPredicate
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.property.getDefaultFluidAmount
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.fluid.CommonFluidTagPrefixes
import hiiragi283.core.api.tag.fluid.HTFluidTagPrefix
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
import java.util.function.IntUnaryOperator

/**
 * [HTItemIngredient]と[HTFluidIngredient]を作成するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
data object HTIngredientCreator {
    /**
     * @since 0.9.0
     */
    @JvmStatic
    private val TAG_COMPARATOR: Comparator<TagKey<*>> = compareBy(TagKey<*>::location)

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

    @JvmName("createItem")
    fun create(tagKeys: Iterable<TagKey<Item>>, amount: Int = 1): HTItemIngredient =
        create(tagKeys.sortedWith(TAG_COMPARATOR).map(Ingredient::TagValue), amount)

    // Ingredient
    @JvmName("createValues")
    fun create(values: Iterable<Ingredient.Value>, amount: Int = 1): HTItemIngredient =
        create(Ingredient.fromValues(values.toList().stream()), amount)

    @HTBuilderMarker
    inline fun create(
        strict: Boolean,
        vararg items: ItemLike,
        amount: Int = 1,
        builderAction: DataComponentPredicate.Builder.() -> Unit,
    ): HTItemIngredient = create(DataComponentIngredient.of(strict, buildDataPredicate(builderAction), *items), amount)

    fun create(ingredient: SizedIngredient): HTItemIngredient = create(ingredient.ingredient(), ingredient.count())

    fun create(ingredient: Ingredient, amount: Int = 1): HTItemIngredient = HTItemIngredient(ingredient, amount)

    //    Fluid    //

    // Fluid
    fun create(fluid: Fluid, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidIngredient = create(FluidIngredient.of(fluid), amount)

    // Tag
    @JvmName("createFluid")
    fun create(tagKey: TagKey<Fluid>, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidIngredient =
        create(FluidIngredient.tag(tagKey), amount)

    @JvmName("createFluid")
    fun create(tagKeys: Iterable<TagKey<Fluid>>, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidIngredient =
        create(CompoundFluidIngredient.of(tagKeys.sortedWith(TAG_COMPARATOR).map(FluidIngredient::tag)), amount)

    fun create(content: HTFluidContent, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidIngredient = create(content.fluidTag, amount)

    fun water(amount: Int): HTFluidIngredient = create(VanillaFluidContents.WATER, amount)

    fun lava(amount: Int): HTFluidIngredient = create(VanillaFluidContents.LAVA, amount)

    fun milk(amount: Int): HTFluidIngredient = create(VanillaFluidContents.MILK, amount)

    fun molten(material: HTMaterialLike, operator: IntUnaryOperator = IntUnaryOperator.identity()): HTFluidIngredient =
        create(CommonFluidTagPrefixes.MOLTEN, material, operator)

    fun create(
        prefix: HTFluidTagPrefix,
        material: HTMaterialLike,
        operator: IntUnaryOperator = IntUnaryOperator.identity(),
    ): HTFluidIngredient {
        val amount: Int = HiiragiCoreAccess.INSTANCE
            .materialManager
            .getOrEmpty(material)
            .getDefaultFluidAmount()
            .let(operator::applyAsInt)
        return create(prefix.createTagKey(material), amount)
    }

    // Ingredient
    fun create(ingredient: SizedFluidIngredient): HTFluidIngredient = HTFluidIngredient(ingredient.ingredient(), ingredient.amount())

    fun create(ingredient: FluidIngredient, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidIngredient =
        HTFluidIngredient(ingredient, amount)
}
