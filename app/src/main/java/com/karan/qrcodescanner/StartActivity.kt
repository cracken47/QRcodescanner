package com.karan.qrcodescanner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        cnct_wifi_btn.setOnClickListener {

            if(ssid_et.text.toString().isEmpty()){
                ssid_et.setError("Enter UserName !")
            }else if(password_et.text.toString().isEmpty()){
                password_et.setError("Enter Password!")
            }else {
                Config.SSID = "\"" + ssid_et.text.toString() + "\""
                Config.PASS = password_et.text.toString()
                Toast.makeText(
                    applicationContext,
                    "Searching for wifi  " + Config.SSID,
                    Toast.LENGTH_LONG
                )
                    .show()
                connectToWPAWiFi(Config.SSID, Config.PASS)
            }
        }

        start_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

// start your next activity
            startActivity(intent)
        }


    }

    fun connectToWPAWiFi(ssid: String, pass: String) {
        if (isConnectedTo(ssid)) { //see if we are already connected to the given ssid
            Toast.makeText(applicationContext, "Connected to" + ssid, Toast.LENGTH_LONG).show()
            return
        }
        val wm: WifiManager =
            applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var wifiConfig = getWiFiConfig(ssid)
        if (wifiConfig == null) {//if the given ssid is not present in the WiFiConfig, create a config for it
            createWPAProfile(ssid, pass)
            wifiConfig = getWiFiConfig(ssid)
        }

        if(wifiConfig==null){
            Toast.makeText(applicationContext,"No wifi with this username found",Toast.LENGTH_LONG).show()
            return
        }
        wm.disconnect()
        wm.enableNetwork(wifiConfig!!.networkId, true)
        wm.reconnect()
        Log.d("mLOg", "intiated connection to SSID" + ssid)
    }

    fun isConnectedTo(ssid: String): Boolean {
        val wm: WifiManager =
            applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        if (!wm.isWifiEnabled) {
            wm.isWifiEnabled = true
            return false
        }
        if (wm.connectionInfo.ssid == ssid) {
            return true
        }
        return false
    }

    fun getWiFiConfig(ssid: String): WifiConfiguration? {
        val wm: WifiManager =
            applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiList = wm.configuredNetworks
        for (item in wifiList) {
            if (item.SSID != null && item.SSID.equals(ssid)) {
                return item
            }
        }
        return null
    }

    fun createWPAProfile(ssid: String, pass: String) {
        Log.d("mLOg", "Saving SSID :" + ssid)
        val conf = WifiConfiguration()
        conf.SSID = ssid
        conf.preSharedKey = pass
        val wm: WifiManager =
            applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wm.addNetwork(conf)
        Log.d("mLOg", "saved SSID to WiFiManger")
    }

    class WiFiChngBrdRcr :
        BroadcastReceiver() { // shows a toast message to the user when device is connected to a AP
        private val TAG = "WiFiChngBrdRcr"
        override fun onReceive(context: Context, intent: Intent) {
            val networkInfo = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
            if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                val bssid = intent.getStringExtra(WifiManager.EXTRA_BSSID)
                Log.d(TAG, "Connected to BSSID:" + bssid)
                val ssid = intent.getParcelableExtra<WifiInfo>(WifiManager.EXTRA_WIFI_INFO).ssid
                val log = "Connected to SSID:" + ssid
                Log.d(TAG, "Connected to SSID:" + ssid)
                Toast.makeText(context, log, Toast.LENGTH_SHORT).show()
            }
        }
    }

    object Config {
         var SSID = "\"ssid\""
        var PASS = "\"passphrase\""
    }
}
