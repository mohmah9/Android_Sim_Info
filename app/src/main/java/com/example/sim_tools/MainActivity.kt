package com.example.sim_tools

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.CellInfoGsm
import android.telephony.CellInfo






class MainActivity : AppCompatActivity() {

    private var permit_s =1
    private var permit_a=1
    lateinit var tm : TelephonyManager
//    lateinit var ci : CellIdentity
    lateinit var phone_model :String
    lateinit var phone_brand :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this ,Manifest.permission.READ_PHONE_STATE)== PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this ,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

        }else{
            permissiongranter()
        }
        val Actionbutton : Button = findViewById(R.id.ACTION_button)
        tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        phone_model= Build.MODEL
        phone_brand = Build.BRAND
        Actionbutton.setOnClickListener {
            Toast.makeText(this, "viewing info ...", Toast.LENGTH_SHORT).show()
            getinfo()
        }

    }

    @SuppressLint("MissingPermission")
    private fun getinfo() {
        var ci1 = tm.allCellInfo
        val out = getCellInfo(ci1.get(0))
        println(out)
        var final : String = ""
        val imei_num : String = tm.getDeviceId()
        val imsi_num : String = tm.getSubscriberId()
        val plmn_num : String = tm.getNetworkOperator()
        val sim_opt_name : String = tm.getSimOperatorName()
        val net_opt_country : String = tm.getNetworkCountryIso()
        val net_type : String = getNetworkClass()
//        val cell_identity : Int = CellIdentityLte
        var msisdn : String = tm.getLine1Number()
        if (msisdn==null || msisdn==""){
            msisdn="Not Available"
        }
        final += "IMEI=" +imei_num+"\n"
        final += "Phone model="+phone_model+"\n"
        final += "Phone brand="+phone_brand+"\n"
        final += "IMSI=" +imsi_num+"\n"
        final += "sim operator_name=" +sim_opt_name+"\n"
        final += "net operator_country=" +net_opt_country+"\n"
        final += "net operator PLMNid=" +plmn_num+"\n"
        final += "net type=" +net_type+"\n"
        final += "MSISDN=" +msisdn+"\n"
        final += out
        val INFOtext : TextView = findViewById(R.id.INFO_text)
        INFOtext.text = final
    }

    private fun permissiongranter(){
        if( ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_PHONE_STATE) ){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Permission needed")
            builder.setMessage("we need this permission to continue.")
//            builder.setIcon(android.R.drawable.btn_star)
            builder.setPositiveButton("OK"){dialogInterface, which ->
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_FINE_LOCATION),permit_s)

            }
            builder.setNegativeButton("Cancel"){dialogInterface, which ->
                dialogInterface.dismiss()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_FINE_LOCATION),permit_s)

        }
    }

    private fun getNetworkClass(): String {
        val networkType = tm.getDataNetworkType()
        when (networkType) {
            TelephonyManager.NETWORK_TYPE_GPRS -> return "GPRS(2G)"
            TelephonyManager.NETWORK_TYPE_GSM -> return "GSM(2G)"
            TelephonyManager.NETWORK_TYPE_EDGE -> return "EDGE(2G)"
            TelephonyManager.NETWORK_TYPE_CDMA -> return "CDMA(2G)"
            TelephonyManager.NETWORK_TYPE_1xRTT -> return "1XRTT(2G)"
            TelephonyManager.NETWORK_TYPE_IDEN -> return "IDEN(2G)"
            TelephonyManager.NETWORK_TYPE_UMTS -> return "UMTS(3G)"
            TelephonyManager.NETWORK_TYPE_TD_SCDMA -> return "TD_SCDMA(3G)"
            TelephonyManager.NETWORK_TYPE_EVDO_0-> return "EVDO_0(3G)"
            TelephonyManager.NETWORK_TYPE_EVDO_A-> return "EVDO_A(3G)"
            TelephonyManager.NETWORK_TYPE_HSDPA-> return "HSDPA(3G)"
            TelephonyManager.NETWORK_TYPE_HSUPA-> return "HSUPA(3G)"
            TelephonyManager.NETWORK_TYPE_HSPA-> return "HSPA(3G)"
            TelephonyManager.NETWORK_TYPE_EVDO_B-> return "EVDO_B(3G)"
            TelephonyManager.NETWORK_TYPE_EHRPD-> return "EHRPD(3G)"
            TelephonyManager.NETWORK_TYPE_HSPAP -> return "HSPAP(3G)"
            TelephonyManager.NETWORK_TYPE_LTE -> return "LTE(4G)"
//            TelephonyManager.NETWORK_TYPE_NR -> return "5G"
            TelephonyManager.NETWORK_TYPE_UNKNOWN -> return "Unknown"
            else -> return "No network"
        }
    }
    private fun getCellInfo(cellInfo: CellInfo): String {
        var additional_info: String =""
        if (cellInfo is CellInfoGsm) {
            val cellIdentityGsm = cellInfo.cellIdentity
            additional_info = ("cell identity " + cellIdentityGsm.cid + "\n"
                    + "Mobile country code " + cellIdentityGsm.mcc + "\n"
                    + "Mobile network code " + cellIdentityGsm.mnc + "\n"
                    + "local area " + cellIdentityGsm.lac + "\n")
        } else if (cellInfo is CellInfoLte) {
            val cellIdentityLte = cellInfo.cellIdentity
            additional_info = ("cell identity " + cellIdentityLte.ci + "\n"
                    + "Mobile country code " + cellIdentityLte.mcc + "\n"
                    + "Mobile network code " + cellIdentityLte.mnc + "\n"
                    + "physical cell " + cellIdentityLte.pci + "\n"
                    + "Tracking area code " + cellIdentityLte.tac + "\n")
        } else if (cellInfo is CellInfoWcdma) {
            val cellIdentityWcdma = cellInfo.cellIdentity
            additional_info = ("cell identity " + cellIdentityWcdma.cid + "\n"
                    + "Mobile country code " + cellIdentityWcdma.mcc + "\n"
                    + "Mobile network code " + cellIdentityWcdma.mnc + "\n"
                    + "local area " + cellIdentityWcdma.lac + "\n")
        }
        return additional_info
    }
}
