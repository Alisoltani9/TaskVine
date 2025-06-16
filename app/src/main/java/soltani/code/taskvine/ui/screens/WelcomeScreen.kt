package soltani.code.taskvine.ui.screens

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.launch
import soltani.code.taskvine.R
import soltani.code.taskvine.helpers.LocalCustomColors
import soltani.code.taskvine.helpers.PreferenceHelper
import soltani.code.taskvine.ui.theme.BrunswickGreen
import soltani.code.taskvine.ui.theme.TimberWolf
import soltani.code.taskvine.viewmodel.AuthViewModel

@Composable
fun WelcomeScreen(
    navController: NavController,
    preferenceHelper: PreferenceHelper,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var authErrorMessage by remember { mutableStateOf<String?>(null) }

    var showEmailDialog by remember { mutableStateOf(false) }
    var isSignUpMode by remember { mutableStateOf(true) }

    val pagerState = rememberPagerState(pageCount = { 3 })
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }

    val titles = listOf(
        "Easily Create and Manage Your Tasks Anytime!",
        "Set Reminders and Never Miss an Important Task!",
        "Quickly Find and Organize Tasks with Powerful Search!"
    )
    val images = listOf(
        R.drawable.swipe_task,
        R.drawable.swipe_reminder,
        R.drawable.swipe_search
    )

    // Dialog UI
    if (showEmailDialog) {

        val submitButtonText = if (isSignUpMode) "Sign Up" else "Log In"

        EmailSignupAuthDialog(
            isVisible = showEmailDialog,
            isSignUp = isSignUpMode,
            submitButtonText = submitButtonText, // Pass to dialog
            onDismiss = { showEmailDialog = false; authErrorMessage = null },
            onSubmit = { emailInput, passwordInput ->


                coroutineScope.launch {
                    val handleResult: (Result<Boolean>) -> Unit = { result ->
                        result.fold(
                            onSuccess = {
                                val message = if (isSignUpMode)
                                    "Account created and logged in successfully."
                                else
                                    "You have successfully logged in."
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                preferenceHelper.setWelcomeScreenShown()
                                navController.navigate("home") { popUpTo(0) }
                                showEmailDialog = false
                                authErrorMessage = null
                            },
                            onFailure = { e ->
                                authErrorMessage = getAuthErrorMessage(e)
                            }
                        )
                    }

                    if (isSignUpMode) {
                        authViewModel.signUpWithEmail(emailInput, passwordInput, handleResult)
                    } else {
                        authViewModel.signInWithEmail(emailInput, passwordInput, handleResult)
                    }
                }
            },
            authErrorMessage = authErrorMessage // pass it here
        )


    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Skip button in top right
        Text(
            text = "Skip",
            color = Color.White,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clickable {
                    preferenceHelper.setWelcomeScreenShown()
                    navController.navigate("home") { popUpTo(0) }
                }
                .padding(8.dp)
        )
        // Main UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Pager with animation
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) { page ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(images[page]),
                            contentDescription = "Page Image",
                            modifier = Modifier
                                .size(200.dp)
                                .padding(bottom = 16.dp)
                        )
                        Text(
                            text = titles[page],
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
                AnimatedPagerIndicator(
                    totalDots = titles.size,
                    selectedIndex = currentPage,
                    activeColor = BrunswickGreen,
                    inactiveColor = TimberWolf
                )
            }

            // Buttons
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            authViewModel.signInWithGoogle { result ->
                                if (result) {
                                    preferenceHelper.setWelcomeScreenShown()
                                    navController.navigate("home")
                                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Google Sign-In Failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.icons8_google),
                        contentDescription = "Google Icon",
                        modifier = Modifier
                            .size(20.dp)
                            .offset(x = (-15).dp),
                        tint = Color.Unspecified
                    )
                    Text("Continue With Google", color = Color.Black)
                }

                Button(
                    onClick = { isSignUpMode = true; showEmailDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrunswickGreen)
                ) {
                    Text("Sign Up Using Email", color = MaterialTheme.colorScheme.onPrimary)
                }

                Button(
                    onClick = { isSignUpMode = false; showEmailDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrunswickGreen)
                ) {
                    Text(
                        "Already have an account? Sign In",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}
@Composable
fun AnimatedPagerIndicator(
    totalDots: Int,
    selectedIndex: Int,
    activeColor: Color,
    inactiveColor: Color
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        for (i in 0 until totalDots) {
            val color by animateColorAsState(
                targetValue = if (i == selectedIndex) activeColor else inactiveColor,
                animationSpec = tween(durationMillis = 300), label = ""
            )

            val size by animateDpAsState(
                targetValue = if (i == selectedIndex) 16.dp else 8.dp,
                animationSpec = tween(durationMillis = 300), label = ""
            )

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(size)
                    .background(color, shape = CircleShape)
            )
        }
    }
}


