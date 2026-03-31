package hiiragi283.core.api.serialization

import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.common.util.ValueIOSerializable

interface EmptyValueIOSerializable : ValueIOSerializable {
    override fun deserialize(input: ValueInput) {}

    override fun serialize(output: ValueOutput) {}
}
