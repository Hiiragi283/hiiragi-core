package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.registry.holderSetOrNull
import hiiragi283.core.api.storage.resource.HTResourceType
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTextResult
import hiiragi283.core.api.text.toTextResult
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

/**
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
abstract class HTResourceRecipeResult<TYPE : Any, RESOURCE : HTResourceType<TYPE>, STACK : Any>(
    protected val contents: Ior<RESOURCE, TagKey<TYPE>>,
    protected val amount: Int,
) : HTRecipeResult<STACK> {
    fun getStackOrEmpty(provider: HolderLookup.Provider?): STACK = getStackResult(provider).mapOrElse(identity()) { getEmptyStack() }

    protected abstract fun getEmptyStack(): STACK

    //    HTRecipeResult    //

    final override fun getStackResult(provider: HolderLookup.Provider?): HTTextResult<STACK> = contents.fold(
        { resource: RESOURCE -> HTTextResult.success(createStack(resource, amount)) },
        { tagKey: TagKey<TYPE> ->
            getFirstHolder(provider, tagKey).map { createStack(it, amount) }
        },
        { resource: RESOURCE, tagKey: TagKey<TYPE> ->
            getFirstHolder(provider, tagKey)
                .map { createStack(it, amount) }
                .let { either: HTTextResult<STACK> ->
                    HTTextResult.success(either.value().orElse(createStack(resource, amount)))
                }
        },
    )

    protected abstract fun createStack(resource: RESOURCE, amount: Int): STACK

    protected abstract fun createStack(holder: Holder<TYPE>, amount: Int): STACK

    private fun getFirstHolder(provider: HolderLookup.Provider?, tagKey: TagKey<TYPE>): HTTextResult<Holder<TYPE>> {
        val provider1: HolderLookup.Provider = (provider ?: HiiragiCoreAPI.getActiveAccess())
            ?: return HTCommonTranslation.MISSING_SERVER.toTextResult()
        return provider1
            .holderSetOrNull(tagKey)
            ?.firstOrNull()
            ?.let { HTTextResult.success(it) }
            ?: HTCommonTranslation.EMPTY_TAG_KEY.toTextResult()
    }

    override fun getId(): ResourceLocation = contents.map(HTResourceType<TYPE>::getId, TagKey<TYPE>::location)
}