fun getAuthErrorMessage(e: Throwable): String {
    return when {
        e is FirebaseAuthException -> {
            when (e.errorCode) {
                "ERROR_WRONG_PASSWORD", "INVALID_LOGIN_CREDENTIALS" -> "Incorrect email or password."
                "ERROR_USER_NOT_FOUND" -> "No account found with this email."
                "ERROR_NETWORK_REQUEST_FAILED" -> "Network error. Please try again."
                "ERROR_EMAIL_ALREADY_IN_USE" -> "Email already registered."
                else -> "Login failed: ${e.localizedMessage ?: "Unknown error"}"
            }
        }
        e.message?.contains("INVALID_LOGIN_CREDENTIALS") == true -> "Incorrect email or password."
        else -> "Login failed: ${e.localizedMessage ?: "Unknown error"}"
    }
}
@Composable
fun EmailSignupAuthDialog(
    isVisible: Boolean,
    isSignUp: Boolean,
    submitButtonText: String,
    onDismiss: () -> Unit,
    onSubmit: (String, String) -> Unit,
    authErrorMessage: String? = null // add this
) {
    if (!isVisible) return

    val customColors = LocalCustomColors.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }


    val backgroundColor = customColors.cardBackground
    val textColor = customColors.primaryText
    val errorColor = MaterialTheme.colorScheme.error // Keeping the default error color
    val buttonColor = customColors.primaryText
    val buttonTextColor = customColors.primaryText
    val buttonPlaceHolderColor = customColors.secondaryText


    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isSignUp) "Sign Up" else "Sign In",
                color = textColor
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email")
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = buttonColor,
                        unfocusedTextColor = buttonColor,
                        unfocusedContainerColor = backgroundColor,
                        focusedContainerColor = backgroundColor,
                        unfocusedIndicatorColor = buttonColor,
                        focusedIndicatorColor =buttonColor ,
                        focusedLabelColor = buttonColor,
                        cursorColor = buttonColor,
                        unfocusedPlaceholderColor = buttonPlaceHolderColor,
                    )
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        // Please provide localized description for accessibility services
                        val description = if (passwordVisible) "Hide password" else "Show password"

                        IconButton(onClick = {passwordVisible = !passwordVisible}){
                            Icon(imageVector  = image, description)
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = buttonColor,
                        unfocusedPlaceholderColor = buttonPlaceHolderColor,
                        unfocusedTextColor = buttonColor,
                        unfocusedContainerColor = backgroundColor,
                        focusedContainerColor = backgroundColor,
                        unfocusedIndicatorColor = buttonColor,
                        focusedIndicatorColor =buttonColor ,
                        focusedLabelColor = buttonColor,
                        cursorColor = buttonColor,
                    )
                )

                if (!authErrorMessage.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = authErrorMessage,
                        color = errorColor,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSubmit(email.trim(), password.trim()) },
                colors = ButtonDefaults.textButtonColors(contentColor = buttonTextColor)
            ) {
                Text(submitButtonText, color = buttonTextColor)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = buttonTextColor)
            ) {
                Text("Cancel",color = buttonTextColor)
            }
        },
        containerColor = backgroundColor
    )
}
