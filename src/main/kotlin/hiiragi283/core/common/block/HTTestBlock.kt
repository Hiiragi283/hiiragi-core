package hiiragi283.core.common.block

import hiiragi283.core.setup.HCBlockEntityTypes

class HTTestBlock(properties: Properties) :
    HTBasicEntityBlock(HCBlockEntityTypes.TEST, properties),
    HTBlockWithModularUI
