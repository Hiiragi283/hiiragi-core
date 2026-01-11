package hiiragi283.core.api.event

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.property.HTPropertyMap
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent

/**
 * 素材のプロパティを改変するイベントクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
class HTMaterialPropertyEvent(val isDataGen: Boolean, val factory: (HTMaterialKey) -> HTPropertyMap.Mutable) :
    Event(),
    IModBusEvent {
    /**
     * 指定された[key]に紐づくプロパティを改変します。
     * @param key 対象となる素材のキー
     * @param builderAction プロパティの改変を行うブロック
     */
    inline fun modify(key: HTMaterialKey, builderAction: HTPropertyMap.Mutable.() -> Unit) {
        factory(key).apply(builderAction)
    }
}
