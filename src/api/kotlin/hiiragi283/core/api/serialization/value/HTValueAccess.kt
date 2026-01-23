package hiiragi283.core.api.serialization.value

import com.google.gson.JsonObject
import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag

/**
 * [HTValueInput]と[HTValueOutput]のインスタンスを作成するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTValueAccess {
    companion object {
        /**
         * [HTValueAccess]のインスタンス
         */
        @JvmField
        val INSTANCE: HTValueAccess = HiiragiCoreAPI.getService()
    }

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
}
