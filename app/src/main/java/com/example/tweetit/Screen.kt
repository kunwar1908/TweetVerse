package com.example.tweetit
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import java.util.Date

@Composable
fun SplashScreen(onAnimationComplete: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    var endAnimation by remember { mutableStateOf(false) }

    // Inspired by Star Wars opening crawl
    val animationDuration = 4000
    val startAnimationSpec = tween<Float>(
        durationMillis = animationDuration,
        easing = FastOutSlowInEasing
    )

    // Animate the text size and position
    val textSize by animateFloatAsState(
        targetValue = if (startAnimation) 24f else 10f, // Larger size when animation starts
        animationSpec = startAnimationSpec, label = ""
    )
    val textYPosition by animateFloatAsState(
        targetValue = if (startAnimation) 0f else -100f, // Move up from bottom
        animationSpec = startAnimationSpec, label = ""
    )

    val boxRotation by animateFloatAsState(
        targetValue = if (endAnimation) 360f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing), label = ""
    )

    val boxSize by animateFloatAsState(
        targetValue = if (endAnimation) 50f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing), label = ""

    )


    val rotationState by animateFloatAsState(targetValue = if(startAnimation) 360f else 0f, animationSpec = tween(1000))

    LaunchedEffect(key1 = true) {
        startAnimation = true
        kotlinx.coroutines.delay(animationDuration.toLong())
        endAnimation = true
        onAnimationComplete()
    }


    androidx.compose.animation.AnimatedVisibility(
        visible = startAnimation,
        enter = fadeIn(animationSpec = tween(durationMillis = 1000)) ,
        exit = fadeOut(animationSpec = tween(durationMillis = 500)) + slideOutHorizontally(
            targetOffsetX = { -it * 2 }, animationSpec = tween(durationMillis = 500)
        ),
        modifier = Modifier.background(DeepSpaceBlue)
        )
    {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(TwitterBlue)
                ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "CosmoConnect", color = CyberYellow,
                    fontSize = textSize.sp, // Animated size
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.rotate(rotationState))


                Spacer(modifier = Modifier.height(16.dp))
                Icon(

                    imageVector = Filled.Favorite,
                    modifier = Modifier.rotate(rotationState),
                    contentDescription = "App Logo",
                    tint = Color.White,

                )
                Spacer(modifier = Modifier.height(16.dp))
                }
        }

    }

    if (endAnimation) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(TwitterBlue)
            .clickable(onClick = {
                // Handle click if needed
            })
        ) {
            Box(modifier = Modifier
                .align(Alignment.Center)
                .size(boxSize.dp)
                .rotate(boxRotation)

                .background(
                    CyberYellow,
                    shape = RoundedCornerShape(8.dp)
                )
            )
        }




        LaunchedEffect(key1 = endAnimation) {
            kotlinx.coroutines.delay(500)
        }
    }

}

@Composable
fun PartyBackground() {
    var isEffectOn by remember { mutableStateOf(true) }
    var colorIndex by remember { mutableStateOf(0) }
    val colors = listOf(
        Color(0xFFFCE4EC), // Light Pink
        Color(0xFFE3F2FD), // Light Blue
        Color(0xFFFFFDE7), // Light Yellow
        Color(0xFFF1F8E9), // Light Green
        Color(0xFFFFF3E0), // Light Orange
        Color(0xFFE0F7FA), // Light Cyan
        Color(0xFFF3E5F5), // Light Purple
        Color(0xFFDCEDC8), // Light Lime
        Color(0xFFE8EAF6),  // Light Indigo
        Color(0xFFFFF8E1), // Light Amber
        Color(0xFFE0F2F1),  // Light Teal
        Color(0xFFECEFF1) //Light Gray

    )



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End // Align to the right
    ) {
        Text(text = if (isEffectOn) "Effects ON" else "Effects OFF",
            textAlign = TextAlign.Center,
            color = if (isEffectOn) Color.Green else Color.Red,

        )
        Switch(
            checked = isEffectOn,
            onCheckedChange = { isEffectOn = it },
            colors = androidx.compose.material3.SwitchDefaults.colors(
                checkedThumbColor = TwitterBlue
            )
        )
    }
        if (isEffectOn) {
            LaunchedEffect(key1 = Unit) {
                while (true) {
                    kotlinx.coroutines.delay(1000)
                    colorIndex = (colorIndex + 1) % colors.size
                }
            }
            val animatedColor by animateColorAsState(targetValue = colors[colorIndex], animationSpec = tween(durationMillis = 1000))
        //i need to change the color opacity for the gradient of the background
            //need more blurr efect
            //add cool things here
            Box(modifier = Modifier.fillMaxSize().background(animatedColor).alpha(0.2f))

        }}
