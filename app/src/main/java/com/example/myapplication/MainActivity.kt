package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


import android.app.ActionBar
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import android.widget.TextView
var a: View?=null
private var lastClickTime: Long = 0
private val clickDelay: Long = 1200
val catViews = mutableListOf<ImageView>()
var chet: Int = 16
class MainActivity : AppCompatActivity() {
//    val theMap = mapOf("cat" to R.drawable.squarecat,)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    // Теперь инициализируем переменную
        //setContentView(R.layout.activity_main) q
        val layout = LinearLayout(applicationContext)
        layout.orientation = LinearLayout.VERTICAL


        val params = LinearLayout.LayoutParams(250,250)
        params.weight = 1.toFloat() // единичный вес

        val massiv = Array(16) { it }
        val r = Random(System.currentTimeMillis())
        massiv.shuffle(r)
//        for (i in 0..15) {
//            massiv[i]=i
//        }
//        val r=Random(1000)
//        massiv = massiv.toList().shuffled(r).toTypedArray()
        for (i in 0..15) {
            var k =(massiv[i])%8+1
            val name = "a$k" // Создаем имя функции для ресурса, например "a1", "a2", ..., "a16"

            // Получение идентификатора ресурса по имени
            val resourceId = resources.getIdentifier("kek", "drawable", packageName)
            catViews.add( // вызываем конструктор для создания нового ImageView
                ImageView(applicationContext).apply {
                    setImageResource(resourceId)
                    val displayMetrics = context.resources.displayMetrics
                    tag = name // TODO: указа                                                                                                                                                                                                       ть тег в зависимости от картинки
                    layoutParams = params
                    setOnClickListener(colorListener)
                })
        }

        val rows = Array(4, { LinearLayout(applicationContext)})

        var count = 0
        for (view in catViews) {
            val row: Int = count / 4
            rows[row].addView(view)
            count ++
        }
        for (row in rows) {
            layout.addView(row)
        }
        setContentView(layout)
    }
    var countopen:Int =0
//    var firstView = resources.getIdentifier("a1", "drawable", packageName)

    suspend fun setBackgroundWithDelay(v: View) {
        if (v is ImageView) {
            if (countopen == 0||countopen == 2) {
                val resourceId = resources.getIdentifier(v.tag.toString(), "drawable", packageName)
                v.setImageResource(resourceId)
                a=v
                v.isClickable = false
                countopen = 1
            }
            else {
                val resourceId = resources.getIdentifier(v.tag.toString(), "drawable", packageName)
                v.setImageResource(resourceId)
                countopen += 1
                delay(1200)
                if (v.tag == a?.tag) {
                    a?.visibility = View.INVISIBLE
                    v.visibility = View.INVISIBLE
                    a?.isClickable = false
                    v.isClickable = false
                    chet=chet-2
                    if(chet<2){
                        Log.d("MyTag", "chet: ${chet}")
                        Toast.makeText( this,"ПОБЕДА",Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Log.d("MyTag2", "chet: ${chet}")
                    }
                } else if (v.tag != a?.tag) {
                    var resourceId = resources.getIdentifier("kek", "drawable", packageName)
                    v.setImageResource(resourceId)
                    (a as? ImageView)?.let {
                        it.setImageResource(resourceId)
                    }
                    a?.isClickable = true
                    v.isClickable = true
                    a=null
                }
            }
        }
    }

    suspend fun openCards() {

    }
    // обработчик нажатия на кнопку
    val colorListener = View.OnClickListener() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= clickDelay || countopen==0||countopen==1) {
            lastClickTime = currentTime
            // запуск функции в фоновом потоке
            GlobalScope.launch(Dispatchers.Main)
            { setBackgroundWithDelay(it)}
        }
    }
}