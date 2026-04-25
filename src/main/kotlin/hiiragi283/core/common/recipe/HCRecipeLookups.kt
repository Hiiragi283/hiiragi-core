package hiiragi283.core.common.recipe

import hiiragi283.core.api.HTColoredContents
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.VanillaColoredContents
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.base.HTTankFillingRecipe
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.recipe.custom.HTPotionArrowFillingRecipe
import hiiragi283.core.common.recipe.custom.HTPotionTankInteraction
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.impl.recipe.HTRecipeLookupImpl
import hiiragi283.core.impl.recipe.HTRecipeLookupManager
import hiiragi283.core.impl.recipe.HTVanillaRecipeLookup
import hiiragi283.core.impl.recipe.addProvider
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

data object HCRecipeLookups {
    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Any> create(path: String): HTRecipeLookupImpl<INPUT, RECIPE> =
        HTRecipeLookupManager.create(HiiragiCoreAPI.id(path))

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(
        recipeType: HTDeferredRecipeType<RECIPE>,
    ): HTRecipeLookup<INPUT, RECIPE> = HTVanillaRecipeLookup(recipeType)

    //    Basic    //

    @JvmField
    val CHARGING: HTRecipeLookup<HCChargingRecipe.Input, HCChargingRecipe> = create(HCRecipeTypes.CHARGING)

    @JvmField
    val CRUSHING: HTRecipeLookupImpl<SingleRecipeInput, HTSingleMultiOutputRecipe> = create(HTConst.CRUSHING)

    @JvmField
    val EXPLODING: HTRecipeLookup<HCExplodingRecipe.Input, HCExplodingRecipe> = create(HCRecipeTypes.EXPLODING)

    @JvmField
    val FORGING: HTRecipeLookupImpl<HTDoubleRecipeInput, HTDoubleMultiOutputRecipe> = create(HTConst.FORGING)

    @JvmField
    val COLORING: HTRecipeLookupImpl<HTItemAndFluidRecipeInput, HCColoringRecipe> = create(HTConst.COLORING)

    //    Tank Interaction    //

    @JvmField
    val EMPTYING: HTRecipeLookupImpl<SingleRecipeInput, HTTankEmptyingRecipe> = create(HTConst.EMPTYING)

    @JvmField
    val FILLING: HTRecipeLookupImpl<HTItemAndFluidRecipeInput, HTTankFillingRecipe> = create(HTConst.FILLING)

    //    Registration    //

    @JvmStatic
    fun init() {
        CRUSHING.addProvider(HCRecipeTypes.CRUSHING.get(), identity())
        FORGING.addProvider(HCRecipeTypes.FORGING.get(), identity())

        EMPTYING.addProvider(HCRecipeTypes.EMPTYING.get(), identity())
        EMPTYING.addProvider(HTConst.MINECRAFT.toId(HTConst.EMPTYING, "potion") to HTPotionTankInteraction.Emptying)

        FILLING.addProvider(HCRecipeTypes.FILLING.get(), identity())
        FILLING.addProvider(HTConst.MINECRAFT.toId(HTConst.FILLING, "potion") to HTPotionTankInteraction.Filling)
        FILLING.addProvider(HTConst.MINECRAFT.toId(HTConst.FILLING, "potion_arrow") to HTPotionArrowFillingRecipe)

        coloring(ItemTags.BANNERS, VanillaColoredContents.BANNER)
        coloring(ItemTags.BEDS, VanillaColoredContents.BED)
        coloring(ItemTags.WOOL, VanillaColoredContents.WOOL)
        coloring(ItemTags.WOOL_CARPETS, VanillaColoredContents.CARPET)
        coloring(Tags.Items.SHULKER_BOXES, VanillaColoredContents.SHULKER_BOX)
    }

    @JvmStatic
    fun coloring(inputTag: TagKey<Item>, contents: HTColoredContents<out SupplierWithId<out ItemLike>>) {
        COLORING.addProvider {
            sequenceOf(
                HTRecipeHolder(
                    inputTag.location().withPrefix("${HTConst.FILLING}/coloring/"),
                    HCColoringRecipe(inputTag, contents),
                ),
            )
        }
    }
}
