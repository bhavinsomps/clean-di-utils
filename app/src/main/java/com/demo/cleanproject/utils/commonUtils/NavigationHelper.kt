package com.demo.cleanproject.utils.commonUtils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import java.io.Serializable
import javax.inject.Inject

@ActivityScoped
class NavigationHelper @Inject constructor(@param:ActivityContext private val context: Context){

    private val activity: Activity
        get() = context as Activity // Cast to Activity for convenience

    fun openActivityWithBundle(cls: Class<*>?, bundle: Bundle?) {
        val intent = Intent(context, cls)
        intent.putExtras(bundle!!)
        context.startActivity(intent)
    }

    fun openActivityWithBundleClearBackStack(cls: Class<*>?, bundle: Bundle?) {
        val intent = Intent(context, cls)
        intent.putExtras(bundle!!)
        clearActivityStack(intent)
        context.startActivity(intent)
    }

    @Suppress("DEPRECATION", "UNCHECKED_CAST")
    fun <T : Serializable?> getSerializable(intent: Intent, key: String, mClass: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(key, mClass)!!
        else
            intent.getSerializableExtra(key) as T //This is suppressed code line
    }

    fun openActivity(cls: Class<*>?) {
        val intent = Intent(context, cls)
        context.startActivity(intent)
    }

    fun openActivityAndClearPreviousStack(cls: Class<*>?) {
        val intent = Intent(context, cls)
        clearActivityStack(intent)
        context.startActivity(intent)
    }

    private fun clearActivityStack(intent: Intent) {
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    }

    lateinit var manager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    fun addFragment(fragment: Fragment, addStack: Boolean, tag: String?) {
        manager = (context as FragmentActivity).supportFragmentManager
        fragmentTransaction = manager.beginTransaction()
        if (addStack) {
            fragmentTransaction.addToBackStack(tag)
        }
//        ft.replace(R.id.FLMainActivity, fragment, tag)
        fragmentTransaction.commitAllowingStateLoss()
    }

    fun removeAllFragment(replaceFragment: Fragment?, tag: String?) {
        manager = (context as FragmentActivity).supportFragmentManager
        fragmentTransaction = manager.beginTransaction()
        manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//        ft.replace(R.id.FLMainActivity, replaceFragment!!)
        fragmentTransaction.commitAllowingStateLoss()
    }


    private lateinit var reviewManager: ReviewManager
    private lateinit var reviewInfo: ReviewInfo
    fun rateUsAtPlayStore() {
        try {
            val uri =
                "https://play.google.com/store/apps/details?id=${context.packageName}".toUri()
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    fun showRateAppGoogleDefault() {
        Log.d("reviewWhat", "> showRate")
        reviewManager = ReviewManagerFactory.create(context)
        val request: Task<ReviewInfo> = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task: Task<ReviewInfo> ->
            if (task.isSuccessful) {
                Log.d("reviewWhat", "> rate shown successfully")
                reviewInfo = task.result
                val flow: Task<Void?> = reviewManager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener { task1: Task<Void?>? -> }
            }
        }
    }

    fun sendEmail(recipient: String, subject: String, message: String) {
        val selectorIntent = Intent(Intent.ACTION_SENDTO)
        selectorIntent.data = "mailto:".toUri()

        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, message)
        emailIntent.selector = selectorIntent
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Choose Email Client..."))
        } catch (e: Exception) {
            Log.d("sendEmail", ":emailEx ${e.message}")
        }
    }

    fun openBrowser(url: String) {
        try {
            var formattedUrl = url
            if (!formattedUrl.startsWith("http://") && !formattedUrl.startsWith("https://")) {
                formattedUrl = "http://$formattedUrl"
            }
            val intent = Intent(Intent.ACTION_VIEW, formattedUrl.toUri())
            context.startActivity(Intent.createChooser(intent, "Choose browser"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}