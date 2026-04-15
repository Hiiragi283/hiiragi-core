package hiiragi283.core.common.recipe.viewer

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTSingleItemRecipe
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.base.HTTankFillingRecipe
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.viewer.HTLookupRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.toText
import hiiragi283.core.api.util.Either
import hiiragi283.core.common.recipe.HCBrewingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCMeltingRecipe
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.common.recipe.HTVanillaRecipeTypes
import hiiragi283.core.setup.HCBlocks
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.ItemLike

object HCRecipeViewerTypes {
    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Any> create(
        recipeType: HTRecipeType<INPUT, RECIPE>,
        icon: ItemLike,
        width: Int,
        height: Int = 18 * 1,
    ): HTLookupRecipeViewerType<INPUT, RECIPE> = HTLookupRecipeViewerType.create(recipeType, ItemStack(icon), HTBounds(0, 0, width, height))

    //    Basic    //

    @JvmField
    val BREWING: HTLookupRecipeViewerType<HTItemAndFluidRecipeInput, HCBrewingRecipe> =
        create(HTVanillaRecipeTypes.BREWING, Items.BREWING_STAND, 18 * 6)

    @JvmField
    val CHARGING: HTLookupRecipeViewerType<SingleRecipeInput, HTSingleItemRecipe> =
        create(HCRecipeLookups.CHARGING, Items.LIGHTNING_ROD, 18 * 4)

    @JvmField
    val CRUSHING: HTLookupRecipeViewerType<SingleRecipeInput, HTSingleMultiOutputRecipe> =
        create(HCRecipeLookups.CRUSHING, Items.ANVIL, 18 * 5, 18 * 2)

    @JvmField
    val EXPLODING: HTLookupRecipeViewerType<HCExplodingRecipe.Input, HCExplodingRecipe> =
        create(HCRecipeLookups.EXPLODING, Items.TNT, 18 * 6)

    @JvmField
    val FORGING: HTLookupRecipeViewerType<HTDoubleRecipeInput, HTDoubleMultiOutputRecipe> =
        create(HCRecipeLookups.FORGING, HCBlocks.FORGING_ANVIL, 18 * 6, 18 * 3)

    @JvmField
    val MELTING: HTLookupRecipeViewerType<HCMeltingRecipe.Input, HCMeltingRecipe> =
        create(HCRecipeLookups.MELTING, Items.LAVA_BUCKET, 18 * 4)

    //    Tank Interaction    //

    @JvmField
    val EMPTYING: HTLookupRecipeViewerType<SingleRecipeInput, HTTankEmptyingRecipe> =
        create(HCRecipeLookups.EMPTYING, Items.BUCKET, 18 * 3, 18 * 3)

    @JvmField
    val FILLING: HTLookupRecipeViewerType<HTItemAndFluidRecipeInput, HTTankFillingRecipe> =
        create(HCRecipeLookups.FILLING, Items.BUCKET, 18 * 3, 18 * 3)

    //    Material    //

    data object MaterialType : HTRecipeViewerType<HTMaterialManager.Entry> {
        override val recipeClass: Class<HTMaterialManager.Entry> = HTMaterialManager.Entry::class.java
        override val icon: Either<ResourceLocation, ItemStack> = Either.Right(ItemStack(Items.IRON_INGOT))
        override val bounds: HTBounds = HTBounds(0, 0, 142, 110)
        override val workStations: List<ItemStack> = emptyList()

        override fun getText(): Text = "Material Parts".toText()

        override fun getId(): ResourceLocation = HiiragiCoreAPI.id("material")
    }
}
