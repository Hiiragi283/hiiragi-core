package hiiragi283.core.api.data.map

import com.mojang.datafixers.util.Either
import com.mojang.serialization.JsonOps
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTServerResourceGenTask
import hiiragi283.core.api.resource.HTKeyLike
import net.mehvahdjukaar.moonlight.api.resources.ResType
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.common.conditions.ConditionalOps
import net.neoforged.neoforge.common.conditions.WithConditions
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.DataMapLoader
import net.neoforged.neoforge.registries.datamaps.DataMapEntry
import net.neoforged.neoforge.registries.datamaps.DataMapFile
import net.neoforged.neoforge.registries.datamaps.DataMapType
import java.util.Optional

/**
 * データマップを生成する[HTServerResourceGenTask]の抽象クラスです。
 * @param T 値のクラス
 * @param R レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 * @see DataMapProvider
 */
abstract class HTDataMapProvider<T : Any, R : Any>(private val type: DataMapType<R, T>) : HTServerResourceGenTask {
    private val registryKey: ResourceKey<Registry<R>> = type.registryKey()
    private val tagValues: MutableMap<TagKey<R>, DataMapEntry<T>> = hashMapOf()
    private val keyValues: MutableMap<ResourceKey<R>, DataMapEntry<T>> = hashMapOf()

    override fun accept(sink: ResourceSink) {
        gather()

        val file: DataMapFile<T, R> = DataMapFile(
            false,
            buildMap {
                for ((tagKey: TagKey<R>, value: DataMapEntry<T>) in tagValues) {
                    this[Either.left(tagKey)] = Optional.of(WithConditions(value))
                }
                for ((key: ResourceKey<R>, value: DataMapEntry<T>) in keyValues) {
                    this[Either.right(key)] = Optional.of(WithConditions(value))
                }
            },
            listOf(),
        )

        ConditionalOps
            .createConditionalCodecWithConditions(DataMapFile.codec(registryKey, type))
            .encodeStart(JsonOps.INSTANCE, Optional.of(WithConditions(file)))
            .ifSuccess {
                val folderLocation: String = DataMapLoader.getFolderLocation(type.registryKey().location())
                val id: ResourceLocation = type.id().withPrefix("${DataMapLoader.PATH}/$folderLocation/")
                HiiragiCoreAPI.LOGGER.debug("Data map path: {}", id)
                sink.addJson(id, it, ResType.JSON)
            }
    }

    protected abstract fun gather()

    //    Builder    //

    fun add(holder: HTKeyLike<R>, value: T, replace: Boolean = false) {
        this.add(holder.getResourceKey(), value, replace)
    }

    fun add(key: ResourceKey<R>, value: T, replace: Boolean = false) {
        keyValues[key] = DataMapEntry(value, replace)
    }

    fun add(tagKey: TagKey<R>, value: T, replace: Boolean = false) {
        tagValues[tagKey] = DataMapEntry(value, replace)
    }
}
