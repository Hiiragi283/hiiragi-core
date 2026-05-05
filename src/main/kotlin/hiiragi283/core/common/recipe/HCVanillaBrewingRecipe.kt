package hiiragi283.core.common.recipe

import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.base.HTBrewingRecipe
import hiiragi283.core.api.util.Ior
import hiiragi283.core.mixin.PotionBrewingMixAccessor
import hiiragi283.core.util.HCPotionFluidHelper
import net.minecraft.core.Holder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class HCVanillaBrewingRecipe(val potionFrom: Holder<Potion>, val ingredient: Ingredient, val potionTo: Holder<Potion>) :
    HTBrewingRecipe {
    constructor(accessor: PotionBrewingMixAccessor<Potion>) : this(accessor.from, accessor.ingredient, accessor.to)

    override fun test(first: ItemStack, second: FluidStack): Boolean =
        ingredient.test(first) && HTPotionHelper.getPotion(second).`is`(potionFrom)

    override fun assemble(firstInput: ItemStack, secondInput: FluidStack): Ior<ItemStack, FluidStack> =
        Ior.Right(BottledPotionContents(potionTo).let(HCPotionFluidHelper::createFluid))
}
