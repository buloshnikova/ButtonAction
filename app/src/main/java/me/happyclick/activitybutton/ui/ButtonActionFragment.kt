package me.happyclick.activitybutton.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.ww.roxie.BaseViewModel
import kotlinx.android.synthetic.main.button_action.*
import me.happyclick.activitybutton.R
import me.happyclick.activitybutton.di
import me.happyclick.activitybutton.util.NotificationsHelper

class ButtonActionFragment : Fragment() {

    companion object {
        fun newInstance() = ButtonActionFragment()
    }

    private lateinit var viewModel: BaseViewModel<ButtonAction, ButtonActionState>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.button_action, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = this.context!!.di.buttonActionViewModel

        viewModel.observableState.observe(this, Observer { state ->
            state?.let { renderState(it) }
        })

        activity_button.setOnClickListener {
            viewModel.dispatch(ButtonAction.GetActionButtonClicked)
        }
    }

    private fun renderState(state: ButtonActionState) {
        with(state) {
            if (!action.isNullOrEmpty()) {
                Log.d("XXX", action.get(0).type ?: " empty")
                when (action.get(0).type) {
                    ButtonType.ANIMATION.value -> rotateButton()
                    ButtonType.TOAST.value -> showSnackbar(activity_button)
                    ButtonType.CALL.value -> call()
                    ButtonType.NOTIFICATION.value -> showNotification()
                    else -> rotateButton()
                }

            }
            activity_button.isEnabled = !activity
        }
    }

    private fun rotateButton() {
        val rotateAnimation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.duration = 5000
        rotateAnimation.interpolator = LinearInterpolator()
        activity_button.startAnimation(rotateAnimation)

    }

    private fun showSnackbar(view: View) {
        val snackbar = Snackbar.make(
            view, resources.getString(R.string.snackbar_text),
            Snackbar.LENGTH_LONG
        ).setAction(resources.getString(R.string.action_text), null)
        snackbar.setActionTextColor(Color.WHITE)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.BLACK)
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 18f
        snackbar.show()
    }

    private fun call() {}

    private fun showNotification() {
        NotificationsHelper(this.context!!).createNofitication()
    }
}