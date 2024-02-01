package gr.gkortsaridis.superherosquadmaker.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

abstract class BaseViewModel() : ViewModel(), CoroutinesUseCaseRunner {

    override val useCaseCoroutineScope = viewModelScope
    override fun withUseCaseScope(
        loadingUpdater: (suspend (Boolean) -> Unit)?,
        onError: (suspend (Throwable) -> Unit)?,
        onComplete: (() -> Unit)?,
        block: suspend () -> Unit
    ) {
        super.withUseCaseScope(
            loadingUpdater = {
                loadingUpdater?.invoke(it)
            },
            onError = {
                onError?.invoke(it)
            },
            onComplete = onComplete,
            block = block
        )
    }
}