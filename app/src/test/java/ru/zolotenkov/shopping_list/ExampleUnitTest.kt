package ru.zolotenkov.shopping_list

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun people(busStops: Array<Pair<Int, Int>>) : Int {
        //code here
        var arraysize = busStops.size

        return arraysize
    }
    @Test
    fun print(){
        println(people(arrayOf(3 to 0,9 to 1,4 to 10,12 to 2,6 to 1,7 to 10)))
    }
}