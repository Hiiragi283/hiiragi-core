package hiiragi283.core.client.gui

import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.client.gui.screens.MenuScreens

fun interface HTBlockEntityScreenFactory<BE : HTBlockEntity> :
    MenuScreens.ScreenConstructor<HTBlockEntityContainerMenu<BE>, HTBlockEntityContainerScreen<BE>>
