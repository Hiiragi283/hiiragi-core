package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTAbstractWidget
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.common.gui.sync.HTFractionSyncSlot
import hiiragi283.core.setup.HCWidgetTypes
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

class HTProgressWidget : HTAbstractWidget {
    private val syncSlot: HTFractionSyncSlot

    constructor(syncSlot: HTFractionSyncSlot, bounds: HTBounds) : super(bounds) {
        this.syncSlot = syncSlot
    }

    constructor(syncSlot: HTFractionSyncSlot, x: Int, y: Int, width: Int, height: Int) : super(x, y, width, height) {
        this.syncSlot = syncSlot
    }

    var texture: ResourceLocation? = null

    fun setTexture(texture: ResourceLocation): HTProgressWidget = apply {
        this.texture = texture
    }

    fun getProgress(): Fraction = syncSlot.amountAsFraction

    override fun getType(): HTWidgetType<HTProgressWidget> = HCWidgetTypes.PROGRESS.get()
}
