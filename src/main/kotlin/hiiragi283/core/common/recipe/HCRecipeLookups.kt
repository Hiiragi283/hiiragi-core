package hiiragi283.core.common.recipe

import hiiragi283.core.api.HTColoredContents
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.VanillaColoredContents
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTSingleItemRecipe
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.base.HTTankFillingRecipe
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.recipe.custom.HTPotionArrowFillingRecipe
import hiiragi283.core.common.recipe.custom.HTPotionTankInteraction
import hiiragi283.core.impl.recipe.HTRecipeTypeImpl
import hiiragi283.core.impl.recipe.HTRecipeTypeManager
import hiiragi283.core.impl.recipe.addProvider
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

data object HCRecipeLookups {
    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Any> create(path: String): HTRecipeTypeImpl<INPUT, RECIPE> =
        HTRecipeTypeManager.create(HiiragiCoreAPI.id(path))

    //    Basic    //

    @JvmField
    val CHARGING: HTRecipeTypeImpl<SingleRecipeInput, HTSingleItemRecipe> = create(HTConst.CHARGING)

    @JvmField
    val CRUSHING: HTRecipeTypeImpl<SingleRecipeInput, HTSingleMultiOutputRecipe> = create(HTConst.CRUSHING)

    @JvmField
    val EXPLODING: HTRecipeTypeImpl<HCExplodingRecipe.Input, HCExplodingRecipe> = create(HTConst.EXPLODING)

    @JvmField
    val FORGING: HTRecipeTypeImpl<HTDoubleRecipeInput, HTDoubleMultiOutputRecipe> = create(HTConst.FORGING)

    @JvmField
    val MELTING: HTRecipeTypeImpl<HCMeltingRecipe.Input, HCMeltingRecipe> = create(HTConst.MELTING)

    @JvmField
    val COLORING: HTRecipeTypeImpl<HTItemAndFluidRecipeInput, HCColoringRecipe> = create(HTConst.COLORING)

    //    Tank Interaction    //

    @JvmField
    val EMPTYING: HTRecipeTypeImpl<SingleRecipeInput, HTTankEmptyingRecipe> = create(HTConst.EMPTYING)

    @JvmField
    val FILLING: HTRecipeTypeImpl<HTItemAndFluidRecipeInput, HTTankFillingRecipe> = create(HTConst.FILLING)

    //    Registration    //

    @JvmStatic
    fun init() {
        CHARGING.addProvider(HCRecipeTypes.CHARGING.get(), identity())
        CRUSHING.addProvider(HCRecipeTypes.CRUSHING.get(), identity())
        EXPLODING.addProvider(HCRecipeTypes.EXPLODING.get(), identity())
        FORGING.addProvider(HCRecipeTypes.FORGING.get(), identity())
        MELTING.addProvider(HCRecipeTypes.MELTING.get(), identity())

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
    fun coloring(inputTag: TagKey<Item>, contents: HTColoredContents<out HTHolderLike<out ItemLike, *>>) {
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
