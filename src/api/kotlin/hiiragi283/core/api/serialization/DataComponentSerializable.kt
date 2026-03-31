package hiiragi283.core.api.serialization

import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap

interface DataComponentSerializable {
    fun applyComponents(input: DataComponentGetter)

    fun collectComponents(builder: DataComponentMap.Builder)

    interface Empty : DataComponentSerializable {
        override fun applyComponents(input: DataComponentGetter) {}

        override fun collectComponents(builder: DataComponentMap.Builder) {}
    }
}
