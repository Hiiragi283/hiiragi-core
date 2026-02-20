package hiiragi283.core.common.registry

import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block

class HTDeferredOnlyBlock<BLOCK : Block> :
    HTDeferredHolder<Block, BLOCK>,
    HTHasTranslationKey,
    HTHasText {
    constructor(key: ResourceKey<Block>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.BLOCK, id)

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Component = get().name
}