val TwitterBlue = Color(0xFF1DA1F2)
val TwitterDarkGray = Color(0xFF1E1E1E)
val FuturisticPurple = Color(0xFF6C4AB6)
val NeonGreen = Color(0xFF2ECC71)
val CyberYellow = Color(0xFFF1C40F)
val DeepSpaceBlue = Color(0xFF2C3E50)
val TechWhite = Color(0xFFECF0F1)
val GradientStart = Color(0xFF4A148C)
val GradientEnd = Color(0xFF8E24AA)

data class Tweet(
    val id: String = "",
    val text: String = "",
    val username: String = "User",
    val message: String = " ",
    val timestamp: Date = Date(),
    val likes: Int = 0
)


@Composable
fun TweetingApp()
{
    val db = FirebaseFirestore.getInstance()
    var tweetMessage by remember { mutableStateOf("") }
    var tweets by remember { mutableStateOf<List<Tweet>>(emptyList()) }
    var userName by remember { mutableStateOf("") }

    // Fetch tweets when the app starts
    LaunchedEffect(Unit) {
        db.collection("tweets")
            .orderBy("timestamp" , Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                tweets = snapshot?.documents?.mapNotNull{ it.toObject<Tweet>()?.copy(id = it.id) } ?: emptyList()
            }
    }

    Box {
        //PartyBackground()
        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(GradientStart, GradientEnd)
                )
            ))
        Column(
            //add some cool effect here to the entire container
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                .blur(1.dp)

        ) {

        // add spacer here
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "TweetVerse",
            fontSize = 34.sp,
            color = CyberYellow,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 26.dp)
                .align(Alignment.CenterHorizontally)
                .clickable { }
                //animation of text

        )

        // Text field for entering user name
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Enter your code name, cosmic wanderer", color = TechWhite) },
            modifier = Modifier.fillMaxWidth().background(DeepSpaceBlue.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp)),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Text field for typing a tweet
        OutlinedTextField(
            value = tweetMessage,
            onValueChange = { tweetMessage = it },
            label = { Text("Compose your intergalactic message ;)", color = TechWhite) },
            modifier = Modifier.fillMaxWidth().background(DeepSpaceBlue.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp))

        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to post the tweet
        Button(
            onClick = {
                if (tweetMessage.isNotBlank() && userName.isNotBlank()) {

                    val tweet = Tweet(username = userName, message = tweetMessage)
                    // Reset the text fields
                    userName = ""
                    tweetMessage = ""

                    db.collection("tweets").add(tweet)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = FuturisticPurple
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Broadcast")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tweets) { tweet ->
                TweetCard(
                    tweet = tweet,
                    onLike = { isLiked ->
                        // Update likes in Firestore
                        val newLikes = if (isLiked) tweet.likes + 1 else tweet.likes - 1
                        db.collection("tweets").document(tweet.id)
                            .update("likes", newLikes)
                    },
                    onDelete = {
                        db.collection("tweets").document(tweet.id).delete()
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    }
}

@Composable
fun TweetCard(
    tweet:Tweet,
    onLike: (Boolean) -> Unit,
    onDelete: () -> Unit
) {

    val scale = remember { androidx.compose.animation.core.Animatable(1f) }


    var isLiked by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = DeepSpaceBlue.copy(alpha = 0.7f),
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Username and formatted timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "@${tweet.username}",
                    fontSize = 16.sp,
                    color = TechWhite
                )
                androidx.compose.material3.Text(
                    text = tweet.timestamp.toString(), // Format timestamp
                    color = TechWhite,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))



            // Tweet message change for intergalactic
            Text(
                text = tweet.message,
                fontSize = 16.sp,
                color = TechWhite
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Like button and delete button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Like button and like count
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = { isLiked = !isLiked
                        onLike(isLiked) }, modifier = Modifier.scale(scale = scale.value)){

                        val animatedScale = animateFloatAsState(
                            targetValue = if (isLiked) 1.2f else 1f,
                            animationSpec = tween(durationMillis = 300), label = ""
                        )

                        LaunchedEffect(key1 = isLiked){
                            scale.animateTo(animatedScale.value, tween(durationMillis = 300))
                        }

                        Icon(
                            imageVector = if (isLiked)
                                Icons.Default.Favorite
                            else
                                Icons.Default.FavoriteBorder,
                            modifier = Modifier.scale(animatedScale.value),
                            contentDescription = "Like",
                            tint = if (isLiked) TwitterBlue else TwitterDarkGray
                        )
                    }
                    // Like count underneath the like icon
                    Text(
                        text = "${tweet.likes} Galaxies of Support",
                        fontSize = 12.sp,
                        color = TechWhite
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = TechWhite
                    )
                }
            }
        }
    }
}