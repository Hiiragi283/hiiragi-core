package hiiragi283.core.common

import com.google.gson.JsonObject
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.toLike
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialAccess
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTFluidHolderLike
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.holderSetOrNull
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.fluid.HTFluidTagPrefix
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTextResult
import hiiragi283.core.api.text.toTextResult
import hiiragi283.core.common.material.HTMaterialContentsImpl
import hiiragi283.core.common.serialization.value.HTEmptyValueInput
import hiiragi283.core.common.serialization.value.HTJsonValueInput
import hiiragi283.core.common.serialization.value.HTJsonValueOutput
import hiiragi283.core.common.serialization.value.HTTagValueInput
import hiiragi283.core.common.serialization.value.HTTagValueOutput
import hiiragi283.core.config.HCConfig
import hiiragi283.core.setup.HCMiscRegister
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.tags.TagKey
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.TagsUpdatedEvent

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
class HiiragiCoreAccessImpl : HiiragiCoreAccess() {
    companion object {
        @JvmStatic
        internal lateinit var materialManagerCache: HTMaterialManager

        @JvmStatic
        private val modIdComparator: Comparator<HTIdLike> by lazy {
            Comparator
                .comparingInt { id: HTIdLike ->
                    val modIds: List<String> = HCConfig.COMMON.tagOutputPriority.get()
                    when (val priority: Int = modIds.indexOf(id.namespace)) {
                        -1 -> modIds.size
                        else -> priority
                    }
                }.thenBy(HTIdLike::namespace)
        }

        @JvmStatic
        private val tagResultCache: MutableMap<TagKey<*>, HTHolderLike.HolderDelegate<*, *>> = hashMapOf()

        @SubscribeEvent
        @JvmStatic
        fun clearTagCache(event: TagsUpdatedEvent) {
            if (event.updateCause == TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) {
                tagResultCache.clear()
                HiiragiCoreAPI.LOGGER.debug("Cleared tag holder cache")
            }
        }
    }

    override val materialManager: HTMaterialManager get() = materialManagerCache

    override val existingContents: HTMaterialAccess = object : HTMaterialAccess {
        override val blocks: HTMaterialContents<HTTagPrefix, HTBlockHolderLike<*, *>> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.existingBlocks.toLike()) { prefix: HTTagPrefix, key: HTMaterialKey ->
                "Unknown ${prefix.name} block for ${key.getId()}"
            }
        }
        override val items: HTMaterialContents<HTTagPrefix, HTItemHolderLike<*>> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.existingItems.toLike()) { prefix: HTTagPrefix, key: HTMaterialKey ->
                "Unknown ${prefix.name} item for ${key.getId()}"
            }
        }
        override val tools: HTMaterialContents<HTToolType, HTItemHolderLike<*>> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.existingTools.toLike()) { toolType: HTToolType, key: HTMaterialKey ->
                "Unknown ${toolType.name} item for ${key.getId()}"
            }
        }
    }

    override val registeredContents: HTMaterialAccess = object : HTMaterialAccess {
        override val blocks: HTMaterialContents<HTTagPrefix, HTBlockHolderLike<*, *>> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.materialBlocks.toLike()) { prefix: HTTagPrefix, key: HTMaterialKey ->
                "Unregistered ${prefix.name} block for ${key.getId()}"
            }
        }
        override val items: HTMaterialContents<HTTagPrefix, HTItemHolderLike<*>> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.materialItems.toLike()) { prefix: HTTagPrefix, key: HTMaterialKey ->
                "Unregistered ${prefix.name} item for ${key.getId()}"
            }
        }
        override val tools: HTMaterialContents<HTToolType, HTItemHolderLike<*>> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.materialTools.toLike()) { toolType: HTToolType, key: HTMaterialKey ->
                "Unregistered ${toolType.name} item for ${key.getId()}"
            }
        }
    }

    override val registeredFluids: HTMaterialContents<HTFluidTagPrefix, HTFluidHolderLike<*>> by lazy {
        HTMaterialContentsImpl(HCMiscRegister.materialFluids.toLike()) { prefix: HTFluidTagPrefix, key: HTMaterialKey ->
            "Unregistered ${prefix.name} fluid for ${key.getId()}"
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getFirstHolder(
        provider: HolderLookup.Provider?,
        tagKey: TagKey<T>,
    ): HTTextResult<HTHolderLike.HolderDelegate<T, T>> {
        // キャッシュから優先して取得
        val cachedHolder: HTHolderLike.HolderDelegate<T, T>? = tagResultCache[tagKey] as? HTHolderLike.HolderDelegate<T, T>
        if (cachedHolder != null) {
            return HTTextResult.success(cachedHolder)
        }
        // キャッシュから取得できない場合はレジストリから取得
        val provider1: HolderLookup.Provider = (provider ?: HiiragiCoreAPI.getActiveAccess())
            ?: return HTCommonTranslation.MISSING_SERVER.toTextResult()
        val holder: HTHolderLike.HolderDelegate<T, T> = provider1
            .holderSetOrNull(tagKey)
            ?.asSequence()
            ?.map(Holder<T>::toLike)
            ?.sortedWith(modIdComparator)
            ?.firstOrNull()
            ?: return HTCommonTranslation.EMPTY_TAG_KEY.toTextResult(tagKey)
        // キャッシュを保存
        tagResultCache[tagKey] = holder
        HiiragiCoreAPI.LOGGER.debug("Cached first holder: {} for tag: {}", holder.getId(), tagKey)
        return HTTextResult.success(holder)
    }

    override fun createInput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueInput = when {
        jsonObject.isEmpty -> HTEmptyValueInput
        else -> HTJsonValueInput(provider, jsonObject)
    }

    override fun createOutput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueOutput =
        HTJsonValueOutput(provider, jsonObject)

    override fun createInput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueInput = when {
        compoundTag.isEmpty -> HTEmptyValueInput
        else -> HTTagValueInput(provider, compoundTag)
    }

    override fun createOutput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueOutput =
        HTTagValueOutput(provider, compoundTag)
}
