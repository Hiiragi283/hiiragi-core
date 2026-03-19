package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.resource.HTIdLike

interface HTRecipeResult<STACK : Any> : HTIdLike {
    fun create(): STACK
}
