package gr.gkortsaridis.superherosquadmaker.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface CoroutinesUseCaseRunner {
    val useCaseCoroutineScope: CoroutineScope

    fun withUseCaseScope(
        loadingUpdater: (suspend (Boolean) -> Unit)? = null,
        onError: (suspend (Throwable) -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
        block: (suspend () -> Unit)
    ) {
        useCaseCoroutineScope.launch {
            loadingUpdater?.invoke(true)
            try {
                block()
            } catch (e: Exception) {
                onError?.invoke(e)
            } finally {
                loadingUpdater?.invoke(false)
                onComplete?.invoke()
            }
        }
    }
}