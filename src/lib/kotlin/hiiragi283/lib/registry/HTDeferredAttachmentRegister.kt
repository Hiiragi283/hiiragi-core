package hiiragi283.lib.registry

import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.attachment.IAttachmentHolder
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator
import net.neoforged.neoforge.common.util.ValueIOSerializable

class HTDeferredAttachmentRegister(namespace: String) : HTDeferredRegister<AttachmentType<*>>(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, namespace) {
    fun <TYPE : Any> registerType(name: String, builder: AttachmentType.Builder<TYPE>): HTDeferredAttachmentType<TYPE> {
        this.register(name) { _ -> builder.build() }
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
