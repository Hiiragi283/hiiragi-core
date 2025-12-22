package hiiragi283.core.api.block.entity

import hiiragi283.core.api.serialization.component.HTComponentSerializable
import hiiragi283.core.api.serialization.value.HTValueSerializable

/**
 * @see mekanism.common.tile.component.ITileComponent
 */
interface HTBlockEntityComponent :
    HTValueSerializable,
    HTComponentSerializable
