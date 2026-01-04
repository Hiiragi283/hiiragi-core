package hiiragi283.core.api.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.holderSetOrNull
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTextResult
import hiiragi283.core.api.text.toTextResult
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import kotlin.collections.firstOrNull

/**
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
interface HTTagUtil {
    companion object {
        @JvmField
        val INSTANCE: HTTagUtil = HiiragiCoreAPI.getService()
    }

    fun <T : Any> getFirstHolder(provider: HolderLookup.Provider?, tagKey: TagKey<T>): HTTextResult<Holder<T>> {
        val provider1: HolderLookup.Provider = (provider ?: HiiragiCoreAPI.getActiveAccess())
            ?: return HTCommonTranslation.MISSING_SERVER.toTextResult()
        val holders: HolderSet<T> = provider1.holderSetOrNull(tagKey)
            ?: return HTCommonTranslation.EMPTY_TAG_KEY.toTextResult(tagKey)
        for (modId: String in getModIdPriorityList()) {
            val first: Holder<T>? = holders.firstOrNull { holder: Holder<T> -> holder.toLike().getNamespace() == modId }
            if (first != null) return HTTextResult.success(first)
        }
        return holders
            .firstOrNull()
            ?.let(HTTextResult.Companion::success)
            ?: HTCommonTranslation.EMPTY_TAG_KEY.toTextResult(tagKey)
    }

    fun getModIdPriorityList(): List<String>
}
