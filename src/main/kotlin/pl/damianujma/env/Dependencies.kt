package pl.damianujma.env

import arrow.core.Either
import arrow.core.right
import pl.damianujma.ConnectionError
import pl.damianujma.repo.alarmConditionsPersistence
import pl.damianujma.service.AlarmConditionsService
import pl.damianujma.service.alarmConditionsService


class Dependencies(val alarmConditionsService: AlarmConditionsService)

fun dependencies(env: Env): Either<ConnectionError, Dependencies> {
    return sqlSetup(env.dataSource).map {
        val alarmConditionsPersistence = alarmConditionsPersistence()
        val alarmConditionsService = alarmConditionsService(alarmConditionsPersistence)
        return Dependencies(alarmConditionsService).right()
    }
}
