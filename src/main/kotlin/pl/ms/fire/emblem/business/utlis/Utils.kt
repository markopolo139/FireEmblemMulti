package pl.ms.fire.emblem.business.utlis

import pl.ms.fire.emblem.business.values.character.Stat

fun Map<Stat, Int>.getStat(stat: Stat) = get(stat) ?: 0