package hiiragi283.core.common

import com.google.gson.JsonObject
import hiiragi283.core.api.HTComparators
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionFluidManager
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialAccess
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.HTSimpleMaterialContents
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.registry.holderSetOrNull
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.HTItemResourceType
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
import hiiragi283.core.util.HTPluginLoader
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.nbt.CompoundTag
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.block.Block
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModList
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.MutableDataComponentHolder
import net.neoforged.neoforge.common.Tags
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
        private val tagResultCache: MutableMap<TagKey<*>, HTHolderLike<*, *>> = hashMapOf()

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
            override fun get(holder: DataComponentHolder): HTBottleType? = holder.get(HCDataComponents.BOTTLE_TYPE)

            override fun set(holder: MutableDataComponentHolder, bottleType: HTBottleType) {
                holder.set(HCDataComponents.BOTTLE_TYPE, bottleType)
            }
        }
    }

    override val materialPlugins: Sequence<HTMaterialPlugin> by lazy {
        HTPluginLoader
            .collectPlugins<HTMaterialPlugin>()
            .filter {
                val modId: String = it.namespace
                modId in HTConst.getBuiltInIdSet(HiiragiCoreAPI.MOD_ID) || ModList.get().isLoaded(modId)
            }.sortedWith(
                compareBy(HTMaterialPlugin::priority)
                    .thenComparing(HTMaterialPlugin::getId, HTComparators.ID),
            )
    }

    override val partManager: Map<String, HTPart> by lazy {
        buildMap {
            forEachPlugin("Register Part") { plugin: HTMaterialPlugin ->
                plugin.registerPart { name: String, idPattern: String, properties: HTPropertyMap ->
                    val part = HTPart(name, idPattern, properties)
                    check(this.put(name, part) == null) { "Duplicated part registration: $name" }
                    part
                }
            }
        }
    }

    override val materialManager: HTMaterialManager get() = materialManagerCache

    override val existingContents: HTMaterialAccess = object : HTMaterialAccess {
        override val blocks: HTSimpleMaterialContents<HTPart, Block> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.existingBlocks) { part: HTPart, key: HTMaterialKey ->
                "Unknown ${part.name} block for ${key.getId()}"
            }
        }
        override val items: HTMaterialContents<HTPart, HTMaterialContents.ItemEntry> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.existingItems) { part: HTPart, key: HTMaterialKey ->
                "Unknown ${part.name} item for ${key.getId()}"
            }
        }
        override val tools: HTMaterialContents<HTToolType, HTMaterialContents.ItemEntry> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.existingTools) { toolType: HTToolType, key: HTMaterialKey ->
                "Unknown ${toolType.name} item for ${key.getId()}"
            }
        }
    }

    override val registeredContents: HTMaterialAccess = object : HTMaterialAccess {
        override val blocks: HTSimpleMaterialContents<HTPart, Block> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.materialBlocks) { part: HTPart, key: HTMaterialKey ->
                "Unregistered ${part.name} block for ${key.getId()}"
            }
        }
        override val items: HTMaterialContents<HTPart, HTMaterialContents.ItemEntry> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.materialItems) { part: HTPart, key: HTMaterialKey ->
                "Unregistered ${part.name} item for ${key.getId()}"
            }
        }
        override val tools: HTMaterialContents<HTToolType, HTMaterialContents.ItemEntry> by lazy {
            HTMaterialContentsImpl(HCMiscRegister.materialTools) { toolType: HTToolType, key: HTMaterialKey ->
                "Unregistered ${toolType.name} item for ${key.getId()}"
            }
        }
    }

    override val registeredFluids: HTMaterialContents<HTFluidPart, HTMaterialContents.FluidEntry> by lazy {
        HTMaterialContentsImpl(HCMiscRegister.materialFluids) { part: HTFluidPart, key: HTMaterialKey ->
            "Unregistered ${part.asPartName()} fluid for ${key.getId()}"
        }
    }

    override fun getContents(resource: HTFluidResourceType): BottledPotionContents? {
        val handler: HTPotionFluidManager.Handler = when {
            resource.isOf(Tags.Fluids.WATER) -> return BottledPotionContents(Potions.WATER)
            else -> HTPotionFluidManager.getFluidHandler(resource.getHolder()) ?: DEFAULT_POTION_HANDLER
        }
        val bottleType: HTBottleType = handler[resource] ?: return null
        val contents: PotionContents = HTPotionHelper.getPotion(resource)
        return BottledPotionContents(contents, bottleType)
    }

    override fun getContents(resource: HTItemResourceType): BottledPotionContents? {
        val bottleType: HTBottleType = DEFAULT_POTION_HANDLER[resource] ?: return null
        val contents: PotionContents = HTPotionHelper.getPotion(resource)
        return BottledPotionContents(contents, bottleType)
    }

    override fun setContents(stack: FluidStack, contents: BottledPotionContents) {
        HTPotionHelper.setPotion(stack, contents.contents)
        val handler: HTPotionFluidManager.Handler = HTPotionFluidManager.getFluidHandler(stack.fluidHolder) ?: DEFAULT_POTION_HANDLER
        handler[stack] = contents.bottleType
    }

    override fun setContents(stack: ItemStack, contents: BottledPotionContents) {
        HTPotionHelper.setPotion(stack, contents.contents)
        DEFAULT_POTION_HANDLER[stack] = contents.bottleType
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getFirstHolder(provider: HolderLookup.Provider?, tagKey: TagKey<T>): HTTextResult<HTSimpleHolderLike<T>> {
        // キャッシュから優先して取得
        val cachedHolder: HTSimpleHolderLike<T>? = tagResultCache[tagKey] as? HTSimpleHolderLike<T>
        if (cachedHolder != null) {
            return HTTextResult.success(cachedHolder)
        }
        // キャッシュから取得できない場合はレジストリから取得
        val provider1: HolderLookup.Provider = (provider ?: HiiragiCoreAPI.getActiveAccess())
            ?: return HTCommonTranslation.MISSING_SERVER.toTextResult()
        val holder: HTSimpleHolderLike<T> = provider1
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
