package hiiragi283.core.api

import com.google.gson.JsonObject
import hiiragi283.core.api.item.alchemy.HTPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.material.HTMaterialAccess
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.get
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.HTSimpleHolderLikeDelegate
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.tag.fluid.HTFluidTagPrefix
import hiiragi283.core.api.text.HTTextResult
import io.netty.buffer.ByteBuf
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.nbt.CompoundTag
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.MutableDataComponentHolder
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

    abstract val materialPlugins: Sequence<HTMaterialPlugin>

    fun forEachPlugin(title: String, action: Consumer<HTMaterialPlugin>) {
        this.forEachPlugin(title, action::accept)
    }

    @HTBuilderMarker
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

    abstract val partManager: Map<String, HTPart>

    val partCodec: BiCodec<ByteBuf, HTPart> = BiCodecs.lazy {
        BiCodec.STRING.flatXmap({ name: String -> partManager[name] ?: error("Unknown part: $name") }, HTPart::name)
    }

    /**
     * 素材マネージャを取得します。
     */
    abstract val materialManager: HTMaterialManager

    /**
     * 既存の素材コンテンツを取得します。
     */
    abstract val existingContents: HTMaterialAccess

    /**
     * 登録された素材コンテンツを取得します。
     */
    abstract val registeredContents: HTMaterialAccess

    abstract val registeredFluids: HTMaterialContents<HTFluidTagPrefix, Fluid>

    fun getMaterialBlock(part: HTPartLike, material: HTMaterialLike): HTMaterialContents.Entry<Block>? =
        existingContents.blocks[part, material] ?: registeredContents.blocks[part, material]

    fun getMaterialItem(part: HTPartLike, material: HTMaterialLike): HTMaterialContents.Entry<Item>? =
        existingContents.items[part, material] ?: registeredContents.items[part, material]

    fun getMaterialBlockOrItem(part: HTPartLike, material: HTMaterialLike): HTMaterialContents.Entry<out ItemLike>? =
        existingContents.getBlockOrItem(part, material) ?: registeredContents.getBlockOrItem(part, material)

    //    Potion    //

    /**
     * 指定した[holder]から[HTPotionContents]を取得します。
     * @return 取得できなかった場合は`null`
     * @since 0.11.0
     * @see HTPotionHelper.getContents
     */
    abstract fun getContents(holder: DataComponentHolder): HTPotionContents?

    /**
     * 指定した[holder]に[HTPotionContents]を設定します。
     * @since 0.11.0
     * @see HTPotionHelper.setContents
     */
    abstract fun setContents(holder: MutableDataComponentHolder, contents: HTPotionContents)

    //    Tag    //

    /**
     * 指定した[provider]から，[tagKey]に紐づいた[HTHolderLike]を取得します。
     * @param T レジストリの種類のクラス
     * @return [HTHolderLike]の[結果][HTTextResult]
     */
    abstract fun <T : Any> getFirstHolder(provider: HolderLookup.Provider?, tagKey: TagKey<T>): HTTextResult<HTSimpleHolderLikeDelegate<T>>

    //    Value IO    //

    /**
     * 指定した[レジストリ][provider]と[JSON][jsonObject]から[HTValueInput]を作成します。
     */
    abstract fun createInput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueInput

    /**
     * 指定した[レジストリ][provider]と[JSON][jsonObject]から[HTValueOutput]を作成します。
     */
    abstract fun createOutput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueOutput

    /**
     * 指定した[レジストリ][provider]と[NBT][compoundTag]から[HTValueInput]を作成します。
     */
    abstract fun createInput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueInput

    /**
     * 指定した[レジストリ][provider]と[NBT][compoundTag]から[HTValueOutput]を作成します。
     */
    abstract fun createOutput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueOutput
}
