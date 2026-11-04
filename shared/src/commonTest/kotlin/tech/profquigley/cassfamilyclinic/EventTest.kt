package tech.profquigley.cassfamilyclinic

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EventTest {

    @Test
    fun formatsMorningAfternoonMidnightAndNoon() {
        assertEquals("11/04/2026 08:30 AM", formatEventDateTime("2026-11-04 08:30:00"))
        assertEquals("11/04/2026 04:00 PM", formatEventDateTime("2026-11-04 16:00:00"))
        assertEquals("01/02/2027 12:05 AM", formatEventDateTime("2027-01-02 00:05:00"))
        assertEquals("01/02/2027 12:00 PM", formatEventDateTime("2027-01-02 12:00:00"))
    }

    @Test
    fun leavesUnexpectedDateFormatUnchanged() {
        assertEquals("sometime", formatEventDateTime("sometime"))
    }

    @Test
    fun parsesEventsAndVenueVariants() {
        val json = """
            {"events": [
              {"title": "A", "url": "https://x/a", "start_date": "2026-11-04 08:30:00",
               "end_date": "2026-11-04 16:00:00", "extra": 1,
               "venue": {"address": "6810 US-12", "city": "Three Oaks", "state": "MI",
                         "stateprovince": "MI", "zip": "49128", "geo_lat": 41.79}},
              {"title": "B", "url": "https://x/b", "start_date": "2026-11-12 08:30:00",
               "end_date": "2026-11-12 16:00:00",
               "venue": {"address": "18035 M-60", "city": "Vandalia", "zip": "49095"}},
              {"title": "C", "url": "https://x/c", "start_date": "2026-11-17 08:30:00",
               "end_date": "2026-11-17 16:00:00", "venue": []}
            ]}
        """.trimIndent()
        val events = eventsJson.decodeFromString(EventsResponse.serializer(), json).events
        assertEquals(3, events.size)
        assertEquals("Three Oaks, MI 49128", events[0].venue?.cityLine)
        assertEquals("Vandalia 49095", events[1].venue?.cityLine)
        assertNull(events[2].venue)
    }

    @Test
    fun cityLineFallsBackToStateProvince() {
        assertEquals("Marcellus, Michigan 49067", Venue(city = "Marcellus", stateProvince = "Michigan", zip = "49067").cityLine)
    }
}
