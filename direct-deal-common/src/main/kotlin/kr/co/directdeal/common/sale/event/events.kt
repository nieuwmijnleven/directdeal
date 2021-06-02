package kr.co.directdeal.common.sale.event

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ItemRecoveredEvent(@TargetAggregateIdentifier var id: String)
