package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.Text
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block

typealias HTSimpleDeferredBlock = HTDeferredBlock<Block>

class HTDeferredBlock<out BLOCK : Block> :
    HTDeferredHolder<Block, BLOCK>,
    HTIdLike.Translatable {
    constructor(key: ResourceKey<Block>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.BLOCK.createKey(id))

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Text = get().name
}
