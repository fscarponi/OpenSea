package it.fscarponi.opensea.utils

import android.content.Context
import android.widget.Toast
import com.markodevcic.peko.Peko
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun CoroutineScope.doWithPermissionAsync(context: Context, permission: String, deniedBlock: () -> Unit = {}, grantedBlock: () -> Unit) =
    launch(Dispatchers.Main) {
        runWithPermissionAsync(context, listOf(permission), deniedBlock, grantedBlock)
    }

fun CoroutineScope.doWithPermissionAsync(context: Context, permissions: List<String>, deniedBlock: () -> Unit = {}, grantedBlock: () -> Unit) =
    launch(Dispatchers.Main) {
        runWithPermissionAsync(context, permissions, deniedBlock, grantedBlock)
    }

suspend fun runWithPermissionAsync(context: Context, permissions: List<String>, deniedBlock: () -> Unit = {}, grantedBlock: () -> Unit) {
    val permissionResult = Peko.requestPermissionsAsync(context, *permissions.toTypedArray())
    if (permissionResult is PermissionResult.Granted) {
        grantedBlock()
    } else if (permissionResult is PermissionResult.Denied) {
        Toast.makeText(context, "Permission denied! \n ${permissionResult.deniedPermissions.joinToString("\n")}", Toast.LENGTH_SHORT).show()
        deniedBlock()
    }
}

