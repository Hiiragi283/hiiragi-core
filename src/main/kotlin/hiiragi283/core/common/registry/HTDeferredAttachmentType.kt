package hiiragi283.core.common.registry

import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.attachment.IAttachmentHolder
import net.neoforged.neoforge.registries.NeoForgeRegistries

class HTDeferredAttachmentType<TYPE : Any> : HTBasicHolderLike<AttachmentType<*>, AttachmentType<TYPE>> {
    constructor(key: ResourceKey<AttachmentType<*>>) : super(key)

    constructor(id: ResourceLocation) : super(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, id)

    @Suppress("UNCHECKED_CAST")
    override fun get(): AttachmentType<TYPE> = NeoForgeRegistries.ATTACHMENT_TYPES.getOrThrow(key) as AttachmentType<TYPE>

    fun hasData(holder: IAttachmentHolder): Boolean = holder.hasData(this)

    fun getData(holder: IAttachmentHolder): TYPE = holder.getData(this)

    fun getExistingData(holder: IAttachmentHolder): TYPE? = holder.getExistingDataOrNull(this)

    fun setData(holder: IAttachmentHolder, data: TYPE): TYPE? = holder.setData(this, data)

    fun removeData(holder: IAttachmentHolder): TYPE? = holder.removeData(this)

    override fun toString(): String = "HTDeferredAttachmentType(key=$key)"
}
