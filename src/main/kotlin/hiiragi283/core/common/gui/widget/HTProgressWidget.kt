package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTAbstractWidget
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.gui.sync.HTFractionSyncSlot
import hiiragi283.core.setup.HCWidgetTypes
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

class HTProgressWidget : HTAbstractWidget {
    companion object {
        @JvmStatic
        fun createArrow(syncSlot: HTFractionSyncSlot, x: Int, y: Int): HTProgressWidget = HTProgressWidget(
            syncSlot,
            x,
            y,
            24,
            16,
        ).setTexture(HTConst.MINECRAFT.toId("textures", "gui", "sprites", "container", "furnace", "burn_progress.png"))
    }

    private val syncSlot: HTFractionSyncSlot

    constructor(syncSlot: HTFractionSyncSlot, bounds: HTBounds) : super(bounds) {
        this.syncSlot = syncSlot
    }

    constructor(syncSlot: HTFractionSyncSlot, x: Int, y: Int, width: Int, height: Int) : super(x, y, width, height) {
        this.syncSlot = syncSlot
    }

    var texture: ResourceLocation? = null

    fun setTexture(texture: ResourceLocation): HTProgressWidget = apply { this.texture = texture }

    var fillDirection = HTFillDirection.LEFT_TO_RIGHT

    fun setDirection(direction: HTFillDirection): HTProgressWidget = apply { this.fillDirection = direction }

    fun getProgress(): Fraction = syncSlot.amountAsFraction

    override fun getType(): HTWidgetType<HTProgressWidget> = HCWidgetTypes.PROGRESS.get()
}
