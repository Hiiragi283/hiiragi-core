package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.registry.HTFluidContent
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.CompoundFluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

class HTFluidIngredientCreator(getter: HolderGetter<Fluid>) : HTSizedIngredientCreatorImpl<Fluid, HTFluidIngredient>(getter) {
    fun create(content: HTFluidContent, size: Int = getDefaultSize()): HTFluidIngredient = tag(content.fluidTag, size)

    fun water(size: Int = getDefaultSize()): HTFluidIngredient = tag(Tags.Fluids.WATER, size)

    fun lava(size: Int = getDefaultSize()): HTFluidIngredient = tag(Tags.Fluids.LAVA, size)

    fun milk(size: Int = getDefaultSize()): HTFluidIngredient = tag(Tags.Fluids.MILK, size)

    fun create(ingredient: FluidIngredient, size: Int = getDefaultSize()): HTFluidIngredient = HTFluidIngredient(ingredient, size)

    fun create(ingredient: SizedFluidIngredient): HTFluidIngredient = HTFluidIngredient(ingredient)

    //    HTSizedIngredientCreator    //

    override fun getDefaultSize(): Int = FluidType.BUCKET_VOLUME

    override fun holderSet(holderSet: HolderSet<Fluid>, size: Int): HTFluidIngredient = create(FluidIngredient.of(holderSet), size)

    override fun holderSets(holderSets: Collection<HolderSet<Fluid>>, size: Int): HTFluidIngredient = when (holderSets.size) {
        1 -> holderSet(holderSets.first(), size)
        else -> create(CompoundFluidIngredient(holderSets.map(FluidIngredient::of)), size)
    }

    @Suppress("DEPRECATION")
    override fun getHolder(type: Fluid): Holder<Fluid> = type.builtInRegistryHolder()
}
