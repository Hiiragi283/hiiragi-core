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

class HTDeferredAttachmentRegister(namespace: String) :
    HTDeferredRegister<AttachmentType<*>>(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, namespace) {
    fun <TYPE : Any> registerType(name: String, supplier: Supplier<AttachmentType.Builder<TYPE>>): HTDeferredAttachmentType<TYPE> {
        register(name) { _: ResourceLocation -> supplier.get().build() }
        return HTDeferredAttachmentType(createId(name))
    }

    fun <TYPE : INBTSerializable<CompoundTag>> registerSerializable(
        name: String,
        factory: Function<IAttachmentHolder, TYPE>,
    ): HTDeferredAttachmentType<TYPE> = registerType(name) { AttachmentType.serializable(factory) }

    fun <TYPE : INBTSerializable<CompoundTag>> registerSerializable(
        name: String,
        supplier: Supplier<TYPE>,
    ): HTDeferredAttachmentType<TYPE> = registerType(name) { AttachmentType.serializable(supplier) }
}
