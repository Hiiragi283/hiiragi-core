package hiiragi283.lib.material

import com.mojang.serialization.Codec
import hiiragi283.lib.HTRegistries
import hiiragi283.lib.tag.createTagKey
import hiiragi283.lib.util.Ior
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

class HTMaterialContents private constructor(
    val primalKey: HTMaterialPartKey,
    @PublishedApi internal var contents: Map<HTMaterialPartKey, Ior<HTMaterialItemEntry, TagKey<Item>>>,
) {
    companion object {
        @JvmField
        val DIRECT_CODEC: Codec<HTMaterialContents> = Codec.lazyInitialized(HTRegistries.MATERIAL_CONTENTS::byNameCodec)

        @JvmField
        val HOLDER_CODEC: Codec<Holder<HTMaterialContents>> = Codec.lazyInitialized(HTRegistries.MATERIAL_CONTENTS::holderByNameCodec)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMaterialContents> = ByteBufCodecs.registry(HTRegistries.Keys.MATERIAL_CONTENTS)

        @JvmStatic
        inline fun create(primalKey: HTMaterialPartKey, builderAction: Builder.() -> Unit): HTMaterialContents = Builder(primalKey).apply(builderAction).build()
    }

    val primalEntry: Ior<HTMaterialItemEntry, TagKey<Item>> = getRawEntry(primalKey)!!

    fun getRawEntry(key: HTMaterialPartKey): Ior<HTMaterialItemEntry, TagKey<Item>>? = contents[key]

    fun getEntry(key: HTMaterialPartKey): HTMaterialItemEntry? = getRawEntry(key)?.getLeft()

    fun getTagKey(key: HTMaterialPartKey): TagKey<Item>? = getRawEntry(key)?.getRight()

    fun getBlockTagKey(key: HTMaterialPartKey): TagKey<Block>? = getTagKey(key)?.let { Registries.BLOCK.createTagKey(it.location()) }

    //    Builder    //

    class Builder(private val primalKey: HTMaterialPartKey) {
        private var contents: MutableMap<HTMaterialPartKey, Ior<HTMaterialItemEntry, TagKey<Item>>> = hashMapOf()

        fun add(key: HTMaterialPartKey, entry: HTMaterialItemEntry) {
            contents[key] = Ior.Left(entry)
        }

        fun add(key: HTMaterialPartKey, tagKey: TagKey<Item>) {
            contents[key] = Ior.Right(tagKey)
        }

        fun add(key: HTMaterialPartKey, entry: HTMaterialItemEntry, tagKey: TagKey<Item>) {
            contents[key] = Ior.Both(entry, tagKey)
        }

        @PublishedApi
        internal fun build(): HTMaterialContents {
            check(primalKey in contents) { "Requires entry for primary part" }
            return HTMaterialContents(primalKey, contents.toSortedMap())
        }
    }
}
