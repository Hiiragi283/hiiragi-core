package hiiragi283.core.api.integration.ae2.storage

import appeng.api.stacks.AEFluidKey
import appeng.api.stacks.AEItemKey
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource

//    Fluid    //

fun AEFluidKey.toResource(): HTFluidResourceType? = this.toStack(1).toResource()

fun HTFluidResourceType.toAEKey(): AEFluidKey? = this.toStack(1).let(AEFluidKey::of)

//    Item    //

fun AEItemKey.toResource(): HTItemResourceType? = this.toStack(1).toResource()

fun HTItemResourceType.toAEKey(): AEItemKey? = this.toStack(1).let(AEItemKey::of)
