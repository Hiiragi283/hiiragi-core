package hiiragi283.core.impl.serialization.value

import com.mojang.logging.LogUtils
import com.mojang.serialization.Codec
import hiiragi283.core.api.serialization.value.HTValueIOAccess
import hiiragi283.core.api.serialization.value.HTValueInput
import kotlin.jvm.optionals.getOrNull
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.NumericTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.minecraft.nbt.TagType
import net.minecraft.resources.RegistryOps
import org.slf4j.Logger

internal class HTTagValueInput(private val provider: HolderLookup.Provider, private val compoundTag: CompoundTag) : HTValueInput {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    private val registryOps: RegistryOps<Tag> = provider.createSerializationContext(NbtOps.INSTANCE)

    private inline fun <reified T : Tag> getTypedTag(key: String, type: TagType<T>): T? {
        val tagIn: Tag = compoundTag.get(key) ?: return null
        val tagType: TagType<*> = tagIn.type
        return if (tagType == type) tagIn as T else null
    }

    private fun getNumericTag(key: String): NumericTag? {
        val tagIn: Tag = compoundTag.get(key) ?: return null
        return tagIn as? NumericTag
    }

    //    HTNbtInput    //

    override fun <T : Any> read(key: String, codec: Codec<T>): T? {
        val tagIn: Tag = compoundTag.get(key) ?: return null
        return codec.parse(registryOps, tagIn).ifError { LOGGER.error(it.message()) }.result().getOrNull()
    }

    override fun child(key: String): HTValueInput? {
        val tagIn: CompoundTag = getTypedTag(key, CompoundTag.TYPE) ?: return null
        return HTValueIOAccess.createInput(provider, tagIn)
    }

    override fun childOrEmpty(key: String): HTValueInput = child(key) ?: HTEmptyValueInput

    override fun childrenList(key: String): Iterable<HTValueInput>? {
        val tagIn: ListTag = getTypedTag(key, ListTag.TYPE) ?: return null
        return when {
            tagIn.isEmpty() -> null
            else -> tagIn.filterIsInstance<CompoundTag>().map { HTValueIOAccess.createInput(provider, it) }
        }
    }

    override fun childrenListOrEmpty(key: String): Iterable<HTValueInput> = childrenList(key) ?: emptySet()

    override fun <T : Any> list(key: String, codec: Codec<T>): Iterable<T>? {
        val tagIn: ListTag = getTypedTag(key, ListTag.TYPE) ?: return null
        return when {
            tagIn.isEmpty() -> null
            else -> TypedInputList(provider, tagIn, codec)
        }
    }

    override fun <T : Any> listOrEmpty(key: String, codec: Codec<T>): Iterable<T> = list(key, codec) ?: emptySet()

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        val tagIn: NumericTag = getNumericTag(key) ?: return defaultValue
        return tagIn.asByte != 0.toByte()
    }

    override fun getByte(key: String, defaultValue: Byte): Byte {
        val tagIn: NumericTag = getNumericTag(key) ?: return defaultValue
        return tagIn.asByte
    }

    override fun getShort(key: String, defaultValue: Short): Short {
        val tagIn: NumericTag = getNumericTag(key) ?: return defaultValue
        return tagIn.asShort
    }

    override fun getInt(key: String): Int? {
        val tagIn: NumericTag = getNumericTag(key) ?: return null
        return tagIn.asInt
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        val tagIn: NumericTag = getNumericTag(key) ?: return defaultValue
        return tagIn.asInt
    }

    override fun getLong(key: String): Long? {
        val tagIn: NumericTag = getNumericTag(key) ?: return null
        return tagIn.asLong
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        val tagIn: NumericTag = getNumericTag(key) ?: return defaultValue
        return tagIn.asLong
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        val tagIn: NumericTag = getNumericTag(key) ?: return defaultValue
        return tagIn.asFloat
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        val tagIn: NumericTag = getNumericTag(key) ?: return defaultValue
        return tagIn.asDouble
    }

    override fun getString(key: String): String? {
        val tagIn: StringTag = getTypedTag(key, StringTag.TYPE) ?: return null
        return tagIn.asString
    }

    override fun getString(key: String, defaultValue: String): String = getString(key) ?: defaultValue

    //    TypedInputList    //

    private class TypedInputList<T : Any>(provider: HolderLookup.Provider, private val list: ListTag, private val codec: Codec<T>) : Iterable<T> {
        private val registryOps: RegistryOps<Tag> = provider.createSerializationContext(NbtOps.INSTANCE)

        override fun iterator(): Iterator<T> = list
            .mapNotNull { tag: Tag ->
                codec.parse(registryOps, tag).ifError { LOGGER.error(it.message()) }.result().getOrNull()
            }.iterator()
    }
}
