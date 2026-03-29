package hiiragi283.core.impl.block

import hiiragi283.core.common.block.HTBlockWithEntity
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import net.minecraft.world.level.block.Block

open class HTBasicEntityBlock(protected val type: HTDeferredBlockEntityType<*>, properties: Properties) :
    Block(properties),
    HTBlockWithEntity {
    override fun getBlockEntityType(): HTDeferredBlockEntityType<*> = type
}
