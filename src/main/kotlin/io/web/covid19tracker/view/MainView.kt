package io.web.covid19tracker.view

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.charts.Chart
import com.vaadin.flow.component.charts.model.*
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
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
    private lateinit var horizontalLayout: HorizontalLayout
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
            val currentCountryData = countryService.getCurrentCountryData(slug)

            val areaChart = createAreaChart(totalCountryData)
            val pieChart = createPieChart(currentCountryData)
            horizontalLayout = HorizontalLayout()
            horizontalLayout.isPadding = true
            horizontalLayout.width = "100%"
            horizontalLayout.height = "700px"
            horizontalLayout.justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
            horizontalLayout.add(areaChart, pieChart)

            add(horizontalLayout)
        }

        val submit = Button("Submit")

        val verticalLayout = VerticalLayout()
        verticalLayout.add(comboBox)
        verticalLayout.add(submit)

        verticalLayout.alignItems = FlexComponent.Alignment.CENTER

        add(verticalLayout)
    }

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
        plotOptionsArea.stacking = Stacking.NONE
        configuration.setPlotOptions(plotOptionsArea)

        configuration.addSeries(ListSeries("Confirmed", getConfirmedCases(totalCountryData)))
        configuration.addSeries(ListSeries("Active", getActiveCases(totalCountryData)))
        configuration.addSeries(ListSeries("Recovered", getRecoveredCases(totalCountryData)))
        configuration.addSeries(ListSeries("Deaths", getDeaths(totalCountryData)))

        return chart
    }

    private fun createPieChart(currentCountryData: CountryData): Chart {
        val chart = Chart(ChartType.PIE)

        val configuration: Configuration = chart.configuration

        configuration.setTitle("Covid-19 Data Now")
        configuration.setSubTitle("Source: covid19api.com")

        val tooltip = Tooltip()

        configuration.tooltip = tooltip

        val plotOptions = PlotOptionsPie()
        plotOptions.allowPointSelect = true
        plotOptions.cursor = Cursor.POINTER
        plotOptions.showInLegend = true
        configuration.setPlotOptions(plotOptions)

        val series = DataSeries()
        val confirmed = DataSeriesItem("Confirmed", currentCountryData.confirmed)
        confirmed.sliced = true
        confirmed.isSelected = true
        series.name = "count"
        series.add(confirmed)
        series.add(DataSeriesItem("Active", currentCountryData.active))
        series.add(DataSeriesItem("Recovered", currentCountryData.recovered))
        series.add(DataSeriesItem("Deaths", currentCountryData.deaths))
        configuration.setSeries(series)
        chart.setVisibilityTogglingDisabled(true)

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