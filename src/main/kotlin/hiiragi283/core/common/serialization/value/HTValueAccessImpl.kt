package hiiragi283.core.common.serialization.value

import com.google.gson.JsonObject
import hiiragi283.core.api.serialization.value.HTValueAccess
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag

/**
 * @suppress
 */
class HTValueAccessImpl : HTValueAccess {
    override fun createInput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueInput =
        HTJsonValueInput.create(provider, jsonObject)

    override fun createOutput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueOutput =
        HTJsonValueOutput(provider, jsonObject)

    override fun createInput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueInput =
        HTTagValueInput.create(provider, compoundTag)

    override fun createOutput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueOutput =
        HTTagValueOutput(provider, compoundTag)
}
