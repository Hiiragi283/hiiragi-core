package hiiragi283.core.api

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.common.util.INBTSerializable

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[INBTSerializable]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
interface HTDataSerializable : INBTSerializable<CompoundTag> {
    /**
     * [nbt]に値を書き込みます。
     */
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

    /**
     * 何も値を読み書きしないことを表すインターフェースです。
     */
    interface Empty : HTDataSerializable {
        override fun serializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {}

        override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {}
    }
}
