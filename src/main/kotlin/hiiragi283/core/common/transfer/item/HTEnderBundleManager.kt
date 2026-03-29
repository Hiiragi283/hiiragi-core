package hiiragi283.core.common.transfer.item

import hiiragi283.core.api.HTDyeColor
import hiiragi283.core.api.transfer.ItemResourceHandler
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.common.util.ValueIOSerializable

class HTEnderBundleManager private constructor(private val map: MutableMap<HTDyeColor, ItemResourceHandler>): ValueIOSerializable {
    override fun serialize(output: ValueOutput) {
        TODO("Not yet implemented")
    }

    override fun deserialize(input: ValueInput) {
        TODO("Not yet implemented")
    }
}
