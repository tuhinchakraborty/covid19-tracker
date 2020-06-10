package io.web.covid19tracker.view

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.mockk
import io.web.covid19tracker.models.Country
import io.web.covid19tracker.service.DataService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MainViewTest {
    private val dataService: DataService = mockk()

    @InjectMockKs
    private lateinit var mainView: MainView

    @BeforeEach
    internal fun setUp() {
        every { dataService.getCountries() } returns getListOfCountries()
        mainView = MainView(dataService)
    }

    @Test
    fun shouldHaveVerticalLayout() {
        val firstComponent = mainView.getComponentAt(0)

        assertEquals(1, mainView.componentCount)
        assertEquals(VerticalLayout::class, firstComponent::class)
    }

    @Test
    fun shouldHaveComboBoxAndButtonWithinVerticalLayout() {
        val verticalLayout = mainView.getComponentAt(0)
        assertEquals(2, verticalLayout.children.count())

        val components = mutableListOf<Component>()
        verticalLayout.children.forEachOrdered {
            components.add(it)
        }

        assertEquals(ComboBox::class, components[0]::class)
        assertEquals(Button::class, components[1]::class)
        assertTrue(components[0].isVisible)
        assertTrue(components[1].isVisible)
    }

    private fun getListOfCountries() : List<Country> = listOf(Country("Country One", "country-one", "co"),
            Country("Country Two", "country-two", "ct"))
}