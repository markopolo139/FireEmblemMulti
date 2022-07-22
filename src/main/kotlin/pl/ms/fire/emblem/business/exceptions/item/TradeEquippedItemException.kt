package pl.ms.fire.emblem.business.exceptions.item

import pl.ms.fire.emblem.business.exceptions.BusinessException

class TradeEquippedItemException: BusinessException("Can't trade equipped item")