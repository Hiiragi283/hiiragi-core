package hiiragi283.core.common

import com.google.gson.JsonObject
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionFluidManager
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialAccess
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.HTSimpleHolderLikeDelegate
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
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCMiscRegister
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.MutableDataComponentHolder
import net.neoforged.neoforge.event.TagsUpdatedEvent
import net.neoforged.neoforge.fluids.FluidStack

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

        @JvmField
        val DEFAULT_POTION_HANDLER: HTPotionFluidManager.Handler = object : HTPotionFluidManager.Handler {
            override fun get(holder: DataComponentHolder): HTBottleType =
                holder.getOrDefault(HCDataComponents.BOTTLE_TYPE, HTBottleType.DEFAULT)

            override fun set(holder: MutableDataComponentHolder, bottleType: HTBottleType) {
                holder.set(HCDataComponents.BOTTLE_TYPE, bottleType)
            }
        }
    }

    override val materialManager: HTMaterialManager get() = materialManagerCache

    override val existingContents: HTMaterialAccess = object : HTMaterialAccess {
        override val blocks: HTMaterialContents<HTTagPrefix, Block> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.existingBlocks) { prefix: HTTagPrefix, key: HTMaterialKey ->
                "Unknown ${prefix.name} block for ${key.getId()}"
            }
        }
        override val items: HTMaterialContents<HTTagPrefix, Item> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.existingItems) { prefix: HTTagPrefix, key: HTMaterialKey ->
                "Unknown ${prefix.name} item for ${key.getId()}"
            }
        }
        override val tools: HTMaterialContents<HTToolType, Item> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.existingTools) { toolType: HTToolType, key: HTMaterialKey ->
                "Unknown ${toolType.name} item for ${key.getId()}"
            }
        }
    }

    override val registeredContents: HTMaterialAccess = object : HTMaterialAccess {
        override val blocks: HTMaterialContents<HTTagPrefix, Block> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.materialBlocks) { prefix: HTTagPrefix, key: HTMaterialKey ->
                "Unregistered ${prefix.name} block for ${key.getId()}"
            }
        }
        override val items: HTMaterialContents<HTTagPrefix, Item> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.materialItems) { prefix: HTTagPrefix, key: HTMaterialKey ->
                "Unregistered ${prefix.name} item for ${key.getId()}"
            }
        }
        override val tools: HTMaterialContents<HTToolType, Item> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.materialTools) { toolType: HTToolType, key: HTMaterialKey ->
                "Unregistered ${toolType.name} item for ${key.getId()}"
            }
        }
    }

    override val registeredFluids: HTMaterialContents<HTFluidTagPrefix, Fluid> by lazy {
        HTMaterialContentsImpl(HCMiscRegister.materialFluids) { prefix: HTFluidTagPrefix, key: HTMaterialKey ->
            "Unregistered ${prefix.name} fluid for ${key.getId()}"
        }
    }

    override fun getContents(holder: DataComponentHolder): HTPotionContents? {
        val handler: HTPotionFluidManager.Handler = when (holder) {
            is FluidStack -> HTPotionFluidManager.getHandler(holder.fluid)
            else -> null
        } ?: DEFAULT_POTION_HANDLER
        return HTPotionContents.fromVanilla(HTPotionHelper.getPotion(holder), handler[holder])
    }

    override fun setContents(holder: MutableDataComponentHolder, contents: HTPotionContents) {
        holder.set(DataComponents.POTION_CONTENTS, contents.vanilla)
        val handler: HTPotionFluidManager.Handler = when (holder) {
            is FluidStack -> HTPotionFluidManager.getHandler(holder.fluid)
            else -> null
        } ?: DEFAULT_POTION_HANDLER
        handler[holder] = contents.bottleType
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getFirstHolder(
        provider: HolderLookup.Provider?,
        tagKey: TagKey<T>,
    ): HTTextResult<HTSimpleHolderLikeDelegate<T>> {
        // キャッシュから優先して取得
        val cachedHolder: HTSimpleHolderLikeDelegate<T>? = tagResultCache[tagKey] as? HTSimpleHolderLikeDelegate<T>
        if (cachedHolder != null) {
            return HTTextResult.success(cachedHolder)
        }
        // キャッシュから取得できない場合はレジストリから取得
        val provider1: HolderLookup.Provider = (provider ?: HiiragiCoreAPI.getActiveAccess())
            ?: return HTCommonTranslation.MISSING_SERVER.toTextResult()
        val holder: HTSimpleHolderLikeDelegate<T> = provider1
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
