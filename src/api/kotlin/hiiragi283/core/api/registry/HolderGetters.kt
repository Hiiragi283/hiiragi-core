package hiiragi283.core.api.registry

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.RawTagKey
import hiiragi283.core.api.util.HTTextResult
import hiiragi283.core.api.util.flatMap
import hiiragi283.core.api.util.right
import java.util.Optional
import kotlin.jvm.optionals.getOrElse
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.registries.datamaps.DataMapType

fun <T : Any> HolderGetter<T>.getResult(key: ResourceKey<T>): HTTextResult<Holder<T>> = this.get(key).map(Holder<T>::right).getOrElse { HTTextResult("Missing element $key") }

fun <T : Any> HolderGetter<T>.getResult(key: TagKey<T>): HTTextResult<HolderSet<T>> = this.get(key).map(HolderSet<T>::right).getOrElse { HTTextResult("Missing tag $key") }

fun <T : Any, R : Any> HolderLookup<T>.forEachData(type: DataMapType<T, R>, action: (Holder<T>, R) -> Unit) {
    for (holder: Holder<T> in this.listElements()) {
        val data: R = holder.getData(type) ?: continue
        action(holder, data)
    }
}

fun <T : Any> HolderLookup.RegistryLookup<T>.get(prefix: HTTagPrefix, material: HTMaterialKey): Optional<HolderSet.Named<T>> = this.get(prefix.materialTag(material))

fun <T : Any> HolderLookup.RegistryLookup<T>.get(key: RawTagKey): Optional<HolderSet.Named<T>> = this.get(key.create(this.key().toRegistryKey()))

fun <T : Any> HolderLookup.RegistryLookup<T>.getResult(prefix: HTTagPrefix, material: HTMaterialKey): HTTextResult<HolderSet<T>> = this.getResult(prefix.materialTag(material))

fun <T : Any> HolderLookup.RegistryLookup<T>.getResult(key: RawTagKey): HTTextResult<HolderSet<T>> = this.getResult(key.create(this.key().toRegistryKey()))

//    Provider    //

fun <T : Any> HolderGetter.Provider.lookupResult(key: RegistryKey<T>): HTTextResult<HolderGetter<T>> = this.lookup(key).map(HolderGetter<T>::right).getOrElse { HTTextResult("Registry ${key.location()} not found") }

fun <T : Any> HolderGetter.Provider.getResult(key: ResourceKey<T>): HTTextResult<Holder<T>> = this.lookupResult(key.registryKey()).flatMap { it.getResult(key) }

fun <T : Any> HolderGetter.Provider.getResult(key: TagKey<T>): HTTextResult<HolderSet<T>> = this.lookupResult(key.registry()).flatMap { it.getResult(key) }

fun <T : Any> HolderLookup.Provider.lookupResult(key: RegistryKey<T>): HTTextResult<HolderLookup.RegistryLookup<T>> = this.lookup(key).map(HolderLookup.RegistryLookup<T>::right).getOrElse { HTTextResult("Registry ${key.location()} not found") }

fun <T : Any> RegistryAccess.lookupResult(key: RegistryKey<T>): HTTextResult<HolderLookup.RegistryLookup<T>> = this.lookup(key).map(HolderLookup.RegistryLookup<T>::right).getOrElse { HTTextResult("Registry ${key.location()} not found") }
