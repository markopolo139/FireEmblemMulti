package pl.ms.fire.emblem.business.values.character

import pl.ms.fire.emblem.business.utlis.Displayable
import pl.ms.fire.emblem.business.values.category.AttackCategory

enum class Stat(
    val category: AttackCategory
): Displayable {

    HEALTH(AttackCategory.NONE),
    STRENGTH(AttackCategory.PHYSICAL),
    DEFENSE(AttackCategory.PHYSICAL),
    MAGICK (AttackCategory.MAGICAL),
    RESISTANCE (AttackCategory.MAGICAL),
    SKILL (AttackCategory.NONE),
    LUCK (AttackCategory.NONE),
    SPEED (AttackCategory.NONE);

    override val displayName: String
        get() = name
}