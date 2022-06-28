package one.adconnection.addemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import one.adconnection.sdk.AdConnector
import one.adconnection.sdk.NativeAdView
import one.adconnection.sdk.NativeAdViewBinder
import one.adconnection.sdk.core.native.NativeResultListener
import java.lang.Exception
import java.util.ArrayList

class NativeAdActivity : AppCompatActivity() {

    lateinit var adConnector: AdConnector
    private var listview: ListView? = null
    private val adIdx = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native)

        adConnector = AdConnector(this, "ciyYa7JBUdPZ")

        val adapter: ListViewAdapter = ListViewAdapter()
        listview = findViewById<View>(R.id.listview) as ListView
        listview!!.adapter = adapter
        for (i in 0..19) {
            adapter.addItem("Item " + (i + 1) + "")
        }
        requestAd()
    }

    private fun requestAd() {
        val viewBinder: NativeAdViewBinder = NativeAdViewBinder.Builder(
            R.layout.native_ad_template,
            R.id.native_ad_title,
            R.id.native_ad_icon
        )
            .setDescriptionId(R.id.native_ad_description)
            .setMainImageId(R.id.native_ad_main)
            .setButtonId(R.id.native_ad_button)
            .build()

        val listener: NativeResultListener = object : NativeResultListener {

            override fun onReceiveAd(nativeView: NativeAdView) {
                (listview!!.adapter as ListViewAdapter).addItem(
                    adIdx,
                    nativeView
                )
                (listview!!.adapter as ListViewAdapter).notifyDataSetChanged()
            }

            override fun onError(errorCode: Int) {
                Log.d("AdConnection", "[Native] onFailedToReceiveAd : $errorCode")
            }
        }

        if (adConnector != null) adConnector.requestNativeAd(viewBinder, listener)
    }

    override fun onResume() {
        super.onResume()
        if(adConnector!= null) {
            adConnector.resume(this)
        }
    }

    override fun onPause() {
        if(adConnector!= null) {
            adConnector.pause(this)
        }
        super.onPause()
    }

    override fun onDestroy() {
        if(adConnector!= null) {
            adConnector.destroy(this)
        }
        super.onDestroy()
    }

    private inner class ListViewAdapter : BaseAdapter() {
        private val listViewItemList = ArrayList<Any>()
        override fun getCount(): Int {
            return listViewItemList.size
        }

        override fun getItem(position: Int): Any {
            return listViewItemList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View? {
            var convertView = view
            if (convertView == null) {
                convertView = LayoutInflater.from(parent?.context).inflate(R.layout.list_item, null)
            }
            val adContainer = convertView?.findViewById<ViewGroup>(R.id.ad_container)
            val textView = convertView?.findViewById<TextView>(R.id.text_view)
            if (adContainer != null) {
                adContainer.removeAllViews()
            }
            if (listViewItemList[position] != null) {
                if (listViewItemList[position] is NativeAdView) {
                    adContainer?.visibility = View.VISIBLE
                    textView?.visibility = View.GONE
                    try {
                        val nativeAdView: NativeAdView = listViewItemList[position] as NativeAdView
                        adContainer?.addView(nativeAdView)
                    } catch (e: Exception) {
                    }
                } else {
                    adContainer?.visibility = View.GONE
                    textView?.visibility = View.VISIBLE
                    val listViewItem = listViewItemList[position] as String
                    textView?.text = listViewItem
                }
            }
            return convertView
        }

        fun addItem(item: Any) {
            listViewItemList.add(item)
        }

        fun addItem(index: Int, item: Any) {
            if (listViewItemList[index] is NativeAdView) {
                listViewItemList.removeAt(index)
            }
            listViewItemList.add(index, item)
        }
    }
}