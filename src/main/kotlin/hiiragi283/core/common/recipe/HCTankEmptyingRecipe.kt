package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import hiiragi283.core.api.recipe.base.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.util.getOrEmpty
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

class HCTankEmptyingRecipe(val ingredient: Ingredient, val fluidResult: HTFluidResult, val itemResult: Optional<HTItemResult>) :
    HTTankEmptyingRecipe,
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCTankEmptyingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTCodecs.INGREDIENT.fieldOf(HTConst.INGREDIENT).forGetter(HCTankEmptyingRecipe::ingredient),
                    HTFluidResult.CODEC.fieldOf(HTConst.FLUID_RESULT).forGetter(HCTankEmptyingRecipe::fluidResult),
                    HTItemResult.CODEC.optionalFieldOf(HTConst.ITEM_RESULT).forGetter(HCTankEmptyingRecipe::itemResult),
                ).apply(instance, ::HCTankEmptyingRecipe)
        }
    }

    override fun testContainer(stack: ItemStack): Boolean = ingredient.test(stack)

    override fun assemble(input: SingleRecipeInput, preview: Boolean): ItemStack = itemResult.map { it.getOrEmpty(preview) }.getOrEmpty()

    override fun assembleFluid(input: SingleRecipeInput): FluidStack = fluidResult.getOrEmpty()

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.EMPTYING

    override fun getType(): RecipeType<*> = HCRecipeTypes.EMPTYING.get()
}
