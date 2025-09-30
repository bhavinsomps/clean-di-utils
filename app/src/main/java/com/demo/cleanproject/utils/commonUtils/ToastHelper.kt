package com.demo.cleanproject.utils.commonUtils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.demo.cleanproject.R
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ToastHelper @Inject constructor(@param:ActivityContext private val context: Context) {
    private val activity: Activity
        get() = context as Activity // Cast to Activity for convenience

    private val lifecycleScope: LifecycleCoroutineScope
        get() = (activity as LifecycleOwner).lifecycleScope

    fun showToastWithCompletion(msg: String, completion: (b: Boolean) -> Unit) {
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            completion(true)
        }
    }

    fun showToast(msg: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    fun showToastCustom(msg: String, type: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
            CustomToast.Companion.makeText(
                activity,
                msg,
                CustomToast.Companion.LENGTH_SHORT,
                type,
                isIcon = false
            ).show()
        }
    }
}

class CustomToast private constructor(context: Context) : Toast(context) {
    private var mView: View? = null
    private var mType: Int = TYPE_INFO

    companion object {
        const val TYPE_SUCCESS = 1
        const val TYPE_ERROR = 2
        const val TYPE_WARNING = 3
        const val TYPE_INFO = 4
        const val LENGTH_SHORT = Toast.LENGTH_SHORT

        @Suppress("DEPRECATION")
        @SuppressLint("MissingInflatedId", "InflateParams")
        fun makeText(
            context: Context,
            message: String,
            duration: Int,
            type: Int,
            isIcon: Boolean,
        ): CustomToast {
            val mdToast = CustomToast(context)

            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.custom_toast_container, null)
            val icon: ImageView = view.findViewById(R.id.icon)
            val text: TextView = view.findViewById(R.id.textLable)
            val bgView: LinearLayout = view.findViewById(R.id.mLlBgView)
            val backgroundRes = when (type) {
                TYPE_SUCCESS -> R.color.colorGreen
                TYPE_WARNING -> R.color.colorWarning
                TYPE_ERROR -> R.color.colorError
                else -> R.color.colorDef
            }
            bgView.backgroundTintList = ContextCompat.getColorStateList(context, backgroundRes)
            icon.visibility = View.GONE
            if (isIcon) {
                icon.visibility = View.VISIBLE
            }
            text.text = message
            mdToast.duration = duration
            mdToast.view = view
            mdToast.mView = view
            mdToast.mType = type

            return mdToast
        }
    }

    override fun setText(@StringRes resId: Int) {
        setText(mView?.context?.getString(resId) ?: return)
    }

    override fun setText(s: CharSequence) {
        val tv: TextView? = mView?.findViewById(R.id.textLable)
        tv?.text = s
    }

    fun setIcon(@DrawableRes iconId: Int) {
        setIcon(ContextCompat.getDrawable(mView?.context ?: return, iconId) ?: return)
    }

    private fun setIcon(icon: Drawable) {
        val iv: ImageView? = mView?.findViewById(R.id.icon)
        iv?.setImageDrawable(icon)
    }

    fun setType(type: Int) {
        mType = type
    }

    fun getType(): Int {
        return mType
    }

}
