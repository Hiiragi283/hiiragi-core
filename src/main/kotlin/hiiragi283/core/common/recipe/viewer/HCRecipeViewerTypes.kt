package hiiragi283.core.common.recipe.viewer

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.base.HTTankFillingRecipe
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTLookupRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTSimpleRecipeViewerType
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.toText
import hiiragi283.core.api.util.Either
import hiiragi283.core.common.recipe.HCBrewingRecipe
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCForgingRecipe
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import hiiragi283.core.common.recipe.HTVanillaRecipeTypes
import hiiragi283.core.setup.HCBlocks
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

data object HCRecipeViewerTypes {
    @JvmStatic
    private fun <BASE : Any, RECIPE : BASE> lookup(
        recipeType: HTRecipeType<*, BASE>,
        icon: ItemLike,
        width: Int,
        height: Int = 18 * 1,
    ): HTLookupRecipeViewerType<BASE, RECIPE> = HTLookupRecipeViewerType.create(recipeType, ItemStack(icon), HTBounds(0, 0, width, height))

    @JvmStatic
    private inline fun <reified RECIPE : Any> simple(
        recipeType: HTRecipeType<*, RECIPE>,
        icon: ItemLike,
        width: Int,
        height: Int = 18 * 1,
    ): HTHolderRecipeViewerType<RECIPE> =
        HTSimpleRecipeViewerType.create<HTRecipeHolder<RECIPE>>(recipeType, recipeType, ItemStack(icon), HTBounds(0, 0, width, height))

    //    Basic    //

    @JvmField
    val BREWING: HTHolderRecipeViewerType<HCBrewingRecipe> =
        simple(HTVanillaRecipeTypes.BREWING, Items.BREWING_STAND, 18 * 6)

    @JvmField
    val CHARGING: HTHolderRecipeViewerType<HCChargingRecipe> =
        simple(HCRecipeLookups.CHARGING, Items.LIGHTNING_ROD, 18 * 4, 18 * 2)

    @JvmField
    val CRUSHING: HTLookupRecipeViewerType<HTSingleMultiOutputRecipe, HCCrushingRecipe> =
        lookup(HCRecipeLookups.CRUSHING, Items.ANVIL, 18 * 5, 18 * 2)

    @JvmField
    val EXPLODING: HTHolderRecipeViewerType<HCExplodingRecipe> =
        simple(HCRecipeLookups.EXPLODING, Items.TNT, 18 * 4, 18 * 2)

    @JvmField
    val FORGING: HTLookupRecipeViewerType<HTDoubleMultiOutputRecipe, HCForgingRecipe> =
        lookup(HCRecipeLookups.FORGING, HCBlocks.FORGING_ANVIL, 18 * 6, 18 * 3)

    //    Tank Interaction    //

    @JvmField
    val EMPTYING: HTLookupRecipeViewerType<HTTankEmptyingRecipe, HCTankEmptyingRecipe> =
        lookup(HCRecipeLookups.EMPTYING, Items.BUCKET, 18 * 6)

    @JvmField
    val FILLING: HTLookupRecipeViewerType<HTTankFillingRecipe, HCTankFillingRecipe> =
        lookup(HCRecipeLookups.FILLING, Items.WATER_BUCKET, 18 * 6)

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
