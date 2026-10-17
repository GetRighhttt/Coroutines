package coroutines.coroutinebuilders

/**
 * Produces values lazily as the sequence is iterated.
 *
 * Each yield returns a value to the consumer. The next request resumes the sequence after
 * that yield. Use Flow when producing values requires suspending work.
 *
 * @author Stefan Bayne
 */
val sequenceOne = sequence {
    println("Generating first sequence")
    yield("Sequence one built...")
    println("Generating second sequence")
    yield("Sequence two built...")
    println("Generating third sequence")
    yield("Sequence three built...")
}

fun main() {
    for (value in sequenceOne) println(value)

}
