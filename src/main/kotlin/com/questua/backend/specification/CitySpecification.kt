package com.questua.backend.specification

import com.questua.backend.filter.CityFilter
import com.questua.backend.model.City
import org.springframework.data.jpa.domain.Specification
import java.util.UUID

object CitySpecification {

    fun fromFilter(filter: CityFilter): Specification<City> {
        var spec: Specification<City> = Specification { _, _, cb -> cb.conjunction() }

        spec = spec.and(cityName(filter.cityName))
        spec = spec.and(countryCode(filter.countryCode))
        spec = spec.and(languageId(filter.languageId))
        spec = spec.and(premium(filter.isPremium))
        spec = spec.and(ai(filter.isAiGenerated)) 
        spec = spec.and(isPublished(filter.isPublished))

        return spec
    }

    private fun cityName(value: String?): Specification<City>? =
        value?.let {
            Specification { root, _, cb ->
                cb.like(cb.lower(root.get("cityName")), "%${it.lowercase()}%")
            }
        }

    private fun countryCode(value: String?): Specification<City>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<String>("countryCode"), it)
            }
        }

    private fun languageId(value: UUID?): Specification<City>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<UUID>("languageId"), it)
            }
        }

    private fun premium(value: Boolean?): Specification<City>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<Boolean>("isPremium"), it)
            }
        }

    private fun ai(value: Boolean?): Specification<City>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<Boolean>("isAiGenerated"), it)
            }
        }

    private fun isPublished(value: Boolean?): Specification<City>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<Boolean>("isPublished"), it)
            }
        }
}