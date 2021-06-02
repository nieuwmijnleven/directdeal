package kr.co.directdeal.saleservice.api

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ItemDeleteCommand(@TargetAggregateIdentifier var id: String)
data class ItemDeleteEvent(@TargetAggregateIdentifier var id: String)