package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.recipe.HTTankEmptyingRecipe
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.ingredient.getRequiredAmount
import hiiragi283.lib.recipe.ingredient.test
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemAndFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.convert
import hiiragi283.lib.util.Option
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

class HCTankEmptyingRecipe(val ingredient: Ingredient, val fluidResult: HTFluidResult, val itemResult: Option<HTItemResult>) :
    HTTankEmptyingRecipe,
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCTankEmptyingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    Ingredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(HCTankEmptyingRecipe::ingredient),
                    HTFluidResult.CODEC.fieldOf(HTConstants.FLUID_RESULT).forGetter(HCTankEmptyingRecipe::fluidResult),
                    HTItemResult.CODEC.optionalFieldOf(HTConstants.ITEM_RESULT).convert().forGetter(HCTankEmptyingRecipe::itemResult),
                ).apply(instance, ::HCTankEmptyingRecipe)
        }
    }

    override fun test(input: TypedInstance<Item>): Boolean = ingredient.test(input)

    override fun getRequiredAmount(input: TypedInstance<Item>): Int = ingredient.getRequiredAmount(input)

    override fun assemble(input: ItemInstance): HTItemAndFluidResult {
        val fluidStack: FluidStack = fluidResult.create()
        return itemResult.map(HTItemResult::createOrEmpty).fold(
            { HTItemAndFluidResult.create(fluidStack) },
            { HTItemAndFluidResult.create(it, fluidStack) },
        )
    }

    override fun getSerializer(): RecipeSerializer<HCTankEmptyingRecipe> = HCRecipeSerializers.EMPTYING

    override fun getType(): RecipeType<HCTankEmptyingRecipe> = HCRecipeTypes.EMPTYING.get()
}
