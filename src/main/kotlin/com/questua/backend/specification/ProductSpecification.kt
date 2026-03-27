package com.questua.backend.specification

import com.questua.backend.filter.ProductFilter
import com.questua.backend.model.Product
import com.questua.backend.model.TargetType
import org.springframework.data.jpa.domain.Specification
import java.util.UUID

object ProductSpecification {

    fun fromFilter(filter: ProductFilter): Specification<Product> {
        var spec: Specification<Product> =
            Specification { root, _, cb -> cb.conjunction() }

        spec = spec.and(sku(filter.sku))
        spec = spec.and(title(filter.title))
        spec = spec.and(targetType(filter.targetType))
        spec = spec.and(targetId(filter.targetId))
        spec = spec.and(currency(filter.currency))

        return spec
    }

    private fun sku(value: String?): Specification<Product>? =
        value?.let {
            Specification { root, _, cb ->
                cb.like(cb.lower(root.get("sku")), "%${it.lowercase()}%")
            }
        }

    private fun title(value: String?): Specification<Product>? =
        value?.let {
            Specification { root, _, cb ->
                cb.like(cb.lower(root.get("title")), "%${it.lowercase()}%")
            }
        }

    private fun targetType(value: TargetType?): Specification<Product>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<TargetType>("targetType"), it) 
            }
        } 

    private fun targetId(value: UUID?): Specification<Product>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<UUID>("targetId"), it)
            }
        }

    private fun currency(value: String?): Specification<Product>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(cb.lower(root.get("currency")), it.lowercase())
            }
        }
}
