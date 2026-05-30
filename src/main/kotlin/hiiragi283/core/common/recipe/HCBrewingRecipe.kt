package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.util.HCPotionFluidHelper
import hiiragi283.lib.HTComparators
import hiiragi283.lib.HTConstants
import hiiragi283.lib.fluid.createOrEmpty
import hiiragi283.lib.fluid.toTemplateOrNull
import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.recipe.base.HTItemOrFluidRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.test
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemAndFluidResult
import hiiragi283.lib.registry.toLike
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidType

@JvmRecord
data class HCBrewingRecipe(val potionFrom: Holder<Potion>, val ingredient: Ingredient, val potionTo: Holder<Potion>) :
    HTItemOrFluidRecipe,
    HTProgressRecipe.Simple<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCBrewingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTCodecs.holder(Registries.POTION).fieldOf("potion_from").forGetter(HCBrewingRecipe::potionFrom),
                    Ingredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(HCBrewingRecipe::ingredient),
                    HTCodecs.holder(Registries.POTION).fieldOf("potion_to").forGetter(HCBrewingRecipe::potionFrom),
                ).apply(instance, ::HCBrewingRecipe)
        }

        @JvmField
        val SORTER: Comparator<in HCBrewingRecipe> =
            compareBy<HCBrewingRecipe, Identifier>(HTComparators.ID) { it.potionTo.toLike().getId() }
                .thenComparing({ it.potionFrom.toLike().getId() }, HTComparators.ID)
    }

    constructor(accessor: PotionBrewing.Mix<Potion>) : this(accessor.from, accessor.ingredient, accessor.to)

    @Suppress("DEPRECATION")
    override fun test(first: ItemInstance, second: FluidInstance): Boolean {
        val contents: BottledPotionContents = HTPotionHelper.getContents(second) ?: return false
        val potionIn: Holder<Potion> = contents.potion ?: return false
        return ingredient.test(first) && potionIn.`is`(potionFrom)
    }

    override fun getRequiredAmount(first: ItemInstance, second: FluidInstance): Pair<Int, Int> = when {
        test(first, second) -> 1 to FluidType.BUCKET_VOLUME
        else -> 0 to 0
    }

    override fun assemble(firstInput: ItemInstance, secondInput: FluidInstance): HTItemAndFluidResult = BottledPotionContents(potionTo)
        .let(HCPotionFluidHelper::createFluid)
        .toTemplateOrNull()
        .createOrEmpty()
        .let(HTItemAndFluidResult.Companion::create)

    override val progressData: HTProgressData
        get() = HTProgressData.time(200)
}
