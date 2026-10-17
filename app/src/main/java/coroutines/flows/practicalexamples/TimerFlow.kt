package coroutines.flows.practicalexamples

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.roundToLong
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/** Emits the elapsed time between ticks until the collector is cancelled. */
fun timeAndEmit(emissionsPerSecond: Float): Flow<Duration> {
    require(emissionsPerSecond > 0) { "emissionsPerSecond must be greater than zero" }

    return flow {
        var lastEmitTime = System.currentTimeMillis()
        emit(Duration.ZERO)

        while (true) {
            delay((1000L / emissionsPerSecond).roundToLong())

            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - lastEmitTime

            emit(elapsedTime.milliseconds)
            lastEmitTime = currentTime
        }
    }
}
