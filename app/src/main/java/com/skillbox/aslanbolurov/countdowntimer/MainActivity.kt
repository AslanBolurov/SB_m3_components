package com.skillbox.aslanbolurov.countdowntimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.slider.Slider
import com.skillbox.aslanbolurov.countdowntimer.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlin.concurrent.timer
import kotlinx.coroutines.currentCoroutineContext as currentCoroutineContext1

const val MAX=60
const val MIN=0
const val STOP=0
const val START=1

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var btnState= START
    lateinit var progressBar: ProgressBar
    lateinit var btnStart: Button
    lateinit var slider: Slider

    private var scope= CoroutineScope(Job()+Dispatchers.IO)



    override fun onCreate(savedInstanceState: Bundle?) {
        var isContinue:Boolean=false


        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar=binding.progressBar
        btnStart=binding.btnStartPause
        slider=binding.slider

        btnStart.setOnClickListener {
            var sliderValue=slider.value.toInt()
            when(btnState){
                START->{
                        if (isContinue)
                            sliderValue= binding.tvCounter.text.toString().toInt()

                        scope=CoroutineScope(Job()+Dispatchers.IO)
                        scope.launch {
                            timerStart(sliderValue)
                        }


                        btnState= STOP
                        slider.isEnabled=false
                        btnStart.setText(R.string.btnStop)

                }
                STOP ->{
                    scope.cancel()
                    isContinue=true
                    btnState= START
                    slider.isEnabled=true
                    btnStart.setText(R.string.btnStart)
                }

            }
        }
        slider.addOnChangeListener { _, value, _ ->
            isContinue=false
            binding.tvCounter.setText(slider.value.toInt().toString())
            progressBar.progress=value.toInt()
        }

    }
    suspend fun timerStart(seconds:Int){

        (seconds downTo 0).forEach {
            progressBar.progress=it
            binding.tvCounter.setText(it.toString())
            delay(1000)
        }
    }

}