package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.GeminiAiService
import com.example.data.SamplePlacesData
import com.example.data.UserProfileRepository
import com.example.model.AccessibilityCategory
import com.example.model.AccessibilityProfile
import com.example.model.PlaceWithStats
import com.example.model.Review
import com.example.model.resolveImagesList
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("AccessPath", appName)
  }

  @Test
  fun `review overall score calculation`() {
    val review = Review(
        id = "test_1",
        placeId = "place_1",
        authorName = "Alex",
        authorRole = "Wheelchair User",
        timestamp = "Today",
        stepFreeRating = 5,
        liftsRating = 5,
        washroomsRating = 4,
        seatingRating = 5,
        wayfindingRating = 5,
        comment = "Great step free access"
    )
    // (5 + 5 + 4 + 5 + 5) / 5 = 4.8
    assertEquals(4.8f, review.overallRating, 0.01f)
    assertEquals(5, review.ratingFor(AccessibilityCategory.STEP_FREE))
  }

  @Test
  fun `pre-seeded venues count and 3-photo slideshow images`() {
    val places = SamplePlacesData.initialPlaces
    assertEquals(15, places.size)

    places.forEach { place ->
      val images = place.resolveImagesList()
      assertEquals(3, images.size)
      assertTrue(images[0] != 0)
      assertTrue(images[1] != 0)
      assertTrue(images[2] != 0)
    }
  }

  @Test
  fun `user profile update and persistence`() {
    val repo = UserProfileRepository()
    val initial = repo.userProfile.value
    assertEquals("Alex Morgan", initial.fullName)

    val updated = repo.updateProfile(
        fullName = "Jordan Rivera",
        email = "jordan@accesspath.org",
        phone = "+1 555-0199",
        location = "Oakland, CA",
        avatarIndex = 2,
        preferredProfile = AccessibilityProfile.LOW_VISION,
        bio = "Vision guide advocate"
    )

    assertEquals("Jordan Rivera", updated.fullName)
    assertEquals("jordan@accesspath.org", updated.email)
    assertEquals(AccessibilityProfile.LOW_VISION, updated.preferredProfile)
    assertEquals(updated, repo.userProfile.value)
  }

  @Test
  fun `ai summary synthesis from venue data and reviews`() {
    val place = SamplePlacesData.initialPlaces.first()
    val reviews = SamplePlacesData.initialReviews.filter { it.placeId == place.id }
    val stats = PlaceWithStats(
        place = place,
        reviews = reviews,
        overallScore = 4.8f,
        stepFreeScore = 5.0f,
        liftsScore = 4.8f,
        washroomsScore = 4.5f,
        seatingScore = 4.5f,
        wayfindingScore = 4.8f,
        reviewCount = reviews.size
    )

    val aiService = GeminiAiService()
    val summary = aiService.synthesizeFromVenueData(stats)

    assertNotNull(summary)
    assertEquals(place.id, summary.placeId)
    assertTrue(summary.strengths.isNotEmpty())
    assertTrue(summary.practicalTip.isNotBlank())
    assertTrue(summary.headline.isNotBlank())
  }
}
