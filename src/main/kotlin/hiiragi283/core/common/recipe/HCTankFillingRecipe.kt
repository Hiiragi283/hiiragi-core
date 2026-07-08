package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.recipe.HTTankFillingRecipe
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.getRequiredAmount
import hiiragi283.lib.recipe.ingredient.test
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidInstance

class HCTankFillingRecipe(val itemIngredient: Ingredient, val fluidIngredient: HTFluidIngredient, val result: HTItemResult) :
    HTTankFillingRecipe,
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCTankFillingRecipe> = HTCodecs.recordMap { instance ->
            instance
                .group(
                    Ingredient.CODEC.fieldOf(HTConstants.ITEM_INGREDIENT).forGetter(HCTankFillingRecipe::itemIngredient),
                    HTFluidIngredient.CODEC.fieldOf(HTConstants.FLUID_INGREDIENT).forGetter(HCTankFillingRecipe::fluidIngredient),
                    HTItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(HCTankFillingRecipe::result),
                ).apply(instance, ::HCTankFillingRecipe)
        }
    }

    override fun testContainer(instance: TypedInstance<Item>): Boolean = itemIngredient.test(instance)

    override fun testFluid(instance: TypedInstance<Fluid>): Boolean = fluidIngredient.test(instance)

    override fun getRequiredAmount(first: TypedInstance<Item>, second: TypedInstance<Fluid>): Pair<Int, Int> = itemIngredient.getRequiredAmount(first) to fluidIngredient.getRequiredAmount(second)

    override fun assemble(firstInput: ItemInstance, secondInput: FluidInstance): ItemStack = result.createOrEmpty()

    override fun getSerializer(): RecipeSerializer<HCTankFillingRecipe> = HCRecipeSerializers.FILLING

    override fun getType(): RecipeType<HCTankFillingRecipe> = HCRecipeTypes.FILLING
}
