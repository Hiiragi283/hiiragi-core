package hiiragi283.core.api.material

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.util.HTTextResult
import hiiragi283.core.api.util.toTextResult
import hiiragi283.core.internal.material.HTMaterialContentsRegister
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation

/**
 * 素材のプロパティを管理するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.9.0
 */
interface HTMaterialManager : Iterable<HTMaterialManager.Entry> {
    companion object {
        @JvmStatic
        fun getInstance(): HTMaterialManager = HTMaterialContentsRegister.materialManager

        private fun errorMessage(key: HTMaterialKey): String = "Unregistered material: $key"

        @JvmField
        val CODEC: Codec<Entry> = HTMaterialKey.CODEC.comapFlatMap(
            { key: HTMaterialKey ->
                getInstance()
                    .asSequence()
                    .firstOrNull { it.key == key }
                    ?.let { DataResult.success(it) }
                    ?: DataResult.error { errorMessage(key) }
            },
            Entry::key,
        )

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, Entry> = HTMaterialKey.STREAM_CODEC.map(
            { key: HTMaterialKey -> getInstance().asSequence().firstOrNull { it.key == key } ?: errorMessage(key).let(::error) },
            Entry::key,
        )
    }

    /**
     * 指定した[素材][key]がプロパティを保持しているか判定します。
     */
    operator fun contains(key: HTMaterialKey): Boolean

    /**
     * 指定した[素材][key]のプロパティの一覧を取得します。
     * @return プロパティを保持していない場合は`null`
     */
    operator fun get(key: HTMaterialKey): HTPropertyMap?

    fun getResult(key: HTMaterialKey): HTTextResult<HTPropertyMap> = get(key).toTextResult { "Unregistered material: $key" }

    /**
     * 指定した[素材][key]のプロパティの一覧を取得します。
     * @return プロパティを保持していない場合は[HTPropertyMap.Empty]
     */
    fun getOrEmpty(key: HTMaterialKey): HTPropertyMap = get(key) ?: HTPropertyMap.Empty

    /**
     * 登録された[素材][HTMaterialKey]の一覧を取得します。
     */
    val keys: Set<HTMaterialKey>

    fun asSequence(): Sequence<Entry> = keys.asSequence().map { Entry(it, getOrEmpty(it)) }

    override fun iterator(): Iterator<Entry> = asSequence().iterator()

    data class Entry(val key: HTMaterialKey, val map: HTPropertyMap) :
        HTIdLike,
        HTPropertyGetter by map {
        override fun getId(): ResourceLocation = key.getId()
    }
}
