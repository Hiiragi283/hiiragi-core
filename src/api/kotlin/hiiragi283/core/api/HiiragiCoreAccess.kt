package hiiragi283.core.api

import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialAccess
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTSimpleMaterialContents
import hiiragi283.core.api.material.get
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.registry.holderSetOrNull
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTextResult
import hiiragi283.core.api.text.toTextResult
import hiiragi283.core.impl.material.HTMaterialContentsImpl
import hiiragi283.core.impl.material.HTMaterialContentsRegister
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Consumer
import kotlin.time.Duration
import kotlin.time.measureTime

/**
 * モジュールをまたいで実装する要素をまとめたインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
abstract class HiiragiCoreAccess {
    companion object {
        /**
         * [HiiragiCoreAccess]のインスタンス
         */
        @JvmField
        val INSTANCE: HiiragiCoreAccess = HiiragiCoreAPI.getService()
    }

    //    Material    //

    /**
     * [素材のプラグイン][HTMaterialPlugin]の一覧を取得します。
     * @since 0.12.0
     */
    abstract val materialPlugins: Sequence<HTMaterialPlugin>

    fun forEachPlugin(title: String, action: Consumer<HTMaterialPlugin>) {
        this.forEachPlugin(title, action::accept)
    }

    /**
     * 登録された[素材のプラグイン][HTMaterialPlugin]について処理を行います。
     * @param title ログに表示される名前
     * @param action 処理を行うブロック
     * @since 0.12.0
     */
    inline fun forEachPlugin(title: String, action: (HTMaterialPlugin) -> Unit) {
        HiiragiCoreAPI.LOGGER.info("{}...", title)
        val duration: Duration = measureTime {
            for (plugin: HTMaterialPlugin in materialPlugins) {
                runCatching {
                    action(plugin)
                }.onFailure { throwable: Throwable ->
                    HiiragiCoreAPI.LOGGER.error(
                        "Caught an error from plugin: {} {}",
                        plugin::class.java,
                        plugin.getId(),
                        throwable,
                    )
                }
            }
        }
        HiiragiCoreAPI.LOGGER.info("{} took {}", title, duration)
    }

    /**
     * 登録された[部品の種類][HTPart]を取得します。
     * @since 0.12.0
     */
    abstract val partManager: Map<String, HTPart>

    /**
     * 既存の素材コンテンツを取得します。
     */
    val existingContents: HTMaterialAccess = object : HTMaterialAccess {
        override val blocks: HTSimpleMaterialContents<HTPart, Block> by lazy {
            HTMaterialContentsImpl(HTMaterialContentsRegister.existingBlocks) { part: HTPart, key: HTMaterialKey ->
                "Unknown ${part.name} block for ${key.getId()}"
            }
        }
        override val items: HTMaterialContents<HTPart, HTMaterialContents.ItemEntry> by lazy {
            HTMaterialContentsImpl(HTMaterialContentsRegister.existingItems) { part: HTPart, key: HTMaterialKey ->
                "Unknown ${part.name} item for ${key.getId()}"
            }
        }
        override val tools: HTMaterialContents<HTToolType, HTMaterialContents.ItemEntry> by lazy {
            HTMaterialContentsImpl(HTMaterialContentsRegister.existingTools) { toolType: HTToolType, key: HTMaterialKey ->
                "Unknown ${toolType.name} item for ${key.getId()}"
            }
        }
    }

    /**
     * 登録された素材コンテンツを取得します。
     */
    val registeredContents: HTMaterialAccess = object : HTMaterialAccess {
        override val blocks: HTSimpleMaterialContents<HTPart, Block> by lazy {
            HTMaterialContentsImpl(HTMaterialContentsRegister.materialBlocks) { part: HTPart, key: HTMaterialKey ->
                "Unregistered ${part.name} block for ${key.getId()}"
            }
        }
        override val items: HTMaterialContents<HTPart, HTMaterialContents.ItemEntry> by lazy {
            HTMaterialContentsImpl(HTMaterialContentsRegister.materialItems) { part: HTPart, key: HTMaterialKey ->
                "Unregistered ${part.name} item for ${key.getId()}"
            }
        }
        override val tools: HTMaterialContents<HTToolType, HTMaterialContents.ItemEntry> by lazy {
            HTMaterialContentsImpl(HTMaterialContentsRegister.materialTools) { toolType: HTToolType, key: HTMaterialKey ->
                "Unregistered ${toolType.name} item for ${key.getId()}"
            }
        }
    }

    val registeredFluids: HTMaterialContents<HTFluidPart, HTMaterialContents.FluidEntry> by lazy {
        HTMaterialContentsImpl(HTMaterialContentsRegister.materialFluids) { part: HTFluidPart, key: HTMaterialKey ->
            "Unregistered ${part.asPartName()} fluid for ${key.getId()}"
        }
    }

    fun getMaterialBlock(part: HTPartLike, material: HTMaterialLike): HTMaterialContents.SimpleEntry<Block>? =
        existingContents.blocks[part, material] ?: registeredContents.blocks[part, material]

    fun getMaterialItem(part: HTPartLike, material: HTMaterialLike): HTMaterialContents.ItemEntry? =
        existingContents.items[part, material] ?: registeredContents.items[part, material]

    fun getMaterialBlockOrItem(part: HTPartLike, material: HTMaterialLike): HTMaterialContents.ItemEntry? =
        existingContents.getBlockOrItem(part, material) ?: registeredContents.getBlockOrItem(part, material)

    //    Potion    //

    /**
     * 指定した[resource]から[BottledPotionContents]を取得します。
     * @return 取得できなかった場合は`null`
     * @since 0.11.0
     * @see HTPotionHelper.getContents
     */
    abstract fun getContents(resource: HTFluidResourceType): BottledPotionContents?

    abstract fun getContents(resource: HTItemResourceType): BottledPotionContents?

    /**
     * 指定した[stack]に[BottledPotionContents]を設定します。
     * @since 0.11.0
     * @see HTPotionHelper.setContents
     */
    abstract fun setContents(stack: FluidStack, contents: BottledPotionContents)

    abstract fun setContents(stack: ItemStack, contents: BottledPotionContents)

    //    Tag    //

    /**
     * 指定した[provider]から，[tagKey]に紐づいた[HTSimpleHolderLike]を取得します。
     * @param T レジストリの種類のクラス
     * @return [HTSimpleHolderLike]の[結果][HTTextResult]
     */
    fun <T : Any> getFirstHolder(provider: HolderLookup.Provider?, tagKey: TagKey<T>): HTTextResult<HTSimpleHolderLike<T>> {
        val provider1: HolderLookup.Provider =
            (provider ?: HiiragiCoreAPI.getActiveAccess()) ?: return HTCommonTranslation.MISSING_SERVER.toTextResult()
        val holderSet: HolderSet<T> =
            provider1.holderSetOrNull(tagKey) ?: return HTCommonTranslation.EMPTY_TAG_KEY.toTextResult(tagKey)
        return getFirstHolder(holderSet)
    }

    /**
     * 指定した[holderSet]から，最初の[HTSimpleHolderLike]を取得します。
     * @param T レジストリの種類のクラス
     * @return [HTSimpleHolderLike]の[結果][HTTextResult]
     * @since 0.15.2
     */
    abstract fun <T : Any> getFirstHolder(holderSet: HolderSet<T>): HTTextResult<HTSimpleHolderLike<T>>
}
