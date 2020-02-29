package io.web.covid19tracker.controller

import io.web.covid19tracker.service.DataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class Controller(
        @Autowired val dataService: DataService
) {

    @GetMapping("/")
    fun home(model: Model): String {
        val fetchedData = dataService.fetchData()
        val totalCases = fetchedData.map { data ->
            data.sumBy {
                it.currentCount
            }
        }

        val previousCases = fetchedData.map { data ->
            data.sumBy {
                it.previousCount
            }
        }

        val totalNewCases = totalCases
                .zipWith(previousCases)
                .map {
                    it.t1 - it.t2
                }

        model.addAttribute("data", fetchedData)
        model.addAttribute("totalCases", totalCases)
        model.addAttribute("totalNewCases", totalNewCases)
        return "home"
    }
}