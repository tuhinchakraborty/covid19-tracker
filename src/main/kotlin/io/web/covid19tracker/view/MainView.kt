package io.web.covid19tracker.view

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import io.web.covid19tracker.service.DataService
import org.springframework.beans.factory.annotation.Autowired

@Theme(value = Lumo::class, variant = Lumo.DARK)
@Route
class MainView(@Autowired val dataService: DataService) : VerticalLayout() {
    init {
        val countryNames = dataService.getCountryNames()
        val comboBox = ComboBox<String>()
        comboBox.setItems(countryNames)

        val submit = Button("Submit") {
            Notification.show("Clicked and the country is ${comboBox.value}")
        }

        val verticalLayout = VerticalLayout()
        verticalLayout.add(comboBox)
        verticalLayout.add(submit)

        verticalLayout.alignItems = FlexComponent.Alignment.CENTER

        add(verticalLayout)
    }

}