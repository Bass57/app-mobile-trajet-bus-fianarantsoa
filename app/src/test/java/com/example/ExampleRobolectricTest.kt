package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.datasource.FianarBusData
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
    assertEquals("Bus Fianarantsoa", appName)
  }

  @Test
  fun `verify bus lines and university stop are present`() {
    val lines = FianarBusData.LINES
    assertTrue(lines.size >= 10)
    val line1 = lines.find { it.id == "L1" }
    assertNotNull(line1)
    assertTrue(line1!!.stops.contains("Andrainjato Université"))
  }
}

