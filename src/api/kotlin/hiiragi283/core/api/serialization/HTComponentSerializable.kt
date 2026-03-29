package hiiragi283.core.api.serialization

import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap

interface HTComponentSerializable {
    fun applyComponents(input: DataComponentGetter)

    fun collectComponents(builder: DataComponentMap.Builder)

    interface Empty : HTComponentSerializable {
        override fun applyComponents(input: DataComponentGetter) {}

        override fun collectComponents(builder: DataComponentMap.Builder) {}
    }
}
