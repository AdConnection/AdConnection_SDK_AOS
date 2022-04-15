package one.adconnection.addemo.ads

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.coupang.ads.config.AdsCreativeSize
import com.coupang.ads.config.AdsMode
import com.coupang.ads.view.banner.AdsBannerView
import com.coupang.ads.viewmodels.AdsViewModel
import com.coupang.ads.tools.createAdsViewModel
import com.coupang.ads.view.banner.auto.AutoScrollBannerView
import one.adconnection.sdk.SubAdViewCore

class SubAdViewCoupang(context: Context?, attrs: AttributeSet? = null) :
    SubAdViewCore(context, attrs) {

    var myctx: Context? = null
    var adView : AdsBannerView? = null
    //var adView : AutoScrollBannerView? = null


    override fun query() {
        if(adView == null) {
            initCoupang(myctx)
        }
    }
    
    fun initCoupang(ctx: Context?) {
        if(ctx == null) {
            return
        }

        adView = AdsBannerView(ctx)
        //adView = AutoScrollBannerView(ctx)
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        adView!!.layoutParams = params
        this.addView(adView)

        val bannerAdsViewModel: AdsViewModel = (ctx as AppCompatActivity).createAdsViewModel(
            "YOUR WIDGET ID",
            AdsCreativeSize._320x100,
            AdsMode.AUTO
        )
        /*
        val bannerAdsViewModel: AdsViewModel = (ctx as AppCompatActivity).createAdsViewModel(
            "YOUR WIDGET ID",
            AdsCreativeSize._320x100,
            AdsMode.SCROLL
        )
        */
        adView!!.bindViewModel(context as AppCompatActivity,  bannerAdsViewModel)


        // Observe ads loading results.
        bannerAdsViewModel.observe(context as AppCompatActivity) {
            if (it.isSuccess) {
                // Code to be executed when an ad finishes loading.
                Log.i("observe", "loadAdData success")
                gotAd()
            } else {
                // Code to be executed when ad failed to load.
                Log.w("observe", "loadAdData failed", it.exceptionOrNull())
                failed()
            }
        }
        // Load ads data
        bannerAdsViewModel.loadAdData()
    }
    
    init {
        myctx = context
        initCoupang(context)
    }
}