package hiiragi283.core.api.serialization.value

import com.google.gson.JsonObject
import hiiragi283.core.impl.serialization.value.HTEmptyValueInput
import hiiragi283.core.impl.serialization.value.HTJsonValueInput
import hiiragi283.core.impl.serialization.value.HTJsonValueOutput
import hiiragi283.core.impl.serialization.value.HTTagValueInput
import hiiragi283.core.impl.serialization.value.HTTagValueOutput
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag

data object HTValueIOAccess {
    /**
     * 指定した[レジストリ][provider]と[JSON][jsonObject]から[HTValueInput]を作成します。
     */
    fun createInput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueInput = when {
        jsonObject.isEmpty -> HTEmptyValueInput
        else -> HTJsonValueInput(provider, jsonObject)
    }

    /**
     * 指定した[レジストリ][provider]と[JSON][jsonObject]から[HTValueOutput]を作成します。
     */
    fun createOutput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueOutput = HTJsonValueOutput(provider, jsonObject)

    /**
     * 指定した[レジストリ][provider]と[NBT][compoundTag]から[HTValueInput]を作成します。
     */
    fun createInput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueInput = when {
        compoundTag.isEmpty -> HTEmptyValueInput
        else -> HTTagValueInput(provider, compoundTag)
    }

    /**
     * 指定した[レジストリ][provider]と[NBT][compoundTag]から[HTValueOutput]を作成します。
     */
    fun createOutput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueOutput = HTTagValueOutput(provider, compoundTag)
}
