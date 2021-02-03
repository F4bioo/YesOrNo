package fbo.costa.yesorno.ui

import android.hardware.SensorManager
import android.os.*
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.jackandphantom.instagramvideobutton.InstagramVideoButton
import com.squareup.seismic.ShakeDetector
import dagger.hilt.android.AndroidEntryPoint
import fbo.costa.yesorno.R
import fbo.costa.yesorno.databinding.ActivityMainBinding
import fbo.costa.yesorno.util.DataState
import fbo.costa.yesorno.util.MainStateEvent
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), InstagramVideoButton.ActionListener,
    ShakeDetector.Listener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var blinkInfAnim: Animation? = null
    private var blink3xAnim: Animation? = null
    private var shakeAnim: Animation? = null
    private var canShake: Boolean = false
    private var sd: ShakeDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applicationContext

        blinkInfAnim = AnimationUtils.loadAnimation(this, R.anim.blink_inf_anim)
        blink3xAnim = AnimationUtils.loadAnimation(this, R.anim.blink_3x_anim)
        shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_anim)

        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sd = ShakeDetector(this)
        sd?.setSensitivity(10)
        sd?.start(sensorManager)

        binding.apply {
            imageAns.loadImage(null)
            instaButton.setMinimumVideoDuration(1000)
            instaButton.actionListener = this@MainActivity
        }

        eventsObserves()
    }

    private fun eventsObserves() {
        viewModel.ansEvent.observe(this, { _dataState ->
            canShake = false
            mngChronometer(base = true, run = false)
            mngVisibility(getString(R.string.text_description), true)

            when (_dataState) {
                is DataState.Loading -> {
                    binding.apply {
                        textAns.text = ""
                        imageAns.setImageBitmap(null)
                        progress.visibility = View.VISIBLE
                    }
                }
                is DataState.Success -> {
                    viewModel.setStateEvent(MainStateEvent.VibrateEvent(3))
                    binding.apply {
                        textAns.text = _dataState.data.answer
                        textAns.startAnimation(blink3xAnim)
                        imageAns.loadImage(_dataState.data.image)
                        progress.visibility = View.INVISIBLE
                    }
                }
                is DataState.Error -> {
                    binding.apply {
                        textAns.text = ""
                        imageAns.loadImage(null)
                        progress.visibility = View.INVISIBLE
                    }
                    Toast.makeText(this, _dataState.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun ShapeableImageView.loadImage(url: String?) {
        Glide.with(this@MainActivity)
            .load(url ?: R.drawable.yes_or_no)
            .centerCrop()
            .into(this)
    }

    override fun onStartRecord() {
        canShake = false
        viewModel.setStateEvent(MainStateEvent.VibrateEvent(1))
        mngChronometer(base = true, run = true)
        binding.apply {
            imageBlink.startAnimation(blinkInfAnim)
        }
    }

    override fun onEndRecord() {
        canShake = true
        viewModel.setStateEvent(MainStateEvent.VibrateEvent(2))
        mngChronometer(base = false, run = false)
        mngVisibility(getString(R.string.text_description_shake), false)
        binding.apply {
            imageBlink.clearAnimation()
            imageShake.startAnimation(shakeAnim)
            textDescription.startAnimation(shakeAnim)
        }
    }

    override fun onSingleTap() {
        canShake = false
        viewModel.setStateEvent(MainStateEvent.VibrateEvent(0))
        Snackbar.make(
            binding.imageBlink,
            getString(R.string.text_hold_to_rec),
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun onCancelled() {
    }

    override fun onDurationTooShortError() {
        mngChronometer(base = true, run = false)
        binding.imageBlink.clearAnimation()
    }

    override fun hearShake() {
        if (canShake) viewModel.setStateEvent(MainStateEvent.AnsEvent)
    }

    private fun mngVisibility(text: String, visible: Boolean) {
        binding.apply {
            textDescription.text = text
            instaButton.visibility = if (visible) View.VISIBLE else View.INVISIBLE
            imageMic.visibility = if (visible) View.VISIBLE else View.INVISIBLE
            imageShake.visibility = if (visible) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun mngChronometer(base: Boolean, run: Boolean) {
        binding.apply {
            if (base) timer.base = SystemClock.elapsedRealtime()
            if (run) timer.start() else timer.stop()
        }
    }

    override fun onStop() {
        sd?.stop()
        super.onStop()
    }

    override fun onDestroy() {
        mngChronometer(base = true, run = false)
        super.onDestroy()
    }
}
