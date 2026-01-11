package hiiragi283.core.api

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.common.util.INBTSerializable

interface HTDataSerializable : INBTSerializable<CompoundTag> {
    fun serializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag)

    /**
     * @suppress
     */
    @Deprecated("Use `serializeNBT(HolderLookup.Provider, CompoundTag)` instead", level = DeprecationLevel.ERROR)
    override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag {
        val tag = CompoundTag()
        serializeNBT(provider, tag)
        return tag
    }

    interface Empty : HTDataSerializable {
        override fun serializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {}

        override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {}
    }
}
