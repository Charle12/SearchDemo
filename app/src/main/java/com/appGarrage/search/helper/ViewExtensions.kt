package com.appGarrage.search.helper

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.androidadvance.topsnackbar.TSnackbar
import com.google.android.material.snackbar.Snackbar
import com.appGarrage.search.R


//Extension method to provide simpler access to {@link View#getResources()#getString(int)}.
fun View.getString(stringResId: Int): String = resources.getString(stringResId)


fun Context.toastShort(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastLong(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}


fun View.snack(@StringRes messageRes: Int) {
    snack(resources.getString(messageRes))
}

fun View.snack(
    message: String,
    backgroundColor: Int = R.color.black,
    textColor: Int = R.color.white,
    icon: Int = R.drawable.ic_cross_filled
) {
    val snackbar = TSnackbar.make(this, message, TSnackbar.LENGTH_LONG)
    snackbar.setActionTextColor(Color.WHITE)
    snackbar.setIconLeft(icon, 18f)
    snackbar.setIconPadding(16)
    val snackbarView = snackbar.view
    snackbarView.setBackgroundColor(resources.getColor(backgroundColor))
    val textView =
        snackbarView.findViewById<View>(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
    textView.setTextColor(resources.getColor(textColor))
    snackbar.show()
}

fun Snackbar.action(@StringRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(ContextCompat.getColor(context, color)) }
}

//Extension method to show a keyboard for View.
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

//Try to hide the keyboard and returns whether it worked
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
        log("keyboard error${AppConstants.AUTH_TOKEN}")
    }
    return false
}

//fun setCount(context: Context?, count: Int?, menu: Menu?) {
//    val menuItem: MenuItem? = menu?.findItem(R.id.action_cart)
//    val icon = menuItem?.icon as LayerDrawable
//    val badge: CountDrawable
//
//    // Reuse drawable if possible
//    val reuse = icon.findDrawableByLayerId(R.id.ic_group_count)
//    badge = (if (reuse != null && reuse is CountDrawable) {
//        reuse
//    } else {
//        context?.let { CountDrawable(it) }
//    }) as CountDrawable
//    badge.setCount(count.toString()!!)
//    icon.mutate()
//    icon.setDrawableByLayerId(R.id.ic_group_count, badge)
//}