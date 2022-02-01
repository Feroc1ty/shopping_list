package ru.rykunov.shopping_list.utils

import android.view.MotionEvent
import android.view.View

class MyTouchListner: View.OnTouchListener {
    var xDelta = 0.0f
    var yDelta = 0.0f
    override fun onTouch(v: View, event: MotionEvent?): Boolean {
        when(event?.action){
            /*
            Когда мы отпустили
             */
            MotionEvent.ACTION_DOWN ->{
                /*
                Читаем позицию элемента на экране, где он был и где он сейчас
                 */
                xDelta = v.x - event.rawX
                yDelta = v.y - event.rawY
            }
            /*
            когда двигаем на экране
             */
            MotionEvent.ACTION_MOVE ->{
                v.x = xDelta + event.rawX
                v.y = yDelta + event.rawY
            }
        }
        return true
    }
}