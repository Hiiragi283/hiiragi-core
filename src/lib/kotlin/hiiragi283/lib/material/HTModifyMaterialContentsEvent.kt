@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.material

import hiiragi283.lib.registry.getKeyOrThrow
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.Holder
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent

class HTModifyMaterialContentsEvent(val holder: Holder<HTMaterialContents>) :
    Event(),
    IModBusEvent {
    inline fun modify(contents: HTMaterialContents, action: HTMaterialContents.Builder.() -> Unit) {
        modify({ it.value() == contents }, action)
    }

    inline fun modify(filter: (Holder<HTMaterialContents>) -> Boolean, action: HTMaterialContents.Builder.() -> Unit) {
        contract {
            callsInPlace(filter, InvocationKind.EXACTLY_ONCE)
        }
        if (filter(holder)) {
            val contents: HTMaterialContents = holder.value()
            val addition: HTMaterialContents = HTMaterialContents.create(holder.getKeyOrThrow(), contents.primalKey, action)

            val entryMap: MutableMap<HTMaterialPartKey, HTMaterialRawEntry> = contents.contents.toMutableMap()
            for ((key: HTMaterialPartKey, entry: HTMaterialRawEntry) in addition.contents) {
                if (entryMap[key] == null) {
                    entryMap[key] = entry
                }
            }
            contents.contents = entryMap
        }
    }
}
