package hiiragi283.core.common.recipe.viewer

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeDisplay
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.toText
import hiiragi283.core.api.util.Either
import hiiragi283.core.common.recipe.HCBrewingRecipe
import hiiragi283.core.common.recipe.HTVanillaRecipeTypes
import hiiragi283.core.impl.recipe.viewer.HTRecipeViewerTypeImpl
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

data object HCRecipeViewerTypes {
    //    Basic    //

    @JvmField
    val BREWING: HTHolderRecipeViewerType<HCBrewingRecipe> =
        creteHolder(HTVanillaRecipeTypes.BREWING, Items.BREWING_STAND, 18 * 6)

    @JvmField
    val CHARGING: HTRecipeViewerType<HTProgressRecipeDisplay> =
        create(HCRecipeTypes.CHARGING, Items.LIGHTNING_ROD, 18 * 4, 18 * 2)

    @JvmField
    val CRUSHING: HTRecipeViewerType<HTProgressRecipeDisplay> =
        create(HCRecipeTypes.CRUSHING, Items.ANVIL, 18 * 5, 18 * 2)

    @JvmField
    val EXPLODING: HTRecipeViewerType<HTRecipeDisplay.Simple> =
        create(HCRecipeTypes.EXPLODING, Items.TNT, 18 * 4)

    //    Tank Interaction    //

    @JvmField
    val EMPTYING: HTRecipeViewerType<HTRecipeDisplay.Simple> = create(HCRecipeTypes.EMPTYING, Items.BUCKET, 18 * 6)

    @JvmField
    val FILLING: HTRecipeViewerType<HTRecipeDisplay.Simple> = create(HCRecipeTypes.FILLING, Items.WATER_BUCKET, 18 * 6)

    //    Material    //

    data object MaterialType : HTRecipeViewerType<HTMaterialManager.Entry> {
        override val recipeClass: Class<HTMaterialManager.Entry> = HTMaterialManager.Entry::class.java
        override val icon: Either<ResourceLocation, ItemStack> = Either.Right(ItemStack(Items.IRON_INGOT))
        override val bounds: HTBounds = HTBounds(0, 0, 142, 110)
        override val workStations: List<ItemStack> = emptyList()

        override fun getText(): Text = "Material Parts".toText()

        override fun getId(): ResourceLocation = HiiragiCoreAPI.id("material")
    }

    @JvmStatic
    private inline fun <reified T : Any> create(
        recipeType: HTIdLike.Translatable,
        iconItem: ItemLike,
        width: Int,
        height: Int = 18 * 1,
        builderAction: HTRecipeViewerTypeImpl.Builder.() -> Unit = {},
    ): HTRecipeViewerType<T> = HTRecipeViewerTypeImpl.create<T> {
        id = recipeType
        title = recipeType
        val iconStack = ItemStack(iconItem)
        icon = Either.Right(iconStack)
        bounds = HTBounds(0, 0, width, height)
        workStations += iconStack
        builderAction()
    }

    @JvmStatic
    private inline fun <reified RECIPE : Any> creteHolder(
        recipeType: HTIdLike.Translatable,
        iconItem: ItemLike,
        width: Int,
        height: Int = 18 * 1,
        builderAction: HTRecipeViewerTypeImpl.Builder.() -> Unit = {},
    ): HTHolderRecipeViewerType<RECIPE> = create<HTRecipeHolder<RECIPE>>(recipeType, iconItem, width, height, builderAction)
}
