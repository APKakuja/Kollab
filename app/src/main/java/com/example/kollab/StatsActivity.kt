package com.example.kollab

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class StatsActivity : AppCompatActivity() {

    private lateinit var barChart: BarChart
    private lateinit var pieChart: PieChart
    private lateinit var statsDataStore: StatsDataStore
    private lateinit var txtTiempoUso: android.widget.TextView
    private lateinit var txtEnergia: android.widget.TextView
    private lateinit var txtCo2: android.widget.TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        statsDataStore = StatsDataStore(this)

        barChart = findViewById(R.id.barChart)
        pieChart = findViewById(R.id.pieChart)
        txtTiempoUso = findTextViewByName("txtTiempoUso")
        txtEnergia = findTextViewByName("txtEnergia")
        txtCo2 = findTextViewByName("txtCo2")

        lifecycleScope.launch {
            val chats = statsDataStore.visitasChats.first()
            val ajustes = statsDataStore.visitasAjustes.first()
            val tiempoUsoMs = statsDataStore.tiempoUsoMs.first()

            configurarBarChart(chats, ajustes)
            configurarPieChart(chats, ajustes)
            mostrarIndicadoresAmbientales(tiempoUsoMs, chats, ajustes)
        }
    }

    private fun findTextViewByName(idName: String): android.widget.TextView {
        val id = resources.getIdentifier(idName, "id", packageName)
        require(id != 0) { "No existe el id $idName en activity_stats.xml" }
        return findViewById(id)
    }

    private fun mostrarIndicadoresAmbientales(tiempoUsoMs: Long, chats: Int, ajustes: Int) {
        val horasUso = tiempoUsoMs / 3_600_000.0
        val interaccionesPonderadas = chats + (ajustes * 0.4)

        // Energia base por tiempo activo + pequeño coste por interacciones de menu.
        val energiaKWhTiempo = (horasUso * StatsDataStore.POTENCIA_MEDIA_W) / 1000.0
        val energiaKWhInteracciones = interaccionesPonderadas * 0.00005
        val energiaKWhTotal = energiaKWhTiempo + energiaKWhInteracciones
        val co2Kg = energiaKWhTotal * StatsDataStore.FACTOR_CO2_KG_POR_KWH

        txtTiempoUso.text = getString(R.string.stats_tiempo_uso_format, horasUso)
        txtEnergia.text = getString(R.string.stats_energia_format, energiaKWhTotal)
        txtCo2.text = getString(R.string.stats_co2_format, co2Kg)
    }

    private fun configurarBarChart(chats: Int, ajustes: Int) {
        val entries = listOf(
            BarEntry(0f, chats.toFloat()),
            BarEntry(1f, ajustes.toFloat())
        )

        val dataSet = BarDataSet(entries, "Visites").apply {
            colors = listOf(
                Color.parseColor("#4CAF50"),
                Color.parseColor("#2196F3")
            )
            valueTextSize = 14f
        }

        barChart.apply {
            data = BarData(dataSet)
            description.isEnabled = false
            setFitBars(true)
            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(listOf("Chats", "Ajustes"))
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                setDrawGridLines(false)
            }
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false
            animateY(800)
            invalidate()
        }
    }

    private fun configurarPieChart(chats: Int, ajustes: Int) {
        val entries = listOf(
            PieEntry(chats.toFloat(), "Chats"),
            PieEntry(ajustes.toFloat(), "Ajustes")
        )

        val dataSet = PieDataSet(entries, "Proporció").apply {
            colors = listOf(
                Color.parseColor("#4CAF50"),
                Color.parseColor("#2196F3")
            )
            valueTextSize = 14f
            valueTextColor = Color.WHITE
        }

        pieChart.apply {
            data = PieData(dataSet)
            description.isEnabled = false
            isDrawHoleEnabled = true
            holeRadius = 40f
            centerText = "Ús App"
            setCenterTextSize(14f)
            animateY(800)
            invalidate()
        }
    }
}