package com.asaf.basic.basic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.interop.LocalUIViewController
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIGestureRecognizerStateEnded
import platform.UIKit.UIScreenEdgePanGestureRecognizer
import platform.UIKit.UIRectEdgeLeft
import platform.UIKit.UIViewController
import platform.darwin.NSObject

private class BackGestureDelegate(private val onBack: () -> Unit) : NSObject() {
    @ObjCAction
    fun handle(sender: UIScreenEdgePanGestureRecognizer) {
        if (sender.state == UIGestureRecognizerStateEnded) {
            onBack()
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    val currentOnBack by rememberUpdatedState(onBack)
    val vc: UIViewController? = LocalUIViewController.current

    DisposableEffect(enabled, vc) {
        if (!enabled || vc == null) return@DisposableEffect onDispose { }

        val delegate = BackGestureDelegate { currentOnBack() }
        val recognizer = UIScreenEdgePanGestureRecognizer(target = delegate, action = NSSelectorFromString("handle:"))
        recognizer.edges = UIRectEdgeLeft
        vc.view?.addGestureRecognizer(recognizer)

        onDispose {
            vc.view?.removeGestureRecognizer(recognizer)
        }
    }
}
