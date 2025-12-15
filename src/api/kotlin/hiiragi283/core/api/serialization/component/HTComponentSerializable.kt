package hiiragi283.core.api.serialization.component

import net.minecraft.core.component.DataComponentMap

interface HTComponentSerializable {
    fun applyComponents(input: HTComponentInput)

    fun collectComponents(builder: DataComponentMap.Builder)
}
