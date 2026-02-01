package hiiragi283.core.api

import com.google.gson.JsonObject
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetRenderer
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.holderSetOrNull
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTextResult
import hiiragi283.core.api.text.toTextResult
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.nbt.CompoundTag
import net.minecraft.tags.TagKey
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * モジュールをまたいで実装する要素をまとめたインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
interface HiiragiCoreAccess {
    companion object {
        /**
         * [HiiragiCoreAccess]のインスタンス
         */
        @JvmField
        val INSTANCE: HiiragiCoreAccess = HiiragiCoreAPI.getService()
    }

    //    Material    //

    /**
     * 素材マネージャを取得します。
     */
    val materialManager: HTMaterialManager

    /**
     * 登録された素材コンテンツを取得します。
     */
    val materialContents: HTMaterialContents

    /**
     * バニラ由来の素材コンテンツを取得します。
     */
    val vanillaContents: HTMaterialContents

    fun getBlockOrVanilla(prefix: HTTagPrefix, material: HTMaterialLike): HTBlockHolderLike<*, *>? =
        materialContents.getBlock(prefix, material) ?: vanillaContents.getBlock(prefix, material)

    fun getItemOrVanilla(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*>? =
        materialContents.getItem(prefix, material) ?: vanillaContents.getItem(prefix, material)

    fun getToolOrVanilla(toolType: HTToolType, material: HTMaterialLike): HTItemHolderLike<*>? =
        materialContents.getTool(toolType, material) ?: vanillaContents.getTool(toolType, material)

    //    Tag    //

    /**
     * 指定した[provider]から，[tagKey]に紐づいた[Holder]を取得します。
     * @param T レジストリの種類のクラス
     * @return [getModIdPriorityList]に基づいて選出された[Holder]の[結果][HTTextResult]
     */
    fun <T : Any> getFirstHolder(provider: HolderLookup.Provider?, tagKey: TagKey<T>): HTTextResult<Holder<T>> {
        val provider1: HolderLookup.Provider = (provider ?: HiiragiCoreAPI.getActiveAccess())
            ?: return HTCommonTranslation.MISSING_SERVER.toTextResult()
        val holders: HolderSet<T> = provider1.holderSetOrNull(tagKey)
            ?: return HTCommonTranslation.EMPTY_TAG_KEY.toTextResult(tagKey)
        for (modId: String in getModIdPriorityList()) {
            val first: Holder<T>? = holders.firstOrNull { holder: Holder<T> -> holder.toLike().namespace == modId }
            if (first != null) return HTTextResult.success(first)
        }
        return holders
            .firstOrNull()
            ?.let(HTTextResult.Companion::success)
            ?: HTCommonTranslation.EMPTY_TAG_KEY.toTextResult(tagKey)
    }

    fun getModIdPriorityList(): List<String>

    //    Value IO    //

    /**
     * 指定した[レジストリ][provider]と[JSON][jsonObject]から[HTValueInput]を作成します。
     */
    fun createInput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueInput

    /**
     * 指定した[レジストリ][provider]と[JSON][jsonObject]から[HTValueOutput]を作成します。
     */
    fun createOutput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueOutput

    /**
     * 指定した[レジストリ][provider]と[NBT][compoundTag]から[HTValueInput]を作成します。
     */
    fun createInput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueInput

    /**
     * 指定した[レジストリ][provider]と[NBT][compoundTag]から[HTValueOutput]を作成します。
     */
    fun createOutput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueOutput

    //    Client    //

    /**
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    @OnlyIn(Dist.CLIENT)
    interface Client {
        companion object {
            /**
             * [Client]のインスタンス
             */
            @JvmField
            val INSTANCE: Client = HiiragiCoreAPI.getService()
        }

        /**
         * 指定した[widget]から[HTWidgetRenderer]を作成します。
         * @param WIDGET [HTWidget]を実装したクラス
         * @return 対応する[HTWidgetRenderer]がない場合は`null`
         */
        fun <WIDGET : HTWidget> createRenderer(widget: WIDGET): HTWidgetRenderer<WIDGET>?
    }
}
