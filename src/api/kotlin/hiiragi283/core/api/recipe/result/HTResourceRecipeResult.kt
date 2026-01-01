package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.registry.holderSetOrNull
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.storage.resource.HTResourceType
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTextResult
import hiiragi283.core.api.text.toTextResult
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

/**
 * [HTResourceType]の基づいた[HTRecipeResult]の抽象クラスです。
 * @param TYPE 種類のクラス
 * @param RESOURCE [HTResourceType]を継承したクラス
 * @param STACK 完成品のクラス
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
abstract class HTResourceRecipeResult<TYPE : Any, RESOURCE : HTResourceType<TYPE>, STACK : Any>(
    protected val contents: Ior<RESOURCE, TagKey<TYPE>>,
    protected val amount: Int,
) : HTRecipeResult<STACK> {
    /**
     * 指定した[レジストリ][provider]から完成品を取得します。
     * @return 完成品を取得できなかった場合は[getEmptyStack]
     */
    fun getStackOrEmpty(provider: HolderLookup.Provider?): STACK = getStackResult(provider).value().orElseGet(::getEmptyStack)

    /**
     * 空の完成品のインスタンスを取得します。
     */
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

    /**
     * 指定した[resource]と[amount]から完成品を作成します。
     */
    protected abstract fun createStack(resource: RESOURCE, amount: Int): STACK

    /**
     * 指定した[holder]と[amount]から完成品を作成します。
     */
    protected abstract fun createStack(holder: Holder<TYPE>, amount: Int): STACK

    private fun getFirstHolder(provider: HolderLookup.Provider?, tagKey: TagKey<TYPE>): HTTextResult<Holder<TYPE>> {
        val provider1: HolderLookup.Provider = (provider ?: HiiragiCoreAPI.getActiveAccess())
            ?: return HTCommonTranslation.MISSING_SERVER.toTextResult()
        val holders: HolderSet<TYPE> = provider1.holderSetOrNull(tagKey)
            ?: return HTCommonTranslation.EMPTY_TAG_KEY.toTextResult(tagKey)
        for (modId: String in HiiragiCoreAccess.INSTANCE.getModIdPriorityList()) {
            val first: Holder<TYPE>? = holders.firstOrNull { holder: Holder<TYPE> -> holder.toLike().getNamespace() == modId }
            if (first != null) return HTTextResult.success(first)
        }
        return holders
            .firstOrNull()
            ?.let(HTTextResult.Companion::success)
            ?: HTCommonTranslation.EMPTY_TAG_KEY.toTextResult(tagKey)
    }

    override fun getId(): ResourceLocation = contents.map(HTResourceType<TYPE>::getId, TagKey<TYPE>::location)
}
