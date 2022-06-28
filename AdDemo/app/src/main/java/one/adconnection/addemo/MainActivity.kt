package one.adconnection.addemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 샘플 프로젝트 화면 구성
        initLayout()
    }


    fun initLayout() {
        var itemList: ArrayList<DemoListItem> = ArrayList<DemoListItem>()

        itemList.add(DemoListItem("BANNER_MEDIATION"))
        itemList.add(DemoListItem("NATIVE"))

        val listView = findViewById<ListView>(R.id.listview)
        var adapter: DemoListAdapter = DemoListAdapter(itemList)
        listView.adapter = adapter
        listView.setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
            val item = parent.getItemAtPosition(position) as DemoListItem
            var intent: Intent? = null
            print(item.value)
            when (item.value) {
                "BANNER_MEDIATION" -> intent = Intent(this, MediationBannerActivity::class.java)
                "NATIVE" -> intent = Intent(this, NativeAdActivity::class.java)
            }

            if (intent != null) {
                startActivity(intent)
            }
        }
    }

    class DemoListAdapter(private val list: ArrayList<DemoListItem>) : BaseAdapter() {
        override fun getCount(): Int {
            return list.size
        }

        override fun getItem(position: Int): DemoListItem {
            return list[position]
        }

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            var convertView = view
            if (convertView == null) {
                convertView = LayoutInflater.from(parent?.context).inflate(R.layout.main_list_item, null)
            }
            var item = list[position]
            val txtItem = convertView?.findViewById<View>(R.id.text) as TextView
            txtItem.text = item.value
            return convertView
        }
    }

    data class DemoListItem(val value: String)
}