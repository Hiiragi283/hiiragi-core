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
    companion object {
        /**
         * 何も処理を行わない[HTValueSerializable]のインスタンス
         * @since 21.1.1.0
         */
        @JvmField
        val NOTHING: HTValueSerializable = object : HTValueSerializable {
            override fun serialize(output: HTValueOutput): Unit = Unit

            override fun deserialize(input: HTValueInput): Unit = Unit
        }
    }

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
        val output: HTValueOutput = HTValueIOAccess.createOutput(provider, tag)
        serialize(output)
        return tag
    }

    /**
     * @suppress
     */
    @Deprecated("Use `deserialize(HTValueInput)` instead", level = DeprecationLevel.ERROR)
    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        val input: HTValueInput = HTValueIOAccess.createInput(provider, nbt)
        deserialize(input)
    }
}
