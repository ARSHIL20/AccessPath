package com.example.data

import com.example.R
import com.example.model.AccessibilityCategory
import com.example.model.Place
import com.example.model.Review

object SamplePlacesData {

    val initialPlaces: List<Place> = listOf(
        Place(
            id = "place_1",
            name = "Grand Central Transit Concourse",
            category = "Transit Hub",
            address = "450 Concourse Way",
            neighborhood = "Downtown Core",
            description = "Major rail and metro terminus featuring level boarding platforms, audible train announcements, and tactile wayfinding strips throughout.",
            supportedTags = setOf(
                AccessibilityCategory.STEP_FREE,
                AccessibilityCategory.LIFTS,
                AccessibilityCategory.ACCESSIBLE_WASHROOMS,
                AccessibilityCategory.SEATING,
                AccessibilityCategory.WAYFINDING
            ),
            amenityDetails = listOf(
                "Zero-step level boarding from platform to carriages",
                "Four dual-capacity glass elevators connecting mezzanine and platforms",
                "Universal accessible restroom with emergency pull-cord alarm",
                "Dedicated wheelchair waiting bays with AC and USB power",
                "Continuous high-contrast yellow tactile guidance paving"
            ),
            openingHours = "24 Hours Daily",
            imageDrawableRes = R.drawable.img_transit_hub_1791713415968,
            mapX = 0.48f,
            mapY = 0.45f,
            lat = 37.7892,
            lng = -122.4014
        ),
        Place(
            id = "place_2",
            name = "Civic Centre Modern Art Museum",
            category = "Art Museum",
            address = "120 Cultural Plaza",
            neighborhood = "Arts District",
            description = "Contemporary art museum designed with wide continuous galleries, step-free access, gentle ramps, and touchable tactile art models.",
            supportedTags = setOf(
                AccessibilityCategory.STEP_FREE,
                AccessibilityCategory.LIFTS,
                AccessibilityCategory.ACCESSIBLE_WASHROOMS,
                AccessibilityCategory.WAYFINDING
            ),
            amenityDetails = listOf(
                "Gradual 1:20 incline ramp at main foyer with automatic push plates",
                "High-contrast gallery room numbering with Braille plaques",
                "Two spacious elevators with voice annunciators on all floors",
                "Sensory-friendly morning hours with reduced lighting and low sound",
                "Roll-under interactive touch screens at accessible 75cm height"
            ),
            openingHours = "10:00 AM – 6:00 PM (Tue–Sun)",
            imageDrawableRes = R.drawable.img_art_museum_1791713436963,
            mapX = 0.35f,
            mapY = 0.38f,
            lat = 37.7794,
            lng = -122.4182
        ),
        Place(
            id = "place_3",
            name = "Harborview Waterfront Gardens",
            category = "City Park",
            address = "88 Marina Shoreline",
            neighborhood = "Harbor District",
            description = "Sprawling public park with smooth asphalt waterfront paths, sensory herb garden, and accessible wooden viewing boardwalks.",
            supportedTags = setOf(
                AccessibilityCategory.STEP_FREE,
                AccessibilityCategory.SEATING,
                AccessibilityCategory.ACCESSIBLE_WASHROOMS
            ),
            amenityDetails = listOf(
                "3.5 km completely flat, smoothly paved coastal pathway",
                "Ergonomic rest benches with backrests and armrests every 60 meters",
                "Accessible family restroom near the marina visitors kiosk",
                "Accessible picnic tables with 1.2m wheelchair overhang clearances"
            ),
            openingHours = "6:00 AM – 10:00 PM Daily",
            imageDrawableRes = R.drawable.img_waterfront_park_1791713462668,
            mapX = 0.72f,
            mapY = 0.28f,
            lat = 37.8052,
            lng = -122.4087
        ),
        Place(
            id = "place_4",
            name = "Central Metropolitan Public Library",
            category = "Public Library",
            address = "310 Knowledge Avenue",
            neighborhood = "Midtown",
            description = "Multi-level public library with assistive workstations, hearing loop service desks, and an adult changing facilities suite.",
            supportedTags = setOf(
                AccessibilityCategory.STEP_FREE,
                AccessibilityCategory.LIFTS,
                AccessibilityCategory.ACCESSIBLE_WASHROOMS,
                AccessibilityCategory.SEATING,
                AccessibilityCategory.WAYFINDING
            ),
            amenityDetails = listOf(
                "Double sliding motion sensor entrance with wide 110cm clearance",
                "Changing Places accredited restroom with ceiling track hoist and adult bench",
                "Hearing induction loop at all info counters and lecture hall",
                "Motorized height-adjustable computer workstations on Level 2",
                "Large-print catalog search terminals and screen reader headsets"
            ),
            openingHours = "9:00 AM – 8:00 PM (Mon–Sat)",
            imageDrawableRes = R.drawable.img_public_library_1791713499087,
            mapX = 0.52f,
            mapY = 0.32f,
            lat = 37.7812,
            lng = -122.4162
        ),
        Place(
            id = "place_5",
            name = "Old Merchant Historic Arcade",
            category = "Historic Market",
            address = "14 Heritage Lane",
            neighborhood = "Old Quarter",
            description = "Historic Victorian market hall. The East portal features a modern ramp, though the West entrance still has preserved stone steps.",
            supportedTags = setOf(
                AccessibilityCategory.STEP_FREE,
                AccessibilityCategory.ACCESSIBLE_WASHROOMS,
                AccessibilityCategory.SEATING
            ),
            amenityDetails = listOf(
                "Step-free entrance strictly via East Gate with automated ramp",
                "Interior brick pavers are mostly flat, with minor historic joints",
                "Single wheelchair accessible WC located on the south mezzanine via staff lift",
                "Resting booths in the central courtyard food court"
            ),
            openingHours = "8:30 AM – 7:00 PM Daily",
            imageDrawableRes = R.drawable.img_historic_arcade_1791716947902,
            mapX = 0.28f,
            mapY = 0.55f,
            lat = 37.7698,
            lng = -122.4285
        ),
        Place(
            id = "place_6",
            name = "St. Luke Regional Diagnostic Pavilion",
            category = "Healthcare",
            address = "500 Wellness Boulevard",
            neighborhood = "Medical Campus",
            description = "State-of-the-art public healthcare pavilion featuring full universal accessibility, low counters, and wide corridors.",
            supportedTags = setOf(
                AccessibilityCategory.STEP_FREE,
                AccessibilityCategory.LIFTS,
                AccessibilityCategory.ACCESSIBLE_WASHROOMS,
                AccessibilityCategory.SEATING,
                AccessibilityCategory.WAYFINDING
            ),
            amenityDetails = listOf(
                "Automatic revolving door with slow-speed wheelchair button and side automatic swing doors",
                "High-speed hospital-grade bed & chair elevators with priority call buttons",
                "Wheelchair-height check-in counters and low self-service kiosks",
                "Color-coded tactile wayfinding floor lines leading to all clinical suites",
                "Restrooms with power-assisted sliding doors and grab bars"
            ),
            openingHours = "24 Hours Daily",
            imageDrawableRes = R.drawable.img_hospital_pavilion_1791716977552,
            mapX = 0.65f,
            mapY = 0.62f,
            lat = 37.7634,
            lng = -122.3995
        ),
        Place(
            id = "place_7",
            name = "Horizon Community Symphony Hall",
            category = "Cultural Center",
            address = "75 Symphony Walk",
            neighborhood = "Arts District",
            description = "Modern acoustic performance venue with integrated companion wheelchair spaces, assisted audio headsets, and step-free foyer.",
            supportedTags = setOf(
                AccessibilityCategory.STEP_FREE,
                AccessibilityCategory.LIFTS,
                AccessibilityCategory.ACCESSIBLE_WASHROOMS,
                AccessibilityCategory.WAYFINDING
            ),
            amenityDetails = listOf(
                "Seamless street-to-seat wheelchair access across orchestra level",
                "Infrared assisted listening headsets and neck loops available gratis",
                "Spacious passenger elevator serving dress circle and balconies",
                "Two accessible stalls per floor with right and left-hand grab bar configurations"
            ),
            openingHours = "11:00 AM – 10:00 PM (Show Days)",
            imageDrawableRes = R.drawable.img_symphony_hall_1791717005320,
            mapX = 0.40f,
            mapY = 0.42f,
            lat = 37.7781,
            lng = -122.4208
        ),
        Place(
            id = "place_8",
            name = "Pinecrest Community Sports Complex",
            category = "Civic Recreation",
            address = "220 Recreation Parkway",
            neighborhood = "North District",
            description = "Public community sports facility with ramped pool access, accessible court viewing stands, and adaptive gym equipment.",
            supportedTags = setOf(
                AccessibilityCategory.STEP_FREE,
                AccessibilityCategory.LIFTS,
                AccessibilityCategory.ACCESSIBLE_WASHROOMS,
                AccessibilityCategory.SEATING
            ),
            amenityDetails = listOf(
                "Aquatics center equipped with hydraulic pool chair hoist and gradual ramp",
                "Accessible locker rooms with barrier-free roll-in showers and folding benches",
                "Ramped viewing gallery overlooking indoor basketball courts",
                "Automatic push-button doors at all main wing intersections"
            ),
            openingHours = "6:00 AM – 9:30 PM (Mon–Sun)",
            imageDrawableRes = R.drawable.img_sports_complex_1791717038248,
            mapX = 0.55f,
            mapY = 0.18f,
            lat = 37.8011,
            lng = -122.4298
        ),
        Place(
            id = "place_9",
            name = "Pacific Maritime Ferry Terminal",
            category = "Ferry Port",
            address = "1 Ferry Building Pier",
            neighborhood = "Harbor District",
            description = "Major coastal commuter terminal offering floating hydraulic ramps that self-level with tides for zero-step boarding onto ferries.",
            supportedTags = setOf(
                AccessibilityCategory.STEP_FREE,
                AccessibilityCategory.ACCESSIBLE_WASHROOMS,
                AccessibilityCategory.SEATING,
                AccessibilityCategory.WAYFINDING
            ),
            amenityDetails = listOf(
                "Tide-compensating hydraulic gangways for level vessel boarding",
                "Audible maritime departure bells and visual bilingual LED sign boards",
                "Priority boarding line and assisted embarkation crew",
                "Wide automatic sliding doors connecting concourse to docks"
            ),
            openingHours = "5:30 AM – 11:30 PM Daily",
            imageDrawableRes = R.drawable.img_ferry_pier_1791719313054,
            mapX = 0.82f,
            mapY = 0.36f,
            lat = 37.7955,
            lng = -122.3937
        ),
        Place(
            id = "place_10",
            name = "Heritage Botanical Conservatory",
            category = "Botanical Garden",
            address = "600 Golden Pavilion",
            neighborhood = "West Park",
            description = "Victorian glass palm conservatory featuring level composite boardwalk paths, scented touch gardens, and electric mobility scooter rentals.",
            supportedTags = setOf(
                AccessibilityCategory.STEP_FREE,
                AccessibilityCategory.ACCESSIBLE_WASHROOMS,
                AccessibilityCategory.SEATING
            ),
            amenityDetails = listOf(
                "Free motorized mobility scooter and wheelchair loan at reception",
                "Smooth wide composite decking through tropical and arid biomes",
                "Raised sensory plant displays with Braille and high-contrast labels",
                "Bench alcoves nestled every 35 meters in peaceful greenhouse wings"
            ),
            openingHours = "9:30 AM – 5:00 PM Daily",
            imageDrawableRes = R.drawable.img_botanical_garden_1791719288905,
            mapX = 0.18f,
            mapY = 0.32f,
            lat = 37.7726,
            lng = -122.4612
        ),
        Place(
            id = "place_11",
            name = "City Municipal Hall & Civic Chambers",
            category = "Civic Center",
            address = "1 Dr. Carlton Goodlett Plaza",
            neighborhood = "Downtown Core",
            description = "Historic municipal complex with modern ADA retrofits including west ramp entrance, automatic brass double doors, and captioning in council chambers.",
            supportedTags = setOf(
                AccessibilityCategory.STEP_FREE,
                AccessibilityCategory.LIFTS,
                AccessibilityCategory.ACCESSIBLE_WASHROOMS,
                AccessibilityCategory.WAYFINDING
            ),
            amenityDetails = listOf(
                "Accessible security portal and low metal-detector threshold",
                "Elevators serving all 4 floors with voice prompts and tactile buttons",
                "Real-time CART live transcription screens in public hearing rooms",
                "Companion wheelchair spaces alongside elected chambers floor"
            ),
            openingHours = "8:00 AM – 5:00 PM (Mon–Fri)",
            imageDrawableRes = R.drawable.img_historic_arcade_1791716947902,
            mapX = 0.45f,
            mapY = 0.52f,
            lat = 37.7792,
            lng = -122.4191
        ),
        Place(
            id = "place_12",
            name = "Beacon Hill Science & Planetarium Discovery Center",
            category = "Science Center",
            address = "700 Observatory Way",
            neighborhood = "North Hill",
            description = "Interactive technology and space museum featuring sensory breakout rooms, roll-in planetarium wheelchair spaces, and tactile astro-models.",
            supportedTags = setOf(
                AccessibilityCategory.STEP_FREE,
                AccessibilityCategory.LIFTS,
                AccessibilityCategory.ACCESSIBLE_WASHROOMS,
                AccessibilityCategory.SEATING,
                AccessibilityCategory.WAYFINDING
            ),
            amenityDetails = listOf(
                "Sensory quiet room equipped with noise-cancelling headphones and weighted blankets",
                "Planetarium dome equipped with 6 designated companion wheelchair recliners",
                "Audio-described exhibit tours and tactile 3D celestial models",
                "Large push-plate automatic entrances between exhibit wings"
            ),
            openingHours = "10:00 AM – 7:00 PM (Daily)",
            imageDrawableRes = R.drawable.img_public_library_1791713499087,
            mapX = 0.32f,
            mapY = 0.22f,
            lat = 37.8005,
            lng = -122.4385
        ),
        Place(
            id = "place_13",
            name = "University Central Student Forum & Hub",
            category = "Higher Ed",
            address = "240 Campus Walk",
            neighborhood = "University Campus",
            description = "Multi-level student commons featuring gentle switchback ramps, ergonomic low-height group study pods, and accessible dining hall counters.",
            supportedTags = setOf(
                AccessibilityCategory.STEP_FREE,
                AccessibilityCategory.LIFTS,
                AccessibilityCategory.ACCESSIBLE_WASHROOMS,
                AccessibilityCategory.SEATING
            ),
            amenityDetails = listOf(
                "Dual oversized freight/passenger elevator serving all 3 forum floors",
                "Lowered micro-kitchen food serving counters and beverage dispensers",
                "Height-adjustable study desks with integrated power outlets",
                "Gender-neutral universal accessible restrooms with push-button lock"
            ),
            openingHours = "7:00 AM – 11:00 PM (Daily)",
            imageDrawableRes = R.drawable.img_hospital_pavilion_1791716977552,
            mapX = 0.22f,
            mapY = 0.68f,
            lat = 37.7582,
            lng = -122.4412
        ),
        Place(
            id = "place_14",
            name = "Lumière Art-House Community Cinema",
            category = "Cultural Cinema",
            address = "42 Cinema Alley",
            neighborhood = "Old Quarter",
            description = "Renovated indie theater featuring open caption screenings, street-level foyer, companion seating in prime viewing rows, and cup-holder audio receivers.",
            supportedTags = setOf(
                AccessibilityCategory.STEP_FREE,
                AccessibilityCategory.ACCESSIBLE_WASHROOMS,
                AccessibilityCategory.SEATING,
                AccessibilityCategory.WAYFINDING
            ),
            amenityDetails = listOf(
                "Closed caption personal display units and assisted audio headsets at box office",
                "Zero-stair access from ticket booth directly into all 3 screening auditoriums",
                "Wide armrest luxury seats with liftable sidebars for easy slide transfers",
                "All-gender accessible washroom with emergency pull alarm on ground floor"
            ),
            openingHours = "1:00 PM – 11:00 PM (Wed–Sun)",
            imageDrawableRes = R.drawable.img_symphony_hall_1791717005320,
            mapX = 0.38f,
            mapY = 0.60f,
            lat = 37.7675,
            lng = -122.4215
        ),
        Place(
            id = "place_15",
            name = "Riverfront Public Market & Food Pavilion",
            category = "Food Market",
            address = "90 River Walk",
            neighborhood = "Riverside",
            description = "Covered artisanal market with wide polished concrete concourses, roll-up dining counters, and level access to outdoor river terraces.",
            supportedTags = setOf(
                AccessibilityCategory.STEP_FREE,
                AccessibilityCategory.ACCESSIBLE_WASHROOMS,
                AccessibilityCategory.SEATING,
                AccessibilityCategory.WAYFINDING
            ),
            amenityDetails = listOf(
                "Polished non-slip concrete flooring without steps or thresholds throughout",
                "Dedicated low dining counter sections with knee clearance at every eatery",
                "Accessible restroom bank with wide baby changing and automatic faucets",
                "Spacious outdoor riverside terrace with gentle 1:24 grade connecting ramp"
            ),
            openingHours = "8:00 AM – 9:00 PM (Daily)",
            imageDrawableRes = R.drawable.img_historic_arcade_1791716947902,
            mapX = 0.62f,
            mapY = 0.48f,
            lat = 37.7788,
            lng = -122.3912
        )
    )

