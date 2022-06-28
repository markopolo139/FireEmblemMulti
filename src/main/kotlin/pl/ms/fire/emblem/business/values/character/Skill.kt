package pl.ms.fire.emblem.business.values.character

enum class Skill {
    AETHER,
    IGNIS,
    AEGIS,
    LUNA,
    PAVISE,
    VANTAGE,
    ASTRA,
    SOL,
    LETHALITY,
    VENGEANCE,
    MIRACLE, //TODO: if calculated dmg is bigger than remaining hp set dmg to remaining hp - 1
}