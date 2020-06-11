package io.web.covid19tracker.view

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import io.web.covid19tracker.models.Country
import io.web.covid19tracker.service.CountryService
import org.springframework.beans.factory.annotation.Autowired

@Theme(value = Lumo::class, variant = Lumo.DARK)
@Route
class MainView(@Autowired val countryService: CountryService) : VerticalLayout() {
    init {
        val countries = countryService.getCountries()
        val countryNames = countries?.map {
            it.country
        }

        val comboBox = ComboBox<String>()
        comboBox.setItems(countryNames)
        comboBox.placeholder = "Country"
        comboBox.addValueChangeListener {
            val (_, slug, _) = countryService.getCountry(countries, it.value)
                    ?: Country("", "", "")


        }

        val submit = Button("Submit")

        val verticalLayout = VerticalLayout()
        verticalLayout.add(comboBox)
        verticalLayout.add(submit)

        verticalLayout.alignItems = FlexComponent.Alignment.CENTER

        add(verticalLayout)
    }

}