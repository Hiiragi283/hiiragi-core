package hiiragi283.lib.material

import com.mojang.serialization.Codec
import hiiragi283.lib.HTRegistries
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.lib.tag.RawTagKey
import hiiragi283.lib.util.Ior
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

typealias HTMaterialRawEntry = Ior<HTMaterialItemEntry, TagKey<Item>>

class HTMaterialContents private constructor(val primalKey: HTMaterialPartKey, @PublishedApi internal var contents: Map<HTMaterialPartKey, HTMaterialRawEntry>) {
    companion object {
        @JvmField
        val DIRECT_CODEC: Codec<HTMaterialContents> = Codec.lazyInitialized(HTRegistries.MATERIAL_CONTENTS::byNameCodec)

        @JvmField
        val HOLDER_CODEC: Codec<Holder<HTMaterialContents>> = Codec.lazyInitialized(HTRegistries.MATERIAL_CONTENTS::holderByNameCodec)

        @JvmField
        val DIRECT_STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMaterialContents> = ByteBufCodecs.registry(HTRegistries.Keys.MATERIAL_CONTENTS)

        @JvmField
        val HOLDER_STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Holder<HTMaterialContents>> = HTStreamCodecs.holder(HTRegistries.Keys.MATERIAL_CONTENTS)

        @JvmStatic
        inline fun create(key: HTMaterialKey, primalKey: HTMaterialPartKey, builderAction: Builder.() -> Unit): HTMaterialContents = Builder(key, primalKey).apply(builderAction).build()
    }

    val primalEntry: HTMaterialRawEntry = getRawEntry(primalKey)!!

    fun getRawEntry(key: HTMaterialPartKey): HTMaterialRawEntry? = contents[key]

    fun getEntry(key: HTMaterialPartKey): HTMaterialItemEntry? = getRawEntry(key)?.getLeft()

    fun getTagKey(key: HTMaterialPartKey): TagKey<Item>? = getRawEntry(key)?.getRight()

    //    Builder    //

    class Builder(val key: HTMaterialKey, private val primalKey: HTMaterialPartKey) {
        private var contents: MutableMap<HTMaterialPartKey, HTMaterialRawEntry> = hashMapOf()

        fun add(part: HTMaterialPartKey, entry: HTMaterialItemEntry) {
            add(part, Ior.Left(entry))
        }

        fun add(part: HTMaterialPartKey, tagKey: TagKey<Item>) {
            add(part, Ior.Right(tagKey))
        }

        fun add(part: HTMaterialPartKey, tagKey: RawTagKey) {
            add(part, tagKey.create(Registries.ITEM))
        }

        fun add(part: HTMaterialPartKey, prefix: HTTagPrefix) {
            add(part, prefix.materialTag(key))
        }

        fun add(part: HTMaterialPartKey, entry: HTMaterialItemEntry, tagKey: TagKey<Item>) {
            add(part, Ior.Both(entry, tagKey))
        }

        fun add(part: HTMaterialPartKey, entry: HTMaterialItemEntry, tagKey: RawTagKey) {
            add(part, entry, tagKey.create(Registries.ITEM))
        }

        fun add(part: HTMaterialPartKey, entry: HTMaterialItemEntry, prefix: HTTagPrefix) {
            add(part, entry, prefix.materialTag(key))
        }

        private fun add(key: HTMaterialPartKey, value: HTMaterialRawEntry) {
            check(contents.put(key, value) == null) { "Duplicate material entry: ${key.name}" }
        }

        @PublishedApi
        internal fun build(): HTMaterialContents {
            check(primalKey in contents) { "Requires entry for primary part" }
            return HTMaterialContents(primalKey, contents.toSortedMap())
        }
    }
}
