package hiiragi283.core.api.serialization.component

interface DataComponentSerializable {
    fun applyComponents(getter: DataComponentGetter)

    fun collectComponents(setter: DataComponentSetter)

    interface Empty : DataComponentSerializable {
        override fun applyComponents(getter: DataComponentGetter) {}

        override fun collectComponents(setter: DataComponentSetter) {}
    }
}
