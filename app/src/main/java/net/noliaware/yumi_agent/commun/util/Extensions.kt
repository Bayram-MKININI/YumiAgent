package net.noliaware.yumi_agent.commun.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.FlowCollector
import net.noliaware.yumi_agent.BuildConfig
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.*
import net.noliaware.yumi_agent.commun.data.remote.dto.AppMessageDTO
import net.noliaware.yumi_agent.commun.data.remote.dto.ErrorDTO
import net.noliaware.yumi_agent.commun.data.remote.dto.SessionDTO
import net.noliaware.yumi_agent.commun.domain.model.AppMessageType
import net.noliaware.yumi_agent.commun.domain.model.SessionData
import net.noliaware.yumi_agent.feature_login.presentation.controllers.LoginActivity
import java.io.Serializable
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun isNetworkReachable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        @Suppress("DEPRECATION")
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        return networkInfo.isConnected
    }
}

fun generateToken(timestamp: String, methodName: String, randomString: String): String {
    return "noliaware|$timestamp|${methodName}|${timestamp.reversed()}|$randomString".sha256()
}

fun getCommonWSParams(sessionData: SessionData, tokenKey: String) = mapOf(
    LOGIN to sessionData.login,
    APP_VERSION to BuildConfig.VERSION_NAME,
    DEVICE_ID to sessionData.deviceId,
    SESSION_ID to sessionData.sessionId,
    SESSION_TOKEN to sessionData.sessionTokens[tokenKey].toString()
)

suspend fun <T> FlowCollector<Resource<T>>.handleSessionWithNoFailure(
    session: SessionDTO?,
    sessionData: SessionData,
    tokenKey: String,
    appMessage: AppMessageDTO?,
    error: ErrorDTO?
): Boolean {

    val errorType = session?.let { sessionDTO ->
        sessionData.apply {
            sessionId = sessionDTO.sessionId
            sessionTokens[tokenKey] = sessionDTO.sessionToken
        }

        ErrorType.RECOVERABLE_ERROR
    } ?: run {
        ErrorType.SYSTEM_ERROR
    }

    error?.let { errorDTO ->
        emit(
            Resource.Error(
                errorType = errorType,
                errorMessage = errorDTO.errorMessage,
                appMessage = appMessage?.toAppMessage()
            )
        )
        return false
    } ?: run {
        return true
    }
}

fun handlePaginatedListErrorIfAny(
    session: SessionDTO?,
    sessionData: SessionData,
    tokenKey: String
): ErrorType {
    val errorType = session?.let { sessionDTO ->
        sessionData.apply {
            sessionId = sessionDTO.sessionId
            sessionTokens[tokenKey] = sessionDTO.sessionToken
        }
        ErrorType.RECOVERABLE_ERROR
    } ?: run {
        ErrorType.SYSTEM_ERROR
    }
    return errorType
}

fun String.parseDateToFormat(
    destFormat: String
) = parseDateStringToFormat(this, DATE_SOURCE_FORMAT, destFormat).orEmpty()

fun String.parseTimeToFormat(
    destFormat: String
) = parseDateStringToFormat(this, TIME_SOURCE_FORMAT, destFormat).orEmpty()

private fun parseDateStringToFormat(
    sourceDate: String,
    sourceFormat: String,
    destFormat: String
): String? {
    val sourceFormatter = SimpleDateFormat(sourceFormat, Locale.FRANCE)
    val date = sourceFormatter.parse(sourceDate)
    val destFormatter = SimpleDateFormat(destFormat, Locale.FRANCE)
    date?.let {
        return destFormatter.format(it)
    }
    return null
}

fun Long.parseTimestampToDate(
    destFormat: String
): String = SimpleDateFormat(
    destFormat,
    Locale.FRANCE
).format(this * 1000L)

fun Int.parseSecondsToMinutesString(): String = SimpleDateFormat(
    MINUTES_TIME_FORMAT,
    Locale.FRANCE
).format(this * 1000L)

