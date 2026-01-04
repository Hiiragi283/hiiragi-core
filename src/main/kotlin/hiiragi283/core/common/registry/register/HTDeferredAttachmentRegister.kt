package hiiragi283.core.common.registry.register

import hiiragi283.core.api.registry.HTDeferredRegister
import hiiragi283.core.common.registry.HTDeferredAttachmentType
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.attachment.IAttachmentHolder
import net.neoforged.neoforge.common.util.INBTSerializable
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator

class HTDeferredAttachmentRegister(namespace: String) :
    HTDeferredRegister<AttachmentType<*>>(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, namespace) {
    fun <TYPE : Any> registerType(name: String, builder: AttachmentType.Builder<TYPE>): HTDeferredAttachmentType<TYPE> {
        register(name) { _: ResourceLocation -> builder.build() }
        return HTDeferredAttachmentType(createId(name))
    }

    fun <TYPE : INBTSerializable<CompoundTag>> registerSerializable(
        name: String,
        factory: Function<IAttachmentHolder, TYPE>,
        operator: UnaryOperator<AttachmentType.Builder<TYPE>> = UnaryOperator.identity(),
    ): HTDeferredAttachmentType<TYPE> = registerType(name, AttachmentType.serializable(factory).let(operator::apply))

    fun <TYPE : INBTSerializable<CompoundTag>> registerSerializable(
        name: String,
        supplier: Supplier<TYPE>,
        operator: UnaryOperator<AttachmentType.Builder<TYPE>> = UnaryOperator.identity(),
    ): HTDeferredAttachmentType<TYPE> = registerType(name, AttachmentType.serializable(supplier).let(operator::apply))
}
