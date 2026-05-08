package hiiragi283.core.impl.recipe.viewer

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.util.Either
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

/**
 * [HTRecipeViewerType]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 * @see mekanism.client.recipe_viewer.type.FakeRVRecipeType
 */
class HTRecipeViewerTypeImpl<T : Any>(
    override val recipeClass: Class<out T>,
    idLike: HTIdLike,
    hasText: HTHasText,
    override val icon: Either<ResourceLocation, ItemStack>,
    override val bounds: HTBounds,
    override val workStations: List<ItemStack>,
) : HTRecipeViewerType<T>,
    HTIdLike by idLike,
    HTHasText by hasText {
    companion object {
        @JvmStatic
        inline fun <reified T : Any> create(builderAction: Builder.() -> Unit): HTRecipeViewerTypeImpl<T> = Builder().apply(builderAction).build()
    }

    override fun toString(): String = "HTSimpleRecipeViewerType(class=${recipeClass.canonicalName})"

    //    Builder    //

    class Builder {
        lateinit var id: HTIdLike
        lateinit var title: HTHasText
        lateinit var icon: Either<ResourceLocation, ItemStack>
        lateinit var bounds: HTBounds
        val workStations: MutableList<ItemStack> = mutableListOf()

        inline fun <reified T : Any> build(): HTRecipeViewerTypeImpl<T> = build(T::class.java)

        fun <T : Any> build(recipeClass: Class<out T>): HTRecipeViewerTypeImpl<T> = HTRecipeViewerTypeImpl(recipeClass, id, title, icon, bounds, workStations)
    }
}
