package edu.acervigni.saludable.connectionHelper

import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.Network
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.*
import edu.acervigni.saludable.R
import edu.acervigni.saludable.view.LoginActivity

class ConnectionHelper() : AppCompatActivity() {

    companion object {

        @JvmStatic
        fun hayInternet(context: Context): Boolean {
            val cn: ConnectivityManager? =
                context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?

            if (cn != null) {
                val network: Network? = cn.activeNetwork
                if (network != null)
                    return true
            }
            return false
        }

        @JvmStatic
        fun crearAlertDialogoInternet(context: Context) {
            val alert: AlertDialog? =
                AlertDialog.Builder(context).setIcon(R.drawable.ic_baseline_cancel_24)
                    .setTitle("No tiene conexión a Internet.")
                    .setMessage(
                        "Puede utilizar la aplicación normalmente.\n" +
                                "Luego, se le pedirá que sincronice sus datos."
                    )
                    .setCancelable(true)
                    .setPositiveButton(
                        "ACEPTAR",
                        DialogInterface.OnClickListener { dialogInterface, i -> })
                    .show()
        }
    }



}
