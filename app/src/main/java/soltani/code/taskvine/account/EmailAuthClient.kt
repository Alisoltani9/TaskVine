package soltani.code.taskvine.account

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EmailAuthClient @Inject constructor(
    private val context: Context
) {
    private val firebaseAuth = FirebaseAuth.getInstance()


    suspend fun signUp(email: String, password: String): Result<Boolean> = try {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun signIn(email: String, password: String): Result<Boolean> = try {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun signOut() {
        firebaseAuth.signOut()
    }

}