fun Fragment.handleSharedEvent(
    sharedEvent: UIEvent
) = context?.let {

    when (sharedEvent) {

        is UIEvent.ShowAppMessage -> {

            val appMessage = sharedEvent.appMessage
            when (appMessage.type) {
                AppMessageType.POPUP -> {
                    MaterialAlertDialogBuilder(it)
                        .setTitle(appMessage.title)
                        .setMessage(appMessage.body)
                        .setPositiveButton(R.string.ok) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setCancelable(false)
                        .create().apply {
                            setCanceledOnTouchOutside(false)
                            show()
                        }
                }

                AppMessageType.SNACKBAR -> {
                    Snackbar.make(
                        requireView(),
                        appMessage.body,
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                AppMessageType.TOAST -> {
                    Toast.makeText(
                        context,
                        appMessage.body,
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> Unit
            }
        }

        is UIEvent.ShowError -> {
            Toast.makeText(
                context,
                getString(sharedEvent.errorStrRes),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

fun Fragment.redirectToLoginScreenFromSharedEvent(sharedEvent: UIEvent) {
    if (sharedEvent is UIEvent.ShowError) {
        if (sharedEvent.errorType == ErrorType.SYSTEM_ERROR) {
            redirectToLoginScreenInternal()
        }
    }
}

fun Fragment.handlePaginationError(loadState: CombinedLoadStates) {
    when (val currentState = loadState.refresh) {
        is LoadState.Error -> {
            if (
                currentState.error is PaginationException &&
                (currentState.error as PaginationException).errorType == ErrorType.SYSTEM_ERROR
            ) {
                redirectToLoginScreenInternal()
            }
        }

        else -> Unit
    }
}

private fun Fragment.redirectToLoginScreenInternal() {
    startActivity(
        Intent(requireActivity(), LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )
}

inline fun <reified T : Serializable> Intent.getSerializableExtraCompat(key: String): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializableExtra(key) as? T
    }

fun <T : Fragment> T.withArgs(vararg pairs: Pair<String, Any?>) =
    apply { arguments = bundleOf(*pairs) }

fun ViewGroup.inflate(
    layoutRes: Int,
    attachToRoot: Boolean = false
): View = LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun View.getStatusBarHeight() = convertDpToPx(24)

fun View.measureWrapContent() {
    measure(
        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
    )
}

fun View.layoutToTopLeft(left: Int, top: Int) {
    val right = left + measuredWidth
    val bottom = top + measuredHeight
    layout(left, top, right, bottom)
}

fun View.layoutToTopRight(right: Int, top: Int) {
    val left = right - measuredWidth
    val bottom = top + measuredHeight
    layout(left, top, right, bottom)
}

fun View.layoutToBottomLeft(left: Int, bottom: Int) {
    val right = left + measuredWidth
    val top = bottom - measuredHeight
    layout(left, top, right, bottom)
}

fun View.layoutToBottomRight(right: Int, bottom: Int) {
    val left = right - measuredWidth
    val top = bottom - measuredHeight
    layout(left, top, right, bottom)
}

fun View.convertDpToPx(dpValue: Int): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, dpValue.toFloat(), context.resources.displayMetrics
).toInt()

fun View.getLocationRectOnScreen(): Rect {
    val location = IntArray(2)
    getLocationOnScreen(location)
    return Rect().apply {
        left = location[0]
        top = location[1]
        right = left + measuredWidth
        bottom = top + measuredHeight
    }
}

fun ViewPager2.removeOverScroll() {
    (getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
}

@JvmOverloads
@Dimension(unit = Dimension.PX)
fun Number.dpToPx(
    metrics: DisplayMetrics = Resources.getSystem().displayMetrics
): Float {
    return toFloat() * metrics.density
}

@JvmOverloads
@Dimension(unit = Dimension.DP)
fun Number.pxToDp(
    metrics: DisplayMetrics = Resources.getSystem().displayMetrics
): Float {
    return toFloat() / metrics.density
}

fun Context.showKeyboard() {
    (this as? Activity)?.let {
        WindowInsetsControllerCompat(window, window.decorView).show(WindowInsetsCompat.Type.ime())
    }
}

fun Context.hideKeyboard() {
    (this as? Activity)?.let {
        WindowInsetsControllerCompat(window, window.decorView).hide(WindowInsetsCompat.Type.ime())
    }
}

inline fun <reified T : View> View.find(id: Int): T = findViewById(id)
inline fun <reified T : View> Activity.find(id: Int): T = findViewById(id)
inline fun <reified T : View> Fragment.find(id: Int): T = view?.findViewById(id) as T
inline fun <reified T : View> RecyclerView.ViewHolder.find(id: Int): T =
    itemView.findViewById(id) as T

inline fun <reified T : View> View.findOptional(id: Int): T? = findViewById(id) as? T
inline fun <reified T : View> Activity.findOptional(id: Int): T? = findViewById(id) as? T
inline fun <reified T : View> Fragment.findOptional(id: Int): T? = view?.findViewById(id) as? T
inline fun <reified T : View> RecyclerView.ViewHolder.findOptional(id: Int): T? =
    itemView.findViewById(id) as? T

fun String.sha256(): String {
    return try {
        val md = MessageDigest.getInstance("SHA-256")
        val messageDigest = md.digest(this.toByteArray(StandardCharsets.UTF_8))
        val number = BigInteger(1, messageDigest)
        val hexString = StringBuilder(number.toString(16))
        while (hexString.length < 64) {
            hexString.insert(0, '0')
        }
        hexString.toString()

    } catch (e: NoSuchAlgorithmException) {
        throw RuntimeException(e)
    }
}

@ColorInt
fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

fun Context.getDrawableCompat(@DrawableRes drawableRes: Int) =
    AppCompatResources.getDrawable(this, drawableRes)

@CheckResult
fun Drawable.tint(@ColorInt color: Int): Drawable {
    val tintedDrawable = DrawableCompat.wrap(this).mutate()
    DrawableCompat.setTint(tintedDrawable, color)
    return tintedDrawable
}

@CheckResult
fun Drawable.tint(context: Context, @ColorRes color: Int): Drawable {
    return tint(context.getColorCompat(color))
}

fun Number.formatNumber(): String = NumberFormat.getNumberInstance(Locale.getDefault()).format(this)

fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)
val <T> T.exhaustive: T get() = this

fun Context.startWebBrowserAtURL(url: String) {
    Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }.run {
        startActivity(this)
    }
}