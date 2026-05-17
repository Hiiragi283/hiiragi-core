package hiiragi283.lib.material

import hiiragi283.lib.util.Ior
import net.minecraft.core.Holder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent

class HTModifyMaterialContentsEvent(val holder: Holder<HTMaterialContents>) :
    Event(),
    IModBusEvent {
    inline fun modify(contents: HTMaterialContents, action: HTMaterialContents.Builder.() -> Unit) {
        modify({ it.value() == contents }, action)
    }

    inline fun modify(filter: (Holder<HTMaterialContents>) -> Boolean, action: HTMaterialContents.Builder.() -> Unit) {
        if (filter(holder)) {
            val contents: HTMaterialContents = holder.value()
            val addition: HTMaterialContents = HTMaterialContents.create(contents.primalKey, action)

            val entryMap: MutableMap<HTMaterialPartKey, Ior<HTMaterialItemEntry, TagKey<Item>>> = contents.contents.toMutableMap()
            for ((key: HTMaterialPartKey, entry: Ior<HTMaterialItemEntry, TagKey<Item>>) in addition.contents) {
                if (entryMap[key] == null) {
                    entryMap[key] = entry
                }
            }
            contents.contents = entryMap
        }
    }
}
