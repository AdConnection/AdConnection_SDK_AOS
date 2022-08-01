package one.adconnection.addemo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import one.adconnection.sdk.AdConnector
import one.adconnection.sdk.AdConnectorListener
import one.adconnection.sdk.AdSize

class MediationBannerActivity : AppCompatActivity() {

    lateinit var adConnector: AdConnector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_banner)

        adConnector = AdConnector(this, "JxNuarw0z6Ro")
        adConnector.bindPlatform("COUPANG", "one.adconnection.addemo.ads.SubAdViewCoupang")
        adConnector.bindAdBannerView(findViewById(R.id.container))
    }

    fun onClick(v: View) {
        if (v.id == R.id.request) {
            requestAd()
        }
    }

    // 띠배너 광고 요청
    private fun requestAd() {
        val listener: AdConnectorListener = object : AdConnectorListener {

            override fun onReceiveAd(message: String?) {
                // 광고 수신 성공시 호출됩니다. plaform : 수신 성공한 광고 플랫폼명
                Log.d("ADConnection", "[Dynamic] onReceiveAd ")
            }

            override fun onFailedToReceiveAd(error: String?) {
                // 광고 수신 실패시 호출됩니다. AdConnection 광고가 아닌 타 플랫폼 광고는 SubAdView에서 자세한 실패 로그를 확인할 수 있습니다.
                Log.d("ADConnection", "[Dynamic] onFailedToReceiveAd : $error")
            }
        }

        if (adConnector != null) adConnector.requestBanner(AdSize.BANNER_320X100, listener)
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
}