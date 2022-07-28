package pl.ms.fire.emblem.business.values.category

enum class WeaponCategory{
    SWORD {
        override val superiorCategory: WeaponCategory
            get() = AXE

        override val weakCategory: WeaponCategory
            get() = LANCE
    },
    LANCE {
        override val superiorCategory: WeaponCategory
            get() = SWORD

        override val weakCategory: WeaponCategory
            get() = AXE
    },
    BOW {
        override val superiorCategory: WeaponCategory
            get() = STAFF

        override val weakCategory: WeaponCategory
            get() = TOME
    },
    AXE{
        override val superiorCategory: WeaponCategory
            get() = LANCE

        override val weakCategory: WeaponCategory
            get() = SWORD
    },
    TOME {
        override val superiorCategory: WeaponCategory
            get() = BOW

        override val weakCategory: WeaponCategory
            get() = STAFF
    },
    STAFF {
        override val superiorCategory: WeaponCategory
            get() = TOME

        override val weakCategory: WeaponCategory
            get() = BOW
    };

    abstract val weakCategory: WeaponCategory
    abstract val superiorCategory: WeaponCategory
}