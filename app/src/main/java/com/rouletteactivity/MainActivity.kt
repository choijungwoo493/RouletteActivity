package com.rouletteactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this
        val rouletteData = listOf("치킨", "중국집", "한식", "족발/보쌈", "버거", "피자/양식", "분식", "카페/디저트")

        binding.roulette.apply {
            setRouletteSize(8)
            binding.rouletteSizeTv.text = getRouletteSize().toString()
            setRouletteDataList(rouletteData)
        }
    }
    override fun onDestroy() {
        super.onDestroy()

        with(compositeDisposable) {
            clear()
            dispose()
        }
    }

    fun rotateRoulette() {
        val rouletteListener = object : RotateListener {
            override fun onRotateStart() {
                binding.rotateResultTv.text = "Result : "
            }

            override fun onRotateEnd(result: String) {
                binding.rotateResultTv.text = "Result : $result"
            }
        }

        val toDegrees = (2000..10000).random().toFloat()
        binding.roulette.rotateRoulette(toDegrees, 4000, rouletteListener)
    }

    fun rotateRouletteRx() {
        val toDegrees = (2000..10000).random().toFloat()

        binding.roulette
                .rotateRoulette(toDegrees, 4000)
                .subscribe({ result ->
                    binding.rotateResultTv.text = "Result : $result"
                }, { e ->
                    e.printStackTrace()
                }).addTo(compositeDisposable)
    }

    fun plusRouletteSize() {
        var rouletteSize = binding.roulette.getRouletteSize()
        if (rouletteSize == 8) return

        binding.roulette.setRouletteSize(++rouletteSize)
        binding.rouletteSizeTv.text = rouletteSize.toString()
    }

    fun minusRouletteSize() {
        var rouletteSize = binding.roulette.getRouletteSize()
        if (rouletteSize == 2) return

        binding.roulette.setRouletteSize(--rouletteSize)
        binding.rouletteSizeTv.text = rouletteSize.toString()
    }
}