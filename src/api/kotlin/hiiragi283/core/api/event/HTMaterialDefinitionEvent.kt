package hiiragi283.core.api.event

import hiiragi283.core.api.material.HTMaterialDefinition
import hiiragi283.core.api.material.HTMaterialKey
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent

/**
 * 素材の属性を改変するイベントクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
class HTMaterialDefinitionEvent(val isDataGen: Boolean, val factory: (HTMaterialKey) -> HTMaterialDefinition.Builder) :
    Event(),
    IModBusEvent {
    /**
     * 指定された[key]に紐づく属性を改変します。
     * @param key 対象となる素材のキー
     * @param builderAction 属性の改変を行うブロック
     */
    inline fun modify(key: HTMaterialKey, builderAction: HTMaterialDefinition.Builder.() -> Unit) {
        factory(key).apply(builderAction)
    }
}
