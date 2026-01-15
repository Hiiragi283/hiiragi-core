package hiiragi283.core.api

import com.mojang.serialization.Codec
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.neoforged.neoforge.common.util.INBTSerializable
import java.util.function.BiConsumer
import java.util.function.Function

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
     * 何も値を読み書きしない[HTDataSerializable]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.7.0
     */
    interface Empty : HTDataSerializable {
        override fun serializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {}

        override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {}
    }

    /**
     * [Codec]に基づいた[HTDataSerializable]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.7.0
     */
    interface CodecBased : HTDataSerializable {
        fun serialize(ops: RegistryOps<Tag>, consumer: BiConsumer<String, Tag>)

        fun deserialize(ops: RegistryOps<Tag>, function: Function<String, Tag>)

        /**
         * @suppress
         */
        @Deprecated("Use `serialize(RegistryOps<Tag>, BiConsumer<String, Tag>)` instead", level = DeprecationLevel.ERROR)
        override fun serializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
            val ops: RegistryOps<Tag> = provider.createSerializationContext(NbtOps.INSTANCE)
            serialize(ops, nbt::put)
        }

        /**
         * @suppress
         */
        @Deprecated("Use `deserializeNBT(RegistryOps<Tag>, Function<String, Tag>)` instead", level = DeprecationLevel.ERROR)
        override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
            val ops: RegistryOps<Tag> = provider.createSerializationContext(NbtOps.INSTANCE)
            deserialize(ops) { key: String -> nbt.get(key) ?: CompoundTag() }
        }
    }
}
