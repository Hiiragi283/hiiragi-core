package hiiragi283.core.api.recipe.handler

/**
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see mekanism.api.recipes.outputs.IOutputHandler
 */
interface HTOutputHandler<STACK : Any> {
    fun canInsert(stack: STACK): Boolean

    fun insert(stack: STACK)
}
