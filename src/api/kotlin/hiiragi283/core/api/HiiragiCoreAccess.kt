package hiiragi283.core.api

import com.google.gson.JsonObject
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetRenderer
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.material.HTMaterialAccess
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTFluidHolderLike
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.fluid.HTFluidTagPrefix
import hiiragi283.core.api.text.HTTextResult
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.tags.TagKey
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.fluids.FluidStack

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

    abstract val registeredFluids: HTMaterialContents<HTFluidTagPrefix, HTFluidHolderLike<*>>

    fun getMaterialBlock(prefix: HTTagPrefix, material: HTMaterialLike): HTBlockHolderLike<*, *>? =
        existingContents.blocks[prefix, material] ?: registeredContents.blocks[prefix, material]

    fun getMaterialItem(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*>? =
        existingContents.items[prefix, material] ?: registeredContents.items[prefix, material]

    fun getMaterialBlockOrItem(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*>? =
        existingContents.getBlockOrItem(prefix, material) ?: registeredContents.getBlockOrItem(prefix, material)

    //    Potion    //

    abstract fun potionFluid(contents: PotionContents, bottleType: HTBottleType): HTFluidResourceType?

    fun potionFluid(contents: PotionContents, bottleType: HTBottleType, amount: Int): FluidStack =
        potionFluid(contents, bottleType)?.toStack(amount) ?: FluidStack.EMPTY

    fun potionFluid(potion: Holder<Potion>, bottleType: HTBottleType): HTFluidResourceType? =
        potionFluid(PotionContents(potion), bottleType)

    fun potionFluid(potion: Holder<Potion>, bottleType: HTBottleType, amount: Int): FluidStack =
        potionFluid(potion, bottleType)?.toStack(amount) ?: FluidStack.EMPTY

    //    Tag    //

    /**
     * 指定した[provider]から，[tagKey]に紐づいた[HTHolderLike]を取得します。
     * @param T レジストリの種類のクラス
     * @return [HTHolderLike]の[結果][HTTextResult]
     */
    abstract fun <T : Any> getFirstHolder(
        provider: HolderLookup.Provider?,
        tagKey: TagKey<T>,
    ): HTTextResult<HTHolderLike.HolderDelegate<T, T>>

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
