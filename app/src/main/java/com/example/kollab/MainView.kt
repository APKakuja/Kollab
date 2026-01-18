package com.example.kollab

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class MainView : AppCompatActivity() {

    private var x1 = 0f
    private var x2 = 0f
    private val mindistance = 150

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_view)

        val profileButton: Button = findViewById(R.id.viewProfileButton)
        val profileCard = findViewById<ImageView>(R.id.profileImage)

        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileView::class.java)
            startActivity(intent)
        }

        val victoriaButton: ImageButton = findViewById(R.id.victoriaButton)

        victoriaButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // Swipe listener
        @Suppress("ClickableViewAccessibility")

        profileCard.setOnTouchListener { view, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    x1 = event.x
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.x - x1
                    view.translationX = deltaX
                    view.rotation = deltaX / 20

                    true
                }

                MotionEvent.ACTION_UP -> {
                    x2 = event.x
                    val deltaX = x2 - x1

                    if (abs(deltaX) > mindistance) {
                        if (deltaX < 0) {
                            animateSwipeLeft(view)
                        } else {
                            onSwipeRight()
                        }
                    }

                    view.performClick()
                    true
                }

                else -> false
            }
        }
    }
    override fun onResume() {
        super.onResume()

        val profileCard = findViewById<ImageView>(R.id.profileImage)

        // Restaurar la foto a estado original
        profileCard.translationX = 0f
        profileCard.rotation = 0f
        profileCard.alpha = 1f
    }


    private fun onSwipeLeft() {
        val intent = Intent(this, ChatActivity::class.java)
        startActivity(intent)
    }

    private fun onSwipeRight() {
        // Por ahora no hace nada
    }

    //Funciones de animacion

    private fun animateSwipeLeft(view: View) {
        view.animate()
            .translationX(-1000f)
            .rotation(-30f)
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                onSwipeLeft()
            }
            .start()
    }

    private fun animateSwipeRight(view: View) {
        view.animate()
            .translationX(1000f)
            .rotation(30f)
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                onSwipeRight()
            }
            .start()
    }
}
