package hiiragi283.lib.recipe.viewer

import hiiragi283.lib.math.HTBounds
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.text.HTHasText
import hiiragi283.lib.util.Either
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack

/**
 * [HTRecipeViewerType]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
class HTSimpleRecipeViewerType<out RECIPE : Any>(
    override val recipeClass: Class<out RECIPE>,
    idLike: HTIdLike,
    hasText: HTHasText,
    override val icon: Either<Identifier, ItemStack>,
    override val bounds: HTBounds,
    override val workStations: List<ItemStack>,
) : HTRecipeViewerType<RECIPE>,
    HTIdLike by idLike,
    HTHasText by hasText {
    companion object {
        @JvmStatic
        inline fun <reified T : Any> create(builderAction: Builder.() -> Unit): HTSimpleRecipeViewerType<T> = Builder().apply(builderAction).build()
    }

    override fun toString(): String = "HTSimpleRecipeViewerType(class=${recipeClass.canonicalName})"

    //    Builder    //

    class Builder {
        lateinit var id: HTIdLike
        lateinit var title: HTHasText
        lateinit var icon: Either<Identifier, ItemStack>
        lateinit var bounds: HTBounds
        val workStations: MutableList<ItemStack> = mutableListOf()

        inline fun <reified T : Any> build(): HTSimpleRecipeViewerType<T> = build(T::class.java)

        fun <T : Any> build(recipeClass: Class<out T>): HTSimpleRecipeViewerType<T> = HTSimpleRecipeViewerType(recipeClass, id, title, icon, bounds, workStations)
    }
}
