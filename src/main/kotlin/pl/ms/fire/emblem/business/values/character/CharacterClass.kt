package pl.ms.fire.emblem.business.values.character

import pl.ms.fire.emblem.business.utlis.Displayable
import pl.ms.fire.emblem.business.values.category.WeaponCategory

enum class CharacterClass(
    val allowedWeapons: List<WeaponCategory>,
    val movement: Int,
    val boostStats: Map<Stat, Int>,
    val skills: List<Skill>
) : Displayable{
    PALADIN(
        allowedWeapons = listOf(WeaponCategory.SWORD, WeaponCategory.LANCE),
        movement = 8,
        boostStats = mapOf(
            Stat.HEALTH to 25, Stat.STRENGTH to 9, Stat.MAGICK to 1, Stat.SKILL to 7,
            Stat.SPEED to 8, Stat.DEFENSE to 10, Stat.RESISTANCE to 6,
        ),
        skills = listOf(Skill.LUNA, Skill.AEGIS)
    ) {
        override val displayName: String
            get() = "Paladin"
    },
    GREAT_KNIGHT(
        allowedWeapons = listOf(WeaponCategory.SWORD, WeaponCategory.LANCE, WeaponCategory.AXE),
        movement = 7,
        boostStats = mapOf(
            Stat.HEALTH to 26, Stat.STRENGTH to 11, Stat.SKILL to 6,
            Stat.SPEED to 5, Stat.DEFENSE to 14, Stat.RESISTANCE to 1,
        ),
        skills = listOf(Skill.LUNA, Skill.PAVISE)
    ) {
        override val displayName: String
            get() = "Great Knight"
    },
    GENERAL(
        allowedWeapons = listOf(WeaponCategory.AXE, WeaponCategory.LANCE),
        movement = 5,
        boostStats = mapOf(
            Stat.HEALTH to 28, Stat.STRENGTH to 12, Stat.SKILL to 7,
            Stat.SPEED to 4, Stat.DEFENSE to 15, Stat.RESISTANCE to 3,
        ),
        skills = listOf(Skill.LUNA, Skill.PAVISE)
    ) {
        override val displayName: String
            get() = "General"
    },
    SWORDMASTER(
        allowedWeapons = listOf(WeaponCategory.SWORD),
        movement = 6,
        boostStats = mapOf(
            Stat.HEALTH to 20, Stat.STRENGTH to 7, Stat.MAGICK to 2, Stat.SKILL to 11,
            Stat.SPEED to 13, Stat.DEFENSE to 6, Stat.RESISTANCE to 4,
        ),
        skills = listOf(Skill.ASTRA, Skill.VANTAGE, Skill.AETHER)
    ) {
        override val displayName: String
            get() = "Swordmaster"
    },
    HERO(
        allowedWeapons = listOf(WeaponCategory.SWORD, WeaponCategory.AXE),
        movement = 6,
        boostStats = mapOf(
            Stat.HEALTH to 22, Stat.STRENGTH to 8, Stat.MAGICK to 1, Stat.SKILL to 11,
            Stat.SPEED to 10, Stat.DEFENSE to 8, Stat.RESISTANCE to 3,
        ),
        skills = listOf(Skill.SOL, Skill.LUNA)
    ) {
        override val displayName: String
            get() = "Hero"
    },
    WARRIOR(
        allowedWeapons = listOf(WeaponCategory.AXE, WeaponCategory.BOW),
        movement = 6,
        boostStats = mapOf(
            Stat.HEALTH to 28, Stat.STRENGTH to 12, Stat.SKILL to 8,
            Stat.SPEED to 7, Stat.DEFENSE to 7, Stat.RESISTANCE to 3,
        ),
        skills = listOf(Skill.LUNA, Skill.PAVISE)
    ) {
        override val displayName: String
            get() = "Warrior"
    },
    SNIPER(
        allowedWeapons = listOf(WeaponCategory.SWORD, WeaponCategory.LANCE),
        movement = 6,
        boostStats = mapOf(
            Stat.HEALTH to 20, Stat.STRENGTH to 7, Stat.MAGICK to 1, Stat.SKILL to 15,
            Stat.SPEED to 9, Stat.DEFENSE to 10, Stat.RESISTANCE to 3,
        ),
        skills = listOf(Skill.LETHALITY)
    ) {
        override val displayName: String
            get() = "Sniper"
    },
    TRICKSTER(
        allowedWeapons = listOf(WeaponCategory.SWORD, WeaponCategory.STAFF),
        movement = 6,
        boostStats = mapOf(
            Stat.HEALTH to 19, Stat.STRENGTH to 4, Stat.MAGICK to 4, Stat.SKILL to 12,
            Stat.SPEED to 11, Stat.DEFENSE to 3, Stat.RESISTANCE to 5,
        ),
        skills = listOf(Skill.LETHALITY, Skill.MIRACLE)
    ) {
        override val displayName: String
            get() = "Trickster"
    },
    FALCON_KNIGHT(
        allowedWeapons = listOf(WeaponCategory.STAFF, WeaponCategory.LANCE),
        movement = 8,
        boostStats = mapOf(
            Stat.HEALTH to 20, Stat.STRENGTH to 6, Stat.MAGICK to 3, Stat.SKILL to 10,
            Stat.SPEED to 11, Stat.DEFENSE to 6, Stat.RESISTANCE to 9,
        ),
        skills = listOf(Skill.MIRACLE, Skill.AETHER)
    ) {
        override val displayName: String
            get() = "Falcon Knight"
    },
    GRIFFON_RIDER(
        allowedWeapons = listOf(WeaponCategory.AXE),
        movement = 8,
        boostStats = mapOf(
            Stat.HEALTH to 22, Stat.STRENGTH to 9, Stat.SKILL to 10,
            Stat.SPEED to 9, Stat.DEFENSE to 8, Stat.RESISTANCE to 3,
        ),
        skills = listOf(Skill.LUNA, Skill.SOL)
    ) {
        override val displayName: String
            get() = "Griffon Rider"
    },
    SAGE(
        allowedWeapons = listOf(WeaponCategory.TOME, WeaponCategory.STAFF),
        movement = 6,
        boostStats = mapOf(
            Stat.HEALTH to 20, Stat.STRENGTH to 1, Stat.MAGICK to 7, Stat.SKILL to 5,
            Stat.SPEED to 7, Stat.DEFENSE to 4, Stat.RESISTANCE to 5,
        ),
        skills = listOf(Skill.VENGEANCE, Skill.IGNIS, Skill.MIRACLE)
    ) {
        override val displayName: String
            get() = "Sage"
    },
    DARK_KNIGHT(
        allowedWeapons = listOf(WeaponCategory.SWORD, WeaponCategory.TOME),
        movement = 8,
        boostStats = mapOf(
            Stat.HEALTH to 25, Stat.STRENGTH to 4, Stat.MAGICK to 5, Stat.SKILL to 6,
            Stat.SPEED to 5, Stat.DEFENSE to 9, Stat.RESISTANCE to 5,
        ),
        skills = listOf(Skill.IGNIS, Skill.AEGIS, Skill.PAVISE)
    ) {
        override val displayName: String
            get() = "Dark Knight"
    },
    VALKYRIE(
        allowedWeapons = listOf(WeaponCategory.TOME, WeaponCategory.STAFF),
        movement = 8,
        boostStats = mapOf(
            Stat.HEALTH to 19, Stat.MAGICK to 5, Stat.SKILL to 4,
            Stat.SPEED to 8, Stat.DEFENSE to 3, Stat.RESISTANCE to 8,
        ),
        skills = listOf(Skill.MIRACLE)
    ) {
        override val displayName: String
            get() = "Valkyrie"
    };
}