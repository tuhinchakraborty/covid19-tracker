package io.web.covid19tracker.view

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.charts.Chart
import com.vaadin.flow.component.charts.model.*
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import io.web.covid19tracker.models.Country
import io.web.covid19tracker.models.CountryData
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
            val totalCountryData = countryService.getTotalCountryData(slug)

            val areaChart = createAreaChart(totalCountryData)
            val verticalLayout = VerticalLayout()
            verticalLayout.removeAll()
            verticalLayout.add(areaChart)
            verticalLayout.alignItems = FlexComponent.Alignment.CENTER
            add(verticalLayout)
        }

        val submit = Button("Submit")

        val verticalLayout = VerticalLayout()
        verticalLayout.add(comboBox)
        verticalLayout.add(submit)

        verticalLayout.alignItems = FlexComponent.Alignment.CENTER

        add(verticalLayout)
    }

//    private fun createBoard() : Board {
//        val board = Board()
//        board.setSizeUndefined()
//
//        val child1: Div = Div()
//        child1.text = "This could be chart 1"
//        val child2: Div = Div()
//        child2.text = "This could be chart 2"
//
//        board.addRow(createAreaChart(), child1)
//
//
//        return board
//    }

    private fun createAreaChart(totalCountryData: List<CountryData>) : Chart {

        val chart = Chart(ChartType.AREA)

        val configuration: Configuration = chart.configuration

        configuration.setTitle("Covid-19 Data")
        configuration.setSubTitle("Source: covid19api.com")

        val xAxis: XAxis = configuration.getxAxis()
        getDates(totalCountryData).map {
            xAxis.addCategory(it)
        }
        xAxis.tickmarkPlacement = TickmarkPlacement.ON

        val yAxis: YAxis = configuration.getyAxis()
        yAxis.setTitle("Count")
        yAxis.labels.formatter = "function () { return this.value / 1000;}"

        configuration.tooltip.valueSuffix = ""

        val plotOptionsArea = PlotOptionsArea()
        plotOptionsArea.stacking = Stacking.NORMAL
        configuration.setPlotOptions(plotOptionsArea)

        configuration.addSeries(ListSeries("Confirmed", *getConfirmedCases(totalCountryData).toTypedArray()))
        configuration.addSeries(ListSeries("Active", *getActiveCases(totalCountryData).toTypedArray()))
        configuration.addSeries(ListSeries("Recovered", *getRecoveredCases(totalCountryData).toTypedArray()))
        configuration.addSeries(ListSeries("Deaths", *getDeaths(totalCountryData).toTypedArray()))

        return chart
    }

    private fun getDeaths(totalCountryData: List<CountryData>): List<Int> {
        return totalCountryData.map {
            it.deaths
        }
    }

    private fun getRecoveredCases(totalCountryData: List<CountryData>): List<Int> {
        return totalCountryData.map {
            it.recovered
        }
    }

    private fun getActiveCases(totalCountryData: List<CountryData>): List<Int> {
        return totalCountryData.map {
            it.active
        }
    }

    private fun getConfirmedCases(totalCountryData: List<CountryData>) : List<Int> {
        return totalCountryData.map {
            it.confirmed
        }
    }

    private fun getDates(totalCountryData: List<CountryData>) : List<String> {
        return totalCountryData.map {
            it.date
        }
    }

}