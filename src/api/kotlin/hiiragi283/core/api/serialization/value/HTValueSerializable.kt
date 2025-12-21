package hiiragi283.core.api.serialization.value

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.common.util.INBTSerializable

/**
 * [HTValueInput]と[HTValueOutput]に対応した[INBTSerializable]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTValueSerializable : INBTSerializable<CompoundTag> {
    /**
     * [output]に値を書き込みます。
     */
    fun serialize(output: HTValueOutput)

    /**
     * [input]から値を読み取ります。
     */
    fun deserialize(input: HTValueInput)

    /**
     * @suppress
     */
    @Deprecated("Use `serialize(HTValueOutput)` instead", level = DeprecationLevel.ERROR)
    override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag {
        val tag = CompoundTag()
        val output: HTValueOutput = HTValueAccess.INSTANCE.createOutput(provider, tag)
        serialize(output)
        return tag
    }

    /**
     * @suppress
     */
    @Deprecated("Use `deserialize(HTValueInput)` instead", level = DeprecationLevel.ERROR)
    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        val input: HTValueInput = HTValueAccess.INSTANCE.createInput(provider, nbt)
        deserialize(input)
    }

    /**
     * 何も値を読み書きしないことを表すインターフェースです。
     */
    interface Empty : HTValueSerializable {
        override fun serialize(output: HTValueOutput) {}

        override fun deserialize(input: HTValueInput) {}
    }
}
