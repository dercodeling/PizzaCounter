package de.dercodeling.pizzacounter

import org.junit.Test

import org.junit.Assert.*

class MainViewModelTest {

    private fun getTestViewModel(): MainViewModel {
        val viewModel = MainViewModel()

        viewModel.setInitialTypes(listOf("Margherita","Tonno","Salami"))

        viewModel.clearTypes()

        viewModel.changeQuantity("Tonno", 2)
        viewModel.changeQuantity("Margherita", 6)
        viewModel.changeQuantity("Salami",2)

        return viewModel
    }

    @Test
    fun getQuantityTest() {
        val viewModel = MainViewModel()

        val type = "Tonno"
        val quantity = 5

        viewModel.addType(type)
        viewModel.changeQuantity(type, quantity)

        assertEquals(quantity, viewModel.getQuantity(type))

        assertEquals(0,viewModel.getQuantity("This type does not exist"))
    }

    @Test
    fun changeQuantityTest() {
        val viewModel = MainViewModel()

        val type = "Tonno"

        viewModel.addType(type)

        viewModel.changeQuantity(type, 3)
        viewModel.changeQuantity(type, -2)

        assertEquals(1, viewModel.getQuantity(type))

        // Quantity cannot become negative

        viewModel.changeQuantity(type, -5)

        assertEquals(0, viewModel.getQuantity(type))

        // Changing a non-existing type doesn't

        assertThrows(java.lang.NullPointerException::class.java) {
            viewModel.changeQuantity("This type doesn't exist", 1)
        }
    }

    @Test
    fun sortingTest() {
        val viewModel = getTestViewModel()

        val sortedLists = listOf(
            listOf("Margherita","Salami","Tonno"),
            listOf("Salami","Tonno","Margherita"),
            listOf("Margherita","Salami","Tonno")
        )

        for (i in 0..2) {
            viewModel.setSortBy(i)
            assertEquals(sortedLists[i], viewModel.getTypes())

            assertEquals(i, viewModel.getSortBy())
        }
    }

    @Test
    fun clearQuantitiesTest() {
        val viewModel = getTestViewModel()

        // Before clearing

        var totalQuantity = viewModel.getTypes().fold(0) { acc, next ->
            acc + viewModel.getQuantity(next)
        }

        assertEquals(10, totalQuantity)

        // After clearing

        viewModel.clearQuantities()

        totalQuantity = viewModel.getTypes().fold(0) { acc, next ->
            acc + viewModel.getQuantity(next) }

        assertEquals(0,totalQuantity)
    }

    @Test
    fun clearTypesTest() {
        val viewModel = getTestViewModel()
        val initialTypes = listOf("Tonno")
        viewModel.setInitialTypes(initialTypes)

        assertEquals(3,viewModel.getSize())

        viewModel.clearTypes()

        assertEquals(initialTypes.size,viewModel.getSize())
    }
}