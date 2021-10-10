package pan.alexander.githubclient.utils

import android.content.Context
import pan.alexander.githubclient.App

val Context.app: App
    get() = applicationContext as App
