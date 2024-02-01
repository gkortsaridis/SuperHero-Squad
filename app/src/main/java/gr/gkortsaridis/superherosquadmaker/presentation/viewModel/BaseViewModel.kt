package gr.gkortsaridis.superherosquadmaker.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.gkortsaridis.superherosquadmaker.utils.CoroutinesUseCaseRunner

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