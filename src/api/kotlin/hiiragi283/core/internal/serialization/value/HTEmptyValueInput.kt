package hiiragi283.core.internal.serialization.value

import com.mojang.serialization.Codec
import hiiragi283.core.api.serialization.value.HTValueInput

internal object HTEmptyValueInput : HTValueInput {
    override fun <T : Any> read(key: String, codec: Codec<T>): T? = null

    override fun child(key: String): HTValueInput? = null

    override fun childOrEmpty(key: String): HTValueInput = this

    override fun childrenList(key: String): Iterable<HTValueInput>? = null

    override fun childrenListOrEmpty(key: String): Iterable<HTValueInput> = emptySet()

    override fun <T : Any> list(key: String, codec: Codec<T>): Iterable<T>? = null

    override fun <T : Any> listOrEmpty(key: String, codec: Codec<T>): Iterable<T> = emptySet()

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean = defaultValue

    override fun getByte(key: String, defaultValue: Byte): Byte = defaultValue

    override fun getShort(key: String, defaultValue: Short): Short = defaultValue

    override fun getInt(key: String): Int? = null

    override fun getInt(key: String, defaultValue: Int): Int = defaultValue

    override fun getLong(key: String): Long? = null

    override fun getLong(key: String, defaultValue: Long): Long = defaultValue

    override fun getFloat(key: String, defaultValue: Float): Float = defaultValue

    override fun getDouble(key: String, defaultValue: Double): Double = defaultValue

    override fun getString(key: String): String? = null

    override fun getString(key: String, defaultValue: String): String = defaultValue
}
