package hiiragi283.core.api.data.recipe.ingredient

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.registry.HTFluidContent
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

/**
 * [HTFluidIngredient]を返す[HTIngredientCreator]の拡張インターフェース
 * @see mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator
 */
interface HTFluidIngredientCreator : HTIngredientCreator<Fluid, HTFluidIngredient> {
    fun from(ingredient: FluidIngredient, amount: Int): HTFluidIngredient = HTFluidIngredient(ingredient, amount)

    fun fromTagKey(content: HTFluidContent<*, *, *>, amount: Int): HTFluidIngredient = fromTagKey(content.fluidTag, amount)

    fun fromTagKeys(vararg contents: HTFluidContent<*, *, *>, amount: Int): HTFluidIngredient =
        fromTagKeys(contents.map(HTFluidContent<*, *, *>::fluidTag), amount)

    fun water(amount: Int): HTFluidIngredient = fromTagKey(Tags.Fluids.WATER, amount)

    fun lava(amount: Int): HTFluidIngredient = fromTagKey(Tags.Fluids.LAVA, amount)

    fun milk(amount: Int): HTFluidIngredient = fromTagKey(Tags.Fluids.MILK, amount)

    // Material
    fun fromTagKey(prefix: HTPrefixLike, material: HTMaterialLike, count: Int = 1): HTFluidIngredient =
        fromTagKey(prefix.fluidTagKey(material), count)

    fun fromTagKeys(prefixes: Iterable<HTPrefixLike>, materials: Iterable<HTMaterialLike>, count: Int = 1): HTFluidIngredient = fromTagKeys(
        prefixes.flatMap { prefix: HTPrefixLike -> materials.map(prefix::fluidTagKey) },
        count,
    )
}
