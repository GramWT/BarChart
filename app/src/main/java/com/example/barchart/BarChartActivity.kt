package com.example.barchart

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barchart.databinding.ActivityBarChartBinding
import com.example.barchart.databinding.ItemBarChartBinding

class BarChartActivity : AppCompatActivity() {

    private val binding: ActivityBarChartBinding by lazy {
        ActivityBarChartBinding.inflate(layoutInflater)
    }

    private var adapter: BarChartAdapter? = null

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, BarChartActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = BarChartAdapter()

        binding.barChartRecyclerView.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }

        val testData = arrayListOf<BarChartData>()

        testData.add(
            BarChartData(
                name = "คลอเรสเตอรอล",
                degreeText = "ปกติ",
                70,
                120,
                150,
                0,
                90
            )
        )

        testData.add(
            BarChartData(
                name = "ระดับน้ำตาลในเลือด",
                degreeText = "สูงกว่าเกณฑ์",
                20,
                100,
                130,
                0,
                121
            )
        )

        testData.add(
            BarChartData(
                name = "ความเข้มข้นของเม็ดเลือดแดง",
                degreeText = "ต่ำกว่าเกณฑ์",
                50,
                160,
                200,
                0,
                40
            )
        )


        adapter?.setData(testData)
    }

    inner class BarChartAdapter : RecyclerView.Adapter<BarChartAdapter.ViewHolder>() {
        private var barChartList = arrayListOf<BarChartData>()
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BarChartAdapter.ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_bar_chart, parent, false)
            )
        }

        override fun onBindViewHolder(holder: BarChartAdapter.ViewHolder, position: Int) {
            ViewHolder(holder.itemView).bind(barChartList[position])
        }

        override fun getItemCount() = barChartList.size

        fun setData(currentData: ArrayList<BarChartData>) {
            this.barChartList = currentData
            notifyDataSetChanged()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val itemBinding = ItemBarChartBinding.bind(itemView)

            fun bind(data: BarChartData) {
                itemBinding.titleNameTextView.text = data.name
                itemBinding.degreeTextView.text = data.degreeText
                itemBinding.degreeTextNumberTextView.text = "${data.minNormal} - ${data.maxNormal}"

                drawChart(data)
            }

            private fun drawChart(data: BarChartData) {
                data.also {
                    itemBinding.degreeNumberTextView.text = it.value.toString()
                    when {
                        it.value in it.minNormal!!..it.maxNormal!! -> {
                            val x = it.maxNormal - it.minNormal
                            val y = (40).toDouble().div(x.toDouble()).toFloat()
                            val z = ((it.value!! - it.minNormal) * y)
                            val percent = ((z * 0.01) + 0.3).toFloat()
                            itemBinding.textTitleGuideline.setGuidelinePercent(percent)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                itemBinding.degreeNumberTextView.setTextColor(
                                    resources.getColor(
                                        R.color.green_A,
                                        null
                                    )
                                )
                                itemBinding.degreeTextView.setTextColor(
                                    resources.getColor(
                                        R.color.green_A,
                                        null
                                    )
                                )
                            }
                        }

                        it.value!! < it.minNormal -> {
                            val x = it.minNormal - it.min!!
                            val y: Float = (30).toDouble().div(x.toDouble()).toFloat()
                            val z = ((it.value - it.min) * y)
                            val percent = (z * 0.01).toFloat()
                            itemBinding.textTitleGuideline.setGuidelinePercent(percent)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                itemBinding.degreeNumberTextView.setTextColor(
                                    resources.getColor(
                                        R.color.yellow,
                                        null
                                    )
                                )
                                itemBinding.degreeTextView.setTextColor(
                                    resources.getColor(
                                        R.color.yellow,
                                        null
                                    )
                                )
                            }
                        }

                        it.value > it.maxNormal -> {
                            val x = it.max!! - it.maxNormal
                            val y: Float = (30).toDouble().div(x.toDouble()).toFloat()
                            val z = (it.value - it.maxNormal) * y
                            val percent = ((z * 0.01) + 0.7).toFloat()
                            itemBinding.textTitleGuideline.setGuidelinePercent(percent)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                itemBinding.degreeNumberTextView.setTextColor(
                                    resources.getColor(
                                        R.color.red,
                                        null
                                    )
                                )
                                itemBinding.degreeTextView.setTextColor(
                                    resources.getColor(
                                        R.color.red,
                                        null
                                    )
                                )
                            }
                        }

                    }
                }
            }
        }

    }
}

data class BarChartData(
    val name: String = "",
    val degreeText: String = "",
    val minNormal: Int? = null,
    val maxNormal: Int? = null,
    val max: Int? = null,
    val min: Int? = null,
    val value: Int? = null
)