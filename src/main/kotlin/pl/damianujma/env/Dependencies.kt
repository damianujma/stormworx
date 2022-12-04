package pl.damianujma.env

import arrow.core.Either
import pl.damianujma.repo.alarmConditionsPersistence
import pl.damianujma.service.AlarmConditionsService
import pl.damianujma.service.alarmConditionsService


class Dependencies(val alarmConditionsService: AlarmConditionsService)

fun dependencies(env: Env) : Dependencies {
  when(sqlSetup(env.dataSource)) {
    is Either.Left -> throw Exception()
    is Either.Right -> {
      val alarmConditionsPersistence = alarmConditionsPersistence()
      val alarmConditionsService = alarmConditionsService(alarmConditionsPersistence)
      return Dependencies(alarmConditionsService)
    }
  }
}
