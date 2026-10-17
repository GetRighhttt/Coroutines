package coroutines.flows.practicalexamples

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

class TimerFlowTest {
    @Test
    fun `timeAndEmit starts with zero duration`() = runBlocking {
        assertEquals(0.milliseconds, timeAndEmit(10f).first())
    }

    @Test
    fun `timeAndEmit rejects a non-positive rate`() {
        val exception = runCatching { timeAndEmit(0f) }.exceptionOrNull()

        assertTrue(exception is IllegalArgumentException)
    }
}
