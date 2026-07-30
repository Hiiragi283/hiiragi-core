package hiiragi283.core.api.material

import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.property.HTPropertyManager
import hiiragi283.core.api.resource.HTIdLike
import net.minecraft.resources.ResourceLocation

class HTMaterial internal constructor(override val key: HTMaterialKey, getter: HTPropertyGetter) :
    HTPropertyManager.Entry<HTMaterialKey>,
    HTIdLike,
    HTPropertyGetter by getter {
    override fun getId(): ResourceLocation = key.getId()

    override fun equals(other: Any?): Boolean = (other as? HTMaterial)?.key == this.key

    override fun hashCode(): Int = key.hashCode()

    override fun toString(): String = "HTMaterial(key=$key)"
}
