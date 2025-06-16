package soltani.code.taskvine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import soltani.code.taskvine.account.EmailAuthClient
import soltani.code.taskvine.account.GoogleAuthClient
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val googleAuthClient: GoogleAuthClient,
    private val taskRepository: TaskRepository,
    private val emailAuthClient: EmailAuthClient
) : ViewModel() {

    private val _isSignedIn = MutableStateFlow(FirebaseAuth.getInstance().currentUser != null)
    val isSignedIn = _isSignedIn.asStateFlow()

    val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    init {
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            _isSignedIn.value = auth.currentUser != null
        }
    }

    fun signInWithGoogle(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = googleAuthClient.signIn()
            if (result) {
                taskRepository.clearLocalTasks()
                taskRepository.syncTasksFromFirebase()
            }
            onResult(result)
        }
    }
    fun signInWithEmail(email: String, password: String, onResult: (Result<Boolean>) -> Unit) {
        viewModelScope.launch {
            val result = emailAuthClient.signIn(email, password)
            result.onSuccess {
                taskRepository.clearLocalTasks()
                taskRepository.syncTasksFromFirebase()
            }
            onResult(result)

        }
    }

    fun signUpWithEmail(email: String, password: String, onResult: (Result<Boolean>) -> Unit) {
        viewModelScope.launch {
            val result = emailAuthClient.signUp(email, password)
            result.onSuccess {
                taskRepository.clearLocalTasks()
                taskRepository.syncTasksFromFirebase()
            }
            onResult(result)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            googleAuthClient.signOut()
            emailAuthClient.signOut()
            taskRepository.clearLocalTasks()

        }
    }
}
