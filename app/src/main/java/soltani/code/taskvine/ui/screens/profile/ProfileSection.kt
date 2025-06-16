package soltani.code.taskvine.ui.screens.profile


import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.Glide
import soltani.code.taskvine.helpers.LocalCustomColors
import soltani.code.taskvine.viewmodel.AuthViewModel

@Composable
fun ProfileSection(
    authViewModel: AuthViewModel = hiltViewModel(),
    navigateToWelcome: () -> Unit

) {
    val isSignedIn by authViewModel.isSignedIn.collectAsState()
    val user = authViewModel.currentUser
    val colors = LocalCustomColors.current
    val context = LocalContext.current // Add this

    if (!isSignedIn || user == null) {
        // Show default guest UI
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colors.SecondaryBackground),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "You're not signed in",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.primaryText
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Please log in to view your profile.",
                    fontSize = 14.sp,
                    color = colors.secondaryText,
                    modifier = Modifier.clickable { navigateToWelcome() }

                )
            }
        }
    } else {
        // Your original Profile UI remains unchanged
        val name = user.displayName ?: "Guest"
        val email = user.email ?: "No email"
        val photoUrl = user.photoUrl?.toString()

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colors.SecondaryBackground),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (photoUrl != null) {
                    AndroidView(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        factory = {
                            ImageView(it).apply {
                                scaleType = ImageView.ScaleType.CENTER_CROP
                                Glide.with(it)
                                    .load(photoUrl)
                                    .circleCrop()
                                    .into(this)
                            }
                        }
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.primaryText
                        )
                        Text(
                            text = email,
                            fontSize = 14.sp,
                            color = colors.secondaryText
                        )
                    }
                }
                else{
                    Text(
                        text = email,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.primaryText,
                        modifier = Modifier.weight(1f)
                    )
                }



                Text(
                    text = "Logout",
                    color = colors.primaryText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable {
                            Toast.makeText(context, "Signing out...", Toast.LENGTH_SHORT).show()
                            authViewModel.signOut()
                        }
                        .padding(8.dp)
                )
            }
        }
    }
}
