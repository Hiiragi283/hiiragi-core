package hiiragi283.core.api.serialization.value

import com.google.gson.JsonObject
import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag

interface HTValueAccess {
    companion object {
        @JvmField
        val INSTANCE: HTValueAccess = HiiragiCoreAPI.getService()
    }

    fun createInput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueInput

    fun createOutput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueOutput

    fun createInput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueInput

    fun createOutput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueOutput
}