    val initialReviews: List<Review> = listOf(
        Review(
            id = "rev_101",
            placeId = "place_1",
            authorName = "Marcus Vance",
            authorRole = "Power Wheelchair User",
            timestamp = "2 days ago",
            stepFreeRating = 5,
            liftsRating = 5,
            washroomsRating = 4,
            seatingRating = 5,
            wayfindingRating = 5,
            comment = "Genuinely one of the smoothest transit hubs in the city. The gap between platform and train was under 2 inches. Elevators are big enough for two chairs and quick. Restroom was clean and spacious.",
            verifiedVisit = true,
            helpfulCount = 18
        ),
        Review(
            id = "rev_102",
            placeId = "place_1",
            authorName = "Eleanor Ross",
            authorRole = "Low Vision (Guide Dog Handler)",
            timestamp = "1 week ago",
            stepFreeRating = 5,
            liftsRating = 4,
            washroomsRating = 5,
            seatingRating = 4,
            wayfindingRating = 5,
            comment = "The yellow tactile tiles lead directly from the entrance turnstiles to the lift and platform edge. Voice cues in the lift are clear and loud without being jarring.",
            verifiedVisit = true,
            helpfulCount = 11
        ),
        Review(
            id = "rev_103",
            placeId = "place_1",
            authorName = "Devon Chen",
            authorRole = "Parent with Double Stroller",
            timestamp = "3 weeks ago",
            stepFreeRating = 4,
            liftsRating = 4,
            washroomsRating = 4,
            seatingRating = 4,
            wayfindingRating = 4,
            comment = "Navigating with two toddlers and a double stroller was very straightforward. Ample seating near Platform 3 to wait without blocking foot traffic.",
            verifiedVisit = true,
            helpfulCount = 6
        ),

        // Museum reviews
        Review(
            id = "rev_201",
            placeId = "place_2",
            authorName = "Sofia Martinez",
            authorRole = "Manual Wheelchair User",
            timestamp = "3 days ago",
            stepFreeRating = 5,
            liftsRating = 5,
            washroomsRating = 4,
            seatingRating = 4,
            wayfindingRating = 4,
            comment = "Ramps have very manageable slopes and non-slip textured surfaces. The staff immediately offered an accessibility gallery map. Elevators are quiet and smooth.",
            verifiedVisit = true,
            helpfulCount = 14
        ),
        Review(
            id = "rev_202",
            placeId = "place_2",
            authorName = "Arthur Pendelton",
            authorRole = "Senior Citizen (Cane)",
            timestamp = "2 weeks ago",
            stepFreeRating = 4,
            liftsRating = 4,
            washroomsRating = 4,
            seatingRating = 3,
            wayfindingRating = 4,
            comment = "The galleries are huge! I wish there were a few more resting benches between galleries 4 and 6, but overall the floor transition is completely level.",
            verifiedVisit = true,
            helpfulCount = 8
        ),

        // Park reviews
        Review(
            id = "rev_301",
            placeId = "place_3",
            authorName = "Clara Gomez",
            authorRole = "Power Wheelchair User",
            timestamp = "5 days ago",
            stepFreeRating = 5,
            liftsRating = 4,
            washroomsRating = 4,
            seatingRating = 5,
            wayfindingRating = 4,
            comment = "Super flat paths along the water. The asphalt is smooth without bumps or roots. Plenty of shaded benches every few minutes of walking.",
            verifiedVisit = true,
            helpfulCount = 9
        ),

        // Library reviews
        Review(
            id = "rev_401",
            placeId = "place_4",
            authorName = "Rachel Kim",
            authorRole = "Accessibility Advocate",
            timestamp = "1 day ago",
            stepFreeRating = 5,
            liftsRating = 5,
            washroomsRating = 5,
            seatingRating = 5,
            wayfindingRating = 5,
            comment = "Gold standard! The Changing Places adult changing table on the ground floor is a game changer for families. Automatic push buttons everywhere and height adjustable desks.",
            verifiedVisit = true,
            helpfulCount = 27
        ),
        Review(
            id = "rev_402",
            placeId = "place_4",
            authorName = "David Thorne",
            authorRole = "Manual Wheelchair User",
            timestamp = "10 days ago",
            stepFreeRating = 5,
            liftsRating = 5,
            washroomsRating = 5,
            seatingRating = 4,
            wayfindingRating = 4,
            comment = "Book aisles are wide enough to turn around easily. Lift button controls are illuminated and at accessible reach height.",
            verifiedVisit = true,
            helpfulCount = 12
        ),

        // Old Market reviews
        Review(
            id = "rev_501",
            placeId = "place_5",
            authorName = "Gregory Bell",
            authorRole = "Manual Wheelchair User",
            timestamp = "4 days ago",
            stepFreeRating = 3,
            liftsRating = 3,
            washroomsRating = 3,
            seatingRating = 4,
            wayfindingRating = 3,
            comment = "Important tip: DO NOT use the West entrance — there are three steep steps without handrails! Always enter through the East Gate where the ramp is installed. Inside is charming but floor has some bumps.",
            verifiedVisit = true,
            helpfulCount = 33
        ),

        // Hospital reviews
        Review(
            id = "rev_601",
            placeId = "place_6",
            authorName = "Fatima Al-Mansoor",
            authorRole = "Healthcare Worker & Caregiver",
            timestamp = "1 week ago",
            stepFreeRating = 5,
            liftsRating = 5,
            washroomsRating = 5,
            seatingRating = 5,
            wayfindingRating = 5,
            comment = "Flawless accessibility as expected for a new medical wing. Drop-off zone is flush with the curb. Floor guidance lines make navigation stress-free.",
            verifiedVisit = true,
            helpfulCount = 16
        ),

        // Theater reviews
        Review(
            id = "rev_701",
            placeId = "place_7",
            authorName = "Julian Becker",
            authorRole = "Deaf / Hard of Hearing",
            timestamp = "6 days ago",
            stepFreeRating = 5,
            liftsRating = 4,
            washroomsRating = 4,
            seatingRating = 5,
            wayfindingRating = 4,
            comment = "The hearing induction loop had zero static and synched instantly with my T-coil. Wheelchair spots have prime viewing angles, not tucked off in the corner.",
            verifiedVisit = true,
            helpfulCount = 15
        ),

        // Sports Complex reviews
        Review(
            id = "rev_801",
            placeId = "place_8",
            authorName = "Kavita Nair",
            authorRole = "Adaptive Sports Athlete",
            timestamp = "3 days ago",
            stepFreeRating = 5,
            liftsRating = 4,
            washroomsRating = 5,
            seatingRating = 4,
            wayfindingRating = 4,
            comment = "Pool hoist was operational and the lifeguard was trained on it without any awkward waiting. Wide locker room stalls with roll-in benches.",
            verifiedVisit = true,
            helpfulCount = 13
        ),

        // Ferry Terminal reviews
        Review(
            id = "rev_901",
            placeId = "place_9",
            authorName = "Timothy Walsh",
            authorRole = "Power Wheelchair User",
            timestamp = "4 days ago",
            stepFreeRating = 5,
            liftsRating = 4,
            washroomsRating = 4,
            seatingRating = 5,
            wayfindingRating = 4,
            comment = "The hydraulic gangway was completely level with the catamaran ferry deck. Friendly crew assisted with priority boarding.",
            verifiedVisit = true,
            helpfulCount = 8
        ),

        // Botanical Garden reviews
        Review(
            id = "rev_1001",
            placeId = "place_10",
            authorName = "Grace Holloway",
            authorRole = "Senior Citizen (Walker)",
            timestamp = "1 week ago",
            stepFreeRating = 5,
            liftsRating = 4,
            washroomsRating = 4,
            seatingRating = 5,
            wayfindingRating = 4,
            comment = "Borrowing the complimentary electric mobility scooter made exploring the palm house a joy. Benches everywhere among orchids.",
            verifiedVisit = true,
            helpfulCount = 19
        ),

        // City Hall reviews
        Review(
            id = "rev_1101",
            placeId = "place_11",
            authorName = "Samuel Wright",
            authorRole = "Manual Wheelchair User",
            timestamp = "5 days ago",
            stepFreeRating = 4,
            liftsRating = 4,
            washroomsRating = 4,
            seatingRating = 4,
            wayfindingRating = 4,
            comment = "Use the West entrance ramp rather than the Grand Staircase. Elevators are vintage but fully functional with Braille floor plates.",
            verifiedVisit = true,
            helpfulCount = 10
        ),

        // Science Center reviews
        Review(
            id = "rev_1201",
            placeId = "place_12",
            authorName = "Maya Patel",
            authorRole = "Sensory / Neurodivergent Advocate",
            timestamp = "3 days ago",
            stepFreeRating = 5,
            liftsRating = 5,
            washroomsRating = 5,
            seatingRating = 4,
            wayfindingRating = 5,
            comment = "The sensory breakout room with quiet lighting and noise-cancelling headsets was a lifesaver. Planetarium companion spots are roomy.",
            verifiedVisit = true,
            helpfulCount = 21
        ),

        // Student Hub reviews
        Review(
            id = "rev_1301",
            placeId = "place_13",
            authorName = "Jordan Reed",
            authorRole = "Power Wheelchair User",
            timestamp = "2 days ago",
            stepFreeRating = 5,
            liftsRating = 5,
            washroomsRating = 4,
            seatingRating = 4,
            wayfindingRating = 4,
            comment = "Great motorized height-adjustable desks. Fast elevators between floors and lowered dining hall service counters.",
            verifiedVisit = true,
            helpfulCount = 14
        ),

        // Cinema reviews
        Review(
            id = "rev_1401",
            placeId = "place_14",
            authorName = "Elena Brody",
            authorRole = "Deaf / Hard of Hearing",
            timestamp = "6 days ago",
            stepFreeRating = 4,
            liftsRating = 4,
            washroomsRating = 4,
            seatingRating = 5,
            wayfindingRating = 4,
            comment = "Closed caption display cups are available on request and fit in the cup holder seamlessly. Comfortable level seating.",
            verifiedVisit = true,
            helpfulCount = 9
        ),

        // Riverfront Market reviews
        Review(
            id = "rev_1501",
            placeId = "place_15",
            authorName = "Liam Gallagher",
            authorRole = "Manual Wheelchair User",
            timestamp = "1 day ago",
            stepFreeRating = 5,
            liftsRating = 4,
            washroomsRating = 5,
            seatingRating = 4,
            wayfindingRating = 4,
            comment = "Polished concrete without bumps makes wheeling through the food stalls effortless. Low counters at every vendor.",
            verifiedVisit = true,
            helpfulCount = 12
        )
    )
}
