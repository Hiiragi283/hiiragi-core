package hiiragi283.core.impl.registry

import hiiragi283.core.api.registry.HTDeferredRegister
import net.minecraft.resources.Identifier
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.attachment.IAttachmentHolder
import net.neoforged.neoforge.common.util.ValueIOSerializable
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator

class HTDeferredAttachmentRegister(namespace: String) :
    HTDeferredRegister<AttachmentType<*>>(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, namespace) {
    fun <TYPE : Any> registerType(name: String, builder: AttachmentType.Builder<TYPE>): HTDeferredAttachmentType<TYPE> {
        delegate.register(name) { _: Identifier -> builder.build() }
        return HTDeferredAttachmentType(createId(name))
    }

    fun <TYPE : ValueIOSerializable> registerSerializable(
        name: String,
        factory: Function<IAttachmentHolder, TYPE>,
        operator: UnaryOperator<AttachmentType.Builder<TYPE>> = UnaryOperator.identity(),
    ): HTDeferredAttachmentType<TYPE> = registerType(name, AttachmentType.serializable(factory).let(operator::apply))

    fun <TYPE : ValueIOSerializable> registerSerializable(
        name: String,
        supplier: Supplier<TYPE>,
        operator: UnaryOperator<AttachmentType.Builder<TYPE>> = UnaryOperator.identity(),
    ): HTDeferredAttachmentType<TYPE> = registerType(name, AttachmentType.serializable(supplier).let(operator::apply))
}
