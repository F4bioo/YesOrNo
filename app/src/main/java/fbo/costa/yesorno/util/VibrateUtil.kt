package fbo.costa.yesorno.util

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi

class VibrateUtil(private val vibrator: Vibrator) {

    companion object {
        const val NO_REPEAT = -1
        const val REPEAT = 0
    }

    operator fun set(millis: LongArray, repeat: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibratorGreater26(millis, repeat)
        } else {
            vibratorUnder26(millis, repeat)
        }
    }

    fun getPattern(position: Int) = arrayOf(
        longArrayOf(0, 50, 50, 50),
        longArrayOf(0, 50),
        longArrayOf(0, 10, 100, 10, 100, 10, 100, 10, 100, 10),
        longArrayOf(0, 100, 100, 100)
    )[position]

    fun set(millis: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibratorGreater26(millis)
        } else {
            vibratorUnder26(millis)
        }
    }

    fun cancel() {
        vibrator.cancel()
    }

    /**
     * Vibração com repetição e customizado para Android acima ou igual a API 26
     *
     * @param millis tempo de vibração
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun vibratorGreater26(millis: LongArray, repeat: Int) {
        vibrator.vibrate(VibrationEffect.createWaveform(millis, repeat))
    }

    /**
     * Vibração com repetição e customizado para Android menor que a API 26
     *
     * @param millis tempo de vibração
     */
    @Suppress("DEPRECATION")
    private fun vibratorUnder26(millis: LongArray, repeat: Int) {
        vibrator.vibrate(millis, repeat)
    }

    /**
     * Vibração sem repetição para Android acima ou igual a API 26
     *
     * @param millis tempo de vibração
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun vibratorGreater26(millis: Long) {
        vibrator.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    /**
     * Vibração sem repetição para Android menor que a API 26
     *
     * @param millis tempo de vibração
     */
    @Suppress("DEPRECATION")
    private fun vibratorUnder26(millis: Long) {
        vibrator.vibrate(millis)
    }
}
