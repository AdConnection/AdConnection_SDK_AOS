package one.adconnection.addemo

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import one.adconnection.sdk.AdConnector
import one.adconnection.sdk.AdConnectorListener
import one.adconnection.sdk.AdSize

class MainActivity : AppCompatActivity() {
    lateinit var adConnector: AdConnector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adConnector = AdConnector(this, "6QSCI3evCp5g")
        adConnector.bindAdBannerView(findViewById(R.id.container))
    }

    fun onClick(v: View) {
        if (v.id == R.id.request) {
            requestAd()
        }
    }

    // 전면배너 Dynamic 광고 요청
    private fun requestAd() {
        val listener: AdConnectorListener = object : AdConnectorListener {

            override fun onReceiveAd(message: String?) {
                Log.d("ADConnection", "[Dynamic] onReceiveAd ")
            }

            override fun onFailedToReceiveAd(error: String?) {
                Log.d("ADConnection", "[Dynamic] onFailedToReceiveAd : $error")
            }
        }

        if (adConnector != null) adConnector.requestBanner(AdSize.BANNER_320X100, listener)
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().getDisplayMetrics().widthPixels
    }

    override fun onResume() {
        super.onResume()
        if(adConnector != null) {
            adConnector.resume(this)
        }
    }

    override fun onPause() {
        if(adConnector != null) {
            adConnector.pause(this)
        }
        super.onPause()
    }

    override fun onDestroy() {
        if(adConnector != null) {
            adConnector.destroy(this)
        }
        super.onDestroy()
    }
}